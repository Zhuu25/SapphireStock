
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReturnGoods {

    JdbcSQLiteConnection db = new JdbcSQLiteConnection();
    ArrayList<GoodsForReturn> li;
    ObservableList list;

    @FXML TextField id;
    @FXML Text picker;
    @FXML Text pickId;

    @FXML TableView table;
    @FXML TableColumn goods;
    @FXML TableColumn weight;
    @FXML TableColumn st_date;
    @FXML TableColumn end_date;
    @FXML TableColumn status;

    @FXML Button search;
    @FXML Button returnn;
    @FXML Button submit;

    @FXML

    public void initialize(){
        goods.setCellValueFactory(new PropertyValueFactory<GoodsForReturn, String>("idd"));
        weight.setCellValueFactory(new PropertyValueFactory<GoodsForReturn, Double>("weightt"));
        st_date.setCellValueFactory(new PropertyValueFactory<GoodsForReturn, String>("st_date"));
        end_date.setCellValueFactory(new PropertyValueFactory<GoodsForReturn, String>("end_date"));
        status.setCellValueFactory(new PropertyValueFactory<GoodsForReturn, String>("status"));
        table.getColumns().setAll(goods, weight, st_date, end_date, status);
    }

    public void newAlert(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING, "", ButtonType.OK);
        alert.setHeaderText(message);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK){
            alert.close();
        }
    }

    @FXML
    public void back(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.CANCEL, ButtonType.OK);
        alert.setTitle("Back to your main page?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.CANCEL){
            alert.close();
        }
        else if (alert.getResult() == ButtonType.OK){
            Button b = (Button) event.getSource();
            Stage stage =  (Stage) b.getScene().getWindow();
            FXMLLoader loadMain = new FXMLLoader(getClass().getResource("StockMainPage.fxml"));
            stage.setScene(new Scene((Parent) loadMain.load()));
        }

    }

    @FXML
    public void search(){
        String idd = id.getText();
        int c = 0;
        if (idd.equals("")){
            newAlert("กรุณากรอกหมายเลขใบเบิกสินค้า");
        }
        else if (db.verifyStatus(idd)){
            newAlert("หมายเลขใบเบิกสินค้าไม่ถูกต้อง กรุณากรอกใหม่");
        }
        else{
            li = db.getReturnGoods(idd);
            if (li.size() == 0){
                newAlert("หมายเลขใบเบิกสินค้าไม่ถูกต้อง กรุณากรอกใหม่");
            }
            else{
                for (int i = 0; i < li.size(); i++){
                    if (li.get(i).getStatus().equals("done")){
                        c++;
                    }
                }
                if (c == li.size()){
                    newAlert("ใบเบิกสินค้านี้ทำการคืนสินค้าครบถ้วนแล้ว");
                }
                else{
                    pickId.setText(db.getPicker(idd));
                    picker.setVisible(true);
                    pickId.setVisible(true);
                    returnn.setVisible(true);
                    returnn.setDisable(false);
                    list = FXCollections.observableArrayList(db.getReturnGoods(idd));
                    table.setItems(list);
                }
            }
        }
    }

    @FXML
    public void return_goods(){
        if (table.getSelectionModel().getSelectedItem() == null){
            newAlert("กรุณาเลือกประเภทของสินค้าที่ต้องการคืน");
        }
        else{
            String ii = ((GoodsForReturn ) table.getSelectionModel().getSelectedItem()).getIdd();
            FXMLLoader supply = new FXMLLoader(getClass().getResource("Returning.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Returning");
            try {
                stage.setScene(new Scene((Parent) supply.load(), 300, 300));
                Returning returning = supply.getController();
                returning.setReturnGoods(this, ii);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void update(String bill, String goods, Double we){
        try {
            Double d = db.getWeight(goods);
            Double dou = db.getWeightForReturn(bill, goods);
            d+=we;
            dou -= we;
            if (dou < 0){
                newAlert("กรอกจำนวนสินค้าผิด กรุณากรอกใหม่");
            }
            else{
                db.updateReturning(bill, goods, dou, d);
                for (int i = 0; i < list.size(); i++){
                    if (li.get(i).getIdd().equals(goods)){
                        li.get(i).setWeightt(dou);
                        if (dou == 0){
                            li.get(i).setStatus("done");
                        }
                        list = FXCollections.observableArrayList(li);
                        table.setItems(list);
                        break;
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void confirm(ActionEvent event) throws IOException {
        Button b = (Button) event.getSource();
        Stage stage =  (Stage) b.getScene().getWindow();
        FXMLLoader loadMain = new FXMLLoader(getClass().getResource("StockMainPage.fxml"));
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("เสร็จสิ้นการปรับปรุงข้อมูลแล้วใช่หรือไม่?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES){
            stage.setScene(new Scene((Parent) loadMain.load()));
        }
        alert.close();
    }

    public String getId(){
        return id.getText();
    }
}
