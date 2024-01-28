package cn.qingweico.repo;

import cn.qingweico.entity.FriendLink;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author zqw
 * @date 2021/9/10
 */
@Repository
public interface FriendLinkRepository extends MongoRepository<FriendLink, String> {
}
