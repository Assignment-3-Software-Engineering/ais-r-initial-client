package org.aisr.aisrinitialclient.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;
import org.aisr.aisrinitialclient.model.GlobalData;
import org.aisr.aisrinitialclient.model.constants.*;
import org.aisr.aisrinitialclient.model.data.AdministrationStaff;
import org.aisr.aisrinitialclient.model.data.ManagementStaff;
import org.aisr.aisrinitialclient.model.data.Recruit;
import org.aisr.aisrinitialclient.model.dto.StaffDto;
import org.aisr.aisrinitialclient.service.UiServices;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    public Button btnNavigateDashboard;

    @FXML
    public Button btnNavigateStaffManagement;

    @FXML
    public Button btnNavigateRecruitManagement;

    @FXML
    public Button btnNavigateMyAccount;

    @FXML
    public Label lblUserFullName;

    @FXML
    public Label lblUserUsername;

    @FXML
    public Button btnLogout;

    @FXML
    public Label lblRecruitTotal;

    @FXML
    public Label lblRecruitAssigned;

    @FXML
    public Label lblRecruitUnAssigned;

    @FXML
    public Label lblStaffTotal;

    @FXML
    public Label lblStaffAdminTotal;

    @FXML
    public Label lblStaffManagementTotal;

    @FXML
    public PieChart pieChartRecruitDepartment;

    @FXML
    public PieChart pieChartRecruitQualifications;

    @FXML
    public PieChart pieChartStaffAdmin;

    @FXML
    public PieChart pieChartStaffManagement;

    @FXML
    public PieChart pieChartStaffBranch;

    @FXML
    public Button btnReportRecruitFull;

    @FXML
    public Button btnReportStaffFull;

    public void logout(ActionEvent event){
        GlobalData.getInstance().setSession(null);
        UiServices.navigateLogin(event);
    }

    public void navigateStaffManagement(ActionEvent event){
        UiServices.navigateStaffManagement(event);
    }

    public void navigateMyAccount(ActionEvent event){
        UiServices.navigateMyAccount(event);
    }

    public void navigateRecruitManagement(ActionEvent event){
        UiServices.navigateRecruitManagement(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblUserFullName.setText(GlobalData.getInstance().getSession().getUserFullName());
        lblUserUsername.setText(GlobalData.getInstance().getSession().getUsername());

        lblRecruitTotal.setText(Integer.toString(GlobalData.getInstance().getRecruitList().size()));
        lblRecruitAssigned.setText(Long.toString(countRecruitByAssignment(true)));
        lblRecruitUnAssigned.setText(Long.toString(countRecruitByAssignment(false)));

        lblStaffTotal.setText(Integer.toString(GlobalData.getInstance().getStaffList().size()));
        lblStaffAdminTotal.setText(Long.toString(countStaffByRole(Role.ADMIN_STAFF)));
        lblStaffManagementTotal.setText(Long.toString(countStaffByRole(Role.MANAGEMENT_STAFF)));

        loadPieChartRecruitDepartment();
        loadPieChartRecruitQualification();
        loadPieChartStaffAdmin();
        loadPieChartStaffBranch();
        loadPieChartStaffManagement();
        initNavBar();
    }
    
    private void initNavBar(){
       // btnNavigateDashboard.setDisable(true);
        btnNavigateStaffManagement.setDisable(GlobalData.getInstance().getSession().isRecruit());
        btnNavigateRecruitManagement.setDisable(GlobalData.getInstance().getSession().isRecruit());
        btnNavigateMyAccount.setDisable(false);
    }

    private void loadPieChartRecruitDepartment (){
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Department value : Department.values()) {
            pieChartData.add(new PieChart.Data(value.getValue(), countRecruitByDepartment(value)));
        }
        pieChartRecruitDepartment.setData(pieChartData);
        pieChartRecruitDepartment.setClockwise(true);
        pieChartRecruitDepartment.setLabelsVisible(true);
        pieChartRecruitDepartment.setStartAngle(180);

        for (PieChart.Data data : pieChartData) {
            Tooltip tooltip = new Tooltip();
            tooltip.setText(data.getName() + ": " + (int) data.getPieValue());
            tooltip.setShowDelay(Duration.seconds(0.1));
            tooltip.setHideDelay(Duration.seconds(0.5));
            Tooltip.install(data.getNode(), tooltip);
        }
    }

    private void loadPieChartRecruitQualification (){
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Qualification value : Qualification.values()) {
            pieChartData.add(new PieChart.Data(value.getValue(), countRecruitByQualifications(value)));
        }
        pieChartRecruitQualifications.setData(pieChartData);
        pieChartRecruitQualifications.setClockwise(true);
        //pieChartRecruitQualifications.setLabelLineLength(10);
        pieChartRecruitQualifications.setLabelsVisible(true);
        pieChartRecruitQualifications.setStartAngle(180);

        for (PieChart.Data data : pieChartData) {
            Tooltip tooltip = new Tooltip();
            tooltip.setText(data.getName() + ": " + (int) data.getPieValue());
            tooltip.setShowDelay(Duration.seconds(0.1));
            tooltip.setHideDelay(Duration.seconds(0.5));
            Tooltip.install(data.getNode(), tooltip);
        }
    }

    private void loadPieChartStaffAdmin (){
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (PositionType value : PositionType.values()) {
            pieChartData.add(new PieChart.Data(value.getValue(), countAdminStaffByPosition(value)));
        }
        pieChartStaffAdmin.setData(pieChartData);
        pieChartStaffAdmin.setClockwise(true);
        pieChartStaffAdmin.setLabelsVisible(true);
        pieChartStaffAdmin.setStartAngle(180);

        for (PieChart.Data data : pieChartData) {
            Tooltip tooltip = new Tooltip();
            tooltip.setText(data.getName() + ": " + (int) data.getPieValue());
            tooltip.setShowDelay(Duration.seconds(0.1));
            tooltip.setHideDelay(Duration.seconds(0.5));
            Tooltip.install(data.getNode(), tooltip);
        }
    }

    private void loadPieChartStaffManagement (){
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Senior",  countManagementStaffByLevel(ManagementLevel.SENIOR_MANAGER)),
                new PieChart.Data("Mid level",  countManagementStaffByLevel(ManagementLevel.MID_LEVEL_MANAGER)),
                new PieChart.Data("Supervisor",  countManagementStaffByLevel(ManagementLevel.SUPERVISOR))
        );

        pieChartStaffManagement.setData(pieChartData);
        pieChartStaffManagement.setClockwise(true);
        pieChartStaffManagement.setLabelsVisible(true);
        pieChartStaffManagement.setStartAngle(180);

        for (PieChart.Data data : pieChartData) {
            Tooltip tooltip = new Tooltip();
            tooltip.setText(data.getName() + ": " + (int) data.getPieValue());
            tooltip.setShowDelay(Duration.seconds(0.1));
            tooltip.setHideDelay(Duration.seconds(0.5));
            Tooltip.install(data.getNode(), tooltip);
        }
    }

    private void loadPieChartStaffBranch(){
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Branch value : Branch.values()) {
            pieChartData.add(new PieChart.Data(value.getValue(), countStaffByBranch(value)));
        }
        pieChartStaffBranch.setData(pieChartData);
        pieChartStaffBranch.setClockwise(true);
        pieChartStaffBranch.setLabelsVisible(true);
        pieChartStaffBranch.setStartAngle(180);

        for (PieChart.Data data : pieChartData) {
            Tooltip tooltip = new Tooltip();
            tooltip.setText(data.getName() + ": " + (int) data.getPieValue());
            tooltip.setShowDelay(Duration.seconds(0.1));
            tooltip.setHideDelay(Duration.seconds(0.5));
            Tooltip.install(data.getNode(), tooltip);
        }
    }

    private long countStaffByRole(Role role){
        return GlobalData.getInstance().getStaffList().stream()
                .filter(staff -> role.equals(staff.getRole())).count();
    }

    private  long countRecruitByAssignment(boolean assigned){
        return GlobalData.getInstance().getRecruitList().stream()
                .filter(recruit -> assigned ? !(recruit.getDepartment() == null) : (recruit.getDepartment() == null))
                .count();
    }

    private long countRecruitByDepartment(Department department){
        return GlobalData.getInstance().getRecruitList().stream()
                .filter(recruit -> department.equals(recruit.getDepartment())).count();
    }

    private long countRecruitByQualifications(Qualification qualification){
        return GlobalData.getInstance().getRecruitList().stream()
                .filter(recruit -> qualification.equals(recruit.getHighestQualificationLevel())).count();
    }

    private long countStaffByBranch(Branch branch){
        return GlobalData.getInstance().getStaffList().stream()
                .filter(staff -> branch.equals(staff.getBranch())).count();
    }

    private long countAdminStaffByPosition(PositionType positionType){
        long count = 0;
        for (StaffDto staff : GlobalData.getInstance().getStaffList()) {
            if (staff.getRole().equals(Role.ADMIN_STAFF)){
                AdministrationStaff adminStaff = StaffDto.initAdministrationStaff(staff);
                if (adminStaff.getPositionType().equals(positionType)){
                    count++;
                }
            }
        }
        return count;
    }

    private long countManagementStaffByLevel(ManagementLevel level){
        long count = 0;
        for (StaffDto staff : GlobalData.getInstance().getStaffList()) {
            if (staff.getRole().equals(Role.MANAGEMENT_STAFF)){
                ManagementStaff adminStaff = StaffDto.initManagementStaff(staff) ;
                if (adminStaff.getManagementLevel().equals(level)){
                    count++;
                }
            }
        }
        return count;
    }

    public void generateReportRecruit(){
        String folderPath = "recruit_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) +
                ".pdf";

        try {
            Document document = new Document();
            document.setMargins(10,10,10,10);
            PdfWriter.getInstance(document, new FileOutputStream(folderPath));
            document.open();

            Paragraph paragraph = new Paragraph("Recruit Report - "+LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm:ss")));
            paragraph.setSpacingAfter(4);
            document.add( paragraph);

            float[] columnWidths = { 3, 1, 2, 1, 1, 1};
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100);
            Font font = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);

            table.addCell(new PdfPCell(new Phrase("Full Name",font)));
            table.addCell(new PdfPCell(new Phrase("Phone",font)));
            table.addCell(new PdfPCell(new Phrase("Email",font)));
            table.addCell(new PdfPCell(new Phrase("interview Date",font)));
            table.addCell(new PdfPCell(new Phrase("Qualification",font)));
            table.addCell(new PdfPCell(new Phrase("Department",font)));

            for (Recruit recruit : GlobalData.getInstance().getRecruitList()){
                table.addCell( new PdfPCell(new Phrase( recruit.getFullName(),font)));
                table.addCell( new PdfPCell(new Phrase( recruit.getPhoneNumber(),font)));
                table.addCell( new PdfPCell(new Phrase( recruit.getEmail(),font)));
                table.addCell( new PdfPCell(new Phrase( recruit.getInterviewDate() == null ? "" : recruit.getInterviewDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),font)));
                table.addCell(  new PdfPCell(new Phrase(recruit.getDepartment() == null ? "" : recruit.getDepartment().getValue(),font)));
            }
            document.add(table);
            document.close();
            UiServices.showMessage("Report Generate Success","Recruit Report -  saved to: "
                    +System.getProperty("user.dir")+FileSystems.getDefault().getSeparator()+folderPath);
        } catch (IOException | DocumentException e){
            e.printStackTrace();

        }
    }

    public void generateReportStaffFull(){
        String folderPath = "staff_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) +
                ".pdf";

        try {
            Document document = new Document();
            document.setMargins(10,10,10,10);
            PdfWriter.getInstance(document, new FileOutputStream(folderPath));
            document.open();

            Paragraph paragraph = new Paragraph("Staff Report - "+LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm:ss")));
            paragraph.setSpacingAfter(4);
            document.add( paragraph);

            float[] columnWidths = {2, 3, 1, 2, 1, 1, 1};
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100);
            Font font = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);

            table.addCell(new PdfPCell(new Phrase("Role",font)));
            table.addCell(new PdfPCell(new Phrase("Full Name",font)));
            table.addCell(new PdfPCell(new Phrase("Phone",font)));
            table.addCell(new PdfPCell(new Phrase("Email",font)));
            table.addCell(new PdfPCell(new Phrase("Branch",font)));
            table.addCell(new PdfPCell(new Phrase("Management Level",font)));
            table.addCell(new PdfPCell(new Phrase("Position",font)));

            for (StaffDto staff : GlobalData.getInstance().getStaffList()) {
                table.addCell( new PdfPCell(new Phrase(staff.getRole().getValue(),font))  );
                table.addCell( new PdfPCell(new Phrase( staff.getFullName(),font)));
                table.addCell( new PdfPCell(new Phrase(staff.getPhoneNumber(),font)));
                table.addCell( new PdfPCell(new Phrase(staff.getEmail(),font)));
                table.addCell(  new PdfPCell(new Phrase(staff.getBranch().getValue(),font)));

                if (staff.getRole().equals(Role.MANAGEMENT_STAFF)){
                    ManagementStaff managementStaff =StaffDto.initManagementStaff(staff);
                    table.addCell(  new PdfPCell(new Phrase(managementStaff.getManagementLevel().getValue(),font)));
                    table.addCell(  new PdfPCell(new Phrase("N/A",font)));
                }

                if (staff.getRole().equals(Role.ADMIN_STAFF)){
                    AdministrationStaff administrationStaff = StaffDto.initAdministrationStaff(staff);
                    table.addCell(  new PdfPCell(new Phrase("N/A",font)));
                    table.addCell(  new PdfPCell(new Phrase(administrationStaff.getPositionType().getValue(),font)));
                }
            }

            document.add(table);
            document.close();

            UiServices.showMessage("Report Generate Success","Staff Report -  saved to: "
                    +System.getProperty("user.dir")+FileSystems.getDefault().getSeparator()+folderPath);
        }catch (IOException | DocumentException e){
            e.printStackTrace();

        }
    }

}
