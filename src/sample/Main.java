package sample;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(final Stage primaryStage) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Myfxml.fxml"));
        loader.setController(new Controller());
        Parent root = loader.load();
        Scene scene = new Scene(root, 500,500);
        scene.getStylesheets().add(getClass().getResource("Mycss.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }

}
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
