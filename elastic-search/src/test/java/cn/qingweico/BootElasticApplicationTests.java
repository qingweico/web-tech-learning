package cn.qingweico;

import cn.qingweico.entity.User;
import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class BootElasticApplicationTests {
    @Autowired
    RestHighLevelClient restHighLevelClient;



    @Autowired
    User user;

    // 创建索引(数据库的表)
    @Test
    void createIndexRequestTest() throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("user");
        CreateIndexResponse response = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    // 查找索引
    @Test
    void getIndexRequestTest() throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest("user");
        boolean exists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    // 删除索引
    @Test
    void deleteIndexRequestTest() throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("user");
        AcknowledgedResponse delete = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        System.out.println(delete.isAcknowledged());
    }

    // 添加文档(数据库表中的一行记录)
    @Test
    void addDocumentTest() throws IOException {
        user.setId(1L);
        user.setName("user01");
        IndexRequest request = new IndexRequest("user");
        // 不写生成默认id
        request.id("1");
        // 设置_source 的类型, 可以为String, 对象, JSON等;这里为JSON;
        request.source(JSON.toJSON(user), XContentType.JSON);
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        System.out.println(response.status());
        System.out.println(response);
    }

    // 判断文档是否存在
    @Test
    void isExistsTest() throws IOException {
        GetRequest request = new GetRequest("user", "1");
        request.fetchSourceContext(new FetchSourceContext(false));
        request.storedFields("_none_");
        boolean isExists = restHighLevelClient.exists(request, RequestOptions.DEFAULT);
        System.out.println(isExists);
    }

    // 获取文档
    @Test
    void getDocumentTest() throws IOException {
        GetRequest request = new GetRequest("user", "1");
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        System.out.println(response.getSourceAsString());
    }

    // 更新文档
    @Test
    void updateDocumentTest() throws IOException {
        UpdateRequest request = new UpdateRequest("user", "3");
        user.setId(3L);
        user.setName("HI");
        // 设置_source 的类型, 可以为String, 对象, JSON等;
        // 更新文档时要注意数据类型要一致, 不一致则添加
        request.doc(JSON.toJSONString(user), XContentType.JSON);
        UpdateResponse response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        System.out.println(response);
        System.out.println(response.status());
    }

    // 删除文档
    @Test
    void deleteDocumentTest() throws IOException {
        DeleteRequest request = new DeleteRequest("user", "1");
        DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        System.out.println(response.status());
    }

    // 批量插入数据
    @Test
    void batchTest() throws IOException {
        BulkRequest request = new BulkRequest();
        for (int i = 0; i < 10; i++) {
            user.setId(i + 1L);
            user.setName("user" + (i + 1));
            request.add(new IndexRequest("user")
                    .id("" + (i + 1))
                    // 设置_source 的类型, 可以为String, 对象, JSON等; 这里为String
                    .source(JSON.toJSONString(user), XContentType.JSON));
        }
        BulkResponse responses = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        System.out.println(responses.hasFailures());
    }
}
