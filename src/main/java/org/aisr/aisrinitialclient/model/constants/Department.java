package org.aisr.aisrinitialclient.model.constants;

public enum Department {
    SOFTWARE("Software"),
    AEROSPACE("Aerospace"),
    MECHANICAL("Mechanical"),
    ELECTRONICS("Electronics");

    private final String value;

    Department(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Department of(String value) {
        for (Department department : Department.values()) {
            if (department.getValue().equals(value)) {
                return department;
            }
        }
        return null;
    }
}
