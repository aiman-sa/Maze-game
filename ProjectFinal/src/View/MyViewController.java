package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.soap.Text;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

public class MyViewController implements IView,Initializable,Observer {
    public Pane bpane;
    private MyViewModel myViewModel;
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    public javafx.scene.control.Button btn_solveMaze;
    public BorderPane board;

    StringProperty update_player_position_row = new SimpleStringProperty();
    StringProperty update_player_position_col = new SimpleStringProperty();
    private Maze maze;
    private Solution sol;
    private MediaPlayer mediaplayer;
    private double zoomFactor = 1;

    public void musicInit(){
        String s="./Resources/music/pokemon.mp3";
        Media h=new Media(Paths.get(s).toUri().toString());
        mediaplayer=new MediaPlayer(h);
        mediaplayer.play();

    }
    public void musicVictory()
    {
        String s="./Resources/music/end.mp3";
        Media h=new Media(Paths.get(s).toUri().toString());
        mediaplayer=new MediaPlayer(h);
        mediaplayer.play();
    }
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void generateMaze() {
        this.btn_solveMaze.disableProperty().setValue(true);
        zoomFactor = 1;
        int rows = Integer.valueOf(textField_mazeRows.getText());
        int cols = Integer.valueOf(textField_mazeColumns.getText());
        myViewModel.generateMaze(rows,cols);
    }
    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);;
        alert.show();
    }
    public void solveMaze() {
        if(this.sol == null)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Please generate a maze before trying to solve it");
            alert.show();
            return;
        }
        ArrayList<AState> mazeSolutionSteps = this.sol.getSolutionPath();
        this.mazeDisplayer.setSolution(this.sol);
        this.mazeDisplayer.setDraw(true);
        this.mazeDisplayer.drawSol(this.sol);
    }

    public void SaveMaze() {
        this.myViewModel.saveMaze();
    }

    public void Exit(){
        this.myViewModel.close();
        Platform.exit();
    }
    public void About(){
        try {
            Stage stage = new Stage();
            stage.setTitle("About");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("About.fxml").openStream());
            Scene scene = new Scene(root, 400, 350);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void ToGetProperties(){
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("config.properties");
            prop.load(input);
            String solve =prop.getProperty("Searching");
            String generator=prop.getProperty("MazeGenerator");
            showAlert("The algorithm used for generating the maze is: "+generator+" which goes by Prim algorithm \n"+"The algorithm used for solving the maze is :\n"+solve);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void Help(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.setTitle("Helper");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Help.fxml").openStream());
            Scene scene = new Scene(root, 400, 350);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void openMaze() {
        this.mazeDisplayer.requestFocus();;
        this.mazeDisplayer.setDraw(false);
        myViewModel.open();
    }
    public void keyPressed(KeyEvent keyEvent) {
        myViewModel.moveCharacter(keyEvent);
        keyEvent.consume();
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
        //mazeDisplayer.autosize();
    }

    public void update(Observable o, Object arg) {
        this.mazeDisplayer.setOpacity(100);
        if(o instanceof MyViewModel) {
            if(maze == null || arg == "file loaded"){//generateMaze
                this.maze = myViewModel.getMaze();
                zoomFactor = 1;
                this.mazeDisplayer.setZoomFactor(zoomFactor);
                this.btn_solveMaze.disableProperty().setValue(true);
                this.mazeDisplayer.setDraw(false);
                drawMaze();
                try {
                    mediaplayer.stop();
                }
                catch(Exception e)
                {
                }
                musicInit();
            }
            else {
                Maze maze = myViewModel.getMaze();
                if(arg == "solution created")
                {
                    this.btn_solveMaze.disableProperty().setValue(false);
                    this.sol = myViewModel.getSolution();
                }
                else if (maze == this.maze){//Not generateMaze
                    int rowChar = mazeDisplayer.getRow_player();
                    int colChar = mazeDisplayer.getCol_player();
                    int rowFromViewModel = myViewModel.getRowChar();
                    int colFromViewModel = myViewModel.getColChar();
                    if(rowFromViewModel == rowChar && colFromViewModel == colChar){//Solve Maze
                    }
                    else {
                        this.mazeDisplayer.set_player_position(rowFromViewModel,colFromViewModel);
                        if(arg == "succeeded") {
                         Alert alert = new Alert(Alert.AlertType.INFORMATION);
                         alert.setContentText("Congratulations! you've won!");
                         alert.show();
                         mediaplayer.stop();
                         musicVictory();
                         alert.setOnCloseRequest(e->{generateMaze();});
                        }
                    }
                }
                else{//GenerateMaze
                    this.maze = maze;
                    this.btn_solveMaze.disableProperty().setValue(false);
                    this.mazeDisplayer.setDraw(false);
                    this.sol = myViewModel.getSolution();
                    zoomFactor = 1;
                    this.mazeDisplayer.setZoomFactor(zoomFactor);
                    drawMaze();
                    try {
                        mediaplayer.stop();
                    }
                    catch(Exception e)
                    {

                    }
                    musicInit();
                }
            }
        }
    }

    public void drawMaze() {
        mazeDisplayer.drawMaze(maze);
    }
    public void setMyViewModel(MyViewModel myViewModel) {
        this.myViewModel = myViewModel;
    }

    public void ZoomScreen(ScrollEvent scrollEvent) {
        if(scrollEvent.isControlDown())
        {
            if(scrollEvent.getDeltaY()<0) {
                this.zoomFactor *= 0.8;
                this.mazeDisplayer.setZoomFactor(zoomFactor);
            }
            else if(scrollEvent.getDeltaY()>0) {
                this.zoomFactor *= 1.2;
                this.mazeDisplayer.setZoomFactor(zoomFactor);
            }
            drawMaze();
        }
    }

    public void CancelUnneccessaryKeys(KeyEvent keyEvent) {
        keyEvent.consume();
    }



}


