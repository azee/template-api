package com.mycompany.template.repositories;

import com.mycompany.template.beans.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by IntelliJ IDEA.
 * User: azee
  */
public interface UserRepository extends PagingAndSortingRepository<User, String>{

    @Query("{ 'token' : ?0 }")
    User findByToken(String token);

    @Query("{ 'sid' : ?0 }")
    User findByCookie(String sid);

    @Query ("{ 'name' : ?0 }")
    User findByName(String name);

    @Query ("{ 'email' : ?0 }")
    User findByEmail(String email);
}
