package sample;

/*
GitHub Account and URL:

https://github.com/jiaminzhu327/group3project3

 */


import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Hangman extends Application {

    @Override
    public void start(final Stage primaryStage) throws IOException {
         final Game game = new Game();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Hangman.fxml"));
        loader.setController(new GameController(game));
        Parent root = loader.load();
        Scene scene = new Scene(root, 500, 800);
        scene.getStylesheets().add(getClass().getResource("Hangman.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

//    public static String readFile(String filePath){
//        try{
//            String encoding = "GBK";
//            File file = new File(filePath);
//            if(file.isFile()&&file.exists()){
//                //test
//                System.out.println("File opened!");
//                InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);
//                BufferedReader bufferedReader = new BufferedReader(read);
//                String lineTxt;
//                List<String> words = new ArrayList<String>();
//                while((lineTxt = bufferedReader.readLine())!= null);
//                {
//                    //test
//                    //System.out.println(lineTxt + "\n");
//                    words.add(lineTxt);
//                    System.out.println(words);
//                    log(words);
//                }
//
//                read.close();
//            }
//            else{
//                System.out.println("File is not found.");
//            }
//
//
//        } catch (FileNotFoundException e) {
//            System.out.println("No File Found.");
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return filePath;
//    }
    public static void log(List<String> s) {
        System.out.println(s);
    }

    public static void main(String[] args) {
//        String filePath = "C:\\Users\\TRans_MKA\\IdeaProjects\\HelloWorld2\\words.txt";
//        readFile(filePath);
        launch(args);
    }

}
