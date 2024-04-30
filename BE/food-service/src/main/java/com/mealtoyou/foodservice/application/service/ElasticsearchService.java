package com.mealtoyou.foodservice.application.service;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.ResponseBody;
import com.mealtoyou.foodservice.domain.model.Food;
import com.mealtoyou.foodservice.global.util.ElasticsearchUtil;
import java.util.List;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ElasticsearchService {

    private final ReactiveElasticsearchClient reactiveElasticsearchClient;
    private final ReactiveElasticsearchOperations reactiveElasticsearchOperations;

    public Mono<List<Food>> fuzzySearch(String approximateName){
        Supplier<Query> supplier= ElasticsearchUtil.createSupplierQuery(approximateName);
        Mono<ResponseBody<Food>> response = reactiveElasticsearchClient.search(
            s -> s.index("food").query(supplier.get()), Food.class);
        System.out.println("elasticsearch supplier fuzzy query"+supplier.get().toString());

        return response.flatMap(
            responseBody -> {
                List<Hit<Food>> hitList=responseBody.hits().hits();
                List<Food> foodList=hitList.stream().map(Hit::source).toList();
                return Mono.just(foodList);
            });
    }

    public Mono<List<Food>> multiSearch(Double protein, Double fat, Double carbohydrate){
        Flux<SearchHit<Food>> proteinResponse = reactiveElasticsearchOperations.search(ElasticsearchUtil.createNativeQuery("protein",protein), Food.class);
        Flux<SearchHit<Food>> fatResponse = reactiveElasticsearchOperations.search(ElasticsearchUtil.createNativeQuery("fat",fat), Food.class);
        Flux<SearchHit<Food>> carbohydrateResponse = reactiveElasticsearchOperations.search(ElasticsearchUtil.createNativeQuery("carbohydrate",carbohydrate), Food.class);

        //        response.subscribe(foodSearchHit -> {Food food=foodSearchHit.getContent();
//            System.out.println(food.toString());});
        return null;
    }



}
