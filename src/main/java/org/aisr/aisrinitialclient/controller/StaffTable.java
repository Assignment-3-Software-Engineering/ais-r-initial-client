package org.aisr.aisrinitialclient.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.aisr.aisrinitialclient.model.constants.Branch;
import org.aisr.aisrinitialclient.model.constants.ManagementLevel;
import org.aisr.aisrinitialclient.model.constants.PositionType;
import org.aisr.aisrinitialclient.model.constants.Role;
import org.aisr.aisrinitialclient.model.data.AdministrationStaff;
import org.aisr.aisrinitialclient.model.data.ManagementStaff;
import org.aisr.aisrinitialclient.model.data.Staff;
import org.aisr.aisrinitialclient.model.dto.StaffDto;


public class StaffTable {

    private final StringProperty id;
    private final StringProperty index;
    private final StringProperty role;
    private final StringProperty fullName;
    private final StringProperty address;
    private final StringProperty phoneNumber;
    private final StringProperty email;
    private final StringProperty username;
    private final StringProperty staffId;
    private final StringProperty branch;
    private final StringProperty positionType;
    private final StringProperty managementLevel;

    public StaffTable( int index,int id,String address, Role role, String fullName, String phoneNumber, String email, String username, String staffId, Branch branch, PositionType positionType, ManagementLevel managementLevel) {
        this.index =  new SimpleStringProperty(String.valueOf(index));
        this.id =  new SimpleStringProperty(String.valueOf(id));
        this.fullName =  new SimpleStringProperty(String.valueOf(fullName));
        this.role =  new SimpleStringProperty(String.valueOf(role.getValue()));
        this.staffId =  new SimpleStringProperty(String.valueOf(staffId));
        this.email =  new SimpleStringProperty(String.valueOf(email));
        this.address =  new SimpleStringProperty(String.valueOf(address == null ? "N/A" : address));
        this.phoneNumber =  new SimpleStringProperty(String.valueOf(phoneNumber == null ? "N/A" : phoneNumber));
        this.username =  new SimpleStringProperty(String.valueOf(username));
        this.branch =  new SimpleStringProperty(String.valueOf(branch.getValue()));
        this.positionType =  new SimpleStringProperty(String.valueOf(positionType == null ? "N/A" : positionType.getValue()));
        this.managementLevel =  new SimpleStringProperty(String.valueOf(managementLevel == null ? "N/A" : managementLevel.getValue()));
    }

    public static StaffTable init(StaffDto staff, int index) {

        PositionType positionType = null;
        ManagementLevel managementLevel = null;

        if (staff.getRole().equals(Role.ADMIN_STAFF)){
            AdministrationStaff administrationStaff = StaffDto.initAdministrationStaff(staff);
            if (administrationStaff.getPositionType() != null){
                positionType = administrationStaff.getPositionType();
            }
        } else if (staff.getRole().equals(Role.MANAGEMENT_STAFF)){
            ManagementStaff managementStaff = StaffDto.initManagementStaff(staff);
            if (managementStaff.getManagementLevel() != null){
                managementLevel = managementStaff.getManagementLevel();
            }
        }

        return new StaffTable(
                index,
                staff.getId(),
                staff.getAddress(),
                staff.getRole(),
                staff.getFullName(),
                staff.getPhoneNumber(),
                staff.getEmail(),
                staff.getUsername(),
                staff.getStaffId(),
                staff.getBranch(),
                positionType,
                managementLevel
        );
    }


    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public String getBranch() {
        return branch.get();
    }

    public StringProperty branchProperty() {
        return branch;
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getFullName() {
        return fullName.get();
    }

    public StringProperty fullNameProperty() {
        return fullName;
    }

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public String getIndex() {
        return index.get();
    }

    public StringProperty indexProperty() {
        return index;
    }

    public String getManagementLevel() {
        return managementLevel.get();
    }

    public StringProperty managementLevelProperty() {
        return managementLevel;
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public String getPositionType() {
        return positionType.get();
    }

    public StringProperty positionTypeProperty() {
        return positionType;
    }

    public String getRole() {
        return role.get();
    }

    public StringProperty roleProperty() {
        return role;
    }

    public String getStaffId() {
        return staffId.get();
    }

    public StringProperty staffIdProperty() {
        return staffId;
    }

    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }
}
