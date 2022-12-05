package com.acme.poc.springboot.backend.database.mysql.repository;

import com.acme.poc.springboot.backend.database.mysql.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface MySQLUserRepository extends CrudRepository<User, Long>, PagingAndSortingRepository<User, Long> {
}
