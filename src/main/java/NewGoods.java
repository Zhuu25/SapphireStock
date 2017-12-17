
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class NewGoods {

    String id;
    String[] ar;

    AddPage con;

    @FXML
    TextField name;

    @FXML
    TextField size;

    @FXML
    Button submit;

    @FXML
    protected void confirm(ActionEvent event){
        if (alert()){
            id = name.getText().charAt(0)+" "+ar[0]+ar[1]+0;
            System.out.println(name.getText().charAt(0));
            Button b = (Button) event.getSource();
            Stage stage = (Stage) b.getScene().getWindow();
            if (alert()){
                con.addItems("goods", id);
                con.setName(name.getText());
                con.setSize(size.getText());
                stage.close();
            }
            stage.close();
        }
    }

    public String error(){
        if (checkGoods().equals("") == false){
            return checkGoods()+" และ"+checkSize();
        }
        else {
            return checkGoods() + checkSize();
        }
    }

    public String checkGoods(){
        String st = "";
        if (name.getText().equals("")){
            st = "ชื่อสินค้า";
        }
        return st;
    }

    public String checkSize(){
        String st = "";
        ar = size.getText().split("x");
        if (ar.length < 2){
            return "ขนาดของสินค้า";
        }
        if (ar[0].equals("") || ar[1].equals("")){
            st = "ขนาดของสินค้า";
            return st;
        }
        else{
            for (int i = 0; i < ar[0].length(); i++){
                if (Character.isLetter(ar[0].charAt(i))){
                    st = "ขนาดของสินค้า";
                    return st;
                }
            }

            for (int i = 0; i < ar[1].length(); i++){
                if (Character.isLetter(ar[1].charAt(i))){
                    st = "ขนาดของสินค้า";
                    return st;
                }
            }
            int c = 0;
            int cha = 0;
            String string = size.getText();
            for (int i = 0; i < string.length(); i++){
                if (string.charAt(i) == 'x'){
                    c++;
                }
                else if (Character.isLetter(string.charAt(i)) && string.charAt(i) != 'x'){
                    cha++;
                }
            }
            if (c != 1 || cha >  0){
                st = "ขนาดของสินค้า";
                return st;
            }
        }
        return st;
    }



    public boolean alert(){
        if (error().equals("") == false){
            Alert alert = new Alert(Alert.AlertType.WARNING, "", ButtonType.OK);
            alert.setHeaderText("กรอกข้อมูล"+error()+"ผิดพลาด กรุณากรอกใหม่");
            alert.show();
            if (alert.getResult() == ButtonType.OK){
                alert.close();
            }
            return false;
        }
        else{
            return true;
        }
    }

    public void setController(AddPage addPage) {
        con = addPage;
    }
}
