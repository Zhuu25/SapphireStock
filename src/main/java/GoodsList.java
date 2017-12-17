import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;

public class GoodsList {

    JdbcSQLiteConnection db = new JdbcSQLiteConnection();
    LendingBill lend;
    SoldBillPage sold;

    @FXML ComboBox list;
    @FXML TextField weight;
    @FXML Button add;

    @FXML
    public void initialize(){
        weight.setDisable(true);
        try {
            list.setItems(FXCollections.observableList(db.getAllGoods("")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void checkGoods(){
        if (list.getValue() != null){
            weight.setDisable(false);
        }
    }

    public boolean checkWeight(String id) throws SQLException {
        String w = weight.getText();
        if (w.equals("")){
            return true;
        }
        for (int i = 0; i < w.length(); i++){
            if (Character.isLetter(w.charAt(i))){
                return true;
            }
        }
        Double have = db.getWeight(id);
        Double we = Double.parseDouble(weight.getText());
        if (have < we){
            return true;
        }
        return false;
    }

    @FXML
    public void addGoods(ActionEvent event){
        if (list.getValue() == null){
            newAlert("กรุณาเลือกรหัสสินค้าที่ต้องการเบิก");
        }
        else {
            try {
                if (checkWeight(list.getValue().toString())) {
                    newAlert("กรอกข้อมูลน้ำหนักผิดพลาด กรุณากรอกใหม่");
                } else {
                    Button button = (Button) event.getSource();
                    Stage stage = (Stage) button.getScene().getWindow();
                    if (lend != null) {
                        lend.addGoodsintoList(new String[]{list.getValue().toString(), weight.getText()});
                    } else {
                        sold.addGoodsintoList(new String[]{list.getValue().toString(), weight.getText()});
                    }
                    stage.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
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

    public void setLendingBill(LendingBill lend){
        this.lend = lend;
    }
    public void setSoldBill(SoldBillPage sold){
        this.sold = sold;
    }
}
