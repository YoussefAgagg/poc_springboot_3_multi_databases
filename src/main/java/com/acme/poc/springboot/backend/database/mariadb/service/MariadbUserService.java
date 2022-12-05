package com.acme.poc.springboot.backend.database.mariadb.service;

import com.acme.poc.springboot.backend.database.mariadb.entity.User;
import com.acme.poc.springboot.backend.database.mariadb.repository.MariadbUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;


/**
 * ???
 */
@Service("PoC_Mariadb_UserService")
//@Transactional(transactionManager = "postgresqlUserTransactionManager")
public class MariadbUserService {

    private final static Logger log = LoggerFactory.getLogger(MariadbUserService.class);

    private final MariadbUserRepository userRepository;


    public MariadbUserService(MariadbUserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public Page<User> getAllUsers(Pageable pageable, String[] sort) {
        return userRepository.findAll(pageable);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(UUID uuid) {
        return userRepository
                        .findById(uuid)
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
