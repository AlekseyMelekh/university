package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        URL fxmlURL = getClass().getResource("task1.fxml");
        loader.setLocation(fxmlURL);
        loader.setController(new Controller());
        Parent root = loader.load();
        Scene scene = new Scene(root);

        primaryStage.setTitle("Lab6");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
