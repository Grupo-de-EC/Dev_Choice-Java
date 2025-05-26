module com.devchoice.devchoicejava {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.devchoice.devchoicejava to javafx.fxml;
    exports com.devchoice.devchoicejava;
}