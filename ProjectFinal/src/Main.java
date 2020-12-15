import Model.*;
import View.MyViewController;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.nio.file.Paths;
import java.util.Optional;
import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
public class Main extends Application {
    static MediaPlayer mediaPlayer;
    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("./View/MazeWindow.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Maze game");
        primaryStage.setScene(new Scene(root, 900, 700));
        primaryStage.show();

        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        MyViewController view = fxmlLoader.getController();
        view.setMyViewModel(viewModel);
        viewModel.addObserver(view);
        view.btn_solveMaze.disableProperty().setValue(true);
        view.mazeDisplayer.heightProperty().bind(view.bpane.heightProperty());
        view.mazeDisplayer.widthProperty().bind(view.bpane.widthProperty());

    }
    public static void main(String[] args) {
        launch(args);
    }


}
