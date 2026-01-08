package cn.qingweico.jpa.controller;

import cn.qingweico.jpa.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zqw
 * @date 2026/01/04
 */
@RestController
@RequestMapping("/rel-user-role")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;

    @GetMapping("/listRoleByUser")
    public ResponseEntity<UserRoleService.UserDetail> listRoleByUser(@RequestParam Long id) {
        return userRoleService.findWithRolesById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/listUserByRole")
    public ResponseEntity<UserRoleService.RoleWithUsers> listUserByRole(@RequestParam String role) {
        return userRoleService.findRoleWithUsersByCode(role)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}

