package exeamediaplayer;

import database.*;
import util.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

/**
 *
 * @author sergio
 */
public class LoginController implements Initializable {
    
    private static final Logger LOG = Logger.getLogger(LoginController.class.getName());
    
    @FXML
    private Label errorLabel;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML 
    private Button loginButton;
    
    private SQLConnector sql;
    
    private Global global;
    private ResourceBundle labels;
    private User user;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.global = Global.getInstance();
        this.labels = global.getLabels();
    }    
    
    @FXML
    public void login() {
        
        errorLabel.setText(labels.getString("connecting"));
        loginButton.setDisable(true);
        
        if(username.getText().isEmpty()) {
            errorLabel.setText(labels.getString("userEmpty"));
            loginButton.setDisable(false);
            return;
        } else if (password.getText().isEmpty()) {
            errorLabel.setText(labels.getString("passwordEmpty"));
            loginButton.setDisable(false);
            return;
        }
        
        sql = new SQLConnector();
        
        this.user = sql.getUser(username.getText());
    
        if (this.user != null) {
            if (Password.checkPassword(password.getText(), this.user.getPassword())) {
                errorLabel.setText(labels.getString("successAuthentication"));
                LOG.log(Level.INFO, "Success authentification");

                try {
                    this.authenticate();
                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                errorLabel.setText(labels.getString("failedAuthentication"));
                LOG.log(Level.INFO, "Failed authentication");
            }
        } else {
            errorLabel.setText(labels.getString("userDoesNotExist"));
            LOG.log(Level.INFO, "Failed authentication");
        }
        
        loginButton.setDisable(false);
    }
    
    private void authenticate() throws IOException {
        // The variable should be initializated before init the controller
        global.setUser(this.user);
        
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"), global.getLabels());
                        
        Scene scene = new Scene(root);
        Stage stage = global.getMainStage();
        stage.hide();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}
