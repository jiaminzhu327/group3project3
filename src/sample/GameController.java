package sample;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class GameController {

    private final ExecutorService executorService;
    private final Game game;
    private String history = ""; /* add */

    public GameController(Game game) {
        this.game = game;
        executorService = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return thread;
            }
        });
    }

    @FXML
    private VBox board ;
    @FXML
    private Label statusLabel ;
    @FXML
    public Label currentAnswer; /* add */
    @FXML
    private Label enterALetterLabel ;
    @FXML
    private TextField textField ;
    @FXML
    private Label inputLabel; /* add */
    @FXML
    private TextField showInput; /* add */
    @FXML
    private Label inputHistory; /* add */

    public void initialize() throws IOException {
        System.out.println("in initialize");
        drawHangman(0);
        addTextBoxListener();
        setUpStatusLabelBindings();
    }

    private void addTextBoxListener() {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if(newValue.length() > 0) {
                    System.out.print(newValue);
                    game.makeMove(newValue);
                    currentAnswer.textProperty().bind(Bindings.format("%s", "  Answer: " + game.tmpAnswer));
                    textField.clear();
                    showInput.setText(newValue); /* add */
                    history = history + newValue; /* add */
                    inputHistory.textProperty().bind(Bindings.format("%s", "The letter you have guessed: " + history)); /* add */
                }
            }
        });
    }

    private void setUpStatusLabelBindings() {

        System.out.println("in setUpStatusLabelBindings");
        statusLabel.textProperty().bind(Bindings.format("%s", game.gameStatusProperty()));
        currentAnswer.textProperty().bind(Bindings.format("%s", "  Answer: " + game.setHiddenAnswer()));
        enterALetterLabel.textProperty().bind(Bindings.format("%s", "Enter a letter:"));
        inputLabel.textProperty().bind(Bindings.format("%s", "  Your Input:")); /* add */
        inputHistory.textProperty().bind(Bindings.format("%s", "The letter you have guessed: ")); /* add */
		/*	Bindings.when(
					game.currentPlayerProperty().isNotNull()
			).then(
				Bindings.format("To play: %s", game.currentPlayerProperty())
			).otherwise(
				""
			)
		);
		*/
    }

    private void drawHangman(int n) {

        Line rope=new Line(250,100,250,150);
        Line stick1=new Line(175,100,250,100);
        Line stick2=new Line(175,100,175,350);
        Line stick3=new Line(150,350,300,350);


        Circle c = new Circle(250,150,10);
        //c.setRadius(10);
        Line body=new Line();
        body.setStartX(250);
        body.setStartY(150);
        body.setEndX(250);
        body.setEndY(230);

        Line leftHand=new Line();
        leftHand.setStartX(200);
        leftHand.setStartY(210);
        leftHand.setEndX(250);
        leftHand.setEndY(180);

        Line rightHand=new Line();
        rightHand.setStartX(300);
        rightHand.setStartY(210);
        rightHand.setEndX(250);
        rightHand.setEndY(180);

        Line leftLeg=new Line();
        leftLeg.setStartX(200);
        leftLeg.setStartY(310);
        leftLeg.setEndX(250);
        leftLeg.setEndY(230);

        Line rightLeg=new Line();
        rightLeg.setStartX(300);
        rightLeg.setStartY(310);
        rightLeg.setEndX(250);
        rightLeg.setEndY(230);


        if(n==1)
        {
            board.getChildren().add(body);
        }
        if(n==2)
        {
            board.getChildren().add(leftHand);
        }
        if(n==3)
        {
            board.getChildren().add(rightHand);
        }
        if(n==4)
        {
            board.getChildren().add(leftLeg);
        }
        if(n==5)
        {

            board.getChildren().add(rightLeg);
        }
        if(n==0)
        {
            board.getChildren().clear();
            board.getChildren().addAll(rope,stick1,stick2,stick3);
            board.getChildren().add(c);
        }
    }



    @FXML
    private void newHangman() {
        game.reset();
        currentAnswer.textProperty().bind(Bindings.format("%s", "  Answer: " + game.setHiddenAnswer())); /* add */
        showInput.setText(""); /* add */
        history = ""; /* add */
        inputHistory.textProperty().bind(Bindings.format("%s", "The letter you have guessed: ")); /* add */

    }

    @FXML
    private void quit() {
        board.getScene().getWindow().hide();
    }

}












//
//import java.io.IOException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ThreadFactory;
//import javafx.beans.binding.Bindings;
//import javafx.beans.value.ChangeListener;
//import javafx.beans.value.ObservableValue;
//import javafx.fxml.FXML;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.VBox;
//import javafx.scene.shape.Circle;
//import javafx.scene.shape.Line;
//
//public class GameController {
//
//    private final ExecutorService executorService;
//    private final Game game;
//
//    public GameController(Game game) {
//        this.game = game;
//        executorService = Executors.newSingleThreadExecutor(new ThreadFactory() {
//            @Override
//            public Thread newThread(Runnable r) {
//                Thread thread = new Thread(r);
//                thread.setDaemon(true);
//                return thread;
//            }
//        });
//    }
//
//    @FXML
//    private VBox board ;
//    @FXML
//    private Label statusLabel ;
//    @FXML
//    private Label enterALetterLabel ;
//    @FXML
//    private TextField textField ;
//
//    public void initialize() throws IOException {
//        System.out.println("in initialize");
//        drawHangman();
//        addTextBoxListener();
//        setUpStatusLabelBindings();
//    }
//
//    private void addTextBoxListener() {
//        textField.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
//                if(newValue.length() > 0) {
//                    System.out.print(newValue);
//                    game.makeMove(newValue);
//                    textField.clear();
//                }
//            }
//        });
//    }
//
//    private void setUpStatusLabelBindings() {
//
//        System.out.println("in setUpStatusLabelBindings");
//        statusLabel.textProperty().bind(Bindings.format("%s", game.gameStatusProperty()));
//        enterALetterLabel.textProperty().bind(Bindings.format("%s", "Enter a letter:"));
//		/*	Bindings.when(
//					game.currentPlayerProperty().isNotNull()
//			).then(
//				Bindings.format("To play: %s", game.currentPlayerProperty())
//			).otherwise(
//				""
//			)
//		);
//		*/
//    }
//
//    private void drawHangman() {
//
//        Line line = new Line();
//        line.setStartX(25.0f);
//        line.setStartY(0.0f);
//        line.setEndX(25.0f);
//        line.setEndY(25.0f);
//
//        Circle c = new Circle();
//        c.setRadius(10);
//
//        board.getChildren().add(line);
//        board.getChildren().add(c);
//
//    }
//
//    @FXML
//    private void newHangman() {
//        game.reset();
//    }
//
//    @FXML
//    private void quit() {
//        board.getScene().getWindow().hide();
//    }
//
//}