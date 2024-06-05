package org.aisr.aisrinitialclient.model.constants;

public enum UserStatus {
    ACTIVE("Active"),
    INACTIVE("In Active");

    private final String value;
    UserStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static UserStatus of(String value) {
        for (UserStatus qualification : UserStatus.values()) {
            if (qualification.getValue().equals(value)) {
                return qualification;
            }
        }
        return null;
    }

}
