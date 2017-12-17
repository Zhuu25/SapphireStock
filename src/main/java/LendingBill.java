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
import java.time.LocalDate;
import java.util.ArrayList;

public class LendingBill {

    JdbcSQLiteConnection db = new JdbcSQLiteConnection();
    ArrayList<KeepingGoods> goodsList = new ArrayList<KeepingGoods>();
    ObservableList li;
    SaleMainPage sales;
    String bill;
    BillManager manager;

    @FXML Text picker;

    @FXML TableView table;
    @FXML TableColumn order;
    @FXML TableColumn id;
    @FXML TableColumn weight;

    @FXML DatePicker st_date;
    @FXML DatePicker end_date;

    @FXML Button add;
    @FXML Button delete;
    @FXML Button submit;
    @FXML Button cancel;

    public void initialize(){
        order.setCellValueFactory(new PropertyValueFactory<KeepingGoods, String>("in"));
        id.setCellValueFactory(new PropertyValueFactory<KeepingGoods, String>("keepId"));
        weight.setCellValueFactory(new PropertyValueFactory<KeepingGoods, String>("keepWeight"));
        table.getColumns().setAll(order, id, weight);

        manager = new BillManager("LendingBill");
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
    public void action(ActionEvent event) {
            Button button = (Button) event.getSource();
            String className = "";
            String title = "";
            if (button == add) {
                className = "GoodsList.fxml";
                title = "Add Goods to your list";
            } else if (button == delete) {
                KeepingGoods ke = (KeepingGoods) table.getSelectionModel().getSelectedItem();
                if (ke == null) {
                    newAlert("กรุณาเลือกข้อมูลที่ต้องการลบ");
                } else {
                    int po = Integer.parseInt(ke.getIn());
                    if (po < goodsList.size()) {
                        goodsList.remove(ke);
                        for (int i = po; i <= goodsList.size(); i++) {
                            goodsList.get(i - 1).setIn(i + "");
                        }
                    } else {
                        goodsList.remove(ke);
                    }
                    li = FXCollections.observableArrayList(goodsList);
                    table.setItems(li);
                }
            }

            if (className.equals("") == false) {
                FXMLLoader supply = new FXMLLoader(getClass().getResource(className));
                Stage stage = new Stage();
                stage.setTitle(title);
                try {
                    stage.setScene(new Scene((Parent) supply.load(), 350, 400));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (className.equals("GoodsList.fxml")) {
                    GoodsList go = supply.getController();
                    go.setLendingBill(this);
                }
                stage.setResizable(false);
                stage.show();
            }
    }

    @FXML
    public void confirm(ActionEvent event) throws SQLException {
        LocalDate start = st_date.getValue();
        LocalDate end = end_date.getValue();
        System.out.println(table.getColumns());
        if (table.getColumns().isEmpty() || start == null || end == null || goodsList.size() < 1){
            newAlert("กรอกข้อมูลไม่ครบถ้วน กรุณาตรวจสอบ");
        }
        else{
            LocalDate today = LocalDate.now();
            LocalDate du = today.plusDays(7);
            LocalDate due = start.plusDays(7);

            if (start.isAfter(end) || today.isAfter(start) || today.isAfter(end)){
                newAlert("วันที่เบิก-คืนผิดพลาด กรุณากรอกใหม่");
            }
            else if (start.isAfter(du) || end.isAfter(due)){
                //check length when lend
                newAlert("กรอกวันที่เบิก-คืนสินค้าเกินเวลาที่กำหนด");
            }
            else {
                String a = picker.getText();
                String d = start + "";
                String e = end + "";
                for (int i = 0; i < goodsList.size(); i++) {
                    String b = goodsList.get(i).getKeepId();
                    Double c = Double.parseDouble(goodsList.get(i).getKeepWeight());
                    db.insertLendingBill(bill, a, b, c, d, e);
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
