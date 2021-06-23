package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Runs an application's window.
 *
 * @author Vladislav
 * @version 2.0
 * @since 1.0
 */
public class Main extends Application {

    /**
     * Sets and shows a primary stage and a scene.
     *
     * @since 1.0
     * @param primaryStage the primary stage of the program.
     * @throws Exception an exception of scene resource file issues.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Учет договоров");
        primaryStage.setScene(new Scene(root, 726, 581));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Launches the application.
     *
     * @since 1.0
     * @param args parameters.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
