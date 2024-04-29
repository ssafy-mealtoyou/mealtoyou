package com.mealtoyou.foodservice.global.util;

import co.elastic.clients.elasticsearch._types.query_dsl.FuzzyQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import java.util.function.Supplier;
import lombok.val;

public class ElasticsearchUtil {

    public static Supplier<Query> createSupplierQuery(String approximateName){
        Supplier<Query> supplier=()->Query.of(q->q.fuzzy(createFuzzyQuery(approximateName)));
        return supplier;
    }

    public static FuzzyQuery createFuzzyQuery(String approximateName){
        val fuzzyQuery=new FuzzyQuery.Builder();
        return fuzzyQuery.field("name").value(approximateName).fuzziness("1").build();
    }
}
