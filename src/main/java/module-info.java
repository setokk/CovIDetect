module com.pasoftxperts.covidetect {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.jgrapht.core;

    opens com.pasoftxperts.covidetect to javafx.fxml;
    exports com.pasoftxperts.covidetect;
    exports com.pasoftxperts.covidetect.guicontrollers;
    opens com.pasoftxperts.covidetect.guicontrollers to javafx.fxml;
}