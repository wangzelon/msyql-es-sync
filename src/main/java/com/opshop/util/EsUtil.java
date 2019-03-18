package com.opshop.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.common.xcontent.json.JsonXContentParser;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryParseContext;
import org.elasticsearch.index.query.QueryParser;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * created by wangzelong 2019/3/18 14:32
 */
@Component
public class EsUtil {

    @Autowired
    private TransportClient client;

    /**
     * @param index
     * @param type
     * @param id
     * @param object
     */
    public IndexResponse addDoc(String index, String type, String id, Object object) {
        IndexResponse indexResponse = client.prepareIndex(index, type, id).setSource(JSON.toJSONString(object), XContentType.JSON).get();
        return indexResponse;
    }

    public Map<String, Object> serachById(String index, String type, String id) {
        GetResponse response = client.prepareGet(index, type, id).get();
        return response.getSource();
    }

    /**
     * @param index
     * @param type
     * @param id
     * @param object
     */
    public UpdateResponse updateDoc(String index, String type, String id, Object object) {
        UpdateResponse updateResponse = client.prepareUpdate(index, type, id).setDoc(JSON.toJSONString(object), XContentType.JSON).get();
        return updateResponse;
    }

    /**
     * @param index
     * @param type
     * @param id
     */
    public DeleteResponse delDoc(String index, String type, String id) {
        DeleteResponse deleteResponse = client.prepareDelete(index, type, id.toString()).get();
        return deleteResponse;
    }

    /**
     * @param index
     * @param keywords
     * @param type
     * @param fields
     * @param currentPage
     * @param pageSize
     * @param isHighlight
     * @return
     * @throws Exception
     */
    public Page getDocHighLight(String index, String keywords, String type, Set<String> fields, int currentPage, int pageSize, boolean isHighlight) {
        // 搜索数据
        SearchResponse response = client.prepareSearch(index).setTypes(type)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                // 查询所有字段
                .setQuery(QueryBuilders.multiMatchQuery(keywords, fields.toArray(new String[fields.size()]))
                        // 分词器
                        .analyzer("ik_max_word"))
                // 高亮标签
                .highlighter(new HighlightBuilder().preTags("<span style=\"color:red\">").postTags("</span>").field("*"))
                // 分页
                .setFrom((currentPage - 1) * pageSize).setSize(pageSize)
                // 评分排序
                .setExplain(true)
                .execute().actionGet();

        // 获取查询结果集
        SearchHits searchHits = response.getHits();
        List<Object> result = new ArrayList<>();
        // 反射填充高亮
        for (SearchHit hit : searchHits) {
            Map<String, Object> source = hit.getSource();
            if (isHighlight) {
                // 获取对应的高亮域
                Map<String, HighlightField> highlight = hit.getHighlightFields();
                for (String field : fields) {
                    // 从设定的高亮域中取得指定域
                    HighlightField titleField = highlight.get(field);
                    if (titleField == null) {
                        continue;
                    }
                    // 取得定义的高亮标签
                    String texts = StringUtils.join(titleField.fragments());
                    source.put(field, texts);
                }
            }
            result.add(JSON.toJSON(source));
        }
        return new Page(currentPage, pageSize, (int) searchHits.totalHits(), result);
    }

    /**
     * @param index
     */
    public void reindex(String index) {
        SearchResponse scrollResp = client.prepareSearch(index)
                .setScroll(new TimeValue(60000))
                .setQuery(QueryBuilders.matchAllQuery())
                // max of 100 hits will be returned for
                .setSize(100).get();
        // Scroll until no hits are returned
        do {
            for (SearchHit hit : scrollResp.getHits().getHits()) {
                client.prepareIndex(index, hit.getType(), hit.getId()).setSource(hit.getSourceAsString()).execute().actionGet();
            }
            scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
        } while (scrollResp.getHits().getHits().length != 0);
    }
}
