package org.example.Entity;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "USERS")
@Data
public class UsersEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotNull(message = "First name should not be null")
    @Column(name = "FIRST_NAME", length = 500)
    private String firstName;

    @Column(name = "LAST_NAME", length = 500)
    private String lastName;

    @Email(message = "Email should be valid")
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE")
    private String phone;

}
