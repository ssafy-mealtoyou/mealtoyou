package com.mealtoyou.foodservice.controller;

import com.mealtoyou.foodservice.application.dto.FoodDto;
import com.mealtoyou.foodservice.application.dto.ResponseFoodDto;
import com.mealtoyou.foodservice.application.service.ElasticsearchService;
import com.mealtoyou.foodservice.application.service.FoodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class FoodController {
    private final FoodService foodService;
    private final ElasticsearchService elasticsearchService;

    @GetMapping("/foods")
    public Mono<ResponseEntity<Flux<FoodDto>>> foodSearch(@RequestParam String keyword, String message){
        return Mono.just(ResponseEntity.ok().body(foodService.getFoods(keyword)));
    }

    @GetMapping("/food/{id}")
    public Mono<ResponseEntity<ResponseFoodDto>> foodInfo(@PathVariable String id,String message){
        elasticsearchService.multiSearch(0.0,0.0,0.0);
        return null;
    }

}
