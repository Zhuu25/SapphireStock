
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import jdk.nashorn.internal.scripts.JD;

import java.io.IOException;

public class SaleMainPage {

    ButtonType lend = new ButtonType("Lending Bill");
    ButtonType sold = new ButtonType("Sold Bill");
    ButtonType lendre = new ButtonType("Lending Bill Report");
    ButtonType soldre = new ButtonType("Sold Bill Report");

    String name = "";
    String user;
    LoginPage log;

    @FXML
    public void check(ActionEvent event) throws IOException {
        Button b = (Button) event.getSource();
        if (b.getId() .equals("bill")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Choose your bill type", lend, sold);
            alert.setTitle("Bill Type");
            alert.showAndWait();
            if (alert.getResult() == lend){
                name = "LendingBill.fxml";
            }
            else if (alert.getResult() ==  sold){
                name = "SoldBill.fxml";
            }
        }
        else if (b.getId().equals("report")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Choose your report type", lendre, soldre);
            alert.setTitle("Report Type");
            alert.showAndWait();
            if (alert.getResult() == lend){
                name = "";
            }
            else if (alert.getResult() == sold){
                name = "";
            }
        }

        Stage stage = (Stage) b.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(name));
        stage.setScene(new Scene((Parent) loader.load()));
        if (name.equals("LendingBill.fxml")){
            LendingBill bill = loader.getController();
            bill.setSales(this);
        }
        else if (name.equals("SoldBill.fxml")){
            SoldBillPage bill = loader.getController();
            bill.setSales(this);
        }
    }

    public void logout(ActionEvent event) throws IOException {
        JdbcSQLiteConnection db = new JdbcSQLiteConnection();
        user = db.getOnlineStaff("Sales");
        System.out.println(user);
        db.updateLoginStatus(user, "offline");
        Button l = (Button) event.getSource();
        Stage stage = (Stage) l.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));
        stage.setScene(new Scene((Parent) loader.load()));
    }

    public void setCon(LoginPage log){
        this.log = log;
    }
}
