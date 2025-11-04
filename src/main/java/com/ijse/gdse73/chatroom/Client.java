package com.ijse.gdse73.chatroom;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

// client (GUI)
public class Client implements Initializable {
    public TextArea chatArea;
    public TextField inputText;
    public Label labelClientName;
    public Button buttonSendText;
    public Button buttonConnection;
    public Button buttonDisconnect;
    public TextField inputServerIP;

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    private String userName;
    private String message;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonSendText.setDisable(true);
        buttonDisconnect.setDisable(true);

        // get client username
        TextInputDialog dialog = new TextInputDialog();
        dialog.setContentText("Username:");
        dialog.setHeaderText("Enter your Username");
        Optional<String> result = dialog.showAndWait();

        userName = result.orElse("Anonymous");
        labelClientName.setText(userName);
    }

    public void sendTextOnAction(ActionEvent actionEvent) {
        if (inputText.getText().isEmpty()) {
            return;
        }

        if (socket == null) {
            return;
        }

        try {
            message = inputText.getText().trim();
            dataOutputStream.writeUTF(userName + ": " + message);
            dataOutputStream.flush();

            inputText.clear();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void connectToServerOnAction(ActionEvent actionEvent) {
        if (inputServerIP.getText().isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION, "Please enter the Server IP.").show();
            return;
        }

        String ip = inputServerIP.getText().trim();
        inputServerIP.setEditable(false);

        new Thread(() -> {
            try {
                socket = new Socket(ip,4000); // client connect to the port
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                dataOutputStream.writeUTF("Client connected: " + userName);
                dataOutputStream.flush();

                buttonConnection.setDisable(true);
                buttonDisconnect.setDisable(false);
                buttonSendText.setDisable(false);

                while (true) {
                    message = dataInputStream.readUTF();
                    chatArea.appendText(message + "\n\n"); // show msg sent by server in the chat area
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }).start();
    }

    // client disconnect from the server
    public void disconnectFromServerOnAction(ActionEvent actionEvent) {
        inputServerIP.setEditable(true);
        chatArea.clear();

        try {
            dataOutputStream.writeUTF(userName + " disconnected.");
            dataOutputStream.flush();

            buttonConnection.setDisable(false);
            buttonDisconnect.setDisable(true);
            buttonSendText.setDisable(true);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
