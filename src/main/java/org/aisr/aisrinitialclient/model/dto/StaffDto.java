package org.aisr.aisrinitialclient.model.dto;


import org.aisr.aisrinitialclient.model.constants.Branch;
import org.aisr.aisrinitialclient.model.constants.ManagementLevel;
import org.aisr.aisrinitialclient.model.constants.PositionType;
import org.aisr.aisrinitialclient.model.constants.Role;
import org.aisr.aisrinitialclient.model.data.AdministrationStaff;
import org.aisr.aisrinitialclient.model.data.ManagementStaff;
import org.aisr.aisrinitialclient.model.data.Staff;
import org.aisr.aisrinitialclient.model.data.User;


public class StaffDto {
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
    private User user;
    private ManagementLevel managementLevel;
    private PositionType positionType;

    @Override
    public String toString() {
        return "StaffDto{" +
                "address='" + address + '\'' +
                ", id=" + id +
                ", role=" + role +
                ", fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", staffId='" + staffId + '\'' +
                ", branch=" + branch +
                ", user=" + user +
                ", managementLevel=" + managementLevel +
                ", positionType=" + positionType +
                '}';
    }

    public StaffDto(int id) {
        this.id = id;
    }

    public StaffDto() {
    }

    public static StaffDto init(Staff staff){
        StaffDto dto = new StaffDto();
        dto.setId(staff.getId());
        dto.setRole(staff.getRole());
        dto.setFullName(staff.getFullName());
        dto.setAddress(staff.getAddress());
        dto.setPhoneNumber(staff.getPhoneNumber());
        dto.setEmail(staff.getEmail());
        dto.setUsername(staff.getUsername());
        dto.setStaffId(staff.getStaffId());
        dto.setBranch(staff.getBranch());
      //  dto.setManagementLevel(staff.getManagementLevel());
     //   dto.setPositionType(staff.getPositionType());
        return dto;
    }

    public static AdministrationStaff initAdministrationStaff(StaffDto dto){
        AdministrationStaff staff = new AdministrationStaff();

        staff.setId(dto.getId());
        staff.setRole(dto.getRole());
        staff.setFullName(dto.getFullName());
        staff.setAddress(dto.getAddress());
        staff.setPhoneNumber(dto.getPhoneNumber());
        staff.setEmail(dto.getEmail());
        staff.setUsername(dto.getUsername());
        staff.setStaffId(dto.getStaffId());
        staff.setBranch(dto.getBranch());
        staff.setPositionType(dto.getPositionType());

        return staff;
    }

    public static ManagementStaff initManagementStaff(StaffDto dto){
        ManagementStaff staff = new ManagementStaff();
        staff.setId(dto.getId());
        staff.setRole(dto.getRole());
        staff.setFullName(dto.getFullName());
        staff.setAddress(dto.getAddress());
        staff.setPhoneNumber(dto.getPhoneNumber());
        staff.setEmail(dto.getEmail());
        staff.setUsername(dto.getUsername());
        staff.setStaffId(dto.getStaffId());
        staff.setBranch(dto.getBranch());
        staff.setManagementLevel(dto.getManagementLevel());
        return staff;
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

    public ManagementLevel getManagementLevel() {
        return managementLevel;
    }

    public void setManagementLevel(ManagementLevel managementLevel) {
        this.managementLevel = managementLevel;
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

    public PositionType getPositionType() {
        return positionType;
    }

    public void setPositionType(PositionType positionType) {
        this.positionType = positionType;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
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
