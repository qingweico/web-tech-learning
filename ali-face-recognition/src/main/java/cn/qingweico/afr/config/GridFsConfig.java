package cn.qingweico.afr.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zqw
 * @date 2021/9/10
 */
@Configuration
public class GridFsConfig {

    @Value("${spring.data.mongodb.database}")
    private String mongodb;

    /**
     * @param mongoClient {@link com.mongodb.client.MongoClient} other than {@link com.mongodb.MongoClient}
     * @return GridFSBucket
     */
    @Bean
    public GridFSBucket gridFsBucket(MongoClient mongoClient) {
        MongoDatabase mongodbDatabase = mongoClient.getDatabase(mongodb);
        return GridFSBuckets.create(mongodbDatabase);
    }
}
