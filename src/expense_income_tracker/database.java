package expense_income_tracker;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class database {
    String url = "jdbc:mysql://localhost:3306/personal_finance_tracker_database";
    Connection connection;
    /*
    b1 : Xóa bảng dữ liệu hiện tại bằng lệnh:
            drop talbe entry_table;

    b2: viết cái này vào mySQL rồi chạy

        create database personal_finance_tracker_database;
        use personal_finance_tracker_database;
        create table entry_table(

            date_ date,
            type_ varchar(15),
            amount numeric(20,2),
            description varchar(100),

            id int primary key auto_increment not null
        );


     */
    String user = "root";
    String password = "123456";




        public void insertToDatabase (Entry e){

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");

                Connection connection = DriverManager.getConnection(url, user, password);

                Statement statement = connection.createStatement();


                String date = e.getDate();
                String type = e.getType();
                Double amount = e.getAmount();
                String description = e.getDescription();


                statement.executeUpdate("insert into entry_table values('" + date + "','" + type + "','" + amount + "','" + description + "','" + 0  +"')");

                connection.close();
                statement.close();

            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }

        Statement connect() {
            Statement statement = null;
            try {
                statement = connection.createStatement();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null ,"Cannot connect to database", "Error", JOptionPane.ERROR_MESSAGE);
            }
            return statement;
        }

        ResultSet cmdExecute(String cmd) {
            ResultSet resultSet = null;

            try {
                resultSet = connect().executeQuery(cmd);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null ,"Cannot execute SQL command", "Error", JOptionPane.ERROR_MESSAGE);
            }
            return resultSet;
        }

        // chuyển dữ liệu trả về từ database về dạng ArrayList
        public Entry_Table databaseToEntryTable(ResultSet result) {
            Entry_Table entry_table = new Entry_Table();
            Entry entry;

            try {
            while(result.next()) {
                String date = result.getString("date_");
                String type = result.getString("type_");
                Double amount = result.getDouble("amount");
                String description = result.getString("description");
                int id = result.getInt("id");

                entry = new Entry(amount,date,type,description,id);
                entry_table.adden(entry);
            }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null ,"retrieved data not found", "Error", JOptionPane.ERROR_MESSAGE);

            }
            return entry_table;
        }




        public Entry_Table returnAll(){
            Entry_Table entry_table = null;
            try {

               // Class.forName("com.mysql.cj.jdbc.Driver");

                Connection connection = DriverManager.getConnection(url, user, password);

                Statement statement = connection.createStatement();

                String cmd = "select * from entry_table";
                ResultSet result = statement.executeQuery(cmd);

                entry_table = this.databaseToEntryTable(result);


                //JOptionPane.showMessageDialog(null,"Successfully retriving data from database");
                //System.out.print("Success");
                
                
            //} catch (ClassNotFoundException ex) {
            //    ex.printStackTrace();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            return entry_table;


        }

        public void removeThis(int id) {

            //Class.forName("com.mysql.cj.jdbc.Driver");

        }




    }

