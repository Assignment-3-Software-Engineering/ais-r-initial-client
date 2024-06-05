package org.aisr.aisrinitialclient.model.dto;

import java.io.Serializable;
import java.util.Objects;

public class LoginDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private String token;

    @Override
    public String toString() {
        return "LoginDto{" +
                "password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginDto loginDto = (LoginDto) o;
        return Objects.equals(getUsername(), loginDto.getUsername()) && Objects.equals(getPassword(), loginDto.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword());
    }

    public LoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginDto(String password, String token, String username) {
        this.password = password;
        this.token = token;
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
