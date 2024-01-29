package cn.qingweico.elasticlowerclient.controller;

import cn.hutool.core.lang.Snowflake;
import cn.qingweico.elasticlowerclient.entity.ElasticUser;
import cn.qingweico.elasticlowerclient.model.ReqParams;
import cn.qinwweico.model.ApiResponse;
import cn.qinwweico.model.PagedResult;
import cn.qinwweico.util.Global;
import cn.qinwweico.util.RandomDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * @author zqw
 * @date 2024/1/28
 */
@Slf4j
@RequestMapping("/elastic")
@RestController
public class UserController {

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    @PostMapping("/searchList")
    public ApiResponse searchList(@RequestBody ReqParams params) {
        Integer page = params.getPage();
        page--;
        Integer pageSize = params.getPageSize();
        Pageable pageable = PageRequest.of(page, pageSize);

        SearchQuery searchQuery;
        AggregatedPage<ElasticUser> pagedUser = new AggregatedPageImpl<>(new ArrayList<>(0));

        String name = params.getName();
        String address = params.getAddress();
        String phone = params.getPhone();
        // 默认查询不带参数
        if (StringUtils.isBlank(name) && StringUtils.isBlank(address) && StringUtils.isBlank(phone)) {
            searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchAllQuery()).withPageable(pageable).build();
            pagedUser = elasticsearchTemplate.queryForPage(searchQuery, ElasticUser.class);
        }

        // 按照姓名查询
        if (StringUtils.isNotEmpty(name)) {
            searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchQuery("name", name)).withPageable(pageable).build();

