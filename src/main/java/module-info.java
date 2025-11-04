module com.ijse.gdse73.chatroom {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.ijse.gdse73.chatroom to javafx.fxml;
    exports com.ijse.gdse73.chatroom;
}