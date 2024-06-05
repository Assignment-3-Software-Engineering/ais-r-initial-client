package org.aisr.aisrinitialclient.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.aisr.aisrinitialclient.model.GlobalData;
import org.aisr.aisrinitialclient.model.UserSession;
import org.aisr.aisrinitialclient.model.constants.Role;
import org.aisr.aisrinitialclient.model.data.Recruit;
import org.aisr.aisrinitialclient.model.data.Staff;
import org.aisr.aisrinitialclient.model.data.User;
import org.aisr.aisrinitialclient.model.dto.LoginDto;
import org.aisr.aisrinitialclient.model.dto.StaffDto;
import org.aisr.aisrinitialclient.service.AppInitializeService;
import org.aisr.aisrinitialclient.service.StaffService;
import org.aisr.aisrinitialclient.service.UiServices;
import org.aisr.aisrinitialclient.util.*;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    StaffService staffService = new StaffService();
    private Socket socket;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    @FXML
    public TextField txtUsername;

    @FXML
    public PasswordField txtPassword;

    @FXML
    public TextField txtToken;

    @FXML
    public Button btnLogin;

    @FXML
    public Label loginError;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginError.setVisible(false);

        txtUsername.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String filteredValue = newValue.replaceAll(",", "").toUpperCase();
                if (!newValue.equals(filteredValue)) {
                    txtUsername.setText(filteredValue);
                }
            }
        });

    }

    public void onClickLogin(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String token = txtToken.getText();

        try {
            socket = new Socket(ServerConnectionInfo.DEFAULT_HOST, ServerConnectionInfo.DEFAULT_PORT);
            ObjectOutputStream out =new ObjectOutputStream(socket.getOutputStream()); // Set up object streams for communication over the socket
            ObjectInputStream in = new ObjectInputStream( socket.getInputStream());

            out.writeObject(ServerRequests.AUTHENTICATE);
            out.writeObject(gson.toJson(StringUtils.isNotEmpty(token) ? new LoginDto(null,token,null) : new LoginDto(password,null,username)));
            out.flush();

            Object responseStatus = in.readObject();
            if (responseStatus.equals(ServerResponse.OK)){

                Object responseBodyUser =  in.readObject();
                User user = gson.fromJson(responseBodyUser.toString(),User.class);
                Object responseBodyUserData =  in.readObject();
                StaffDto staff = null;
                Recruit recruit = null;
                loginError.setVisible(false);
                if (user.getRole().equals(Role.ADMIN_STAFF) || user.getRole().equals(Role.MANAGEMENT_STAFF)){
                    staff = gson.fromJson(responseBodyUserData.toString(), StaffDto.class);
                } else if (user.getRole().equals(Role.RECRUIT)){
                    recruit = gson.fromJson(responseBodyUserData.toString(),Recruit.class);
                }
                AppInitializeService appInitializeService = new AppInitializeService();
                appInitializeService.initialize();
                GlobalData.getInstance().setSession( new UserSession(staff,recruit,user));
                if (GlobalData.getInstance().getSession().isRecruit()){
                    UiServices.navigateMyAccount(event);
                }else {
                    UiServices.navigateDashBoard(event);
                }
            }else if (responseStatus.equals(ServerResponse.UNAUTHORIZED)){
                System.out.println("Username or password is incorrect");
                loginError.setVisible(true);
            }
            in.close();

        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }

/*        Optional<Staff> memberByUsername = staffService.findByUsername(username);
        if (memberByUsername.isPresent()){
            Staff staff = memberByUsername.get();
            boolean matches = BCrypt.checkpw(password, staff.getPassword());
            if (!matches){
                System.out.println("Username or password is incorrect");
                loginError.setVisible(true);
            }else {
                loginError.setVisible(false);
                GlobalData.getInstance().setSession(new UserSession(staff));
                UiServices.navigateDashBoard(event);
            }
        }else {
            System.out.println( "Username or password is incorrect");
            loginError.setVisible(true);
        }*/
    }

}
