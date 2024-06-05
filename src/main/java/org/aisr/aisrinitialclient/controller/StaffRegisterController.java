package org.aisr.aisrinitialclient.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.aisr.aisrinitialclient.model.GlobalData;
import org.aisr.aisrinitialclient.model.constants.Branch;
import org.aisr.aisrinitialclient.model.constants.ManagementLevel;
import org.aisr.aisrinitialclient.model.constants.PositionType;
import org.aisr.aisrinitialclient.model.constants.Role;
import org.aisr.aisrinitialclient.model.data.AdministrationStaff;
import org.aisr.aisrinitialclient.model.data.ManagementStaff;
import org.aisr.aisrinitialclient.model.data.Staff;
import org.aisr.aisrinitialclient.model.dto.StaffDto;
import org.aisr.aisrinitialclient.service.StaffService;
import org.aisr.aisrinitialclient.service.UiServices;
import org.aisr.aisrinitialclient.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class StaffRegisterController  implements Initializable {
    private String staffIdToUpdate;
    private final ObservableList<StaffTable> staffDataList = FXCollections.observableArrayList();
    private final StaffService staffService = new StaffService();

    @FXML
    public Button btnNavigateMyAccount;

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

    @FXML
    public ComboBox<String> cmbStaffType;

    @FXML
    public TextField txtStaffId;

    @FXML
    public TextField txtPassword;

    @FXML
    public ComboBox<String> cmbPosition;

    @FXML
    public ComboBox<String> cmbManagementLevel;

    @FXML
    public ComboBox<String> cmbBranch;

    @FXML
    public Button btnSave;

    @FXML
    public Button btnClear;

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
    public Button btnGeneratePassword;

    @FXML
    private TableView<StaffTable> tableViewStaff;

    @FXML
    private TableColumn<StaffTable, String> indexColumn;

    @FXML
    public TableColumn<StaffTable, String>  nameColumn;

    @FXML
    public TableColumn<StaffTable, String> roleColumn;

    @FXML
    public TableColumn<StaffTable, String>  staffIdColumn;

    @FXML
    public TableColumn<StaffTable, String>  emailColumn;

    @FXML
    public TableColumn<StaffTable, String>  addressColumn;

    @FXML
    public TableColumn<StaffTable, String>  phoneColumn;

    @FXML
    public TableColumn<StaffTable, String>  userNameColumn;

    @FXML
    public TableColumn<StaffTable, String>  branchColumn;

    @FXML
    public TableColumn<StaffTable, String>  managementLevelColumn;

    @FXML
    public TableColumn<StaffTable, String>  positionColumn;

    @FXML
    public void clearForm(){
        initForm();
    }

    @FXML
    private void setBtnGeneratePassword(){
        String password = StringUtil.generatePassword(10);
        StringUtil.copyToClipboard(password);
        txtPassword.setText(password);
    }

    @FXML
    public void save() {
        saveData();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblUserFullName.setText(GlobalData.getInstance().getSession().getUserFullName());
        lblUserUsername.setText(GlobalData.getInstance().getSession().getUsername());
        cmbStaffType.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadForm(Objects.requireNonNull(Role.of(newVal)));
            }
        });

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

        this.loadTable();
        this.initForm();
        this.initNavBar();
    }

    private void initNavBar(){
        btnNavigateDashboard.setDisable(false);
        btnNavigateRecruitManagement.setDisable(GlobalData.getInstance().getSession().isRecruit());
        btnNavigateMyAccount.setDisable(false);
    }

    private void loadForm(Role role){
        Platform.runLater(() -> txtFullName.requestFocus());

        disableOtherElements(false);
        cmbStaffType.setDisable(true);

        cmbBranch.getItems().clear();
        cmbBranch.setPromptText("Select Branch");
        for (Branch branch : Branch.values()) {
            cmbBranch.getItems().add(branch.getValue());
        }

        if (role.equals(Role.ADMIN_STAFF)){
            disableAdminElements(false);
            disableManagementElements(true);
            initAdminOnlyFields();
        }else {
            disableManagementElements(false);
            disableAdminElements(true);
            initManagementOnlyFields();
        }
    }

    private void initAdminOnlyFields(){
        cmbPosition.getItems().clear();
        cmbPosition.setPromptText("Select Position Type");
        for (PositionType positionType: PositionType.values()){
            cmbPosition.getItems().add(positionType.getValue());
        }
    }

    private void initManagementOnlyFields(){
        cmbManagementLevel.getItems().clear();
        cmbManagementLevel.setPromptText("Select Management Level");
        for (ManagementLevel level : ManagementLevel.values()){
            cmbManagementLevel.getItems().add(level.getValue());
        }
    }

    private void initForm(){
        clearAllFields();
        cmbStaffType.setDisable(false);
        staffIdToUpdate = null;
        cmbStaffType.getItems().clear();
        cmbStaffType.setPromptText("Select Staff Type");
        cmbStaffType.getItems().addAll(Role.ADMIN_STAFF.getValue(),Role.MANAGEMENT_STAFF.getValue());

        disableOtherElements(true);
        disableAdminElements(true);
        disableManagementElements(true);
    }

    private void disableAdminElements(boolean disabled){
        cmbPosition.setDisable(disabled);
    }

    private void disableManagementElements(boolean disabled){
        cmbManagementLevel.setDisable(disabled);
    }

    private void disableOtherElements(boolean disabled) {
        txtFullName.setDisable(disabled);
        txtAddress.setDisable(disabled);
        txtPhone.setDisable(disabled);
        txtEmail.setDisable(disabled);
        txtStaffId.setDisable(disabled);
        txtUsername.setDisable(disabled);
        txtPassword.setDisable(disabled);
        btnSave.setDisable(disabled);
        btnClear.setDisable(disabled);
        btnGeneratePassword.setDisable(disabled);
        cmbBranch.setDisable(disabled);
    }

    private void clearAllFields() {
        txtFullName.clear();
        txtAddress.clear();
        txtUsername.clear();
        txtPhone.clear();
        txtEmail.clear();
        cmbStaffType.setValue(null);
        txtStaffId.clear();
        txtPassword.clear();
        cmbPosition.setValue(null);
        cmbManagementLevel.setValue(null);
        cmbBranch.setValue(null);
    }

    private void initStaffTable(){
        indexColumn.setCellValueFactory(cellData -> cellData.getValue().indexProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().fullNameProperty());
        roleColumn.setCellValueFactory(cellData -> cellData.getValue().roleProperty());
        staffIdColumn.setCellValueFactory(cellData -> cellData.getValue().staffIdProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        addressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        phoneColumn.setCellValueFactory(cellData -> cellData.getValue().phoneNumberProperty());
        userNameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        branchColumn.setCellValueFactory(cellData -> cellData.getValue().branchProperty());
        managementLevelColumn.setCellValueFactory(cellData -> cellData.getValue().managementLevelProperty());
        positionColumn.setCellValueFactory(cellData -> cellData.getValue().positionTypeProperty());

        TableColumn<StaffTable, String> actionColumn = new TableColumn<>("");
        //actionColumn.setCellFactory(createButtonColumn());
        //tableViewStaff.getColumns().add(actionColumn);

        tableViewStaff.setItems(staffDataList);
    }

    private void loadTable(){
        try {
            this.staffService.loadData();
            initStaffTable();
            List<StaffDto> staffList = GlobalData.getInstance().getStaffList();
            final int[] index = {0};
            staffDataList.clear();
            staffList.forEach(staffDto ->{
                index[0]++;
                staffDataList.add(StaffTable.init(staffDto, index[0]));
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void saveData(){
        try {
            String alertTitle = "Validation Error";
            String fullName = txtFullName.getText().trim();
            String address = txtAddress.getText().trim();
            String username = txtUsername.getText().trim();
            String phone = txtPhone.getText().trim();
            String email = txtEmail.getText().trim();
            String staffId = txtStaffId.getText().trim();
            String password = txtPassword.getText().trim();
            String position = cmbPosition.getValue();
            String managementLevel = cmbManagementLevel.getValue();
            String branch = cmbBranch.getValue();

            if (StringUtils.isEmpty(cmbStaffType.getValue())){
                UiServices.showAlert(alertTitle, "Staff Type is required.");
            }
            StaffDto staff = new StaffDto();
            staff.setRole(Role.of(cmbStaffType.getValue()));
            

            if (StringUtils.isNotEmpty(fullName)){
                staff.setFullName(StringUtil.removeCommas(fullName));
            }else {
                UiServices.showAlert(alertTitle, "Full Name is required.");
                return;
            }

            if (StringUtils.isNotEmpty(address)){
                staff.setAddress(StringUtil.removeCommas(address));
            }else {
                UiServices.showAlert(alertTitle, "Address is required.");
                return;
            }

            if (StringUtils.isNotEmpty(username)){

                Optional<Staff> staffOptional = staffService.findByUsername(username);
                if (staffOptional.isPresent()){
                    UiServices.showAlert(alertTitle, "A staff member with the username: " + username + " already exists.");
                    return;
                }else {
                    staff.setUsername(StringUtil.removeCommas(username));
                }
            }else {
                UiServices.showAlert(alertTitle, "Username is required.");
                return;
            }

            if (StringUtils.isNotEmpty(phone)){
                staff.setPhoneNumber(StringUtil.removeCommas(phone));
            }else {
                UiServices.showAlert(alertTitle, "Phone is required.");
                return;
            }

            if (StringUtils.isNotEmpty(email)){
                staff.setEmail(StringUtil.removeCommas(email));
            }else {
                UiServices.showAlert(alertTitle, "Email is required.");
                return;
            }

            if (StringUtils.isNotEmpty(staffId)){
                staff.setStaffId(StringUtil.removeCommas(staffId));
            }else {
                UiServices.showAlert(alertTitle, "Staff ID is required.");
                return;
            }

            if (StringUtils.isNotEmpty(password)){
                staff.setPassword(password);
            }else {
                UiServices.showAlert(alertTitle, "Password is required.");
                return;
            }

            if (StringUtils.isNotEmpty(branch)){
                staff.setBranch(Branch.of(branch));
            }else {
                UiServices.showAlert(alertTitle, "Branch is required.");
                return;
            }

            if (staff.getRole().equals(Role.ADMIN_STAFF)){
                if (StringUtils.isNotEmpty(position)){
                    staff.setPositionType(PositionType.of(position));
                }else {
                    UiServices.showAlert(alertTitle, "Position Type is required.");
                    return;
                }
            }

            if (staff.getRole().equals(Role.MANAGEMENT_STAFF)){
                if (StringUtils.isNotEmpty(managementLevel)){
                    staff.setManagementLevel(ManagementLevel.of(managementLevel));
                }else {
                    UiServices.showAlert(alertTitle, "Management Level is required.");
                    return;
                }
            }

            this.staffService.save(staff);
            initForm();
            loadTable();
            UiServices.showMessage("Create Success","Staff create success");
        }catch (Exception e){
            e.printStackTrace();
            UiServices.showAlert("Something went wrong",e.getMessage());
        }
    }


    public void logout(ActionEvent event){
        GlobalData.getInstance().setSession(null);
        UiServices.navigateLogin(event);
    }

    public void navigateDashboard(ActionEvent event){
        UiServices.navigateDashBoard(event);
    }

    public void navigateMyAccount(ActionEvent event) {
        UiServices.navigateMyAccount(event);
    }

    public void navigateRecruitManagement(ActionEvent event){
        UiServices.navigateRecruitManagement(event);
    }

    private void fillStaffView(String id){

    }



  /*  private Callback<TableColumn<StaffTable, String>, TableCell<StaffTable, String>> createButtonColumn() {
        return new Callback<TableColumn<StaffTable, String>, TableCell<StaffTable, String>>() {
            @Override
            public TableCell<StaffTable, String> call(TableColumn<StaffTable, String> param) {
                return new TableCell<StaffTable, String>() {
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
                                StaffTable staffTable = getTableView().getItems().get(getIndex());
                                staffIdToUpdate = staffTable.getId();
                                System.out.println("Fill Form invoice ID: " + staffIdToUpdate);
                                fillStaffView(staffIdToUpdate);
                            });
                        }
                    }
                };
            }
        };
    }
*/
}
