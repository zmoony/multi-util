package com.boot.es.demo.pyIkSuggest;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yuez
 * @since 2023/3/23
 */
@Service
public class HotelService {
    private RestHighLevelClient restHighLevelClient;

    public HotelService(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    public List<String> getSuggestion(String key) throws IOException {
        SearchRequest request = new SearchRequest("hotel");
        SuggestBuilder suggestBuilder = new SuggestBuilder().addSuggestion("suggestions",
                SuggestBuilders.completionSuggestion("suggestion")
                        .prefix(key)
                        .skipDuplicates(true)
                        .size(10));
        request.source().suggest(suggestBuilder);
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        Suggest suggest = response.getSuggest();
        CompletionSuggestion suggestions = suggest.getSuggestion("suggestions");
        List<CompletionSuggestion.Entry.Option> options = suggestions.getOptions();
        List<String> list = options.stream().map(option -> option.getText().toString()).collect(Collectors.toList());
        return list;
    }

    public List<Map<String,Object>> list(Integer page, Integer size,String key) throws IOException {
        SearchRequest request = new SearchRequest("hotel");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        if(StringUtils.isNotEmpty(key)){
            MatchQueryBuilder queryBuilder = new MatchQueryBuilder("all",key);
            sourceBuilder.query(queryBuilder);
        }
        sourceBuilder.from((page-1)*size).size(size);
        request.source(sourceBuilder);
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        List<Map<String,Object>> list = new ArrayList<>(hits.length);
        for (SearchHit hit : hits) {
            Map<String, Object> map = hit.getSourceAsMap();
            list.add(map);
        }
        return list;
    }
}
