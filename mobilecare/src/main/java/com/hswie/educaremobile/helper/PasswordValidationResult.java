package com.hswie.educaremobile.helper;

/**
 * Created by hswie on 2015-12-03.
 */
public class PasswordValidationResult {

    private Boolean haveOneSmallLetter;
    private Boolean haveOneCapitalLetter;
    private Boolean haveOneNumber;
    private Boolean haveOneSpecialCharacter;
    private Boolean isValidLength;


    public PasswordValidationResult(){

        this.haveOneSmallLetter = false;
        this.haveOneCapitalLetter = false;
        this.haveOneNumber = false;
        this.haveOneSpecialCharacter = false;
        this.isValidLength = false;
    }


    public Boolean getHaveOneSmallLetter() {
        return haveOneSmallLetter;
    }

    public void setHaveOneSmallLetter(Boolean haveOneSmallLetter) {
        this.haveOneSmallLetter = haveOneSmallLetter;
    }

    public Boolean getHaveOneCapitalLetter() {
        return haveOneCapitalLetter;
    }

    public void setHaveOneCapitalLetter(Boolean haveOneCapitalLetter) {
        this.haveOneCapitalLetter = haveOneCapitalLetter;
    }

    public Boolean getHaveOneNumber() {
        return haveOneNumber;
    }

    public void setHaveOneNumber(Boolean haveOneNumber) {
        this.haveOneNumber = haveOneNumber;
    }

    public Boolean getHaveOneSpecialCharacter() {
        return haveOneSpecialCharacter;
    }

    public void setHaveOneSpecialCharacter(Boolean haveOneSpecialCharacter) {
        this.haveOneSpecialCharacter = haveOneSpecialCharacter;
    }

    public Boolean getIsValidLength() {
        return isValidLength;
    }

    public void setIsValidLength(Boolean isValidLength) {
        this.isValidLength = isValidLength;
    }

    public Boolean isPasswordValid(){

        return haveOneSmallLetter && haveOneCapitalLetter && haveOneNumber && haveOneSpecialCharacter && isValidLength;

    }

    public int getValidCount(){

        int validCount = 0;

        if (getHaveOneSmallLetter())
            validCount++;
        if (getHaveOneCapitalLetter())
            validCount++;
        if (getHaveOneNumber())
            validCount++;
        if (getHaveOneSpecialCharacter())
            validCount++;
        if (getIsValidLength())
            validCount++;

        return validCount;
    }



}
