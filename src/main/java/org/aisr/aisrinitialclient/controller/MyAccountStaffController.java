package org.aisr.aisrinitialclient.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.aisr.aisrinitialclient.model.GlobalData;
import org.aisr.aisrinitialclient.model.UserSession;
import org.aisr.aisrinitialclient.model.data.Staff;
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

public class MyAccountStaffController implements Initializable {

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
    public Label lblStaffCreateDate;

    @FXML
    public Label lblStaffManagementLevel;

    @FXML
    public Label lblStaffId;

    @FXML
    public Label lblStaffBranch;

    @FXML
    public Label lblStaffPosition;

    @FXML
    public TextField txtEmail;

    @FXML
    public TextField txtPhone;

    @FXML
    public TextArea txtAddress;

    @FXML
    public TextField txtFullName;

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

        txtPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtPhone.setText(oldValue);
            } else if (newValue.length() > 10) {
                txtPhone.setText(newValue.substring(0, 10));
            }
        });

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
            StaffDto staff = GlobalData.getInstance().getSession().getLoggdinStaff();
            staff.setEmail(txtEmail.getText());
            staff.setPhoneNumber(txtPhone.getText());
            staff.setFullName(txtFullName.getText());
            staff.setAddress(txtAddress.getText());

            socket = new Socket(ServerConnectionInfo.DEFAULT_HOST, ServerConnectionInfo.DEFAULT_PORT);
            ObjectOutputStream out =new ObjectOutputStream(socket.getOutputStream()); // Set up object streams for communication over the socket
            ObjectInputStream in = new ObjectInputStream( socket.getInputStream());

            out.writeObject(ServerRequests.UPDATE_STAFF);
            out.writeObject(gson.toJson(staff));
            out.flush();

            Object responseStatus = in.readObject();
            if (responseStatus.equals(ServerResponse.OK)){
                Object responseBodyUser =  in.readObject();
                staff = gson.fromJson(responseBodyUser.toString(),StaffDto.class);
                GlobalData.getInstance().setSession( new UserSession(
                        staff,
                        GlobalData.getInstance().getSession().getLoggdinRecruit(),
                        GlobalData.getInstance().getSession().getLoggdinUser()));
                this.cleanForm();
                this.updateAccountInfo();
                UiServices.showMessage("Update Success","Staff details updated successfully");
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
        StaffDto staff = GlobalData.getInstance().getSession().getLoggdinStaff();
        lblCreateDate.setText("Created Date: "+formatDateTime(user.getCreatedAt()));
        lblUpdateDate.setText("Last Updated Date: "+formatDateTime(user.getUpdatedAt()));
        lblAccUsername.setText("Account Username: "+user.getUsername());
        lblAccountStatus.setText("Account Status: "+user.getStatus().getValue());
        lblAccRole.setText("Account Role: "+user.getRole().getValue());

        lblStaffCreateDate.setText("Created Date: "+formatDateTime(user.getCreatedAt()));
        lblStaffManagementLevel.setText("Management Level: "+(staff.getManagementLevel() != null ? staff.getManagementLevel().getValue() : "N/A"));
        lblStaffId.setText("Staff ID: "+staff.getStaffId());
        lblStaffBranch.setText("Branch: "+(staff.getBranch() != null ? staff.getBranch().getValue() : "N/A"));
        lblStaffPosition.setText("Position : "+(staff.getPositionType() != null ? staff.getPositionType().getValue() : "N/A"));

        txtFullName.setText(staff.getFullName());
        txtPhone.setText(staff.getPhoneNumber());
        txtEmail.setText(staff.getEmail());
        txtAddress.setText(staff.getAddress());
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
