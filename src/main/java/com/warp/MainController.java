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
        PrepareForCompilation();
        for (int i = 0; i < inputList.size(); i++) {
            for (int j = 0; j < inputList.get(i).size(); j++) {
                hasCommand = false;
                if(HasLineBreaker(i)) {
                    CheckPrint(i, j);
                    CheckVariables(i, j);
                }
            }
        }
        result = resultBuilder.toString();
        outputArea.setText(result);
        resultBuilder = new StringBuilder();
        result = "";
    }
    private void CheckPrint(int line, int word){
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
    private void CheckVariables(int line, int word){
        if(hasCommand) {
            return;
        }
        for (String variable : VARIABLES) {
            if (inputList.get(line).get(word).equals(variable)) {
                SaveVariable(line, inputList.get(line).indexOf(variable)+1);
                return;
            }
        }
    }
    private void SaveVariable(int current_line, int variable_index) {
        DeleteSpecialCharacters(inputList.get(current_line));
        if (inputList.get(current_line).get(variable_index).contains("=")) {
            String variableName = inputList.get(current_line).get(variable_index).split("=")[0];
            String variableValue = inputList.get(current_line).get(variable_index).split("=")[1];
            savedVariables.put(variableName, variableValue);
        }
        else if (inputList.get(current_line).get(variable_index+1).equals("=")) {
            String variableName = inputList.get(current_line).get(variable_index);
            StringBuilder variableValueBuilder = new StringBuilder();
            for (int i = variable_index+2; i < inputList.get(current_line).size(); i++) {
                variableValueBuilder.append(inputList.get(current_line).get(i));
                variableValueBuilder.append(" ");
            }

            savedVariables.put(variableName, variableValueBuilder.toString());
        }
    }
    private boolean HasLineBreaker(int this_line_index) {
        return inputList.get(this_line_index).get(inputList.get(this_line_index).size() - 1).contains(LINE_BREAKER);
    }
    private void DeleteSpecialCharacters(List<String> line) {
        for (int i = 0; i < line.size(); i++) {
            for (String specialCharacter : SPECIAL_CHARACTERS) {
                if (line.get(i).contains(specialCharacter)) {
                    line.set(i, line.get(i).replace(specialCharacter, ""));
                }
            }
        }
    }
    private void DeleteLineBreaker(List<String> line) {
        for (int i = 0; i < line.size(); i++) {
            if (line.get(i).contains(LINE_BREAKER)) {
                line.set(i, line.get(i).replace(LINE_BREAKER, ""));
            }
        }
    }
    private boolean IsNextIndexIsSpecialCharacter(List<String> line, int index) {
        if (index + 1 < line.size()) {
            for (String specialCharacter : SPECIAL_CHARACTERS) {
                if (line.get(index + 1).contains(specialCharacter)) {
                    return true;
                }
            }
        }
        return false;
    }
    private void PrepareForCompilation() {
        String[] lines = inputArea.getText().split("\n");
        for (String s : lines) {
            String[] words = s.split(" ");
            List<String> line = new ArrayList<>(Arrays.asList(words));
            inputList.add(line);
        }
    }
}