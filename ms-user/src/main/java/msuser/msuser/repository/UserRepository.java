package msuser.msuser.repository;

import org.springframework.data.repository.CrudRepository;
import msuser.msuser.entity.User;


public interface UserRepository extends CrudRepository<User, Long> {

}

