package com.mealtoyou.foodservice.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mealtoyou.foodservice.application.dto.FoodDto;
import com.mealtoyou.foodservice.domain.model.Food;
import com.mealtoyou.foodservice.domain.repository.FoodRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FoodService {

	private final FoodRepository foodRepository;
	private final ElasticsearchService elasticsearchService;

	//완벽하게 일치하는 값 검색 -> 없으면 1개 차이나는 값 검색
	public Flux<FoodDto> getFoods(String keyword) {
		Flux<Food> foods = foodRepository.findByName(keyword);
		//만약에 foods가 없으면 fuzzysearch를 통해서 한글자 다른 값을 찾는다.
		return foods.switchIfEmpty(
				//fuzzySearch가 Mono<List<Food>>를 반환하므로 변환과정이 필요
				Mono.defer(() -> elasticsearchService.fuzzySearch(keyword)).flatMapMany(Flux::fromIterable))
			.map(FoodDto::toDto);
	}

	public Flux<FoodDto> ratioFood(Double protein, Double fat, Double carbohydrate) {
		Mono<List<Food>> foods = elasticsearchService.ratioNutrient(protein, fat, carbohydrate);
		return foods.flatMapMany(Flux::fromIterable).map(FoodDto::toDto);
	}
}
