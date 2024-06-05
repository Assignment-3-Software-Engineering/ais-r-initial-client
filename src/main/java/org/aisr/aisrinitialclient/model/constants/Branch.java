package org.aisr.aisrinitialclient.model.constants;

public enum Branch {
    MELBOURNE("Melbourne"),
    SYDNEY("Sydney"),
    BRISBANE("Brisbane"),
    ADELAIDE("Adelaide");

    private final String value;

    Branch(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Branch of(String value) {
        for (Branch branch : Branch.values()) {
            if (branch.getValue().equals(value)) {
                return branch;
            }
        }
        return null;
    }
}
