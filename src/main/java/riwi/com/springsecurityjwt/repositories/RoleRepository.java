package riwi.com.springsecurityjwt.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import riwi.com.springsecurityjwt.models.RoleEntity;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {



}
