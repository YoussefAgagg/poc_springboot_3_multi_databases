package com.acme.poc.springboot.backend.database.mariadb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


/**
 * User DTO
 *
 * See also:
 *      - https://github.com/javahippie/jukebox (for alternative to Lombok @Builder)          TODO Check this out!
 */

//@Json
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto  {
        private Long id;
        @JsonProperty(value = "username", required = true)
        private String username;
        private String firstName;
        private String lastName;
        @JsonProperty(value = "email", required = true)
        private String email;
        @JsonProperty(value = "birthday")
        private LocalDate birthday;

}
