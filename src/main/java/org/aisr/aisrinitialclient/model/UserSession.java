package org.aisr.aisrinitialclient.model;

import org.aisr.aisrinitialclient.model.constants.Role;
import org.aisr.aisrinitialclient.model.data.Recruit;
import org.aisr.aisrinitialclient.model.data.Staff;
import org.aisr.aisrinitialclient.model.data.User;
import org.aisr.aisrinitialclient.model.dto.StaffDto;
import org.aisr.aisrinitialclient.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

public class UserSession {
    private final String sessionId;
    private final LocalDateTime loginTime;
    private StaffDto loggdinStaff;
    private  Recruit loggdinRecruit;
    private final User loggdinUser;

    public String getUsername(){
        if (this.loggdinUser.getRole().equals(Role.ADMIN_STAFF) || this.loggdinUser.getRole().equals(Role.MANAGEMENT_STAFF)){
            return this.loggdinStaff.getUsername()+" - "+loggdinUser.getRole().getValue();
        } else  if (this.loggdinUser.getRole().equals(Role.RECRUIT)){
            return this.loggdinRecruit.getUsername()+" - "+loggdinUser.getRole().getValue();
        }
        return "N/A";
    }

    public boolean isRecruit(){
        return this.loggdinUser.getRole().equals(Role.RECRUIT);
    }

    public boolean isStaff(){
        return StringUtils.equalsAny(this.loggdinUser.getRole().getValue(),Role.ADMIN_STAFF.getValue(),Role.MANAGEMENT_STAFF.getValue());
    }

    public String getUserFullName(){
        if (this.loggdinUser.getRole().equals(Role.ADMIN_STAFF) || this.loggdinUser.getRole().equals(Role.MANAGEMENT_STAFF)){
            return this.loggdinStaff.getFullName();
        } else  if (this.loggdinUser.getRole().equals(Role.RECRUIT)){
            return this.loggdinRecruit.getFullName();
        }
        return "N/A";
    }

    public UserSession(StaffDto staff,Recruit recruit,User user) {
        this.sessionId = StringUtil.generateUUID();
        this.loggdinStaff = staff;
        this.loggdinRecruit = recruit;
        this.loggdinUser = user;
        this.loginTime = LocalDateTime.now();
    }

    public Recruit getLoggdinRecruit() {
        return loggdinRecruit;
    }

    public void setLoggdinRecruit(Recruit loggdinRecruit) {
        this.loggdinRecruit = loggdinRecruit;
    }

    public StaffDto getLoggdinStaff() {
        return loggdinStaff;
    }

    public void setLoggdinStaff(StaffDto loggdinStaff) {
        this.loggdinStaff = loggdinStaff;
    }

    public User getLoggdinUser() {
        return loggdinUser;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public String getSessionId() {
        return sessionId;
    }
}
