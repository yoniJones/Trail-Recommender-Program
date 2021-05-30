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
import javafx.beans.value.ObservableValue;
import javafx.fxml.*;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model_tools.SQLite_Manager;

/**
 * FXML Controller class
 *
 * @author yonij
 */
public class QuestionnaireController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    
    @FXML
    private ChoiceBox<String> statesCB;
    
    @FXML
    private ChoiceBox<Integer> difficultyCB;
    
    @FXML
    private RadioButton lblMountainBiking, lblCamping, lblHiking, lblFishing,lblBirding;

    
   
    @FXML
    private RadioButton lblYes;

    @FXML
    private RadioButton lblNo;

    @FXML
    private RadioButton lblDontCare;
    
   


    
    @FXML
    final ToggleGroup activities = new ToggleGroup();
    
    @FXML
    final ToggleGroup dogsAllowed = new ToggleGroup();
    
    RadioButton  selectedActivity, selectedDogOption;
    String stateSelection ="";
    int difficultySelection;
    String activitySelection;
    String dogSelectionString;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // connecting all activities to the same toggle group
        lblMountainBiking.setToggleGroup(activities);
        lblCamping.setToggleGroup(activities);
        lblHiking.setToggleGroup(activities);
        lblBirding.setToggleGroup(activities);
        lblFishing.setToggleGroup(activities);
        
        // connecting all dog options to the same toggle group
        lblYes.setToggleGroup(dogsAllowed);
        lblNo.setToggleGroup(dogsAllowed);
        lblDontCare.setToggleGroup(dogsAllowed);
        
        // setting the text
        lblMountainBiking.setText("Mountain-Biking");
        lblCamping.setText("Camping");
        lblHiking.setText("Hiking");
        lblFishing.setText("Fishing");
        lblBirding.setText("Birding");
        
        lblYes.setText("Yes");
        lblNo.setText("No");
        lblDontCare.setText("Don't Care");
        
        //tglHiking.setSelected(true);
        //tglDontCare.setSelected(true);
        
        
        
        
        
        // populationg the state choicebox 
        SQLite_Manager SQLmanager = new SQLite_Manager();
        ResultSet rs;
        try{
            rs = SQLmanager.getStates();
            while(rs.next()){
                statesCB.getItems().add(rs.getString("state_name"));
                
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        // populating the difficulty level choicebox
        int i = 1;
        while(i < 6){
          difficultyCB.getItems().add(i);
          i++;
        }
    }
    
  // verifies all the user selections and loads the the results paige 
    
    @FXML
    void goToResultPage(MouseEvent event) {
        // getting selected items
        selectedActivity = (RadioButton)activities.getSelectedToggle();
        selectedDogOption = (RadioButton)dogsAllowed.getSelectedToggle();
        stateSelection = statesCB.getSelectionModel().getSelectedItem();
        
        dogSelectionString = selectedDogOption.getText().toLowerCase();
        activitySelection = selectedActivity.getText();
        
        

        
        try{
            stateSelection = statesCB.getSelectionModel().getSelectedItem();
        }catch(NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Selection");
            alert.setHeaderText("A state was not selected");
            alert.setContentText("Select a state");
            alert.showAndWait();
            return;
        }
        
        try{
            difficultySelection = difficultyCB.getSelectionModel().getSelectedItem();
        }catch(NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Selection");
            alert.setHeaderText("A difficulty option not selected");
            alert.setContentText("Select from a difficulty level");
            alert.showAndWait();
            return;
        }

        System.out.println("activity >> " + activitySelection + ": state >> " + stateSelection + ": difficulty >> " + difficultySelection  + ": dogs allowed? " + dogSelectionString);
        
      
        
        
        // test if state selection was made 
        if (statesCB.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Selection");
            alert.setHeaderText("A location was not selected");
            alert.setContentText("Please select a state.");
            alert.showAndWait();
            return;
        }
        // test if difficulty selection was made
        if (difficultyCB.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Selection");
            alert.setHeaderText("A difficulty level was not selected");
            alert.setContentText("Select a difficulty level");
            alert.showAndWait();
            return;
        }

        
// loading next page
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Results.fxml"));
            view_controller.ResultsController controller = new view_controller.ResultsController(activitySelection, stateSelection,difficultySelection, dogSelectionString);                  
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);           
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }

    }
    
}
