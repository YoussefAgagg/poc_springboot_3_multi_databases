package com.acme.poc.springboot.backend.database.h2.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;


/**
 * ???
 */
@Entity(name = "User")
@Table(name = "userh2", indexes = {
        @Index(name = "idx_user_username", columnList = "username")
})
@AllArgsConstructor
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", nullable = false)
    private String username;
    private String firstName;
    private String lastName;
    @Column(name = "email", unique = true)
    @NotBlank(message = "Summary cannot be empty")
    private String email;
    @Column(name = "date_of_birth")
    private LocalDate birthday;

}
