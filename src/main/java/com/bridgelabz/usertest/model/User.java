package com.bridgelabz.usertest.model;

import com.bridgelabz.usertest.dto.UserDTO;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "UserDetails")
public class User {

    @Id
    @GeneratedValue
    private Integer userId;
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String email;
    private String password;
    private String token;
    private String dateOfBirth;
    private LocalDate date = LocalDate.now();

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime tokenCreationDate;

    public User(UserDTO dto) {
        super();
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.mobileNumber = dto.getMobileNumber();
        this.email = dto.getEmail();
        this.password = dto.getPassword();
        this.dateOfBirth = dto.getDateOfBirth();
    }

    public User() {

    }
}
