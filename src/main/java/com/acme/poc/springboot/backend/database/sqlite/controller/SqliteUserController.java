package com.acme.poc.springboot.backend.database.sqlite.controller;

import com.acme.poc.springboot.backend.database.sqlite.dto.UserDto;
import com.acme.poc.springboot.backend.database.sqlite.entity.User;
import com.acme.poc.springboot.backend.database.sqlite.mapper.SqliteUserMapper;
import com.acme.poc.springboot.backend.database.sqlite.service.SqliteUserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController("PoC_Sqlite_UserController")
@RequestMapping(path = "/api/v1/sqlite/users")
public class SqliteUserController {

    private final SqliteUserService userService;
    private final SqliteUserMapper userMapper;


    SqliteUserController(SqliteUserService userService, SqliteUserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }


    @GetMapping(path = "")
    public List<UserDto> getAllUsers(@PageableDefault(sort = { "id" },page = 0,size = 1)Pageable pageable, @RequestParam(defaultValue = "username,asc", required = false) String[] sort) {        // TODO Check if sort could just be: Sort sort (just lige Pageable pageable)
        List<User> users = userService.getAllUsers(pageable, sort);
        System.out.println(users);
        users.forEach(System.out::println);
        return users.stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }

    @PostMapping(path = "")
    public UserDto createUser(@RequestBody @NonNull @Valid UserDto userDto) {
        User user = userMapper.userDtoToUser(userDto);
        return userMapper.userToUserDto(userService.createUser(user));
    }

    @GetMapping(path = "{id}")
    public UserDto getUserById(@PathVariable/*(name = "uuid", required = true)*/ Integer id) {
        return userMapper.userToUserDto(userService.getUserById(id));
    }

//    @PutMapping(path = "")
//    public UserDto updateUser(@RequestBody @NonNull @Valid UserDto userDto) {
//        if (null == userDto.uuid()) throw new IllegalArgumentException("User UUID is missing!");
//        User user = userRepository
//                .findById(userDto.uuid())
//                .orElseThrow(EntityNotFoundException::new);
//        userMapper.updateUserFromUserDto(userDto, user);
//        return userMapper.userToUserDto(
//                userRepository.save(user)
//        );
//    }

}
