package org.aisr.aisrinitialclient.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.aisr.aisrinitialclient.model.GlobalData;
import org.aisr.aisrinitialclient.model.UserSession;
import org.aisr.aisrinitialclient.model.data.Recruit;
import org.aisr.aisrinitialclient.model.data.User;
import org.aisr.aisrinitialclient.model.dto.StaffDto;
import org.aisr.aisrinitialclient.service.UiServices;
import org.aisr.aisrinitialclient.util.*;
import org.apache.commons.lang3.StringUtils;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class MyAccountRecruitController implements Initializable {

    @FXML
    public Button btnNavigateDashboard;

    @FXML
    public Button btnNavigateMyAccount;

    @FXML
    public Button btnNavigateRecruitManagement;

    @FXML
    public Button btnNavigateStaffManagement;

    @FXML
    public Label lblUserFullName;

    @FXML
    public Label lblUserUsername;

    @FXML
    public Button btnLogout;

    @FXML
    public PasswordField txtPassword;

    @FXML
    public PasswordField txtPassword1;

    @FXML
    public Button btnSave;

    @FXML
    public Label lblCreateDate;

    @FXML
    public Label lblUpdateDate;

    @FXML
    public Label lblAccUsername;

    @FXML
    public Label lblAccountStatus;

    @FXML
    public Label lblAccRole;

    @FXML
    public TextField txtEmail;

    @FXML
    public TextField txtPhone;

    @FXML
    public TextArea txtAddress;

    @FXML
    public TextField txtFullName;

    @FXML
    public Label lblRecruitQualification;

    @FXML
    public Label lblRecruitEmail;

    @FXML
    public Label lblRecruitInterview;

    @FXML
    public Label lblRecruitDepartment;

    @FXML
    public Label lblRecruitCreatedBy;

    @FXML
    public Button btnUpdate;

    private Socket socket;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    @FXML
    public void logout(ActionEvent event){
        GlobalData.getInstance().setSession(null);
        UiServices.navigateLogin(event);
    }

    @FXML
    public void navigateStaffManagement(ActionEvent event){
        UiServices.navigateStaffManagement(event);
    }

    @FXML
    public void navigateRecruitManagement(ActionEvent event){
        UiServices.navigateRecruitManagement(event);
    }

    @FXML
    public void navigateDashboard(ActionEvent event){
        UiServices.navigateDashBoard(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblUserFullName.setText(GlobalData.getInstance().getSession().getUserFullName());
        lblUserUsername.setText(GlobalData.getInstance().getSession().getUsername());
        this.updateAccountInfo();
        this.cleanForm();
        this.initNavBar();
    }

    private void initNavBar(){
        btnNavigateDashboard.setDisable(GlobalData.getInstance().getSession().isRecruit());
        btnNavigateRecruitManagement.setDisable(GlobalData.getInstance().getSession().isRecruit());
        btnNavigateStaffManagement.setDisable(GlobalData.getInstance().getSession().isRecruit());
    }

    @FXML
    public void onClickSave(ActionEvent event) {
        String password = txtPassword.getText();
        String password1 = txtPassword1.getText();

        try {

            if (StringUtils.isAnyEmpty(password,password1)){
                UiServices.showAlert("Password Invalid", "Please Enter password");
                return;
            }

            if (!password1.equals(password)){
                UiServices.showAlert("Password Invalid", "The two passwords you entered do not match.");
                return;
            }

            socket = new Socket(ServerConnectionInfo.DEFAULT_HOST, ServerConnectionInfo.DEFAULT_PORT);
            ObjectOutputStream out =new ObjectOutputStream(socket.getOutputStream()); // Set up object streams for communication over the socket
            ObjectInputStream in = new ObjectInputStream( socket.getInputStream());

            out.writeObject(ServerRequests.UPDATE_USER);
            out.writeObject(GlobalData.getInstance().getSession().getLoggdinUser().getId());
            out.writeObject(password);
            out.flush();

            Object responseStatus = in.readObject();
            if (responseStatus.equals(ServerResponse.OK)){
                Object responseBodyUser =  in.readObject();
                User user = gson.fromJson(responseBodyUser.toString(),User.class);
                GlobalData.getInstance().setSession( new UserSession(
                        GlobalData.getInstance().getSession().getLoggdinStaff(),
                        GlobalData.getInstance().getSession().getLoggdinRecruit(),user));
                this.cleanForm();
                this.updateAccountInfo();
                UiServices.showMessage("Update Success","Password Change success");
            }else if (responseStatus.equals(ServerResponse.NOT_FOUND)){
                Object errorMessage =  in.readObject();
                UiServices.showAlert("Update Failed",errorMessage.toString());

            }else if (responseStatus.equals(ServerResponse.SERVER_ERROR)){
                Object errorMessage =  in.readObject();
                System.out.println(errorMessage);
                UiServices.showAlert("Update Failed",errorMessage.toString());
                throw new RuntimeException(String.valueOf(errorMessage));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    public void onClickUpdate(ActionEvent event) {
        try {
            Recruit recruit = GlobalData.getInstance().getSession().getLoggdinRecruit();
            recruit.setEmail(txtEmail.getText());
            recruit.setPhoneNumber(txtPhone.getText());
            recruit.setFullName(txtFullName.getText());
            recruit.setAddress(txtAddress.getText());

            socket = new Socket(ServerConnectionInfo.DEFAULT_HOST, ServerConnectionInfo.DEFAULT_PORT);
            ObjectOutputStream out =new ObjectOutputStream(socket.getOutputStream()); // Set up object streams for communication over the socket
            ObjectInputStream in = new ObjectInputStream( socket.getInputStream());

            out.writeObject(ServerRequests.UPDATE_RECRUIT);
            out.writeObject(gson.toJson(recruit));
            out.flush();

            Object responseStatus = in.readObject();
            if (responseStatus.equals(ServerResponse.OK)){
                Object responseBodyUser =  in.readObject();
                recruit = gson.fromJson(responseBodyUser.toString(), Recruit.class);

                GlobalData.getInstance().setSession( new UserSession(
                        GlobalData.getInstance().getSession().getLoggdinStaff(),
                        recruit,
                        GlobalData.getInstance().getSession().getLoggdinUser()));
                this.cleanForm();
                this.updateAccountInfo();
                UiServices.showMessage("Update Success","Recruit details updated successfully");
            }else if (responseStatus.equals(ServerResponse.NOT_FOUND)){
                Object errorMessage =  in.readObject();

            }else if (responseStatus.equals(ServerResponse.SERVER_ERROR)){
                Object errorMessage =  in.readObject();
                System.out.println(errorMessage);
                throw new RuntimeException(String.valueOf(errorMessage));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateAccountInfo(){
        User user = GlobalData.getInstance().getSession().getLoggdinUser();
        Recruit recruit = GlobalData.getInstance().getSession().getLoggdinRecruit();
        lblCreateDate.setText("Created Date: "+formatDateTime(user.getCreatedAt()));
        lblUpdateDate.setText("Last Updated Date: "+formatDateTime(user.getUpdatedAt()));
        lblAccUsername.setText("Account Username: "+user.getUsername());
        lblAccountStatus.setText("Account Status: "+user.getStatus().getValue());
        lblAccRole.setText("Account Role: "+user.getRole().getValue());

        lblRecruitQualification.setText("Highest qualification: "+(recruit.getHighestQualificationLevel() != null ? recruit.getHighestQualificationLevel().getValue() : "N/A"));
        lblRecruitEmail.setText("Email: "+recruit.getEmail());
        lblRecruitInterview.setText("Interview Date: "+(recruit.getInterviewDate() != null ? recruit.getInterviewDate() : "N/a"));
        lblRecruitDepartment.setText("Departments: "+(recruit.getDepartment() != null ? recruit.getDepartment().getValue() : "N/A"));
        lblRecruitCreatedBy.setText("Created By: "+(recruit.getCreatedBy() != null ? recruit.getCreatedBy().getFullName() : "N/A"));

        txtFullName.setText(recruit.getFullName());
        txtPhone.setText(recruit.getPhoneNumber());
        txtEmail.setText(recruit.getEmail());
        txtAddress.setText(recruit.getAddress());
    }

    public void cleanForm(){
        txtPassword.clear();
        txtPassword1.clear();
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm a");
        return dateTime.format(formatter);
    }


}
