
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    public class AddPage {

    /**
     * bug when choose company more than once
     */

    JdbcSQLiteConnection db = new JdbcSQLiteConnection();
    static ObservableList sup;
    ObservableList list;

    Goods goods;

    String name="";
    String size="";

    @FXML
    ComboBox supplier;

    @FXML
    ComboBox goodsId;

    @FXML
    TextField weight;

    @FXML
    TextField price;

    @FXML
    Button cancel;

    @FXML
    Button submit;

    @FXML
    public void initialize(){
        goodsId.setDisable(true);
        try {
            ArrayList su = db.getAllSupplier();
            su.add("new supplier...");
            sup = FXCollections.observableArrayList(su);
            supplier.setItems(sup);
            goodsId.setItems(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void haveGoods() throws SQLException {
        if (goods != null){
            supplier.setValue(db.getASupplier(goods.getId()));
            try {
                supplier_check();
            } catch (IOException e) {
                e.printStackTrace();
            }
            goodsId.setValue(goods.getId());
        }
    }

    public void addComp(String comp){
        sup.add(sup.size()-1, comp);
    }

    private void addGoods(String goods) {
        list.add(list.size()-1, goods);
    }
    @FXML
    public void supplier_check() throws IOException {
        goodsId.setValue(null);
        if (supplier.getValue().equals("new supplier...")){
            goodsId.setDisable(true);
            newPage("supply");
        }
        else{
            goodsId.setDisable(false);
            try {
                list = FXCollections.observableList(db.getAllGoods(supplier.getValue()+""));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            list.add("new goods...");
            goodsId.setItems(list);
        }
    }

    @FXML
    public void goods_check() throws IOException{
        if (goodsId.getValue() != null){
            if (goodsId.getValue().equals("new goods...")){
                newPage("goods");
            }
        }
    }

    @FXML
    public void cancel(ActionEvent event) throws IOException {
        /*
        not finish cant go back to StockMainPage
         */
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.CANCEL, ButtonType.OK);
        alert.setTitle("Cancel confirm?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.CANCEL){
            alert.close();
        }
        else if (alert.getResult()==ButtonType.OK){
            setPage(event);
        }
    }

    public void setPage(ActionEvent event) throws IOException {
        Button can = (Button) event.getSource();
        Stage stage =  (Stage) can.getScene().getWindow();
        FXMLLoader loadMain = new FXMLLoader(getClass().getResource("StockMainPage.fxml"));
        stage.setScene(new Scene((Parent) loadMain.load()));
    }

    public boolean checkWeightPrice(){
        String st = weight.getText();
        String string = price.getText();
        if (st.equals("") || string.equals("")){
            return true;
        }
        else{
            for (int i = 0; i < st.length(); i++){
                if (Character.isLetter(st.charAt(i))){
                    return true;
                }
            }
            for (int i = 0; i < string.length(); i++){
                if (Character.isLetter(string.charAt(i))){
                    return true;
                }
            }
            Integer in = Integer.parseInt(st);
            Integer p = Integer.parseInt(string);
            if (in > 1000 || in < 1 || p < 100 || p > 10000){
                return true;
            }
            return false;
        }
    }

    @FXML
    public void verify(ActionEvent event) throws IOException, SQLException {
        if (supplier.getValue() == null|| goodsId.getValue() == null || checkWeightPrice()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "กรอกข้อมูลสินค้าผิดพลาด กรุณากรอกใหม่", ButtonType.OK);
            alert.setHeaderText("ข้อมูลผิดพลาด");
            alert.show();
            if (alert.getResult() == ButtonType.OK){
                alert.close();
            }
        }
        else{
            if (goods == null){
                if (db.checkGoods(goodsId.getValue().toString())){
                    insertGoodstoDB(name, size);
                }
                else{
                    updateDatabase();
                }
            }
            else{
               updateDatabase();
            }
            setPage(event);
        }
    }

    public void updateDatabase(){
        Double w = Double.parseDouble(weight.getText());
        Double p = Double.parseDouble(price.getText());
        try {
            Double[] doubles = db.getWeightPrice((String) goodsId.getValue());
            Double total = doubles[1];
            total += w;
            doubles[0] += w;
            int in = Math.toIntExact(Math.round(((doubles[2]*doubles[1])+(p*w))/total*100));
            p = (double) in/100;
            db.updateGoods(goodsId.getValue().toString(), doubles[0], total, p);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void newPage(String type) throws IOException {
        String name = "";
        String title = "";
        if  (type.equals("supply")){
            name = "NewSupplier.fxml";
            title = "New Supplier";
        }
        else if (type.equals("goods")){
            name = "newGoodsPage.fxml";
            title = "new Goods";
        }
        FXMLLoader supply = new FXMLLoader(getClass().getResource(name));;
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene((Parent) supply.load(), 350, 400));
        if (name.equals("NewSupplier.fxml")){
            NewSupplierPage con = supply.getController();
            con.setController(this);
        }
        else if (name.equals("newGoodsPage.fxml")){
            NewGoods con = supply.getController();
            con.setController(this);
        }
        stage.setResizable(false);
        stage.show();
    }

    public void addItems(String type, String word) {
        //add to list
        if (type.equals("supply")){
            addComp(word);
            supplier.setValue(word);
            System.out.println(word);

        }
        else if (type.equals("goods")){
            String string = setId(word);
            addGoods(string);
            goodsId.setValue(string);
            System.out.println(string);
        }
    }

    public String setId(String word){
        int i = 0;
        String[] st = word.split(" ");
        try {
            i = db.getSupplierOrder((String) supplier.getValue());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return st[0]+""+i+st[1];
    }

    public void insertGoodstoDB(String name, String size){
        db.insertGooods(supplier.getValue().toString(), goodsId.getValue().toString(), name, size, weight.getText(), price.getText());
    }

    public void setName(String name){
        this.name = name;
    }

    public void setSize(String size){
        this.size = size;
    }

    public void setGoods(Goods goods){
        this.goods = goods;
        try {
            haveGoods();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
