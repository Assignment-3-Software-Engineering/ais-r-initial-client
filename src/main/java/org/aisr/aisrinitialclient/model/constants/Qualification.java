package org.aisr.aisrinitialclient.model.constants;

public enum Qualification {
    BACHELORS("Bachelors"),
    MASTERS("Masters"),
    PHD("PhD");

    private final String value;

    Qualification(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Qualification of(String value) {
        for (Qualification qualification : Qualification.values()) {
            if (qualification.getValue().equals(value)) {
                return qualification;
            }
        }
        return null;
    }
}
