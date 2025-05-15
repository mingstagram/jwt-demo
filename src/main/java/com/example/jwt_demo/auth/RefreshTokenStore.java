package com.example.jwt_demo.auth;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class RefreshTokenStore {

    private final ConcurrentHashMap<String, String> store = new ConcurrentHashMap<>();

    public void save(String username, String refreshToken) {
        store.put(username, refreshToken);
    }

    public String get(String username) {
        return store.get(username);
    }

    public void remove(String username) {
        store.remove(username);
    }

    public boolean validate(String username, String token) {
        return token.equals(store.get(username));
    }

}
