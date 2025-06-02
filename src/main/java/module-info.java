module com.devchoice.devchoicejava {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.devschoice to javafx.fxml;
    exports com.devschoice;
}