
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;

public class StockMainPage {

    JdbcSQLiteConnection db;
    Stage stage;
    FXMLLoader loader;
    Goods goods;
    String user;

    @FXML TableView table;
    @FXML TableColumn id;
    @FXML TableColumn name;
    @FXML TableColumn size;
    @FXML TableColumn weight;
    @FXML TableColumn price;

    @FXML Button add;
    @FXML Button export;
    @FXML Button returnn;
    @FXML Button logout;

    @FXML
    public void initialize(){
        db = new JdbcSQLiteConnection();

        id.setCellValueFactory(new PropertyValueFactory<Goods, String>("id"));
        name.setCellValueFactory(new PropertyValueFactory<Goods, String>("name"));
        size.setCellValueFactory(new PropertyValueFactory<Goods, String>("size"));
        weight.setCellValueFactory(new PropertyValueFactory<Goods, Double>("weight"));
        price.setCellValueFactory(new PropertyValueFactory<Goods, Double>("price"));
        table.getColumns().setAll(id, name, size, weight, price);


        try {
            table.setItems(getData());
            user = db.getOnlineStaff("Warehouse");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList getData() throws SQLException {
        ArrayList list = db.getTableData();
        ArrayList<Goods> go = new ArrayList<Goods>();
        for (int i = 0; i < list.size(); i++){
            String[] st = list.get(i).toString().split(" ");
            go.add(new Goods(st[0], st[1], st[2], Double.parseDouble(st[3]), Double.parseDouble(st[4])));
        }
        return FXCollections.observableList(go);
    }

    @FXML
    public void changePage(ActionEvent event) throws IOException {
        String nameClass  = "";
        Button b = (Button) event.getSource();
        String name = b.getId();
        if (name.equals("add") || name.equals("returnn")){
            if (name.equals("add")){
                nameClass = "AddPage.fxml";
                goods = (Goods) table.getSelectionModel().getSelectedItem();
            }
            else if (name.equals("returnn")){
                nameClass = "ReturnGoods.fxml";
            }
            stage = (Stage) b.getScene().getWindow();
            loader = new FXMLLoader(getClass().getResource(nameClass));
            stage.setScene(new Scene((Parent) loader.load()));
            if (goods != null){
                AddPage add = loader.getController();
                add.setGoods(goods);
            }
        }

        if (name.equals("export")){
            newPageEx();
        }

    }

    public void newPageEx() throws IOException {
        FXMLLoader supply = new FXMLLoader(getClass().getResource("ExportPage.fxml"));;
        Stage stage = new Stage();
        stage.setTitle("Export");
        stage.setScene(new Scene((Parent) supply.load(), 350, 200));
        ExportPage ex = supply.getController();
        ex.setStock(this);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        db.updateLoginStatus(user, "offline");
        Button l = (Button) event.getSource();
        stage = (Stage) l.getScene().getWindow();
        loader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));
        stage.setScene(new Scene((Parent) loader.load()));
    }


    public TableView getTable() {
        return table;
    }
}
