package org.aisr.aisrinitialclient.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.aisr.aisrinitialclient.model.constants.Department;
import org.aisr.aisrinitialclient.model.constants.Role;
import org.aisr.aisrinitialclient.model.data.Recruit;
import org.aisr.aisrinitialclient.model.data.Staff;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RecruitTable {
    private final StringProperty id;
    private final StringProperty index;
    private final StringProperty fullName;
    private final StringProperty email;
    private final StringProperty department;
    private final StringProperty createdAt;

    public RecruitTable(Department department, int id, int index, String fullName, String email, LocalDateTime createdAt) {
        this.department =  new SimpleStringProperty(String.valueOf(department == null ? "" : department.getValue()));;
        this.id = new SimpleStringProperty(String.valueOf(id));
        this.index = new SimpleStringProperty(String.valueOf(index));
        this.fullName = new SimpleStringProperty(String.valueOf(fullName));
        this.email =  new SimpleStringProperty(String.valueOf(email));
        this.createdAt = new SimpleStringProperty(createdAt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm:ss")));
    }


    public static RecruitTable init(Recruit recruit, int index) {
        return new RecruitTable(
                recruit.getDepartment(),
                recruit.getId(),
                index,
                recruit.getFullName(),
                recruit.getEmail(),
                recruit.getCreatedAt()
        );
    }

    public String getCreatedAt() {
        return createdAt.get();
    }

    public StringProperty createdAtProperty() {
        return createdAt;
    }

    public String getDepartment() {
        return department.get();
    }

    public StringProperty departmentProperty() {
        return department;
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
}
