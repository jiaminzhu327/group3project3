package sample;

/*
GitHub Account and URL:

https://github.com/jiaminzhu327/group3project3

 */

import javafx.beans.Observable;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Game {

    //create global variables

    public String tmpAnswer;
    private String answer;
    //private String tmpAnswer;
    private String[] letterAndPosArray;
    private List<String> allWords; //contains all words from txt file
    //private String[] words;
    private int moves;
    private int index;
    private int numOfMiss;
    private int numOfCorrect;
    private StringBuilder missedLetters;
    private StringBuilder guessedWord;
    private StringBuilder hiddenAnswer;
    private final ReadOnlyObjectWrapper<GameStatus> gameStatus;
    private ObjectProperty<Boolean> gameState = new ReadOnlyObjectWrapper<Boolean>();
    public boolean isGuessed;

    public enum GameStatus {
        GAME_OVER {
            @Override
            public String toString() {
                return "Game over! Press Enter to start a new game.";
            }
        },
        BAD_GUESS {
            @Override
            public String toString() {
                return "The letter is not in the answer, Try again!"; }
        },
        GOOD_GUESS {
            @Override
            public String toString() {
                return "Yes! The letter is in the answer!";
            }
        },
        WON {
            @Override
            public String toString() {
                return "You won the game! Press Enter to start a new game.";
            }
        },
        OPEN {
            @Override
            public String toString() {
                return "Game on, enter the first letter you guess: ";
            }
        },
        ALREADY_GUESS{
            @Override
            public String toString(){
                return "The letter you typed has already guessed!";
            }
        }
    }

    public Game() {

        missedLetters = new StringBuilder();
        guessedWord = new StringBuilder();
        hiddenAnswer = new StringBuilder();
        numOfMiss = 0;
        numOfCorrect = 0;
        moves = 6;
        isGuessed = false;


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

                if(numOfCorrect == 0 && missedLetters.length()==0){
                    log("new game");
                    return GameStatus.OPEN;
                }
                else if (index != -1){
                    log("good guess");
                    return GameStatus.GOOD_GUESS;
                }
                else {
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
            File file = new File("words.txt");
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

    public StringBuilder setHiddenAnswer(){
        hiddenAnswer = new StringBuilder();

        for (int i =0; i<answer.length();i++)
        {
            hiddenAnswer.append('*');
        }
        return hiddenAnswer;
    }

    private void prepTmpAnswer() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < answer.length(); i++) {
            sb.append('*');
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

        StringBuilder sb = new StringBuilder(tmpAnswer);
        //sb.append(hiddenAnswer);
        if(index != -1) {

            sb.setCharAt(index, input.charAt(0));
            tmpAnswer = sb.toString();

        }
        return index;
    }

    private static void drawHangmanFrame() {}

    public void makeMove(String letter) {
        log("\nin makeMove: " + letter);
        index = update(letter);
        isGuessed = false;

        if (index != -1 && guessedWord.indexOf(letter) < 0) {
            char c = letter.charAt(0);
            for (int i = 0; i < answer.length(); i++) {
                if (answer.substring(i, i + 1).equals(letter)) {
                    hiddenAnswer.setCharAt(i, c);
                    tmpAnswer = hiddenAnswer.toString();
                    numOfCorrect++;
//                    sb.setCharAt(index, letter.charAt(0));
//                    tmpAnswer = sb.toString();
//                    numOfCorrect++;
                }
            }
            guessedWord.append(letter);
            log("guessed word is: " + guessedWord);

        } else if (guessedWord.indexOf(letter) >= 0) {
            isGuessed = true;

            log("NO4. guessed word is: " + guessedWord);
            log("Letter " + letter + " is in the word and you have already guessed!");
        } else if (index == -1 && missedLetters.indexOf(letter) < 0) {
            missedLetters.append(letter);
            moves--;
            System.out.println("You have " + moves + " moves left!");
            //System.out.println("Letter added: " + missedLetters);
        } else if (index == -1 && missedLetters.indexOf(letter) >= 0) {
            //test
            System.out.println("The letter " + letter + " you have already guessed.");
            isGuessed = true;
        }



//        if(index == -1 && missedLetters.indexOf(letter)<0 ){
//            missedLetters.append(letter);
//            moves--;
//            System.out.println("You have " + moves + " moves left!");
//            //System.out.println("Letter added: " + missedLetters);
//        }
//        else if(index == -1 && missedLetters.indexOf(letter)>=0){
//            //test
//            System.out.println("The letter "+ letter + " you have already guessed.");
//            isGuessed = true;
//        }
//        else if (index!=-1 && guessedWord.indexOf(letter)<0) {
//            char c = letter.charAt(0);
//            for (int i =0;i<answer.length();i++){
//                if (answer.substring(i,i+1).equals(letter)){
//                    hiddenAnswer.setCharAt(i, c);
//                    tmpAnswer = hiddenAnswer.toString();
//                    numOfCorrect++;
////                    sb.setCharAt(index, letter.charAt(0));
////                    tmpAnswer = sb.toString();
////                    numOfCorrect++;
//                }
//            }
//            guessedWord.append(letter);
//            log("guessed word is: " + guessedWord);
//
//        }else if(index!=-1 && guessedWord.indexOf(letter)>=0){
//            isGuessed = true;
//
//            log("NO4. guessed word is: " + guessedWord);
//            log("Letter "+letter+" is in the word and you have already guessed!");
//        }

        // this will toggle the state of the game
        gameState.setValue(!gameState.getValue());
        //test
        System.out.println("Number of Correct is " + numOfCorrect);


    }

    public boolean getIsGuessed(){
        return isGuessed;
    }

    public int getMoves()
    {
        return moves;
    }

    public int getNumOfCorrect(){
        return numOfCorrect;
    }

    public int getAnswerLength(){
        return hiddenAnswer.length();
    }

    private int numOfTries() {
        //System.out.println("number of tries is: " + answer.length());
        return 0;
    }

    public void reset() {

        setRandomWord();
        setHiddenAnswer();
        //hiddenAnswer = new StringBuilder();
        prepTmpAnswer();
        prepLetterAndPosArray();
        moves = 6;
        numOfCorrect = 0;
        missedLetters = new StringBuilder();
        guessedWord = new StringBuilder();
        gameState.setValue(false);
        isGuessed = false;
        createGameStatusBinding();

    }


    public static void log(String s) {
        System.out.println(s);
    }

    private GameStatus checkForWinner(int status) {
        log("in checkForWinner");
        if(numOfCorrect == tmpAnswer.length()){
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
