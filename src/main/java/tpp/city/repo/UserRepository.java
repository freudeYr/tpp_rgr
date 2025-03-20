package tpp.city.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import tpp.city.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
}
