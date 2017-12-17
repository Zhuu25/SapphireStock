import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


public class LoginPage {

    @FXML
    TextField user;

    @FXML
    TextField password;

    @FXML
    Button logIn;

    JdbcSQLiteConnection db;
    static String st;

    @FXML
    public void initialize(){
        db = new JdbcSQLiteConnection();
    }

    @FXML
    public void verify(ActionEvent event) throws IOException {
        if (user.getText().equals("") || password.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.WARNING, "กรอกชื่อผู้ใช้หรือรหัสผ่านไม่ถูกต้อง กรุณากรอกใหม่", ButtonType.OK);
            alert.setTitle("Please try again.");
            alert.show();
        }
        else{
            try {
                login(event);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void login(ActionEvent event) throws IOException, SQLException {
        String bool = db.getPassword(user.getText());
        if (bool.equals("false") || bool.equals(password.getText()) == false){
            ButtonType tryAgain = new ButtonType("Try again");
            Alert alert = new Alert(Alert.AlertType.WARNING, "",tryAgain);
            alert.setHeaderText("ชื่อผู้ใช้หรือรหัสผ่านผิดพลาด กรุณากรอกใหม่อีกครั้ง");
            alert.setResizable(false);
            alert.show();
            if (alert.getResult() == tryAgain){
                alert.close();
            }
        }
        else{
            st = user.getText();
            db.updateLoginStatus(st, "online");
            String type = "";
            if (user.getText().charAt(0) == 'b'){
                type = "StockMainPage.fxml";
            }
            else{
                type = "SaleMainPage.fxml";
            }
            Button b = (Button) event.getSource();
            Stage stage =  (Stage) b.getScene().getWindow();
            FXMLLoader loadMain = new FXMLLoader(getClass().getResource(type));
            stage.setScene(new Scene((Parent) loadMain.load()));

            if (type.equals("SaleMainPage.fxml")){
                SaleMainPage sales = loadMain.getController();
                sales.setCon(this);
            }
        }

    }


}
