package com.mealtoyou.foodservice.application.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.mealtoyou.foodservice.application.dto.CommunityDietDto;
import com.mealtoyou.foodservice.application.dto.CommunityDietFoodDto;
import com.mealtoyou.foodservice.application.dto.DailyDietsResponseDto;
import com.mealtoyou.foodservice.application.dto.DietFoodDto;
import com.mealtoyou.foodservice.application.dto.DietFoodRequestDto;
import com.mealtoyou.foodservice.application.dto.DietSummaryDto;
import com.mealtoyou.foodservice.domain.model.Diet;
import com.mealtoyou.foodservice.domain.model.DietFood;
import com.mealtoyou.foodservice.domain.model.Food;
import com.mealtoyou.foodservice.domain.repository.DietFoodRepository;
import com.mealtoyou.foodservice.domain.repository.DietRepository;
import com.mealtoyou.foodservice.domain.repository.FoodRepository;
import com.mealtoyou.foodservice.infrastructor.kafka.KafkaMonoUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

@Service
@RequiredArgsConstructor
@Slf4j
public class DietService {

    private final KafkaMonoUtils kafkaMonoUtils;
    private final DietRepository dietRepository;
    private final DietFoodRepository dietFoodRepository;
    private final FoodRepository foodRepository;

    private Mono<Double> requestBMR(long userId) {
        return kafkaMonoUtils.sendAndReceive("health-service-getBmr", userId).map(Double::parseDouble);
    }

    private int calcNutrientsPer(double bmr, double ratio, double nutrientsGram, int caloriesFactor) {
        return (int) (((nutrientsGram * caloriesFactor) / (bmr * ratio)) * 100);
    }

    private Mono<String> requestUserNickname(long userId) {
        return kafkaMonoUtils.sendAndReceive("user-service-getNickname", userId);
    }

