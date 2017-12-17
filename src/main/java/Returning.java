import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Returning {

    ReturnGoods returnGoods;
    String ii;

    @FXML Text id;

    @FXML TextField quantity;

    @FXML Button save;
    @FXML Button cancel;


    @FXML
    public void confirm(ActionEvent event){
        if (quantity.getText().equals("")){
            newAlert("กรุณากรอกจำนวณสินค้า");
        }
        else{
            int c = 0;
            String st = quantity.getText();
            for (int i = 0; i < st.length(); i++){
                if (Character.isLetter(st.charAt(i))){
                    newAlert("กรอกจำนวนสินค้าผิด กรุณากรอกใหม่");
                    c++;
                    break;
                }
            }
            if (c == 0){
                Double d = Double.parseDouble(quantity.getText());
                returnGoods.update(returnGoods.getId(), id.getText(), d);
                back(event);
            }
        }
    }

    @FXML
    public void back(ActionEvent event){
        Button b = (Button) event.getSource();
        Stage stage = (Stage) b.getScene().getWindow();
        stage.close();
    }

    public void newAlert(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING, "", ButtonType.OK);
        alert.setHeaderText(message);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK){
            alert.close();
        }
    }

    public void setReturnGoods(ReturnGoods returnGoods, String ii){
        this.returnGoods = returnGoods;
        id.setText(ii);
    }
}
