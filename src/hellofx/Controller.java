package hellofx;

//import com.sun.jdi.connect.spi.Connection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class Controller implements Initializable {

    @FXML
    private Button close;

    @FXML
    private TextField loginemail;

    @FXML
    private Button loginbar;

    @FXML
    private Pane main_form;

    @FXML
    private TextField loginpassword;

    @FXML
    private Button registerbtn;

    @FXML
    private TextField registeremail;

    @FXML
    private TextField registername;

    @FXML
    private TextField registerpassword;
    
    
    @FXML
    private Button registerredirectbtn;


    //database tools
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    public void registerUser() {

        String emailauthsql = "SELECT (SELECT COUNT(*) FROM users WHERE use_email = ?) > 0 as num_rows;";
        String registersql = "INSERT INTO users (use_name, use_email, use_pass) VALUES (?,?,?) ";

        connect = database.connectDb();

        try {
            if (registeremail.getText().isEmpty() || registerpassword.getText().isEmpty() || registername.getText().isEmpty()) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Por favor complete todos os campos");
                    alert.showAndWait();
                });
            } else {
                prepare = connect.prepareStatement(emailauthsql);
                prepare.setString(1, registeremail.getText());

                result = prepare.executeQuery();

                if (result.next()) {
                    if (result.getBoolean("num_rows")) {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error MessageE");
                        alert.setHeaderText(null);
                        alert.setContentText("Usuario com o mesmo email ja existe");
                        alert.showAndWait();
                    } else {
                        prepare = connect.prepareStatement(registersql);
                        prepare.setString(1, registername.getText());
                        prepare.setString(2, registeremail.getText());
                        prepare.setString(3, registerpassword.getText());

//                        result = prepare.executeQuery();
                        int rowsAffected = prepare.executeUpdate();

                        if (rowsAffected > 0) {
                            Platform.runLater(() -> {
                                Alert alert = new Alert(AlertType.INFORMATION);
                                alert.setTitle("Information Message");
                                alert.setHeaderText(null);
                                alert.setContentText("Successfully Register");
                                alert.showAndWait();

                                registerbtn.getScene().getWindow().hide();
                                try {
                                    Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
                                    Stage stage = new Stage();
                                    Scene scene = new Scene(root);
                                    stage.setScene(scene);
                                    stage.show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        } else {
                            Platform.runLater(() -> {
                                Alert alert = new Alert(AlertType.ERROR);
                                alert.setTitle("Error Message");
                                alert.setHeaderText(null);
                                alert.setContentText("Registration failed");
                                alert.showAndWait();
                            });
                        }
                    }
                } else {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loginUser() {
        String loginsql = "SELECT * FROM users WHERE use_email = ? AND use_pass = ?";

        connect = database.connectDb();

        try {
            prepare = connect.prepareStatement(loginsql);
            prepare.setString(1, loginemail.getText());
            prepare.setString(2, loginpassword.getText());

            result = prepare.executeQuery();

            if (loginemail.getText().isEmpty() || loginpassword.getText().isEmpty()) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Please Fill all blank fields");
                    alert.showAndWait();
                });
            } else {
                if (result.next()) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully Login");
                        alert.showAndWait();

                        loginbar.getScene().getWindow().hide();
                        try {
                            Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
                            Stage stage = new Stage();
                            Scene scene = new Scene(root);
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Wrong email/password");
                        alert.showAndWait();
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void openRegistrationPage(ActionEvent event) {
        
        registerredirectbtn.getScene().getWindow().hide();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Register.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    private PreparedStatement setString(int i, String text) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
