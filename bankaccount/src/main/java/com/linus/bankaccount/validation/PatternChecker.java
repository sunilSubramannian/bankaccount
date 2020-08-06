package com.linus.bankaccount.validation;

import java.util.regex.Pattern;

public class PatternChecker {

    public static final int CHECK_EMPTY  = 1; // Check the field is empty of not
    public static final int CHECK_ADS 	 = 2; // Check the field contains Alphabets, Digits and Symbols
    public static final int CHECK_DIGIT  = 3; // Check whether input contains only digits

    public static boolean validateInput(String input, int checktype){
        switch (checktype) {

            case 1:// Check if input is empty of not
                if(input.isEmpty() || input == null){
                    return false;
                }else{
                    return true;
                }

            case 2: //Check if input contains digits and allowed symbols
                if(Pattern.matches("^[0-9.-]*$", input)){
                    return true;
                }else{
                    return false;
                }

            case 3: // Check if the input contains only digit and minimum 1 input is provided
                if(Pattern.matches("^[0-9]+$", input)){
                    return true;
                }else{
                    return false;
                }

            default:
                break;
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(validateInput("123.4",CHECK_ADS));
    }


}
