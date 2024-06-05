package org.aisr.aisrinitialclient.service;

import org.aisr.aisrinitialclient.model.constants.Branch;
import org.aisr.aisrinitialclient.model.constants.ManagementLevel;
import org.aisr.aisrinitialclient.model.data.ManagementStaff;
import org.aisr.aisrinitialclient.model.data.Staff;

import java.io.IOException;
import java.util.Optional;

public class AppInitializeService {
    private final RecruitService recruitService;
    public final StaffService staffService;

    private final String defaultStaffMemberEmail = "admin@aisrinitialclient.com";
    private final String defaultStaffMemberPassword = "TestUser1";
    private final String defaultStaffMemberStaffID = "EMP0001";
    private final String defaultStaffMemberStaffUsername = "ADMINUSER";
    private final String defaultStaffMemberFullName = "AIS R Initial - Administrator";


    public AppInitializeService() throws IOException {
        this.recruitService = new RecruitService();
        this.staffService = new StaffService();
    }

    public void initialize() {
        try {
            this.loadStaffData();
            this.loadRecruitData();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadRecruitData() throws Exception {
        System.out.println("load recruit data");
        this.recruitService.loadData();
    }

    private void loadStaffData() throws IOException, ClassNotFoundException {
        System.out.println("load staff data");
        this.staffService.loadData();
    }

}
