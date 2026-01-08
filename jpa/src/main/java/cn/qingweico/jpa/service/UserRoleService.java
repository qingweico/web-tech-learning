package cn.qingweico.jpa.service;

import cn.qingweico.jpa.entity.User;
import cn.qingweico.jpa.repository.RoleRepository;
import cn.qingweico.jpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author zqw
 * @date 2026/01/04
 */
@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Optional<UserDetail> findWithRolesById(Long userId) {
        return userRepository.findWithRolesById(userId)
                .map(this::toDetail);
    }

    @Transactional(readOnly = true)
    public Optional<RoleWithUsers> findRoleWithUsersByCode(String roleCode) {
        return roleRepository.findWithUsersByCode(roleCode)
                .map(role -> new RoleWithUsers(
                        role.getId(),
                        role.getName(),
                        role.getCode(),
                        role.getUsers().stream()
                                .map(user -> new RoleWithUsers.UserBrief(user.getId(), user.getUsername()))
                                .toList()
                ));
    }


    private UserDetail toDetail(User account) {
        var roles = account.getRoles().stream()
                .map(role -> new RoleInfo(role.getName(), role.getCode()))
                .collect(Collectors.toList());
        return new UserDetail(
                account.getId(),
                account.getUsername(),
                roles
        );
    }


    public record UserDetail(Long id,
                             String username,
                             List<RoleInfo> roles) {
    }


    public record RoleInfo(String name, String code) {
    }

    public record RoleWithUsers(Long id, String name, String code, List<UserBrief> users) {
        public record UserBrief(Long id, String username) {
        }
    }
}

