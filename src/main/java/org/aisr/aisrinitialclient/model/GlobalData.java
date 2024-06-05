package org.aisr.aisrinitialclient.model;

import org.aisr.aisrinitialclient.model.data.Recruit;
import org.aisr.aisrinitialclient.model.data.Staff;
import org.aisr.aisrinitialclient.model.dto.StaffDto;

import java.util.ArrayList;
import java.util.List;

public class GlobalData {
    private static GlobalData instance;
    private final List<Recruit> recruitList;
    private final List<StaffDto> staffList;
    private UserSession session;

    private GlobalData() {
        recruitList = new ArrayList<>();
        staffList = new ArrayList<>();
        session = null;
    }

    public static synchronized GlobalData getInstance() {
        if (instance == null) {
            instance = new GlobalData();
        }
        return instance;
    }

    public List<Recruit> getRecruitList() {
        return recruitList;
    }

    public List<StaffDto> getStaffList() {
        return staffList;
    }

    public UserSession getSession() {
        return session;
    }

    public void setSession(UserSession session) {
        this.session = session;
    }
}
