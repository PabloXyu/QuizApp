package com.example.android.quizapp;


import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.example.android.quizapp.MainActivity.Constants.NUMBER_OF_QUESTIONS;
import static com.example.android.quizapp.MainActivity.Constants.SECONDS_WELCOME;

public class MainActivity extends AppCompatActivity {

    class Constants {
        public static final int NUMBER_OF_QUESTIONS = 4;
        public static final int SECONDS_WELCOME = 2;
    }


    // prints the "text" of a TextView (xml-named "textViewIdName")
    private void printOnTextView(String textViewIdName, String text) {
        TextView orderSummaryTextView = (TextView)findViewById(getResources().getIdentifier(textViewIdName, "id", getPackageName()));
        orderSummaryTextView.setText(text);
    }

    /**
     * This method sets the height of a LinearLayout (xml-named "linearLayoutIdName")
     * to dp-height of the device screen.
     * with no ScrollView it would be not needed, all Question layouts would have equal heights by "match_parent".
     */
    private void SetLinearLayoutHeight(String linearLayoutIdName) {

        // displayHeight counted:
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayHeight = displayMetrics.heightPixels;

        // actionBarHeight counted:
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true);
        int actionBarHeight = getResources().getDimensionPixelSize(typedValue.resourceId);

        // statusBarHeight counted:
        int statusBarHeight = getResources().getDimensionPixelSize(getResources().getIdentifier("status_bar_height", "dimen", "android"));

        // linearLayoutHeight counted:
        int linearLayoutHeight = displayHeight - actionBarHeight - statusBarHeight;

        //printOnTextView("header_Q1",""+linearLayoutHeight+", "+displayHeight+", "+actionBarHeight+", "+statusBarHeight);
        // ^ above: to see instead of "Question #1" title the heights of: Question Layout, displayWindow, actionBar, statusBar.

        // linearLayoutHeight set:
        LinearLayout linearLayout=(LinearLayout)findViewById(getResources().getIdentifier(linearLayoutIdName,"id",getPackageName()));
        android.view.ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();
        layoutParams.height = linearLayoutHeight;
        linearLayout.setLayoutParams(layoutParams);
    }

    /**
     * This method changes ActionBar Theme due to the question number.
     * Changes "Quiz App" title to "Question #i".
     * Changes ActionBar background color due to question-layout color (colorOdd|colorEven) due to question number.
     */
    private void SetActionBarForQuiz() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getColor(R.color.colorOdd))); // getColor : API level 23 (minimum is 15)
     // actionBar.setTitle("Question #1"+":"); //for question #1.
        actionBar.setTitle(getString(R.string.Q1)+":");
        // defining actionBar as textView and setting its color to BLACK:
        actionBar.getTitle();
        SpannableString spannableString = new SpannableString(actionBar.getTitle());
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, actionBar.getTitle().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(spannableString);

    }


    //????
    private void SetActionBarForWelcome() {

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        for (int i=SECONDS_WELCOME; i>=0; i--) {
            actionBar.setTitle("Quiz App:    the quiz will start in "+ i +" sec.");
            SystemClock.sleep(1000);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  SetLinearLayoutHeight for : "layout_Q1", "layout_Q2" ... "layout_Q4"
        for(int i=1; i<=NUMBER_OF_QUESTIONS; i++) {SetLinearLayoutHeight("layout_Q"+i);}
        SetActionBarForQuiz();
    }

//????
   // @Override
   // public void onStart(){
   //     super.onStart();
   //     SetActionBarForWelcome();
   // }

}
