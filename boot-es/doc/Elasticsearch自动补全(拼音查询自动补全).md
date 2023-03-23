# 拼音自动补全
[参考文档](https://blog.csdn.net/baidu_39378193/article/details/125129819)
1. 拼音分词器
2. IK分词器
3. suggest查询
> 全文搜索引擎会用某种算法对要建索引的文档进行分析， 从文档中提取出若干Token(词元)， 这些算法称为Tokenizer(分词器)， 这些Token会被进一步处理， 比如转成小写等， 这些处理算法被称为Token Filter(词元处理器), 被处理后的结果被称为Term(词)， 文档中包含了几个这样的Term被称为Frequency(词频)。 引擎会建立Term和原文档的Inverted Index(倒排索引)， 这样就能根据Term很快到找到源文档了。 文本被Tokenizer处理前可能要做一些预处理， 比如去掉里面的HTML标记， 这些处理的算法被称为Character Filter(字符过滤器)， 这整个的分析算法被称为Analyzer(分析器)。
## analysis结构分析
analyzer、 tokenizer、 filter
```
index :
    analysis :
        analyzer :
            standard :
                type : standard
                stopwords : [stop1, stop2]
            myAnalyzer1 :
                type : standard
                stopwords : [stop1, stop2, stop3]
                max_token_length : 500
            myAnalyzer2 :
                tokenizer : standard
                filter : [standard, lowercase, stop]
        tokenizer :
            myTokenizer1 :
                type : standard
                max_token_length : 900
            myTokenizer2 :
                type : keyword
                buffer_size : 512
        filter :
            myTokenFilter1 :
                type : stop
                stopwords : [stop1, stop2, stop3, stop4]
            myTokenFilter2 :
                type : length
                min : 0
                max : 2000
```
### analyzer
> ES内置若干analyzer, 另外还可以用内置的character filter, tokenizer, token filter组装一个analyzer(custom analyzer)， 比如
> - character filters：在tokenizer之前对文本进行处理。例如删除字符、替换字符
> - tokenizer：将文本按照一定的规则切割成词条（term）。例如keyword，就是不分词；还有ik_smart
> - tokenizer filter：将tokenizer输出的词条做进一步处理。例如大小写转换、同义词处理、拼音处理等
```
index :
    analysis :
        analyzer :
            myAnalyzer :
                tokenizer : standard
                filter : [standard, lowercase, stop]
```


## 分词器
1. 自定义分词器

```
PUT /test
{
  "settings": {
    "analysis": {
      "analyzer": { // 自定义分词器
        "my_analyzer": {  // 分词器名称
          "tokenizer": "ik_max_word", // 普通分词器
          "filter": "py" // 拼音分词器
        }
      },
      "filter": { // 自定义tokenizer filter
        "py": { // 过滤器名称
          "type": "pinyin", // 过滤器类型，这里是 pinyin
		  "keep_full_pinyin": false, // 单个字分词
          "keep_joined_full_pinyin": true, // 全拼
          "keep_original": true, // 保留中文
          "limit_first_letter_length": 16,
          "remove_duplicated_term": true,
          "none_chinese_pinyin_tokenize": false
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "name": {
        "type": "text",
        "analyzer": "my_analyzer", // 指定分词器（自定义）
        "search_analyzer": "ik_smart" // 指定搜索时不使用拼音
      }
    }
  }
}

```
2. 测试
```
POST test/_analyze
{
  "text": "金智视讯今天上班",
  "analyzer": "my_analyzer"
}
```
## 自动补全
1. 字段类型
```
PUT test/_mapping
{
    "properties": {
      "title":{
        "type": "completion"
      }
    }
}

// 示例数据
POST test/_doc
{
  "title": ["Sony", "WH-1000XM3"]
}
POST test/_doc
{
  "title": ["SK-II", "PITERA"]
}
POST test/_doc
{
  "title": ["Nintendo", "switch"]
}

GET test/_search
{
  "suggest": {
    "title_suggest": {
      "text": "s",
      "completion": {
        "field": "title",
        "skip_duplicates":true,
        "size":10
      }
    }
  }
}
```
## 示例
> 1. 根据用户输入找到suggest，自动补全
> 2. 拿到suggest进行填充输入框，然后点击查询，match到all字段
> 3. 注意：suggest的词尽量跟all的分词保持一致，可以确保查询结果
### index
```
DELETE test

# 修改索引库结构
PUT /hotel
{
  "settings": {
    "analysis": {
      "analyzer": {
        "text_anlyzer": {
          "tokenizer": "ik_max_word",
          "filter": "py"
        },
        "completion_analyzer": {
          "tokenizer": "keyword",
          "filter": "py"
        }
      },
      "filter": {
        "py": {
          "type": "pinyin",
          "keep_full_pinyin": false,
          "keep_joined_full_pinyin": true,
          "keep_original": true,
          "limit_first_letter_length": 16,
          "remove_duplicated_term": true,
          "none_chinese_pinyin_tokenize": false
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "id":{
        "type": "keyword"
      },
      "name":{
        "type": "text",
        "analyzer": "text_anlyzer",
        "search_analyzer": "ik_smart",
        "copy_to": "all"
      },
      "address":{
        "type": "keyword",
        "index": false
      },
      "price":{
        "type": "integer"
      },
      "score":{
        "type": "integer"
      },
      "brand":{
        "type": "keyword",
        "copy_to": "all"
      },
      "city":{
        "type": "keyword"
      },
      "starName":{
        "type": "keyword"
      },
      "business":{
        "type": "keyword",
        "copy_to": "all"
      },
      "location":{
        "type": "geo_point"
      },
      "pic":{
        "type": "keyword",
        "index": false
      },
      "all":{
        "type": "text",
        "analyzer": "text_anlyzer",
        "search_analyzer": "ik_smart"
      },
      "suggestion":{
          "type": "completion",
          "analyzer": "completion_analyzer"
      }
    }
  }
}

#导入数据
POST hotel/_doc/1
{
  "id": 1,
  "name": "金智科技",
  "address": "将军大道100号",
  "price": 100,
  "score": 46,
  "brand": "金智",
  "city": "江宁",
  "starName": "上市",
  "business": "将军大道/电气",
  "location": "40.2315654,136.25646",
  "pic": "",
  "distance": {},
  "isAD": false,
  "suggestion":["将军大道","电气","金智"]
}

#查询

GET hotel/_search
{
  "suggest": {
    "suggestion": {
      "text": "jz",
      "completion": {
        "field": "suggestion",
        "skip_duplicates":true
      }
    }
  }
}
#结果
{
  "took" : 0,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 0,
      "relation" : "eq"
    },
    "max_score" : null,
    "hits" : [ ]
  },
  "suggest" : {
    "suggestion" : [
      {
        "text" : "jz",
        "offset" : 0,
        "length" : 2,
        "options" : [
          {
            "text" : "金智",
            "_index" : "hotel",
            "_type" : "_doc",
            "_id" : "1",
            "_score" : 1.0,
            "_source" : {
              "id" : 1,
              "name" : "金智科技",
              "address" : "将军大道100号",
              "price" : 100,
              "score" : 46,
              "brand" : "金智",
              "city" : "江宁",
              "starName" : "上市",
              "business" : "将军大道/电气",
              "location" : "40.2315654,136.25646",
              "pic" : "",
              "distance" : { },
              "isAD" : false,
              "suggestion" : [
                "将军大道",
                "电气",
                "金智"
              ]
            }
          }
        ]
      }
    ]
  }
}

```
java代码
```java
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

```
```java
@RestController
@RequestMapping("hotel")
public class HotelController {
    private HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/list")
    public R hotelList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "1") Integer size,
            @RequestParam(value = "key" , defaultValue = "") String key
    ) throws IOException {
        List<Map<String, Object>> list = hotelService.list(page, size, key);
        return R.ok().data(list);
    }

    /**
     * 自动补齐推荐词
     * @param key 输入词
     * @return
     * @throws IOException
     */
    @GetMapping("/suggestion")
    public List<String> getSuggestion(@RequestParam("key") String key) throws IOException {
        return hotelService.getSuggestion(key);
    }


}
```







