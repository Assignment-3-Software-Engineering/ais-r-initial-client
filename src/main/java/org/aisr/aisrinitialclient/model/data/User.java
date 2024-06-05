package org.aisr.aisrinitialclient.model.data;


import org.aisr.aisrinitialclient.model.constants.Role;
import org.aisr.aisrinitialclient.model.constants.UserStatus;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {
    private int id;
    private int referenceId;
    private UserStatus status;
    private Role role;
    private String username;
    private String password;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(LocalDateTime createdAt, int id, String password, int referenceId, Role role, UserStatus status, String token, LocalDateTime updatedAt, String username) {
        this.createdAt = createdAt;
        this.id = id;
        this.password = password;
        this.referenceId = referenceId;
        this.role = role;
        this.status = status;
        this.token = token;
        this.updatedAt = updatedAt;
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "createdAt=" + createdAt +
                ", id=" + id +
                ", referenceId=" + referenceId +
                ", status=" + status +
                ", role=" + role +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getId() == user.getId() && getReferenceId() == user.getReferenceId() && getStatus() == user.getStatus() && getRole() == user.getRole() && Objects.equals(getUsername(), user.getUsername()) && Objects.equals(getPassword(), user.getPassword()) && Objects.equals(getToken(), user.getToken()) && Objects.equals(getCreatedAt(), user.getCreatedAt()) && Objects.equals(getUpdatedAt(), user.getUpdatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getReferenceId(), getStatus(), getRole(), getUsername(), getPassword(), getToken(), getCreatedAt(), getUpdatedAt());
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
