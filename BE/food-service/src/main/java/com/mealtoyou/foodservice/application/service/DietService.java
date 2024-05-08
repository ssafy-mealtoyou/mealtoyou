package com.mealtoyou.foodservice.application.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.mealtoyou.foodservice.application.dto.DietFoodRequestDto;
import com.mealtoyou.foodservice.domain.model.Diet;
import com.mealtoyou.foodservice.domain.model.DietFood;
import com.mealtoyou.foodservice.domain.model.Food;
import com.mealtoyou.foodservice.domain.repository.DietFoodRepository;
import com.mealtoyou.foodservice.domain.repository.DietRepository;
import com.mealtoyou.foodservice.domain.repository.FoodRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

@Service
@RequiredArgsConstructor
@Slf4j
public class DietService {

	private final DietRepository dietRepository;
	private final DietFoodRepository dietFoodRepository;
	private final FoodRepository foodRepository;

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


}
