import com.sun.org.apache.regexp.internal.RE;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcSQLiteConnection{

    private Statement statement;
    static Connection conn;

    public JdbcSQLiteConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:DatabaseSA.db";
            if (conn == null){
                conn = DriverManager.getConnection(dbURL);
            }
            if (conn != null) {
                DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
                statement = conn.createStatement();
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Statement getStatement(){
        return statement;
    }

    public String getPassword(String username) throws SQLException {
        String ans = "";
        String sql = "Select password from Staff where username = \'"+username+"\';";
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.next() == false){
            ans = "false";
        }
        else{
            ans = resultSet.getString(1);
        }
        return ans;
    }

    public void updateLoginStatus(String id, String status){
        String sql = "Update Staff Set status = \'"+status+"\' where username = \'"+id+"\'";
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getOnlineStaff(String type){
        String user = "";
        String sql = "Select username from Staff where department = \'"+type+"\' and status = 'online'";
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                user = resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public Integer getSupplierOrder(String company) throws SQLException {
        String sql = "Select orderr from supplier where Address like \'"+company+"%\'";
        ResultSet resultSet = statement.executeQuery(sql);
        return resultSet.getInt(1);
    }

    public String getASupplier(String id) throws SQLException {
        String sql = "Select supplier from Goods where GoodsId = \'"+id+"\'";
        ResultSet resultSet = statement.executeQuery(sql);
        return resultSet.getString(1);
    }

    //maybe use when add new supplier
    public boolean checkSupplier(String id) throws SQLException {
        String sql = "Select * from Staff WHERE username = \'"+id+"\'";
        ResultSet resultSet = statement.executeQuery(sql);;
        while (resultSet.next()){
            return  false;
        }
        return true;
    }

    public ArrayList getAllSupplier() throws SQLException {
        String sql = "Select address from supplier";
        ArrayList list = new ArrayList();
        ResultSet resultSet = statement.executeQuery(sql+"");
        while (resultSet.next()){
            String st = resultSet.getString(1);
            String[] sup = st.split("  ");
            list.add(sup[0]);
        }
        return list;

    }

    public boolean insertSupplier(String name, String address, String mail, String phone, int order){
        String sql = "Insert into Supplier (contactName, Address, mail, phone, orderr) values (\'"+name+ "\', \'"
                +address+"\', \'"+mail+"\', \'"+phone+"\', \'"+order+"\');";
        try {
            statement.executeUpdate(sql+"");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkGoods(String id) throws SQLException {
        String sql = "Select supplier from Goods where GoodsId = \'"+id+"\'";
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.next()){
            return false;
        }
        else{
            return true;
        }
    }

    public List<String> getAllGoods(String company) throws SQLException {
        String sql = "";
        if (company.equals("")){
            sql = "Select GoodsId from Goods";
        }
        else{
            sql = "Select GoodsId from Goods where supplier =  \'"+company+"\'";
        }
        List list = new ArrayList();
        ResultSet resultSet = statement.executeQuery(sql+"");
        while (resultSet.next()){
            list.add(resultSet.getString(1));
        }
        return list;
    }

    public Double getWeight(String id) throws SQLException {
        String sql = "Select weight from Goods where GoodsId = \'"+id+"\'";
        ResultSet resultSet = statement.executeQuery(sql);
        Double dou = resultSet.getDouble(1);
        return dou;
    }

    public Double[] getWeightPrice(String id) throws SQLException {
        Double[] doubles = new Double[3];
        String sql = "Select weight, totalWeight, avgPrice from Goods where GoodsId = \'"+id+"\'";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            doubles[0]  = Double.parseDouble(resultSet.getString(1));
            doubles[1] = Double.parseDouble(resultSet.getString(2));
            doubles[2] = Double.parseDouble(resultSet.getString(3));
        }
        return doubles;
    }

    public ArrayList getTableData() throws SQLException {
        ArrayList list = new ArrayList();
        String sql = "Select * from Goods ";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            String id = resultSet.getString(2);
            String name = id+" "+resultSet.getString(3);
            String size = name+" "+resultSet.getString(4);
            String weight = size+" "+resultSet.getDouble(5);
            String price = weight+" "+resultSet.getDouble(7);
            list.add(price);
        }
        return list;
    }

    public void insertGooods(String supplier, String id, String name, String size, String weight, String price){
        String sql = "Insert into Goods (supplier, GoodsId, goodsName, sizee, weight, totalWeight, avgPrice) VALUES (\'"+supplier
        +"\', \'"+id+"\', \'"+name+"\', \'"+size+"\', \'"+weight+"\', \'"+weight+"\', \'"+price+"\');";
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateGoods(String id, Double weight){
        String sql = "Update Goods Set weight = \'"+weight+"\' where GoodsId = \'"+id+"\'";
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateGoods(String id, Double weight, Double totalWeight, Double price){
        String sql = "UPDATE Goods SET weight = \'"+weight+"\', totalWeight = \'"+totalWeight+"\', avgPrice = \'"+price+"\' WHERE GoodsId = \'"+id+"\'";
        try{
            statement.executeUpdate(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void insertLendingBill(String num, String id, String goodsId, Double weight, String st_date, String end_date) {
        String sql = "Insert into LendingBill (billNum, pickerId, goodsId, weight, st_date, end_date) values (\'"+num+"\', \'"+
                id+"\', \'"+goodsId+"\', \'"+weight+"\', \'"+st_date+"\', \'"+end_date+"\');";

        String sqltoReturn = "Insert into ReturnGoods (billNum, pickerId, goodsId, weight, st_date, end_date, status) values (\'"+num+"\', \'"+
                id+"\', \'"+goodsId+"\', \'"+weight+"\', \'"+st_date+"\', \'"+end_date+"\', 'ready');";
        try {
            statement.executeUpdate(sql);
            statement.executeUpdate(sqltoReturn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertSoldBill(String num, String id, String goodsId, Double weight){
        String sql = "Insert into SoldBill (billNum, pickerId, goodsId, weight, status) values (\'"+num+"\', \'"+
                id+"\', \'"+goodsId+"\', \'"+weight+"\', 'uncheck');";
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBill(Character t, String id){
        String type = "";
        String sta = "";
        if (t == 'S'){
            type = "SoldBill";
            sta = "checked";
        }
        else if (t == 'L'){
            type = "ReturnGoods";
            sta = "waiting";
        }
        String sql = "Update "+type+" Set status =\'"+sta+"\' where billNum = \'"+id+"\'";
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getBillNum(String table) throws SQLException {
        String sql = "Select max(billNum) from "+table;
        ResultSet resultSet = statement.executeQuery(sql);
        return resultSet.getString(1);
    }

    public boolean verifyStatus(String idd){
        String sql = "Select status from ReturnGoods where billNum = \'"+idd+"\'";
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                String st = resultSet.getString(1);
                if (st.equals("ready")){
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getPicker(String id){
        String picker = "";
        String sql = "Select pickerId from ReturnGoods where billNum = \'"+id+"\'";
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                picker = resultSet.getString(1);
                return  picker;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return picker;
    }

    public ArrayList<GoodsForReturn> getReturnGoods(String id){
        ArrayList<GoodsForReturn> d = new ArrayList<GoodsForReturn>();
        String sql = "Select goodsId, weight, st_date, end_date, status from ReturnGoods where billNum = \'"+id+"\'";
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                String idd = resultSet.getString(1);
                Double weight = resultSet.getDouble(2);
                String start = resultSet.getString(3);
                String end = resultSet.getString(4);
                String status = resultSet.getString(5);
                d.add(new GoodsForReturn(idd, weight, start, end, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return d;
    }

    public ArrayList<String> getGoodsBill(Character t, String id){
        String type = "";
        ArrayList<String> d = new ArrayList<String>();
        if (t == 'S'){
            type = "SoldBill";
        }
        else if (t == 'L'){
            type = "ReturnGoods";
        }
        String sql = "Select goodsId from "+type+" where billNum = \'"+id+"\'";
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            d.add(resultSet.getString(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return d;

    }

    public Double getWeightForReturn(String bill, String id){
        Double d = 0.0;
        String sql = "Select weight from ReturnGoods where billNum = \'"+bill+"\' AND goodsId = \'"+id+"\'";
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            d = resultSet.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return d;
    }

    public ArrayList<Double> getWeightBill(Character t, String id){
        String type = "";
        ArrayList<Double> d = new ArrayList<Double>();
        if (t == 'S'){
            type = "SoldBill";
        }
        else if (t == 'L'){
            type = "ReturnGoods";
        }
        String sql = "Select weight from "+type+" where billNum = \'"+id+"\'";
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            d.add(resultSet.getDouble(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return d;
    }


    public String checkBill(Character t, String id){
        String type = "";
        if (t == 'S'){
            type = "SoldBill";
        }
        else if (t == 'L'){
            type = "ReturnGoods";
        }
        String sql = "Select status from "+type+" where billNum = \'"+id+"\'";
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                String st = resultSet.getString(1);
                if (t == 'L' && st.equals("ready")){
                    return "true";
                }
                else if (t == 'S' && st.equals("uncheck")){
                    return "true";
                }
                else{
                    return st;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "false";
    }

    public void updateReturning(String bill, String id, Double Reweight, Double weight){
        String sql = "";
        if (Reweight == 0){
            sql = "Update ReturnGoods Set weight = '0', status = 'done' where billNum = \'"+bill+"\' AND goodsId = \'"+id+"\'";
        }
        else{
            sql = "Update ReturnGoods Set weight = \'"+Reweight+"\' where billNum = \'"+bill+"\' AND goodsId = \'"+id+"\'";
        }
        try {
            statement.executeUpdate(sql);
            updateGoods(id, weight);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}