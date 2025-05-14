package com.example.jwt_demo.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class User {

    @Id @GeneratedValue
    private Long id;

    private String username;
    private String password;
    private String role;  // ROLE_USER, ROLE_ADMIN 등
}