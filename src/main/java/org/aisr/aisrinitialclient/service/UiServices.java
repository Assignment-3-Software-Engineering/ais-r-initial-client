package org.aisr.aisrinitialclient.service;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.aisr.aisrinitialclient.model.GlobalData;

import java.io.IOException;

public class UiServices {

    public static void navigateLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(UiServices.class.getResource("/org/aisr/aisrinitialclient/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setFullScreen(false);
            stage.setResizable(false);
            stage.setTitle("AIS R Initial - Login");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void navigateDashBoard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(UiServices.class.getResource("/org/aisr/aisrinitialclient/dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("AIS R Initial - DashBoard");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void navigateStaffManagement(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(UiServices.class.getResource("/org/aisr/aisrinitialclient/staff-register.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setFullScreen(false);
            stage.setResizable(false);
            stage.setTitle("AIS R Initial - Staff Management");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void navigateMyAccount(ActionEvent event) {
        try {
            FXMLLoader loader;
            if (GlobalData.getInstance().getSession().isRecruit()){
                loader = new FXMLLoader(UiServices.class.getResource("/org/aisr/aisrinitialclient/my-account-recruit.fxml"));
            }else {
                loader = new FXMLLoader(UiServices.class.getResource("/org/aisr/aisrinitialclient/my-account-staff.fxml"));

            }
            Parent root = loader.load();
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setFullScreen(false);
            stage.setResizable(false);
            stage.setTitle("AIS R Initial - My Account");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void navigateRecruitManagement(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(UiServices.class.getResource("/org/aisr/aisrinitialclient/recruit-register.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setFullScreen(false);
            stage.setResizable(false);
            stage.setTitle("AIS R Initial - Recruit Management");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initStyle(StageStyle.UTILITY);
        ButtonType okButton = new ButtonType("OK");
        alert.getButtonTypes().setAll(okButton);
        alert.showAndWait();
    }


    public static void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initStyle(StageStyle.UTILITY);
        ButtonType okButton = new ButtonType("OK");
        alert.getButtonTypes().setAll(okButton);
        alert.showAndWait();
    }

}
