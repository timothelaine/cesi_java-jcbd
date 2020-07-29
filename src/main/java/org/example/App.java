package org.example;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[]argv) throws SQLException {

        DataBase db = new DataBase();

//        db.createTable();
        db.getData();
        db.updateData();
        db.getData();
        db.insertData();
        db.getData();
//        db.deleteData();
        db.closeConnexion();
    }
}
