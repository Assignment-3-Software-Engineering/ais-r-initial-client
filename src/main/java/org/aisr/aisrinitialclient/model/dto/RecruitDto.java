package org.aisr.aisrinitialclient.model.dto;

import org.aisr.aisrinitialclient.model.constants.*;
import org.aisr.aisrinitialclient.model.data.*;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class RecruitDto  implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String fullName;
    private String address;
    private String phoneNumber;
    private String email;
    private String username;
    private String password;
    private LocalDate interviewDate;
    private Qualification highestQualificationLevel;
    private Department department;
    private StaffDto createdBy;
    private User user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static RecruitDto init(Recruit recruit) {
        RecruitDto dto = new RecruitDto();
        dto.setId(recruit.getId());
        dto.setFullName(recruit.getFullName());
        dto.setAddress(recruit.getAddress());
        dto.setPhoneNumber(recruit.getPhoneNumber());
        dto.setEmail(recruit.getEmail());
        dto.setUsername(recruit.getUsername());
        dto.setPassword(recruit.getPassword());
        dto.setInterviewDate(recruit.getInterviewDate());
        dto.setHighestQualificationLevel(recruit.getHighestQualificationLevel());
        dto.setDepartment(recruit.getDepartment());
    //    dto.setCreatedBy(recruit.getCreatedBy());
        dto.setCreatedAt(recruit.getCreatedAt());
        dto.setUpdatedAt(recruit.getUpdatedAt());
        return dto;
    }

    public static List<RecruitDto> initList(List<Recruit> recruitList) {
        return recruitList.stream()
                .map(RecruitDto::init)
                .collect(Collectors.toList());
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public StaffDto getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(StaffDto createdBy) {
        this.createdBy = createdBy;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Qualification getHighestQualificationLevel() {
        return highestQualificationLevel;
    }

    public void setHighestQualificationLevel(Qualification highestQualificationLevel) {
        this.highestQualificationLevel = highestQualificationLevel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(LocalDate interviewDate) {
        this.interviewDate = interviewDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
