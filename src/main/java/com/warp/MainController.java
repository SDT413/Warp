package com.warp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.*;

public class MainController implements Initializable {
    private static final String PRINT = "print";
    private static final String LINE_BREAKER = ";";
    private static final List<String> SPECIAL_CHARACTERS = new ArrayList<>(Arrays.asList(LINE_BREAKER, "\"", "(", ")"));

    private static final List<String> VARIABLES = new ArrayList<>(Arrays.asList("int", "float", "double", "char", "string", "bool"));
    private Map<String, String> savedVariables = new HashMap<>();

    private boolean hasCommand = false;
    @FXML
    private TextArea inputArea;
    @FXML
    private TextArea outputArea;
    private List<List<String>> inputList;
    private StringBuilder resultBuilder;
    private String result;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        outputArea.setEditable(false);
        inputList = new ArrayList<>();
        resultBuilder = new StringBuilder();
        result = "";
    }
    @FXML
    protected void OnRunButtonClick(){
        inputList = new ArrayList<>();
        prepareToCompilation();
        result = resultBuilder.toString();
        outputArea.setText(result);
        resultBuilder = new StringBuilder();
        result = "";
    }


   /* private void prepareToCompilationMaks()
    {
        String[] lines = inputArea.getText().split(LINE_BREAKER);

        for(String line : lines) {
            String[] words = line.split(" ");
            for(String word : words){
                if(!word.isEmpty() && !Objects.equals(word, " ")) {
                    System.out.println("Maks: " + word);
                    //resultBuilder.append(word);
                }
            }
        }
    }
    *//*private void PrepareForCompilation() {
        String[] lines = inputArea.getText().split("\n");
        for (String s : lines) {
            String[] words = s.split(" ");
            List<String> line = new ArrayList<>(Arrays.asList(words));
            inputList.add(line);
        }
    }*//*
    private void prepareToCompilationVanya() {
        // remove \n
        // remove multiple space
        //" " + LINE_BREAKER = LINE_BREAKER

        String[] lines = ValidateUserStrings(inputArea.getText()); // Split text into lines
        for(String iter : lines)
            if(!iter.isEmpty()) // if user print void line
            {
                if(iter.charAt(0) == ' ')
                    iter = iter.replaceFirst(" ", "");
                inputList.add(Arrays.asList(iter.split(" "))); // Split lines into items
            }

        System.out.println("Vanya: " + inputList); //VANYA
    }*/

    private void prepareToCompilation() {
        String[] lines = ValidateUserStrings(inputArea.getText()); // Split text into lines
        for(String iter : lines)
            if(!iter.isEmpty()) // if user print void line
            {
                if(iter.charAt(0) == ' ')
                    iter = iter.replaceFirst(" ", "");
                inputList.add(Arrays.asList(iter.split(" ")));
            }
        
    }

    private String[] ValidateUserStrings(String s)
    {
       String tmp = s.replaceAll("[\\s\\n]+", " ").replaceAll(" (?=" + LINE_BREAKER + ")", "");
        return tmp.split(LINE_BREAKER);
    }
}