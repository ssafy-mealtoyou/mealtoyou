package com.mealtoyou.foodservice.global.util;

import static org.springframework.data.elasticsearch.client.elc.Queries.*;

import java.util.function.Supplier;

import org.springframework.data.elasticsearch.client.elc.NativeQuery;

import co.elastic.clients.elasticsearch._types.InlineScript;
import co.elastic.clients.elasticsearch._types.ScoreSort;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.FuzzyQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.ScriptScoreQuery;
import lombok.val;

public class ElasticsearchUtil {

	public static Supplier<Query> createSupplierQuery(String approximateName) {
		Supplier<Query> supplier = () -> Query.of(q -> q.fuzzy(createFuzzyQuery(approximateName)));
		return supplier;
	}

	public static FuzzyQuery createFuzzyQuery(String approximateName) {
		val fuzzyQuery = new FuzzyQuery.Builder();
		return fuzzyQuery.field("name").value(approximateName).fuzziness("1").build();
	}

	public static NativeQuery createNativeQueryThree(Double protein, Double fat, Double carbohydrate) {
		ScoreSort scoreSort = ScoreSort.of(builder -> builder.order(SortOrder.Asc));
		return NativeQuery.builder()
			.withQuery(q -> q.scriptScore(createScriptScoreQueryThree(protein, fat, carbohydrate)))
			.withMaxResults(3)
			.withSort(SortOptions.of(builder -> builder.score(scoreSort)))
			.build();

	}

	public static ScriptScoreQuery createScriptScoreQueryThree(Double protein, Double fat, Double carbohydrate) {
		String source = String.format(
			"double protein = doc.containsKey('protein(g)') ? (doc['protein(g)'].size() > 0 ? doc['protein(g)'].value : 0) : 0; double carbs = doc.containsKey('carbohydrate(g)') ? (doc['carbohydrate(g)'].size() > 0 ? doc['carbohydrate(g)'].value : 0) : 0; double fat = doc.containsKey('fat(g)') ? (doc['fat(g)'].size() > 0 ? doc['fat(g)'].value : 0) : 0; return Math.abs(protein - %f) + Math.abs(carbs - %f) + Math.abs(fat - %f);",
			protein, carbohydrate, fat);
		InlineScript inlineScript = InlineScript.of(builder -> builder
			.source(source)
			.lang("painless"));
		Script script = Script.of(builder -> builder.inline(inlineScript));
		System.out.println(script.toString());
		return ScriptScoreQuery.of(builder -> builder.script(script).query(matchAllQueryAsQuery()));

	}

	public static NativeQuery createNativeQueryOne(String nutrient, Double amount) {
		ScoreSort scoreSort = ScoreSort.of(builder -> builder.order(SortOrder.Asc));
		return NativeQuery.builder()
			.withQuery(q -> q.scriptScore(createScriptScoreQueryOne(nutrient, amount)))
			.withMaxResults(5)
			.withSort(SortOptions.of(builder -> builder.score(scoreSort)))
			.build();

	}

	public static ScriptScoreQuery createScriptScoreQueryOne(String nutrient, Double amount) {
		String source = String.format(
			"if (doc[%s(g)].size() == 0) { return 100; } else { return Math.abs(doc[%s(g)].value - %f); }", nutrient,
			nutrient, amount);
		InlineScript inlineScript = InlineScript.of(builder -> builder
			.source(source)
			.lang("painless"));
		Script script = Script.of(builder -> builder.inline(inlineScript));
		return ScriptScoreQuery.of(builder -> builder.script(script).query(matchAllQueryAsQuery()));
	}
}
