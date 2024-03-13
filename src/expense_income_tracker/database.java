package expense_income_tracker;

import java.sql.*;

public class database {
    String url = "jdbc:mysql://localhost:3306/personal_finance_tracker_database";
    /*  viết cái này vào mySQL rồi chạy

        create database personal_finance_tracker_database;
        use personal_finance_tracker_database;
        create table entry_table(
            date_ date,
            type_  varchar(15),
            amount numeric(20,2),
            description varchar(100)


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

                System.out.println("den day thi dam dc");
                statement.executeUpdate("insert into entry_table values('" + date + "','" + type + "','" + amount + "','" + description + "')");

            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }




        public void Display_all (){
            try {

                Class.forName("com.mysql.cj.jdbc.Driver");

                Connection connection = DriverManager.getConnection(url, user, password);

                Statement statement = connection.createStatement();

                String cmd = "select * from entry_table";
                ResultSet result = statement.executeQuery(cmd);

                while (result.next()) {
                    String cal = result.getString("date_");
                    String tp = result.getString("type_");
                    String amount = result.getString("amount");
                    String desciption = result.getString("description");
                    System.out.println(cal + " " + tp + " " + amount + " " + desciption);
                }


                //JOptionPane.showMessageDialog(this,"Success");
                System.out.print("Success");

            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }


        }
    }

