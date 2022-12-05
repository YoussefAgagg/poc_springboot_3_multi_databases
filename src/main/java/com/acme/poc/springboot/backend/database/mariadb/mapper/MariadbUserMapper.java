package com.acme.poc.springboot.backend.database.mariadb.mapper;

import com.acme.poc.springboot.backend.database.mariadb.dto.UserDto;
import com.acme.poc.springboot.backend.database.mariadb.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


/**
 * User mapper
 */
//@Component("PoC_PostgreSQL_UserMapper")
@Mapper( componentModel = "spring")
public interface MariadbUserMapper {


    UserDto userToUserDto(User user);

    User userDtoToUser(UserDto userDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromUserDto(UserDto userDto, @MappingTarget User user);

//    @AfterMapping
//    default void linkTasks(@MappingTarget User user) {
//        user.getTasks().forEach(task -> task.setUser(user));
//    }

}
