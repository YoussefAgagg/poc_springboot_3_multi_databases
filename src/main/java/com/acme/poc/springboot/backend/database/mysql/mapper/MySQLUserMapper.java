package com.acme.poc.springboot.backend.database.mysql.mapper;

import com.acme.poc.springboot.backend.database.mysql.dto.UserDto;
import com.acme.poc.springboot.backend.database.mysql.entity.User;
import org.mapstruct.*;


/**
 * User mapper
 */
//@Component("PoC_PostgreSQL_UserMapper")
@Mapper( componentModel = "spring")
public interface MySQLUserMapper {


    UserDto userToUserDto(User user);

    User userDtoToUser(UserDto userDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromUserDto(UserDto userDto, @MappingTarget User user);

//    @AfterMapping
//    default void linkTasks(@MappingTarget User user) {
//        user.getTasks().forEach(task -> task.setUser(user));
//    }

}