            pagedUser = elasticsearchTemplate.queryForPage(searchQuery, ElasticUser.class);
        }
        // 按照地址查询
        if (StringUtils.isNotEmpty(address)) {
            searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchQuery("address", address)).withPageable(pageable).build();
            pagedUser = elasticsearchTemplate.queryForPage(searchQuery, ElasticUser.class);
        }

        // 按照手机号查询, 并且高亮显示关键字
        if (StringUtils.isNotBlank(phone)) {
            pagedUser = queryByPhone(phone, pageable);
        }

        List<ElasticUser> pagedElasticUserList = pagedUser.getContent();
        PagedResult gridResult = new PagedResult();
        gridResult.setRows(pagedElasticUserList);
        gridResult.setCurrentPage(page + 1);
        gridResult.setTotalPage(pagedUser.getTotalPages());
        gridResult.setTotalNumber(pagedUser.getTotalElements());
        return ApiResponse.ok(gridResult);
    }

    private AggregatedPage<ElasticUser> queryByPhone(String phone, Pageable pageable) {
        String preTag = "<font color='red'>";
        String postTag = "</font>";
        String searchTitleField = "phone";
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchQuery(searchTitleField, phone)).withHighlightFields(new HighlightBuilder.Field(searchTitleField).preTags(preTag).postTags(postTag)).withPageable(pageable).build();

        return elasticsearchTemplate.queryForPage(searchQuery, ElasticUser.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                List<ElasticUser> userHighLightList = new ArrayList<>();
                SearchHits searchHits = searchResponse.getHits();
                for (SearchHit hit : searchHits) {
                    HighlightField highlightField = hit.getHighlightFields().get(searchTitleField);
                    String phone = highlightField.getFragments()[0].toString();

                    String id = (String) hit.getSourceAsMap().get("id");
                    String name = (String) hit.getSourceAsMap().get("name");
                    String address = (String) hit.getSourceAsMap().get("address");
                    Long create = (Long) hit.getSourceAsMap().get("create");
                    Date createTime = new Date(create);
                    Long update = (Long) hit.getSourceAsMap().get("update");
                    Date updateTime = new Date(update);

                    ElasticUser userElastic = new ElasticUser();
                    userElastic.setId(id);
                    userElastic.setName(name);
                    userElastic.setAddress(address);
                    userElastic.setPhone(phone);
                    userElastic.setCreate(createTime);
                    userElastic.setUpdate(updateTime);
                    userHighLightList.add(userElastic);
                }
                @SuppressWarnings("unchecked") AggregatedPageImpl<T> ts = new AggregatedPageImpl<>((List<T>) userHighLightList, pageable, searchResponse.getHits().totalHits);
                return ts;
            }

            @Override
            public <T> T mapSearchHit(SearchHit searchHit, Class<T> aClass) {
                return null;
            }
        });
    }

    @GetMapping("/delete/{Id}")
    public ApiResponse delete(@PathVariable("Id") String id) {
        elasticsearchTemplate.delete(ElasticUser.class, id);
        return ApiResponse.ok();
    }

    public ApiResponse deleteByCondition(@PathVariable("Id") String id) {
        DeleteQuery deleteQuery = new DeleteQuery();
        deleteQuery.setQuery(QueryBuilders.termQuery("id", id));
        elasticsearchTemplate.delete(deleteQuery, ElasticUser.class);
        return ApiResponse.ok();
    }

    /**
     * 不同性别的人数
     *
     * @return ApiResponse
     */
    @GetMapping("/differentGendersNumber")
    public ApiResponse differentGendersNumber() {
        Map<String, Object> result = new HashMap<>();
        TermsAggregationBuilder termBuilder = AggregationBuilders.terms("different-genders-number").field("gender.keyword");
        SearchQuery searchQuery = new NativeSearchQueryBuilder().addAggregation(termBuilder).build();
        Aggregations agg = elasticsearchTemplate.query(searchQuery, SearchResponse::getAggregations);
        Map<String, Aggregation> aggregationMap = agg.asMap();
        StringTerms stringTerms = (StringTerms) aggregationMap.get("different-genders-number");
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
        if (buckets.isEmpty()) {
            result.put("man", 0);
            result.put("woman", 0);
        }
        for (StringTerms.Bucket bucket : buckets) {
            // key
            String key = (String) bucket.getKey();
            // value
            long docCount = bucket.getDocCount();
            if ("0".equals(key)) {
                result.put("woman", (int) docCount);
            } else if ("1".equals(key)) {
                result.put("man", (int) docCount);
            }
        }
        return ApiResponse.ok(result);
    }

    public static final String[] REGIONS = {"北京", "天津", "上海", "重庆", "河北", "山西", "辽宁", "吉林", "黑龙江", "江苏", "浙江", "安徽", "福建", "江西", "山东", "河南", "湖北", "湖南", "广东", "海南", "四川", "贵州", "云南", "陕西", "甘肃", "青海", "台湾", "内蒙古", "广西", "西藏", "宁夏", "新疆", "香港", "澳门"};

    /**
     * 不同省份的人数
     *
     * @return ApiResponse
     */
    @GetMapping("/differentProvinceNumber")
    public ApiResponse differentProvinceNumber() {
        // 默认聚合大小限制 default size 10
        TermsAggregationBuilder termBuilder = AggregationBuilders.terms("different-province-number").field("province.keyword").size(REGIONS.length);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .addAggregation(termBuilder).build();
        Aggregations agg = elasticsearchTemplate.query(searchQuery, SearchResponse::getAggregations);
        Map<String, Aggregation> aggregationMap = agg.asMap();
        StringTerms stringTerms = (StringTerms) aggregationMap.get("different-province-number");
        final List<StringTerms.Bucket> buckets = stringTerms.getBuckets();

        List<Map<String, Object>> list = new ArrayList<>();
        for (StringTerms.Bucket bucket : buckets) {
            // key
            String key = (String) bucket.getKey();
            // value
            long docCount = bucket.getDocCount();

            Map<String, Object> map = new HashMap<>();
            map.put("name", key);
            map.put("value", (int) docCount);
            list.add(map);
        }
        return ApiResponse.ok(list);
    }

    /**
     * 随机添加一定数量的数据
     *
     * @return ApiResponse
     */

    @PostMapping("/randomAdd/{quantity}")
    public ApiResponse randomAdd(@PathVariable("quantity") Integer quantity) throws InterruptedException {
        if (quantity != null) {
            CountDownLatch latch = new CountDownLatch(quantity);
            int batch = 10;
            int[] batchArray = Global.splitInteger(quantity, batch);
            long start = System.currentTimeMillis();
            for (int i = 0; i < batch; i++) {
                int frequency = batchArray[i];
                if (frequency != 0) {
                    CompletableFuture.runAsync(() -> {
                        IndexQueryBuilder queryBuilder = new IndexQueryBuilder();
                        Snowflake snowflake = new Snowflake();
                        for (int f = 0; f < frequency; f++) {
                            ElasticUser eu = new ElasticUser();
                            eu.setId(snowflake.nextIdStr());
                            eu.setName(RandomDataUtil.name(true));
                            eu.setAddress(RandomDataUtil.address(true));
                            eu.setAvl(RandomDataUtil.zeroOrOne());
                            eu.setGender(RandomDataUtil.zeroOrOne());
                            eu.setProvince(REGIONS[RandomDataUtil.nextInt(REGIONS.length)]);
                            eu.setPhone(RandomDataUtil.phone(true));
                            eu.setCreate(new Date());
                            eu.setUpdate(new Date());
                            IndexQuery indexQuery = queryBuilder.withObject(eu).build();
                            elasticsearchTemplate.index(indexQuery);
                            latch.countDown();
                        }
                    });
                } log.info(">>>>>>>>>>>>>>>>>>>> 第 {} 批次, 添加的数量为: {}", i, frequency);
            }
            latch.await();
            log.info(">>>>>>>>>>>>>>>>>>>> 本次随机添加的数量为: {}, 共耗时 {} ms", quantity, System.currentTimeMillis() - start);
        }
        return ApiResponse.ok();
    }
}
