package gigov.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import gigov.blog.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByName(String name);
}
