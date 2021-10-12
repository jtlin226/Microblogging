package com.revature.Micro.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SPRING SECURITY
 */
@Getter
@Setter
@NoArgsConstructor

public class AuthenticationRequest {
    private String username;
    private String password;

    /**
     * Full Constructor for AuthenticationRequest
     * @param username
     * @param password
     */
    public AuthenticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}