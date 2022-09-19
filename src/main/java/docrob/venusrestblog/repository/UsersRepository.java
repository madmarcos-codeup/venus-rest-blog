package docrob.venusrestblog.repository;

import docrob.venusrestblog.data.User;
import docrob.venusrestblog.dto.UserFetchDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsersRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);
    User findByEmail(String email);

//    @Query(nativeQuery = true)
//    List<UserFetchDTO> fetchUserDTOs();
}
