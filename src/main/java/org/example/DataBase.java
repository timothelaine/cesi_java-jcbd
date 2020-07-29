package org.example;

import java.sql.*;
import java.text.ParseException;

import org.apache.log4j.*;

public class DataBase {

    private Connection connexion = null;
    private PreparedStatement ps;
    private static final Logger logger = Logger.getLogger(DataBase.class);

     DataBase(){

         PropertyConfigurator.configure("src/log4j.properties");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.error(e);
            return;
        }

        try {
            connexion = DriverManager.getConnection("jdbc:mysql://51.15.2.200:3306/java-database?serverTimezone=UTC", "java-database", "0000");

        } catch (SQLException e) {
            System.out.println("Connection échouée! ");
            logger.error(e);
            return;
        }
        System.out.println("La connexion réussi !");

    }

    public void closeConnexion() throws SQLException {

        try {
            connexion.close();
        } catch (SQLException e) {
            logger.error(e);
        }
    }

    public void createTable(){

        String request =  "CREATE TABLE tlperson" +
                "(id INT PRIMARY KEY NOT NULL, first_name VARCHAR(40) NOT NULL, last_name VARCHAR(40) NOT NULL, dobDATE DATE NOT NULL)";

        try {
            ps = connexion.prepareStatement(request);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("La requête à échoué !");
            logger.error(e);
            return;
        }
        System.out.println("La table à bien été créée");
    }

    public void getData() throws SQLException {

        ResultSet response = null;
        String request = "SELECT * FROM tlperson";

        try {
            connexion.setAutoCommit(false);
            PreparedStatement  ps = connexion.prepareStatement(request, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            response =  ps.executeQuery();

            if(response != null) {
                connexion.commit();
            }else{
                connexion.rollback();
            }
        } catch (SQLException e) {
            System.out.println("La requête à échoué !");
            logger.error(e);
            return;
        }

        System.out.println("Résultat de la requête :");
        if(response != null) {
            while (response.next()) {
                System.out.print(response.getString(2) + ' ' + response.getString(3) + "\n");
                response.updateString(2, response.getString(2) + "=)");
                response.updateRow();
            }
        }else{
            System.out.println("Aucune données trouvées");
            logger.info("Aucune données trouvées");
        }
        }

    public void updateData() throws SQLException {

        String request = "UPDATE tlperson SET first_name = ? WHERE first_name = ?";
        int result;

        try {
            connexion.setAutoCommit(false);
            ps = connexion.prepareStatement(request);
            ps.setString(1, "Billy");
            ps.setString(2, "Bill");
            result =  ps.executeUpdate();
            if(result == 1){
                connexion.commit();
            }else{
                connexion.rollback();
            }
            connexion.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println("La requête à échoué !");
            logger.error(e);
            return;
        }
        System.out.println("La requête s'est bien exécutée");
    }

    public void insertData()  {

        String request = "INSERT INTO tlperson (first_name, last_name, dobDATE) VALUES (? , ? , ?)";

        try {
            ps = connexion.prepareStatement(request);

            ps.setString(1, "Clauvis");
            ps.setString(2, "l");
            ps.setDate(3, new Date(System.currentTimeMillis()));
            ps.executeUpdate(request);


        } catch (SQLException e) {
//            e.printStackTrace();
            System.out.println("La requête à échoué !" + e);
            logger.error(e);
            return;
        }
        System.out.println("La requête s'est bien exécutée");
    }

    public void deleteData()  {

        String request = "DELETE FROM tlperson WHERE id = 1";

        try {
            connexion.setAutoCommit(false);
            ps = connexion.prepareStatement(request);
           int result =  ps.executeUpdate(request);
            if(result == 1){
                connexion.commit();
            }else{
                connexion.rollback();
            }
        } catch (SQLException e) {
            System.out.println("La requête à échoué !" + e);
            logger.error(e);
            return;
        }
        System.out.println("La requête s'est bien exécutée");
    }
}
