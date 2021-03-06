module com.strwatcher.screen_maker.screenmaker {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.desktop;
    requires kotlinx.coroutines.core.jvm;
    requires javafx.swing;
    requires kotlin.stdlib.jdk7;

    opens com.strwatcher.screenmaker to javafx.fxml;
    exports com.strwatcher.screenmaker;
}