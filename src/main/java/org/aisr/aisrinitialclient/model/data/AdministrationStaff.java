package org.aisr.aisrinitialclient.model.data;

import org.aisr.aisrinitialclient.model.constants.PositionType;
import org.aisr.aisrinitialclient.model.constants.Role;

public class AdministrationStaff extends Staff{
    private PositionType positionType;

    public AdministrationStaff() {
        super(Role.ADMIN_STAFF);
    }

    public PositionType getPositionType() {
        return positionType;
    }

    public void setPositionType(PositionType positionType) {
        this.positionType = positionType;
    }
}
