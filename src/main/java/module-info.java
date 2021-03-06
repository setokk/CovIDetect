module com.pasoftxperts.covidetect {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.media;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.jgrapht.core;
    requires java.desktop;
    requires org.apache.commons.lang3;

    opens com.pasoftxperts.covidetect to javafx.fxml;
    exports com.pasoftxperts.covidetect;
    exports com.pasoftxperts.covidetect.guicontrollers;
    opens com.pasoftxperts.covidetect.guicontrollers to javafx.fxml;
    exports com.pasoftxperts.covidetect.guicontrollers.cachefxmlloader;
    opens com.pasoftxperts.covidetect.guicontrollers.cachefxmlloader to javafx.fxml;
}