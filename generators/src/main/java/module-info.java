module com.juandejunin.generators {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.juandejunin.generators to javafx.fxml;
    exports com.juandejunin.generators;
}