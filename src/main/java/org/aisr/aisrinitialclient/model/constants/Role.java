package org.aisr.aisrinitialclient.model.constants;

public enum Role {
    ADMIN_STAFF("Admin Staff"),
    MANAGEMENT_STAFF("Management Staff"),
    RECRUIT("Recruit");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Role of(String value) {
        for (Role role : Role.values()) {
            if (role.getValue().equals(value)) {
                return role;
            }
        }
        return null;
    }
}
