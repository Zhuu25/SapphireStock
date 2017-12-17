
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ExportPage {

    JdbcSQLiteConnection db = new JdbcSQLiteConnection();
    StockMainPage stock;

    @FXML TextField id;
    @FXML Button submit;
    @FXML Button cancel;

    @FXML
    public void initialize(){

    }

    @FXML
    public void verify(ActionEvent event) throws IOException {
        String string = id.getText();
        if (string.equals("")){
            newAlert("กรุณากรอกข้อมูลหมายเลขใบเบิกสินค้า");
        }
        else{
            Character c = string.charAt(0);
            if (Character.isDigit(c) && c != 'L' && c != 'S'){
                newAlert("กรอกหมายเลขใบเบิกสินค้าไม่ถูกต้อง กรุณากรอกใหม่");
            }
            else{
                Button b = (Button) event.getSource();
                Stage stage = (Stage) b.getScene().getWindow();
                String word = db.checkBill(string.charAt(0), string);
                if (word.equals("true")){
                    ArrayList<Double> billWeight = db.getWeightBill(string.charAt(0), string);
                    ArrayList billGoods = db.getGoodsBill(string.charAt(0), string);

                    for(int i = 0; i < billGoods.size(); i++){
                        try {
                            Double d = db.getWeight(billGoods.get(i).toString());
                            db.updateGoods(billGoods.get(i).toString(), d-billWeight.get(i));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        stock.getTable().setItems(stock.getData());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    db.updateBill(string.charAt(0), string);
                    stage.close();
                }
                else if (word.equals("false")){
                    //dont have this billNum
                    newAlert("หมายเลขใบเบิกสินค้าผิดพลาด กรุณากรอกใหม่");
                }
                else{
                    //this bill has checked
                    newAlert("สินค้าถูกเบิกออกจากคลังแล้ว");
                }
            }
        }
    }

    @FXML
    public void back(ActionEvent event) throws IOException {
        Button b = (Button) event.getSource();
        Stage stage = (Stage) b.getScene().getWindow();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.CANCEL, ButtonType.OK);
        alert.setTitle("Cancel confirm?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.CANCEL){
            alert.close();
        }
        else if (alert.getResult() == ButtonType.OK){
            stage.close();
        }
    }

    public void newAlert(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING, "", ButtonType.OK);
        alert.setHeaderText(message);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK){
            alert.close();
        }
    }

    public void setStock(StockMainPage stock) {
        this.stock = stock;
    }
}
