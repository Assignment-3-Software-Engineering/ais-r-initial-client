package org.aisr.aisrinitialclient.model.constants;

public enum ManagementLevel {
    SENIOR_MANAGER("Senior manager"),
    MID_LEVEL_MANAGER("Mid level manager"),
    SUPERVISOR("Supervisor");

    private final String value;

    ManagementLevel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ManagementLevel of(String value) {
        for (ManagementLevel level : ManagementLevel.values()) {
            if (level.getValue().equals(value)) {
                return level;
            }
        }
        return null;
    }
}
