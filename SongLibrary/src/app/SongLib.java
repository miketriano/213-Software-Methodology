/*

    CS213
    Project 1: SongLibrary
    Michael Triano & Joseph Wolak

 */

package app;

import javafx.scene.control.SplitPane;
import controller.SongListController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SongLib extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/songui.fxml"));
        SplitPane root = loader.load();

        SongListController songListController = loader.getController();
        songListController.start(primaryStage);

        primaryStage.setTitle("Song Library");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
