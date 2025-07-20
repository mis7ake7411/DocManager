package com.docmanager.repository.users;

import com.docmanager.model.entity.Users;
import com.docmanager.repository.BaseRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface UsersRepository extends BaseRepository<Users, Long> {

  @Query("SELECT u FROM Users u LEFT JOIN FETCH u.roles WHERE u.account = :account")
  Optional<Users> findByAccount(String account);
}