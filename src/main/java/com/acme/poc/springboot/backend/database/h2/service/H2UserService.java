package com.acme.poc.springboot.backend.database.h2.service;

import com.acme.poc.springboot.backend.database.h2.entity.User;
import com.acme.poc.springboot.backend.database.h2.repository.H2UserRepository;
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
@Service("PoC_H2_UserService")
//@Transactional(transactionManager = "postgresqlUserTransactionManager")
public class H2UserService {

    private final static Logger log = LoggerFactory.getLogger(H2UserService.class);

    private final H2UserRepository userRepository;


    public H2UserService(H2UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public Page<User> getAllUsers(Pageable pageable, String[] sort) {
        return userRepository.findAll(pageable);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
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
