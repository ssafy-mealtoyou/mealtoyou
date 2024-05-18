package com.mealtoyou.foodservice.application.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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
import reactor.util.function.Tuple3;

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

    public Mono<Void> createDiet(long userId, List<DietFoodRequestDto> dietFoodRequestDtoList) {
        if (dietFoodRequestDtoList.isEmpty()) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "요청된 음식 목록이 비어있습니다."));
        }
        return Flux.fromIterable(dietFoodRequestDtoList)
            .flatMap(dto -> foodRepository.findByRid(dto.foodId())
                .switchIfEmpty(Mono.error(
                    new ResponseStatusException(HttpStatus.BAD_REQUEST, "음식 ID가 유효하지 않습니다: " + dto.foodId()))))
            .collectList()
            .flatMap(foods -> {
                if (foods.size() != dietFoodRequestDtoList.size()) {
                    return Mono.error(
                        new ResponseStatusException(HttpStatus.BAD_REQUEST, "요청된 음식의 수량과 일치하지 않는 음식이 있습니다."));
                }

                Diet diet = createDiet(userId);
                return saveDietWithFoods(diet, foods, dietFoodRequestDtoList);
            })
            .flatMapMany(this::saveDietFoods)
            .then();
    }

    private Diet createDiet(long userId) {
        return Diet.builder()
            .userId(userId)
            .createDateTime(LocalDateTime.now())
            .build();
    }

    private Mono<Tuple3<Diet, List<Food>, List<Double>>> saveDietWithFoods(
        Diet diet, List<Food> foods,
        List<DietFoodRequestDto> dietFoodRequestDtoList
    ) {
        return dietRepository.save(diet)
            .map(savedDiet -> calculateTotalNutrients(savedDiet, foods, dietFoodRequestDtoList))
            .flatMap(savedDiet -> {
                Mono<Diet> savedDietMono = dietRepository.save(savedDiet);
                Mono<List<Food>> foodListMono = Mono.just(foods);
                Mono<List<Double>> servingSizeListMono = Mono.just(
                    dietFoodRequestDtoList.stream().map(DietFoodRequestDto::servingSize).toList());
                return Mono.zip(savedDietMono, foodListMono, servingSizeListMono);
            });
    }

    private Diet calculateTotalNutrients(Diet diet, List<Food> foods, List<DietFoodRequestDto> dietFoodRequestDtoList) {
        double totalCalories = 0.0;
        double totalCarbohydrate = 0.0;
        double totalProtein = 0.0;
        double totalFat = 0.0;

        for (int i = 0; i < dietFoodRequestDtoList.size(); i++) {
            Food food = foods.get(i);
            double factor = dietFoodRequestDtoList.get(i).servingSize();
            totalCalories += Optional.ofNullable(food.getEnergy()).orElse(0.0) * factor;
            totalCarbohydrate += Optional.ofNullable(food.getCarbohydrate()).orElse(0.0) * factor;
            totalProtein += Optional.ofNullable(food.getProtein()).orElse(0.0) * factor;
            totalFat += Optional.ofNullable(food.getFat()).orElse(0.0) * factor;
        }

        diet.updateTotalNutrients(totalCalories, totalCarbohydrate, totalProtein, totalFat);
        return diet;
    }

    private Flux<DietFood> saveDietFoods(Tuple3<Diet, List<Food>, List<Double>> tuple3) {
        List<DietFood> dietFoods = new ArrayList<>();
        Diet savedDiet = tuple3.getT1();
        List<Food> foods = tuple3.getT2();
        List<Double> servingSizeList = tuple3.getT3();
        for (int i = 0; i < foods.size(); i++) {
            Food food = foods.get(i);
            double servingSize = servingSizeList.get(i);
            dietFoods.add(DietFood.builder()
                .dietId(savedDiet.getDietId())
                .foodId(food.getRid())
                .servingSize(servingSize)
                .build());
        }
        return dietFoodRepository.saveAll(dietFoods);
    }

    public Mono<DailyDietsResponseDto> getMyDiet(long userId, String dateString) {
        LocalDateTime startOfDay, endOfDay;
        try {
            LocalDate date = parseDate(dateString);
            startOfDay = date.atStartOfDay();
            endOfDay = date.atTime(LocalTime.MAX);
        } catch (RuntimeException re) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "입력 날짜 형식이 잘못되었습니다.");
        }

        return dietRepository.findAllByUserIdAndCreateDateTimeBetween(userId, startOfDay, endOfDay)
            .flatMap(diet -> processDiet(userId, diet))
            .collectList()
            .map(diets -> createDailyDietsResponseDto(diets, dateString));
    }

    private LocalDate parseDate(String dateString) {
        return LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private Mono<DietSummaryDto> processDiet(long userId, Diet diet) {
        Flux<DietFood> dietFoodFlux = dietFoodRepository.findDietFoodsByDietId(diet.getDietId());
        return dietFoodFlux.collectList()
            .flatMap(list -> foodRepository.findFoodsByRidIn(extractFoodIds(list)).collectList())
            .map(this::createDietFoodDtos)
            .flatMap(dietFoods -> calculateNutrients(userId, diet, dietFoods));
    }

    private List<Long> extractFoodIds(List<DietFood> dietFoods) {
        return dietFoods.stream().map(DietFood::getFoodId).collect(Collectors.toList());
    }

    private List<DietFoodDto> createDietFoodDtos(List<Food> foods) {
        return foods.stream().map(this::createDietFoodDto).collect(Collectors.toList());
    }

    private DietFoodDto createDietFoodDto(Food food) {
        return DietFoodDto.builder()
            .name(food.getName())
            .imageUrl("temp image url") // TODO: 음식 이미지 가져오기
            .calories(Optional.ofNullable(food.getEnergy()).orElse(0.0))
            .carbohydrate(Optional.ofNullable(food.getCarbohydrate()).orElse(0.0))
            .protein(Optional.ofNullable(food.getProtein()).orElse(0.0))
            .fat(Optional.ofNullable(food.getFat()).orElse(0.0))
            .build();
    }

    private Mono<DietSummaryDto> calculateNutrients(long userId, Diet diet, List<DietFoodDto> dietFoods) {
        return requestBMR(userId).map(bmr -> createDietSummaryDto(diet, bmr, dietFoods));
    }

    private DietSummaryDto createDietSummaryDto(Diet diet, double bmr, List<DietFoodDto> dietFoods) {
        return DietSummaryDto.builder()
            .dietId(diet.getDietId())
            .totalCalories(diet.getTotalCalories().intValue())
            .carbohydratePer(calcNutrientsPer(bmr, 0.5, diet.getTotalCarbohydrate(), 4))
            .proteinPer(calcNutrientsPer(bmr, 0.25, diet.getTotalProtein(), 4))
            .fatPer(calcNutrientsPer(bmr, 0.25, diet.getTotalFat(), 9))
            .dietFoods(dietFoods)
            .build();
    }

    private DailyDietsResponseDto createDailyDietsResponseDto(List<DietSummaryDto> diets, String dateString) {
        double totalCalories = diets.stream().flatMap(diet -> diet.dietFoods().stream()).mapToDouble(DietFoodDto::calories).sum();
        double totalCarbohydrate = diets.stream().flatMap(diet -> diet.dietFoods().stream()).mapToDouble(DietFoodDto::carbohydrate).sum();
        double totalProtein = diets.stream().flatMap(diet -> diet.dietFoods().stream()).mapToDouble(DietFoodDto::protein).sum();
        double totalFat = diets.stream().flatMap(diet -> diet.dietFoods().stream()).mapToDouble(DietFoodDto::fat).sum();

        diets.sort((a, b) -> Math.toIntExact(a.dietId() - b.dietId()));

        return DailyDietsResponseDto.builder()
            .date(dateString)
            .dailyCaloriesBurned(totalCalories)
            .dailyCarbohydrateTaked(totalCarbohydrate)
            .dailyProteinTaked(totalProtein)
            .dailyFatTaked(totalFat)
            .diets(diets)
            .build();
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
