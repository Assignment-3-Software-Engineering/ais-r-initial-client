package org.aisr.aisrinitialclient.model.constants;

public enum PositionType {
    FULL_TIME("Full-time"),
    PART_TIME("Part-time"),
    VOLUNTEER("Volunteer");

    private final String value;

    PositionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PositionType of(String value) {
        for (PositionType type : PositionType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }
}

