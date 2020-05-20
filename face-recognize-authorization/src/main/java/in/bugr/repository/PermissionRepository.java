package in.bugr.repository;

import in.bugr.common.entity.User;
import in.bugr.common.repository.BaseRepository;
import in.bugr.entity.Permission;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author BugRui
 * @date 2020/4/24 下午3:15
 **/
@Repository
public interface PermissionRepository extends BaseRepository<Permission, Long> {
    /**
     * find Permission list by role
     *
     * @param role {@link User.Role}
     * @return permission
     */
    List<Permission> findByRole(User.Role role);

    /**
     * @param role
     * @return
     */
    List<Permission> findByMethodAndRoleIn(String method, Set<User.Role> role);
}