    @Transactional
    public Mono<Void> createDiet(long userId, List<DietFoodRequestDto> dietFoodRequestDtoList) {
        return Flux.fromIterable(dietFoodRequestDtoList)
                .flatMap(dto -> foodRepository.findByRid(dto.foodId()))
                .collectList()
                .flatMap(foods -> {
                    if (foods.size() != dietFoodRequestDtoList.size()) {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST));
                    }

                    Diet diet = Diet.builder()
                            .userId(userId)
                            .createDateTime(LocalDateTime.now())
                            .build();

                    return dietRepository.save(diet).map(savedDiet -> Tuples.of(savedDiet, foods));
                })
                .flatMap(tuple -> {
                    Diet savedDiet = tuple.getT1();
                    List<Food> foods = tuple.getT2();

                    double totalCalories = 0.0;
                    double totalCarbohydrate = 0.0;
                    double totalProtein = 0.0;
                    double totalFat = 0.0;

                    for (int i = 0; i < dietFoodRequestDtoList.size(); i++) {
                        double factor = dietFoodRequestDtoList.get(i).servingSize();
                        totalCalories += foods.get(i).getEnergy() * factor;
                        totalCarbohydrate += foods.get(i).getCarbohydrate() * factor;
                        totalProtein += foods.get(i).getProtein() * factor;
                        totalFat += foods.get(i).getFat() * factor;
                    }

                    savedDiet.updateTotalNutrients(totalCalories, totalCarbohydrate, totalProtein, totalFat);
                    return dietRepository.save(savedDiet).zipWith(Mono.just(foods));
                })
                .flatMapMany(tuple -> {
                    List<DietFood> dietFoods = new ArrayList<>();
                    Diet savedDiet = tuple.getT1();
                    List<Food> foods = tuple.getT2();
                    for (int i = 0; i < dietFoodRequestDtoList.size(); i++) {
                        Food food = foods.get(i);
                        double factor = dietFoodRequestDtoList.get(i).servingSize();
                        dietFoods.add(DietFood.builder()
                                .dietId(savedDiet.getDietId())
                                .foodId(food.getRid())
                                .servingSize(factor)
                                .build());
                    }
                    return dietFoodRepository.saveAll(dietFoods);
                })
                .then();
    }

    public Mono<DailyDietsResponseDto> getMyDiet(long userId, String dateString) {
        LocalDateTime startOfDay, endOfDay;
        try {
            // 문자열을 LocalDateTime 으로 변환
            LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
            startOfDay = date.atStartOfDay();
            endOfDay = date.atTime(LocalTime.MAX);
        } catch (RuntimeException re) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "입력 날짜 형식이 잘못되었습니다.");
        }

        return dietRepository.findAllByUserIdAndCreateDateTimeBetween(userId, startOfDay, endOfDay)
                .flatMap(diet -> {
                    Flux<DietFood> dietFoodFlux = dietFoodRepository.findDietFoodsByDietId(diet.getDietId());
                    Mono<List<Food>> listMono = dietFoodFlux.collectList()
                            .flatMap(list -> foodRepository.findFoodsByRidIn(list.stream().map(DietFood::getFoodId).toList())
                                    .collectList());
                    return listMono.map(foods ->
                                    foods.stream().map(food -> DietFoodDto.builder()
                                            .name(food.getName())
                                            .imageUrl("temp image url") // TODO: 음식 이미지 가져오기
                                            .calories(food.getEnergy())
                                            .carbohydrate(food.getCarbohydrate())
                                            .protein(food.getProtein())
                                            .fat(food.getFat())
                                            .build()).toList())
                            .flatMap(dietFoods ->
                                    requestBMR(userId).flatMap((bmr) -> {
                                        DietSummaryDto dietInfo = DietSummaryDto.builder()
                                                .dietId(diet.getDietId())
                                                .totalCalories(diet.getTotalCalories().intValue())
                                                .carbohydratePer(calcNutrientsPer(bmr, 0.5, diet.getTotalCarbohydrate(), 4))
                                                .proteinPer(calcNutrientsPer(bmr, 0.25, diet.getTotalProtein(), 4))
                                                .fatPer(calcNutrientsPer(bmr, 0.25, diet.getTotalFat(), 9))
                                                .dietFoods(dietFoods)
                                                .build();
                                        return Mono.just(dietInfo);
                                    })
                            );
                })
                .collectList()
                .map(diets -> {
                            double totalCalories = 0.0;
                            double totalCarbohydrate = 0.0;
                            double totalProtein = 0.0;
                            double totalFat = 0.0;
                            for (DietSummaryDto diet : diets) {
                                for (DietFoodDto food : diet.dietFoods()) {
                                    totalCalories += food.calories();
                                    totalCarbohydrate += food.carbohydrate();
                                    totalProtein += food.protein();
                                    totalFat += food.fat();
                                }
                            }
                            return DailyDietsResponseDto.builder()
                                    .date(dateString)
                                    .dailyCaloriesBurned(totalCalories)
                                    .dailyCarbohydrateTaked(totalCarbohydrate)
                                    .dailyProteinTaked(totalProtein)
                                    .dailyFatTaked(totalFat)
                                    .diets(diets)
                                    .build();
                        }
                );
    }

    public Mono<List<CommunityDietDto>> getCommunityDiets(long userId, List<Long> dietIds) {
        return dietRepository.findAllById(dietIds)
                .flatMap((diet -> {
                    Mono<List<Food>> listMono = dietFoodRepository.findDietFoodsByDietId(diet.getDietId())
                            .flatMap(dietFood -> foodRepository.findByRid(dietFood.getFoodId()))
                            .collectList();
                    Mono<Double> bmrMono = requestBMR(userId);
                    Mono<String> nicknameMono = requestUserNickname(userId);

                    return Mono.zip(listMono, bmrMono, nicknameMono)
                            .map(tuple -> {
                                List<Food> foods = tuple.getT1();
                                double bmr = tuple.getT2();
                                List<CommunityDietFoodDto> dietFoodDtoList =
                                        foods.stream().map(f -> CommunityDietFoodDto.builder()
                                                .name(f.getName())
                                                .imageUrl("temp image url") // TODO: 음식 이미지 조회 필요
                                                .calories(f.getEnergy())
                                                .build()).toList();
                                return CommunityDietDto.builder()
                                        .dietId(diet.getDietId())
                                        .totalCalories(diet.getTotalCalories().intValue())
                                        .carbohydratePer(calcNutrientsPer(bmr, 0.5, diet.getTotalCarbohydrate(), 4))
                                        .proteinPer(calcNutrientsPer(bmr, 0.25, diet.getTotalProtein(), 4))
                                        .fatPer(calcNutrientsPer(bmr, 0.25, diet.getTotalFat(), 9))
                                        .dietFoods(dietFoodDtoList)
                                        .build();
                            });
                }))
                .collectList();
    }

}
