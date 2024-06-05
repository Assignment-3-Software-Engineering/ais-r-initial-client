package org.aisr.aisrinitialclient.model.data;

import org.aisr.aisrinitialclient.model.constants.Branch;
import org.aisr.aisrinitialclient.model.constants.ManagementLevel;
import org.aisr.aisrinitialclient.model.constants.Role;

public class ManagementStaff extends Staff {
    private ManagementLevel managementLevel;

    public ManagementStaff() {
        super(Role.MANAGEMENT_STAFF);
    }

    public ManagementLevel getManagementLevel() {
        return managementLevel;
    }

    public void setManagementLevel(ManagementLevel managementLevel) {
        this.managementLevel = managementLevel;
    }
}
