package org.aisr.aisrinitialclient.model.constants;

public enum Location {
    MELBOURNE("Melbourne"),
    SYDNEY("Sydney"),
    BRISBANE("Brisbane"),
    ADELAIDE("Adelaide");

    private final String value;

    Location(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Location of(String value) {
        for (Location location : Location.values()) {
            if (location.getValue().equals(value)) {
                return location;
            }
        }
        return null;
    }
}
