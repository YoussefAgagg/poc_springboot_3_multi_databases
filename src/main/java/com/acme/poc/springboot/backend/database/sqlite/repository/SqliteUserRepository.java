package com.acme.poc.springboot.backend.database.sqlite.repository;

import com.acme.poc.springboot.backend.database.sqlite.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface SqliteUserRepository extends JpaRepository<User,Integer> {
    @Override
    @Query("SELECT user FROM User user")
    List<User> findAll();
}
