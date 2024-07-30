package com.expensesharing.com.expensesharing.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Mobile number is mandatory")
    @Pattern(regexp = "^\\d{10}$", message = "Mobile number should be 10 digits")
    private String mobile;

    public @NotBlank(message = "Name is mandatory") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Name is mandatory") String name) {
        this.name = name;
    }

    public @Email(message = "Email should be valid") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Email should be valid") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Mobile number is mandatory") @Pattern(regexp = "^\\d{10}$", message = "Mobile number should be 10 digits") String getMobile() {
        return mobile;
    }

    public void setMobile(@NotBlank(message = "Mobile number is mandatory") @Pattern(regexp = "^\\d{10}$", message = "Mobile number should be 10 digits") String mobile) {
        this.mobile = mobile;
    }

    public Long getId() {
        return id;
    }
}
