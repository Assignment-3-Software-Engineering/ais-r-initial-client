package org.aisr.aisrinitialclient.model.data;

import org.aisr.aisrinitialclient.model.constants.Branch;
import org.aisr.aisrinitialclient.model.constants.Role;
import org.aisr.aisrinitialclient.util.StringUtil;
import org.mindrot.jbcrypt.BCrypt;

public class Staff {
    private int id;
    private Role role;
    private String fullName;
    private String address;
    private String phoneNumber; // Must contain 10 digits
    private String email;
    private String username;
    private String password;
    private String staffId;
    private Branch branch;

    public Staff(Role role) {
        this.role = role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

