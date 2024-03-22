package expense_income_tracker;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class database {
    String url = "jdbc:mysql://localhost:3306/personal_finance_tracker_database";
    Connection connection;
    Statement statement ;
    ResultSet result ;
    /*
    b1 : Xóa bảng dữ liệu hiện tại bằng lệnh:
            drop talbe entry_table;

    b2: viết cái này vào mySQL rồi chạy


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


//  để cái này ở đây phòng khi cần:  Class.forName("com.mysql.cj.jdbc.Driver");

        public void insertToDatabase (Entry e){

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");

                //Connection connection;
                connection = DriverManager.getConnection(url, user, password);

                //Statement statement;
                statement = connection.createStatement();


                String date = e.getDate();
                String type = e.getType();
                double amount = e.getAmount();
                String description = e.getDescription();


                statement.executeUpdate("insert into entry_table values('" + date + "','" + type + "','" + amount + "','" + description + "','" + 0  +"')");


//                connection.close();
//                statement.close();

            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }



        private Statement connect() {
            //Statement statement = null;
            try {
                connection = DriverManager.getConnection(url,user,password);
                statement = connection.createStatement();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null ,"Cannot connect to database", "Error", JOptionPane.ERROR_MESSAGE);
            }
            return statement;
        }

        private ResultSet cmdExecute(String cmd) {
            //ResultSet resultSet = null;
            //Statement statement;

            try {
                boolean worked = false;
                statement = connect();
                System.out.println(cmd);
                worked = statement.execute(cmd);
                if(worked) {
                    result = statement.getResultSet();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null ,"Cannot execute SQL command", "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }

            return result;
        }

        // chuyển dữ liệu trả về từ database về dạng ArrayList
        public Entry_Table databaseToEntryTable(ResultSet result) {
            Entry_Table entry_table = new Entry_Table();
            Entry entry;

            try {
            while(result.next()) {
                String date = result.getString("date");
                String type = result.getString("type");
                double amount = result.getDouble("amount");
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

        private void closing() {

//            try {
//                result.close();
//                statement.close();
//                connection.close();
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
        }




        public Entry_Table returnAll(){
            String cmd = "select * from entry_table";

            Entry_Table entry_table = null;
            //ResultSet result = cmdExecute(cmd);
            result = cmdExecute(cmd);
            entry_table = this.databaseToEntryTable(result);

            closing();
            return entry_table;
        }


        public void removeThis(int id) {
            String cmd = "delete from entry_table where id = "+ id;

            cmdExecute(cmd);
            closing();

        }



        public void editEntry(int index, Entry entry) {
            String date = entry.getDate();
            Double amount = entry.getAmount();
            String type = entry.getType();
            String des = entry.getDescription();


        }

        public double balanceCheck() {
            String cmd = "select sum(amount) as total_balance from entry_table";
            //ResultSet result;
            result =  cmdExecute(cmd);
            double balance =0.0;
            try {
                if (result.next()) {

                    balance = result.getDouble("total_balance");

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            closing();
            return balance;
        }

        void search() {

        }






    }

