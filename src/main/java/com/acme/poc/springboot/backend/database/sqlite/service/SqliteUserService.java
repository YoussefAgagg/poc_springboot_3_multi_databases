package com.acme.poc.springboot.backend.database.sqlite.service;

import com.acme.poc.springboot.backend.database.sqlite.entity.User;
import com.acme.poc.springboot.backend.database.sqlite.repository.SqliteUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * ???
 */
@Service("PoC_Sqlite_UserService")
//@Transactional(transactionManager = "postgresqlUserTransactionManager")
public class SqliteUserService {

    private final static Logger log = LoggerFactory.getLogger(SqliteUserService.class);

    private final SqliteUserRepository userRepository;


    public SqliteUserService(SqliteUserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<User> getAllUsers(Pageable pageable, String[] sort) {

        return userRepository.findAll();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(Integer id) {
        return userRepository
                        .findById(id)
                        .orElseThrow(EntityNotFoundException::new);
    }

//    public User updateUser(UserDto userDto) {
//        User userSaved = userRepository
//                .findById(userDto.uuid())
//                .orElseThrow(EntityNotFoundException::new);
//        userMapper.updateUserFromUserDto(userDto, user);
//        return userRepository.save(user);
//    }

}
