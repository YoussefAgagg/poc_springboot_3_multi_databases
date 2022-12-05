package com.acme.poc.springboot.backend.database.mariadb.repository;

import com.acme.poc.springboot.backend.database.mariadb.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface MariadbUserRepository extends CrudRepository<User, Long>, PagingAndSortingRepository<User, Long> {
}
