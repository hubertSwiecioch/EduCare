package com.hswie.educaremobile.helper;

import java.util.regex.Pattern;

/**
 * Created by hswie on 2015-12-03.
 */
public class PasswordValidator {


    private static final Pattern hasUppercase = Pattern.compile("[A-Z]");
    private static final Pattern hasLowercase = Pattern.compile("[a-z]");
    private static final Pattern hasNumber = Pattern.compile("\\d");
    private static final Pattern hasSpecialChar = Pattern.compile("[^a-zA-Z0-9 ]");


    public static PasswordValidationResult isValid(String password){

        PasswordValidationResult passwordValidationResult = new PasswordValidationResult();

        if(hasLowercase.matcher(password).find())
            passwordValidationResult.setHaveOneSmallLetter(true);

        if(hasUppercase.matcher(password).find())
            passwordValidationResult.setHaveOneCapitalLetter(true);

        if(hasNumber.matcher(password).find())
            passwordValidationResult.setHaveOneNumber(true);

        if (hasSpecialChar.matcher(password).find())
            passwordValidationResult.setHaveOneSpecialCharacter(true);

        if (password.length() >= 8)
            passwordValidationResult.setIsValidLength(true);


        return passwordValidationResult;

    }


}
