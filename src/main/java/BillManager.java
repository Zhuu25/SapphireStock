import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class BillManager {

    JdbcSQLiteConnection db = new JdbcSQLiteConnection();

    String type;

    public BillManager(String type){
        this.type = type;
    }

    public String getId(){
        String max_id = null;
        try {
            max_id = db.getBillNum(type);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Integer integer = Integer.parseInt(max_id.substring(1));
        String id = max_id.charAt(0)+""+(integer+1);
        return id;
    }

    @FXML
    public void backToMain(ActionEvent event){
        Button b = (Button) event.getSource();
        Stage stage = (Stage) b.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SaleMainPage.fxml"));
        try {
            stage.setScene(new Scene((Parent) loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
