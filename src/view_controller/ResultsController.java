/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model_tools.SQLite_Manager;
import model_tools.Trail;

/**
 * FXML Controller class
 *
 * @author yonij
 */
public class ResultsController implements Initializable {

    private ObservableList<Trail> trailList = FXCollections.observableArrayList();

    @FXML
    private TableView<Trail> tbvResults;

    @FXML
    private Label lblMessage;

    String activity;
    String state;
    String dogChoiceSelection;
    int difficulty;

    String sqlStatement = "";

    
    SQLite_Manager sqlHandler = new SQLite_Manager();
    ResultSet rs;

    // constructor that takes in an activity, location(state), difficulty level, and a rule for dogs
    ResultsController(String a, String s, int d, String dog) {
        this.activity = a;
        this.state = s;
        this.difficulty = d;
        this.dogChoiceSelection = dog;
    }

    /**
     * Initializes the controller class.
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // if dogs is not a factor
        if (dogChoiceSelection.toLowerCase().contentEquals("don't care")) {
            sqlStatement = "SELECT * FROM ALLTRAILS "
                    + "WHERE  state_name = '" + state + "' AND "
                    + "activities LIKE '%" + activity.trim() + "%' AND difficulty <=" + difficulty
                    + " ORDER BY difficulty DESC;";
            try {
                rs = sqlHandler.executeSelectStatement(sqlStatement);
                while (rs.next()) {
                    Trail trail = new Trail(rs.getString("trail_name"), rs.getString("area_name"), rs.getString("state_name"),
                            rs.getDouble("length"), rs.getInt("difficulty"), rs.getString("features"), rs.getString("activities"));
                    trailList.add(trail);
                    System.out.println(rs.getString("state_name"));
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            if (!trailList.isEmpty()) {
                tbvResults.setItems(trailList);
                tbvResults.refresh();
            } else {
                lblMessage.setText("Sorry, there are no matches with your selected criteria!");
            }
            // if user selects that they want a trail without dogs
        } else if (dogChoiceSelection.toLowerCase().contentEquals("no")) {
            sqlStatement = "SELECT * FROM ALLTRAILS "
                    + "WHERE  state_name = '" + state + "' AND "
                    + "activities LIKE '%" + activity.trim() + "%' AND difficulty <=" + difficulty
                    + " AND features LIKE '%dogs-no%'"
                    + " ORDER BY difficulty DESC;";
            try {
                rs = sqlHandler.executeSelectStatement(sqlStatement);
                while (rs.next()) {
                    Trail trail = new Trail(rs.getString("trail_name"), rs.getString("area_name"), rs.getString("state_name"),
                            rs.getDouble("length"), rs.getInt("difficulty"), rs.getString("features"), rs.getString("activities"));
                    trailList.add(trail);
                    System.out.println(rs.getString("state_name"));
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            if (!trailList.isEmpty()) {
                tbvResults.setItems(trailList);
                tbvResults.refresh();
            } else {
                lblMessage.setText("Sorry, there are no matches with your selected criteria!");
            }
            // trails that dogs are allowed 
        } else {
            sqlStatement = "SELECT * FROM ALLTRAILS "
                    + "WHERE  state_name = '" + state + "' AND "
                    + "activities LIKE '%" + activity.trim() + "%' AND difficulty <=" + difficulty
                    + " AND features NOT LIKE '%dogs-no%'"
                    + " ORDER BY difficulty DESC;";
            try {
                rs = sqlHandler.executeSelectStatement(sqlStatement);
                while (rs.next()) {
                    Trail trail = new Trail(rs.getString("trail_name"), rs.getString("area_name"), rs.getString("state_name"),
                            rs.getDouble("length"), rs.getInt("difficulty"), rs.getString("features"), rs.getString("activities"));
                    trailList.add(trail);
                    System.out.println(rs.getString("state_name"));
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            if (!trailList.isEmpty()) {
                tbvResults.setItems(trailList);
                tbvResults.refresh();
            } else {
                // if no matches are found
                lblMessage.setText("Sorry, there are no matches with your selected criteria!");
            }
        }

        // TODO
    }
// go back to the previus page
    @FXML
    void goBack(MouseEvent event) {
        // reseting the table and the trail results list 
        tbvResults.getItems().clear();
        tbvResults.refresh();
        trailList.clear();
        
                try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("questionnaire.fxml"));
            view_controller.QuestionnaireController controller = new QuestionnaireController();
            
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }catch(IOException e){
            
        }
    }

}
