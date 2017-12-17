import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


public class NewSupplierPage {

    @FXML
    TextField name;

    @FXML
    TextField company;

    @FXML
    TextField address;

    @FXML
    TextField phone;

    @FXML
    TextField mail;

    @FXML
    Button submit;

    static Stage stage;
    AddPage add;

    @FXML
    protected void confirm(ActionEvent event) throws IOException, SQLException {
        Button b = (Button) event.getSource();
        stage = (Stage) b.getScene().getWindow();
        if (alert()){
            add.addItems("supply", company.getText());
            JdbcSQLiteConnection db = new JdbcSQLiteConnection();
            db.insertSupplier(name.getText(), company.getText()+"  "+address.getText(), mail.getText(), phone.getText(),
                    AddPage.sup.size()-1);
            stage.close();
        }
    }

    public String list(){
        String list = "";
        int size = mail.getText().length();
        int point = 0;
        int at = 0;
        if (name.getText().equals("")){
            list = checkError(list, "รายชื่อผู้ติดต่อ");
        }
        else{
            for (int i = 0; i < name.getText().length(); i++){
                if (Character.isDigit(name.getText().charAt(i)) || name.getText().equals("")){
                    list = checkError(list, "รายชื่อผู้ติดต่อ");
                    break;
                }
            }
        }

        if (company.getText().equals("")){
            list = checkError(list, "ชื่อบริษัท");
        }

        if (address.getText().equals("")){
            list = checkError(list, "ที่ตั้งของบริษัท");
        }

        if (mail.getText().equals("")){
            list = checkError(list, "อีเมล์");
        }
        else{
            for (int i = 0; i < size; i++){
                if (mail.getText().charAt(i) == '@' && i < size-5){
                    at++;
                }
                else if (mail.getText().charAt(i) == '.'){
                    point++;
                }
                else if ((i == size-1 && point  == 0) || (i == size-5 && at == 0) || at > 1 || (i == size-1
                        && mail.getText().charAt(i) == '.')){
                    list = checkError(list, "อีเมล์");
                    break;
                }
            }
        }

        if (phone.getText().length() != 10 || phone.getText().charAt(0) != '0' || (phone.getText().charAt(1) != '8'
        && phone.getText().charAt(1) != '9')){
            list = checkError(list, "เบอร์โทรศัพท์");
        }
        else{
            for (int i = 0; i < phone.getText().length(); i++){
                if (Character.isLetter(phone.getText().charAt(i))){
                    list = checkError(list, "เบอร์โทรศัพท์");
                }
            }
        }
        return list;
    }

    private String checkError(String error, String erName){
        if (error.equals("")){
            error += erName;
        }
        else{
            if (erName.equals("เบอร์โทรศัพท์")){
                error += " และ"+erName;
            }
            else {
                error += " "+erName;
            }
        }
        return error;
    }

    public boolean alert(){
        if (list().equals("") == false){
            Alert alert = new Alert(Alert.AlertType.WARNING, "", ButtonType.OK);
            alert.setHeaderText("กรอกข้อมูล"+list()+"ผิดพลาด กรุณากรอกใหม่");
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

    public void setController(AddPage add){
        this.add = add;
    }
}
