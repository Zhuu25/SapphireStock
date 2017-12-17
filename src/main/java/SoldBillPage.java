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

public class SoldBillPage {

    JdbcSQLiteConnection db = new JdbcSQLiteConnection();
    ArrayList<KeepingGoods> goodsList = new ArrayList<KeepingGoods>();
    ObservableList li;
    SaleMainPage sales;
    String bill;
    BillManager manager;

    @FXML Text picker;

    @FXML
    TableView table;
    @FXML
    TableColumn order;
    @FXML TableColumn id;
    @FXML TableColumn weight;

    @FXML Button add;
    @FXML Button delete;
    @FXML Button submit;
    @FXML Button cancel;

    @FXML
    public void initialize(){
        order.setCellValueFactory(new PropertyValueFactory<KeepingGoods, String>("in"));
        id.setCellValueFactory(new PropertyValueFactory<KeepingGoods, String>("keepId"));
        weight.setCellValueFactory(new PropertyValueFactory<KeepingGoods, String>("keepWeight"));
        table.getColumns().setAll(order, id, weight);

        manager = new BillManager("SoldBill");
        bill = manager.getId();
        picker.setText(db.getOnlineStaff("Sales"));
    }

    @FXML
    public void back(ActionEvent event){
        manager.backToMain(event);
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
    public void action(ActionEvent event) throws SQLException {
        Button button = (Button) event.getSource();
        String className = "";
        String title="";
        if (button == add){
            className = "GoodsList.fxml";
            title = "Add Goods to your list";
        }
        else if (button == delete){
            KeepingGoods ke = (KeepingGoods) table.getSelectionModel().getSelectedItem();
            if (ke == null){
                newAlert("กรุณาเลือกข้อมูลที่ต้องการลบ");
            }
            else {
                int po = Integer.parseInt(ke.getIn());
                if (po <  goodsList.size()){
                    goodsList.remove(ke);
                    for (int i = po; i <= goodsList.size(); i++){
                        goodsList.get(i-1).setIn(i+"");
                    }
                }
                else{
                    goodsList.remove(ke);
                }
                li = FXCollections.observableArrayList(goodsList);
                table.setItems(li);
            }
        }

        if (className.equals("") == false){
            FXMLLoader supply = new FXMLLoader(getClass().getResource(className));
            Stage stage = new Stage();
            stage.setTitle(title);
            try {
                stage.setScene(new Scene((Parent) supply.load(), 350, 400));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (className.equals("GoodsList.fxml")){
                GoodsList go = supply.getController();
                go.setSoldBill(this);
            }
            stage.setResizable(false);
            stage.show();
        }
    }

    @FXML
    public void confirm(ActionEvent event) throws SQLException {
        if (goodsList.size() == 0){
            newAlert("กรุณากรอกข้อมูลสินค้า");
        }
        else {
            String a = picker.getText();
            for (int i = 0; i < goodsList.size(); i++) {
                String b = goodsList.get(i).getKeepId();
                Double c = Double.parseDouble(goodsList.get(i).getKeepWeight());
                db.insertSoldBill(bill, a, b, c);
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
            alert.setHeaderText("เลขที่ใบเบิก: " + bill + " กรอกข้อมูลสำเร็จ");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
                back(event);
            }
        }
    }

    public void addGoodsintoList(String[] strings){
        int c = 0;
        for (int i = 0; i < goodsList.size(); i++){
            if (goodsList.get(i).getKeepId().equals(strings[0])){
                c++;
                newAlert("ท่านได้กรอกข้อมูลสินค้าชนิดนี้แล้ว กรุณาตรวจสอบ");
                break;
            }
        }
        if (c == 0){
            goodsList.add(new KeepingGoods(goodsList.size()+1+"", strings[0], strings[1]));
            li = FXCollections.observableArrayList(goodsList);
            table.setItems(li);
        }
    }

    public void setSales(SaleMainPage sales){
        this.sales = sales;
    }
}
