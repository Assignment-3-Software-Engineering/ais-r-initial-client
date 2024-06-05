package org.aisr.aisrinitialclient.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.aisr.aisrinitialclient.model.GlobalData;
import org.aisr.aisrinitialclient.model.constants.Department;
import org.aisr.aisrinitialclient.model.constants.Qualification;
import org.aisr.aisrinitialclient.model.constants.Role;
import org.aisr.aisrinitialclient.model.data.Recruit;
import org.aisr.aisrinitialclient.model.dto.RecruitDto;
import org.aisr.aisrinitialclient.model.dto.StaffDto;
import org.aisr.aisrinitialclient.service.RecruitService;
import org.aisr.aisrinitialclient.service.UiServices;
import org.aisr.aisrinitialclient.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class RecruitRegisterController implements Initializable {

    private String recruitIdToUpdate;
    private Recruit recruitToUpdate;
    private final RecruitService recruitService = new RecruitService();
    private final ObservableList<RecruitTable> recruitDataList = FXCollections.observableArrayList();

    @FXML
    public Button btnNavigateMyAccount;

    @FXML
    public Button btnNavigateDashboard;

    @FXML
    public Button btnNavigateStaffManagement;

    @FXML
    public Button btnNavigateRecruitManagement;

    @FXML
    public Label lblUserFullName;

    @FXML
    public Label lblUserUsername;

    @FXML
    public Button btnLogout;

    @FXML
    public TextField txtFullName;

    @FXML
    public TextArea txtAddress;

    @FXML
    public TextField txtUsername;

    @FXML
    public TextField txtPhone;

    @FXML
    public TextField txtEmail;

/*
    @FXML
    public TextField txtPassword;
*/

    @FXML
    public ComboBox<String> cmbQualification;

    @FXML
    public ComboBox<String> cmbDepartment;

    @FXML
    public DatePicker dateInterview;

    @FXML
    public Label lblCreateBy;

    @FXML
    public Label lblCreatedBranch;

    @FXML
    public Label lblCreatedAt;

    /*@FXML
    public Button btnGeneratePassword;*/

    @FXML
    public Button btnSave;

    @FXML
    public Button btnClear;

    @FXML
    private TableView<RecruitTable> tableViewRecruit;

    @FXML
    private TableColumn<RecruitTable, String> indexColumn;

    @FXML
    public TableColumn<RecruitTable, String>  nameColumn;

    @FXML
    public TableColumn<RecruitTable, String>  emailColumn;

    @FXML
    public TableColumn<RecruitTable, String>  departmentColumn;

    @FXML
    public TableColumn<RecruitTable, String>  dateColumn;

    @FXML
    public void logout(ActionEvent event){
        GlobalData.getInstance().setSession(null);
        UiServices.navigateLogin(event);
    }

    @FXML
    public void navigateDashboard(ActionEvent event){
        UiServices.navigateDashBoard(event);
    }

    @FXML
    public void navigateStaffManagement(ActionEvent event){
        UiServices.navigateStaffManagement(event);
    }

    @FXML
    public void navigateMyAccount(ActionEvent event) {
        UiServices.navigateMyAccount(event);
    }

/*    @FXML
    private void setBtnGeneratePassword(){
        String password = StringUtil.generatePassword(10);
        StringUtil.copyToClipboard(password);
        txtPassword.setText(password);
    }*/

    @FXML
    public void save() {
        System.out.println("recruitIdToUpdate "+recruitIdToUpdate);
        if (recruitIdToUpdate == null){
            saveData();
        }else {
            updateData();
        }
    }

    @FXML
    public void clearForm(){
        initForm();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblUserFullName.setText(GlobalData.getInstance().getSession().getUserFullName());
        lblUserUsername.setText(GlobalData.getInstance().getSession().getUsername());

        txtUsername.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String filteredValue = newValue.replaceAll(",", "").toUpperCase();
                if (!newValue.equals(filteredValue)) {
                    txtUsername.setText(filteredValue);
                }
            }
        });

        txtPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtPhone.setText(oldValue);
            } else if (newValue.length() > 10) {
                txtPhone.setText(newValue.substring(0, 10));
            }
        });

        initRecruitTable();
        loadTable();
        initForm();
        initNavBar();
    }

    private void initNavBar(){
        btnNavigateDashboard.setDisable(false);
        btnNavigateStaffManagement.setDisable(GlobalData.getInstance().getSession().isRecruit());
        btnNavigateMyAccount.setDisable(false);
    }

    private void initForm() {
        recruitIdToUpdate = null;
        recruitToUpdate = null;
        clearFields();

        cmbQualification.getItems().clear();
        cmbQualification.setPromptText("Select Highest Qualification");
        for (Qualification qualification : Qualification.values()) {
            cmbQualification.getItems().add(qualification.getValue());
        }

        cmbDepartment.getItems().clear();
        cmbDepartment.setPromptText("Select Department");
        for (Department department : Department.values()) {
            cmbDepartment.getItems().add(department.getValue());
        }

        if (GlobalData.getInstance().getSession().getLoggdinStaff().getRole().equals(Role.ADMIN_STAFF)){
            disableFormFields(false,true);
            disableButtons(false);
            lblCreateBy.setText("Created By: " + GlobalData.getInstance().getSession().getLoggdinStaff().getFullName());
            lblCreatedBranch.setText("Branch: " + GlobalData.getInstance().getSession().getLoggdinStaff().getBranch().getValue());
            lblCreatedAt.setText("Create Date: " + LocalDate.now());
        }else if (GlobalData.getInstance().getSession().getLoggdinStaff().getRole().equals(Role.MANAGEMENT_STAFF)){
            disableFormFields(true,true);
            disableButtons(true);
        }
    }

    private void clearFields() {
        txtFullName.clear();
        txtUsername.clear();
        txtPhone.clear();
        txtEmail.clear();
       // txtPassword.clear();
        txtAddress.clear();
        cmbQualification.setValue(null);
        cmbDepartment.setValue(null);
        dateInterview.setValue(null);
        lblCreateBy.setText("Created By : ");
        lblCreatedBranch.setText("Branch : ");
        lblCreatedAt.setText("Create Date : ");
    }

    public void disableFormFields(boolean disableAdmin,boolean disableManagement) {
        txtFullName.setDisable(disableAdmin);
        txtUsername.setDisable(disableAdmin);
        txtPhone.setDisable(disableAdmin);
        txtEmail.setDisable(disableAdmin);
        //txtPassword.setDisable(disableAdmin);
        txtAddress.setDisable(disableAdmin);
        cmbQualification.setDisable(disableAdmin);
        cmbDepartment.setDisable(disableManagement);
        dateInterview.setDisable(disableManagement);
    }

    private void disableButtons(boolean disable){
        btnClear.setDisable(disable);
        btnSave.setDisable(disable);
       // btnGeneratePassword.setDisable(disable);
    }

    private void saveData(){
        try {
            String alertTitle = "Validation Error";
            String fullName = txtFullName.getText();
            String address = txtAddress.getText();
            String username = txtUsername.getText();
            String phone = txtPhone.getText();
            String email = txtEmail.getText();
            //String password = txtPassword.getText();
            String qualification = cmbQualification.getValue();


            RecruitDto recruit = new RecruitDto();
            if (StringUtils.isNotEmpty(fullName)){
                recruit.setFullName(StringUtil.removeCommas(fullName));
            }else {
                UiServices.showAlert(alertTitle, "Full Name is required.");
                return;
            }

            if (StringUtils.isNotEmpty(address)){
                recruit.setAddress(StringUtil.removeCommas(address));
            }else {
                UiServices.showAlert(alertTitle, "Address is required.");
                return;
            }

            if (StringUtils.isNotEmpty(username)){
                recruit.setUsername(StringUtil.removeCommas(username));
            }else {
                UiServices.showAlert(alertTitle, "Username is required.");
                return;
            }

            if (StringUtils.isNotEmpty(phone)){
                recruit.setPhoneNumber(StringUtil.removeCommas(phone));
            }else {
                UiServices.showAlert(alertTitle, "Phone is required.");
                return;
            }

            if (StringUtils.isNotEmpty(email)){
                recruit.setEmail(StringUtil.removeCommas(email));
            }else {
                UiServices.showAlert(alertTitle, "Email is required.");
                return;
            }

/*            if (StringUtils.isNotEmpty(password)){
                recruit.setPassword(password);
            }else {
                recruit.setPassword(null);
                //UiServices.showAlert(alertTitle, "Password is required.");
               // return;
            }*/

            if (StringUtils.isNotEmpty(qualification)){
                recruit.setHighestQualificationLevel(Qualification.of(qualification));
            }else {
                UiServices.showAlert(alertTitle, "Highest Qualification Level is required.");
                return;
            }

            this.recruitService.save(recruit);
            initForm();
            loadTable();
            UiServices.showMessage("Create Success","Recruit create success");

        }catch (Exception e){
            e.printStackTrace();
            UiServices.showAlert("Something went wrong",e.getMessage());
        }
    }

    private void updateData(){
        try {
            String alertTitle = "Validation Error";
            String department = cmbDepartment.getValue();
            LocalDate interviewDate = dateInterview.getValue();

            if (StringUtils.isNotEmpty(department)){
                recruitToUpdate.setDepartment(Department.of(department));
            }else {
                UiServices.showAlert(alertTitle, "Department is required.");
                return;
            }

            if (interviewDate != null){
                recruitToUpdate.setInterviewDate(interviewDate);
            }else {
                UiServices.showAlert(alertTitle, "InterviewDate is required.");
                return;
            }
            this.recruitService.update(recruitToUpdate.getId(),recruitToUpdate);
            initForm();
            loadTable();
            UiServices.showMessage("Update Success","Recruit details updated successfully");
        }catch (Exception e){
            e.printStackTrace();
            UiServices.showAlert("Something went wrong",e.getMessage());
        }

    }

    private void loadTable(){
        try {
            this.recruitService.loadData();
            List<Recruit> recruitList = GlobalData.getInstance().getRecruitList();
            final int[] index = {0};
            recruitDataList.clear();
            recruitList.forEach(recruit -> {
                index[0]++;
                recruitDataList.add(RecruitTable.init(recruit, index[0]));
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initRecruitTable(){
        indexColumn.setCellValueFactory(cellData -> cellData.getValue().indexProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().fullNameProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().createdAtProperty());
        departmentColumn.setCellValueFactory(cellData -> cellData.getValue().departmentProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

        TableColumn<RecruitTable, String> actionColumn = new TableColumn<>("");
        actionColumn.setCellFactory(createButtonColumn());
        tableViewRecruit.getColumns().add(actionColumn);

        tableViewRecruit.setItems(recruitDataList);
    }

    private void fillView(String recruitId){
        Optional<Recruit> recruitOptional = recruitService.findById(recruitId);
        recruitToUpdate = recruitOptional.orElseThrow();
        fillForm(recruitToUpdate);

    }

    public void fillForm(Recruit data) {
        txtFullName.setText(data.getFullName());
        txtUsername.setText(data.getUsername());
        txtPhone.setText(data.getPhoneNumber());
        txtEmail.setText(data.getEmail());
        txtAddress.setText(data.getAddress());

        cmbQualification.setValue(data.getHighestQualificationLevel().getValue());
        if (data.getDepartment() != null){
            cmbDepartment.setValue(data.getDepartment().getValue());
        }

        if (data.getInterviewDate() != null){
            dateInterview.setValue(data.getInterviewDate());
        }

        lblCreateBy.setText("Created By: " + data.getCreatedBy().getFullName());
        lblCreatedBranch.setText("Branch: " +  (data.getCreatedBy().getBranch() != null ?  data.getCreatedBy().getBranch().getValue(): ""));
        lblCreatedAt.setText("Create Date: " + data.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm:ss")));

        disableFormFields(true,false);
        disableButtons(false);
       // btnGeneratePassword.setDisable(true);

    }



    private Callback<TableColumn<RecruitTable, String>, TableCell<RecruitTable, String>> createButtonColumn() {
        return new Callback<TableColumn<RecruitTable, String>, TableCell<RecruitTable, String>>() {
            @Override
            public TableCell<RecruitTable, String> call(TableColumn<RecruitTable, String> param) {
                return new TableCell<RecruitTable, String>() {
                    final Button button = new Button("view");

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            setGraphic(button);
                            button.setOnAction(event -> {
                                RecruitTable recruitTable = getTableView().getItems().get(getIndex());
                                recruitIdToUpdate = recruitTable.getId();
                                fillView(recruitIdToUpdate);
                            });
                        }
                    }
                };
            }
        };
    }


}



