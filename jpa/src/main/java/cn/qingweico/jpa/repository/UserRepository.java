package cn.qingweico.jpa.repository;

import cn.qingweico.jpa.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author zqw
 * @date 2026/01/04
 */
public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(value = User.WITH_ROLES, type = EntityGraphType.FETCH)
    Optional<User> findWithRolesById(Long id);
}

