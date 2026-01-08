package cn.qingweico.jpa.repository;

import cn.qingweico.jpa.entity.Role;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author zqw
 * @date 2026/01/04
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    @EntityGraph(attributePaths = {"users"})
    Optional<Role> findWithUsersByCode(String code);
}

