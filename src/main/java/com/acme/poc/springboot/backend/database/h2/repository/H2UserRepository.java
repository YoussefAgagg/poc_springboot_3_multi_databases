package com.acme.poc.springboot.backend.database.h2.repository;

import com.acme.poc.springboot.backend.database.h2.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface H2UserRepository extends CrudRepository<User, UUID>, PagingAndSortingRepository<User, UUID> {
}
