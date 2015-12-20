package com.hswie.educaremobile.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hswie.educaremobile.R;


/**
 * Created by hswie on 2015-12-03.
 */
public class PasswordStrengthRules extends RelativeLayout  {


    LayoutInflater mInflater;
    private RelativeLayout relativeLayout;

    public RelativeLayout getRelativeLayout() {
        return relativeLayout;
    }


    private TextView smallLetterMessage, capitalLetterMessage, oneNumberMessage, specialCharacterMessage, validLenghtMessage;

    public PasswordStrengthRules(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
        init();

    }
    public PasswordStrengthRules(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        mInflater = LayoutInflater.from(context);
        init();
    }
    public PasswordStrengthRules(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        init();
    }
    public void init()
    {
        mInflater.inflate(R.layout.password_strenght_content, this, true);
        relativeLayout = (RelativeLayout) this.findViewById(R.id.signup_password_strenght_content);

        smallLetterMessage = (TextView) this.findViewById(R.id.signup_password_strenght_content_lowerCase);
        capitalLetterMessage = (TextView) this.findViewById(R.id.signup_password_strenght_content_upperCase);
        oneNumberMessage = (TextView) this.findViewById(R.id.signup_password_strenght_content_number);
        specialCharacterMessage = (TextView) this.findViewById(R.id.signup_password_strenght_content_special);
        validLenghtMessage = (TextView) this.findViewById(R.id.signup_password_strenght_content_lenght);

        smallLetterMessage.setText(R.string.at_least_one_small_letter);

        capitalLetterMessage.setText(R.string.at_least_one_capital_letter);

        oneNumberMessage.setText(R.string.at_least_one_number);

        specialCharacterMessage.setText(R.string.at_least_one_special_character);

        validLenghtMessage.setText(R.string.be_at_least_eight_characters);

    }

    public void setColorOnTextChanged(PasswordValidationResult passwordValidationResult){

        if(passwordValidationResult.getHaveOneSmallLetter()) smallLetterMessage.setTextColor(getResources().getColor(R.color.green));
        else smallLetterMessage.setTextColor(getResources().getColor(R.color.red));

        if(passwordValidationResult.getHaveOneCapitalLetter()) capitalLetterMessage.setTextColor(getResources().getColor(R.color.green));
        else capitalLetterMessage.setTextColor(getResources().getColor(R.color.red));

        if(passwordValidationResult.getHaveOneNumber()) oneNumberMessage.setTextColor(getResources().getColor(R.color.green));
        else oneNumberMessage.setTextColor(getResources().getColor(R.color.red));

        if(passwordValidationResult.getHaveOneSpecialCharacter()) specialCharacterMessage.setTextColor(getResources().getColor(R.color.green));
        else specialCharacterMessage.setTextColor(getResources().getColor(R.color.red));

        if(passwordValidationResult.getIsValidLength()) validLenghtMessage.setTextColor(getResources().getColor(R.color.green));
        else validLenghtMessage.setTextColor(getResources().getColor(R.color.red));

    }
}

