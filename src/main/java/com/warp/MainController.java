package com.warp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.*;

public class MainController implements Initializable {
    private static final String PRINT = "print";
    private static final String LINE_BREAKER = ";";
    private static final ArrayList<String> SPECIAL_CHARACTERS = new ArrayList<>(Arrays.asList(LINE_BREAKER, "\"", "(", ")"));

    private static final ArrayList<String> VARIABLES = new ArrayList<>(Arrays.asList("int", "float", "double", "char", "string", "bool"));
    private Map<String, String> savedVariables = new HashMap<>();

    private boolean hasCommand = false;
    @FXML
    private TextArea inputArea;
    @FXML
    private TextArea outputArea;
    private ArrayList<ArrayList<String>> inputList;

    private ArrayList<String> SyntaxErrors = new ArrayList<>();
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
        //printFuncMaksym();
        funcFinderVanya();
        result = resultBuilder.toString();
        if (!SyntaxErrors.isEmpty()) {
           outputArea.setText("Syntax errors: " + SyntaxErrors);
        }
        else {
            outputArea.setText(result);
        }
        resultBuilder = new StringBuilder();
        result = "";
    }

    private void prepareToCompilation() {
        String[] lines = ValidateUserStrings(inputArea.getText()); // Split text into lines
        for(String iter : lines)
            if(!iter.isEmpty()) // if user print void line
            {
                if(iter.charAt(0) == ' ')
                    iter = iter.replaceFirst(" ", "");
                ArrayList<String> temp = new ArrayList<>(Arrays.asList(iter.split(" ")));
                inputList.add(temp);
            }

    }

    private String[] ValidateUserStrings(String s)
    {
       String tmp = s.replaceAll("[\\s\\n]+", " ").replaceAll(" (?=" + LINE_BREAKER + ")", "");
        return tmp.split(LINE_BREAKER);
    }

    /*private void CheckPrint(int line, int word){
        if(hasCommand) {
            return;
        }
        if (inputList.get(line).get(word).equals(PRINT)) {
            ExecuteCurrentPrintCommand(line, inputList.get(line).indexOf(PRINT)+1);
        } else if (inputList.get(line).get(word).contains(PRINT) && IsNextIndexIsSpecialCharacter(inputList.get(line), word)) {
            inputList.get(line).set(word, inputList.get(line).get(word).replace(PRINT, ""));
            ExecuteCurrentPrintCommand(line, word);
        }
    }

     private void ExecuteCurrentPrintCommand(int current_line, int start_index) {
            DeleteSpecialCharacters(inputList.get(current_line));
            for (int i = start_index; i < inputList.get(current_line).size(); i++) {
                if(savedVariables.containsKey(inputList.get(current_line).get(i))) {
                    resultBuilder.append(savedVariables.get(inputList.get(current_line).get(i)));
                    resultBuilder.append(" ");
                    System.out.println(savedVariables.get(inputList.get(current_line).get(i)));
                }
                else {
                    resultBuilder.append(inputList.get(current_line).get(i));
                    resultBuilder.append(" ");
                }
            }
            resultBuilder.append("\n");
            hasCommand = true;
    }
    */
    private void printFuncMaksym() {
        boolean isPrint = false;
        for(List<String> line : inputList) {
            String printString = "";
            for(String word : line){
                if (word.equals(PRINT + "(\"")) {
                    isPrint = true;
                    printString += word;
                    System.out.println(printString);
                    continue;
                }else{
                    if(word.contains(PRINT)){
                        resultBuilder.append(word, PRINT.length() + 2, word.length() - 2);
                        break;
                    }
                }
                if(isPrint){
                    printString += word;
                    if(printString.endsWith("\")")){
                        isPrint = false;
                    }
                    System.out.println(printString);
                }else{
                    if(!printString.isEmpty()) {
                        resultBuilder.append(printString, PRINT.length() + 2, printString.length() - 2);
                    }
                    break;
                }
            }
        }
    }

    private void funcFinderVanya() //O(n^4) --- FIXME: not really, can be O(n^3)
    {
        for(int i = 0; i < inputList.size(); i++)
            for(int j = 0; j < inputList.get(i).size(); j++)
            {
                  if(inputList.get(i).get(j).contains(PRINT)) // FIXME: PRINT instead of "print", because there is a const PRINT = "print"
                      j = printFuncVanya(i, j);
            }
    }

    private int printFuncVanya(int i, int j) {
        StringBuilder tmp = new StringBuilder(inputList.get(i).get(j).replace(PRINT, ""));
        System.out.println("Vanya: 1");
//        while (j < inputList.get(i).size() &&
//                !inputList.get(i).get(j).contains("("))
//        { j++; } //итерирование

        while (j < inputList.get(i).size() &&
                !inputList.get(i).get(j).contains("\""))
        { j++; System.out.println("Vanya: 2");} //итерирование

        if(List.of(inputList.get(i).get(j).split("\"")).size() > 2)
        {
            System.out.println("Vanya: 2.2");
            tmp = new StringBuilder(List.of(inputList.get(i).get(j).split("\"")).get(1)); //центрая от разреза
            System.out.println("Vanya: " + tmp);
            return j;
        }
        System.out.println("Vanya: 3");

        if(!List.of(inputList.get(i).get(j).split("\"")).isEmpty()) {
            System.out.println(List.of(inputList.get(i).get(j).split("\"")));
            tmp.append(List.of(inputList.get(i).get(j).split("\"")).get(1)); //вторая половина от разреза
            j++;
        }
        else
            j++;

        while (j < inputList.get(i).size() &&
                !inputList.get(i).get(j).contains("\""))
        {
            tmp.append(inputList.get(i).get(j));
            System.out.println("Vanya: 4");
            j++;
        }
        System.out.println("Vanya: 5");
        tmp.append(List.of(inputList.get(i).get(j).split("\"")).get(0)); //вторая половина от разреза
        System.out.println("Vanya: " + tmp);
        return j;
    }

}