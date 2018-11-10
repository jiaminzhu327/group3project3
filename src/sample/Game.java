package sample;

import javafx.beans.Observable;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Game {

    private String answer;
    private String tmpAnswer;
    private String[] letterAndPosArray;
    private List<String> allWords;
    //private String[] words;
    private int moves;
    private int index;
    private final ReadOnlyObjectWrapper<GameStatus> gameStatus;
    private ObjectProperty<Boolean> gameState = new ReadOnlyObjectWrapper<Boolean>();

    public enum GameStatus {
        GAME_OVER {
            @Override
            public String toString() {
                return "Game over!";
            }
        },
        BAD_GUESS {
            @Override
            public String toString() { return "Bad guess..."; }
        },
        GOOD_GUESS {
            @Override
            public String toString() {
                return "Good guess!";
            }
        },
        WON {
            @Override
            public String toString() {
                return "You won!";
            }
        },
        OPEN {
            @Override
            public String toString() {
                return "Game on, let's go!";
            }
        }
    }

    public Game() {
        gameStatus = new ReadOnlyObjectWrapper<GameStatus>(this, "gameStatus", GameStatus.OPEN);
        gameStatus.addListener(new ChangeListener<GameStatus>() {
            @Override
            public void changed(ObservableValue<? extends GameStatus> observable,
                                GameStatus oldValue, GameStatus newValue) {
                if (gameStatus.get() != GameStatus.OPEN) {
                    log("in Game: in changed");
                    //currentPlayer.set(null);
                }
            }

        });
        setRandomWord();
        prepTmpAnswer();
        prepLetterAndPosArray();
        moves = 0;

        gameState.setValue(false); // initial state
        createGameStatusBinding();
    }

    private void createGameStatusBinding() {
        List<Observable> allObservableThings = new ArrayList<>();
        ObjectBinding<GameStatus> gameStatusBinding = new ObjectBinding<GameStatus>() {
            {
                super.bind(gameState);
            }
            @Override
            public GameStatus computeValue() {
                log("in computeValue");
                GameStatus check = checkForWinner(index);
                if(check != null ) {
                    return check;
                }

                if(tmpAnswer.trim().length() == 0){
                    log("new game");
                    return GameStatus.OPEN;
                }
                else if (index != -1){
                    log("good guess");
                    return GameStatus.GOOD_GUESS;
                }
                else {
                    moves++;
                    log("bad guess");
                    return GameStatus.BAD_GUESS;
                    //printHangman();
                }
            }
        };
        gameStatus.bind(gameStatusBinding);
    }

    public ReadOnlyObjectProperty<GameStatus> gameStatusProperty() {
        return gameStatus.getReadOnlyProperty();
    }
    public GameStatus getGameStatus() {
        return gameStatus.get();
    }

    private void setRandomWord() {
        try{
            String encoding = "GBK";
            File file = new File("C:\\Users\\TRans_MKA\\IdeaProjects\\HelloWorld2\\words.txt");
            if(file.isFile()&&file.exists()){
                log("File Opened");//test
                InputStreamReader reader = new InputStreamReader(new FileInputStream(file),encoding);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String lineTxt;
                allWords = new ArrayList<>();
                while((lineTxt = bufferedReader.readLine())!=null){
                    allWords.add(lineTxt);
                }
                //test
//                for (int i = 0; i<allWords.size();i++){
//                    log(allWords.get(i));
//                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        List<String> allWords = new ArrayList<String>();
//        allWords.add(new Hangman().readFile("C:\\Users\\TRans_MKA\\IdeaProjects\\HelloWorld2\\words.txt"));
//        for(int i = 0; i<allWords.size(); i++)
//        {
//            log(allWords.get(i));
//        }
//        //words = new Hangman().readFile("C:\\Users\\TRans_MKA\\IdeaProjects\\HelloWorld2\\words.txt");
        int idx = (int) (Math.random() * allWords.size());
        answer = allWords.get(idx);
        log("answer is: "+answer);//test
        //answer = "apple";//words[idx].trim(); // remove new line character
    }

    private void prepTmpAnswer() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < answer.length(); i++) {
            sb.append(" ");
        }
        tmpAnswer = sb.toString();
    }

    private void prepLetterAndPosArray() {
        letterAndPosArray = new String[answer.length()];
        for(int i = 0; i < answer.length(); i++) {
            letterAndPosArray[i] = answer.substring(i,i+1);
        }
    }

    private int getValidIndex(String input) {
        int index = -1;
        for(int i = 0; i < letterAndPosArray.length; i++) {
            if(letterAndPosArray[i].equals(input)) {
                index = i;
                letterAndPosArray[i] = "";
                break;
            }
        }
        return index;
    }

    private int update(String input) {
        int index = getValidIndex(input);
        if(index != -1) {
            StringBuilder sb = new StringBuilder(tmpAnswer);
            sb.setCharAt(index, input.charAt(0));
            tmpAnswer = sb.toString();
        }
        return index;
    }

    private static void drawHangmanFrame() {}

    public void makeMove(String letter) {
        log("\nin makeMove: " + letter);
        index = update(letter);
        // this will toggle the state of the game
        gameState.setValue(!gameState.getValue());
    }

    public void reset() {
        setRandomWord();
        prepTmpAnswer();
        prepLetterAndPosArray();
        moves = 0;
        gameState.setValue(false);
        createGameStatusBinding();

    }

    private int numOfTries() {
        //System.out.println("number of tries is: " + answer.length());
        return answer.length();
    }

    public static void log(String s) {
        System.out.println(s);
    }

    private GameStatus checkForWinner(int status) {
        log("in checkForWinner");
        if(tmpAnswer.equals(answer)) {
            log("won");
            return GameStatus.WON;
        }
        else if(moves == numOfTries()) {
            log("game over");
            return GameStatus.GAME_OVER;
        }
        else {
            return null;
        }
    }
}
