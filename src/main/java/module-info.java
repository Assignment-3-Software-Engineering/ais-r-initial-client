module org.aisr.aisrinitialclient {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires java.logging;
    requires jbcrypt;
    requires java.datatransfer;
    requires java.desktop;
    requires org.apache.commons.lang3;
    requires itextpdf;
    requires com.google.gson;
    requires java.sql;

    opens org.aisr.aisrinitialclient to javafx.fxml;
    opens org.aisr.aisrinitialclient.controller to javafx.fxml;
    opens org.aisr.aisrinitialclient.model.dto to com.google.gson;
    opens org.aisr.aisrinitialclient.model.data to com.google.gson;
//    opens java.time to com.google.gson;
    exports org.aisr.aisrinitialclient;
    exports org.aisr.aisrinitialclient.service;
    exports org.aisr.aisrinitialclient.controller;
    exports org.aisr.aisrinitialclient.model;
    exports org.aisr.aisrinitialclient.model.data;
    exports org.aisr.aisrinitialclient.model.constants;
    exports org.aisr.aisrinitialclient.model.dto;
}