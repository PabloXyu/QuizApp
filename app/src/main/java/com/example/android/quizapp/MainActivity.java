package com.example.android.quizapp;


import android.app.ActionBar;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import static com.example.android.quizapp.MainActivity.Constants.NUMBER_OF_QUESTIONS;


public class MainActivity extends AppCompatActivity {

    class Constants {
        public static final int NUMBER_OF_QUESTIONS = 4;
    }

    /** List of Private Methods (alphabetically):
     ***********************************************************************************************
     * . printOnTextView
     * . setActionBarForQLayout
     * . setAllQLayoutsHeight
     * . setLinearLayoutHeight
     *
     * ********************************************************************************************/

    /**
     * . Method
     * printOnTextView prints the "text" of a TextView
     *
     * @param textViewIdName   // =(xml-named "textViewIdName")
     * @param text             // text to display
     */
    private void printOnTextView(String textViewIdName, String text) {
        TextView orderSummaryTextView = (TextView)findViewById(getResources().getIdentifier(textViewIdName, "id", getPackageName()));
        orderSummaryTextView.setText(text);
    }


    /**
     * . Method
     * setActionBarForQLayout changes ActionBar Theme due to the question layout Id name.
     * Changes "Quiz App" title to "Quiz App / Question #n".
     * Changes ActionBar background color due to question-layout color/number (colorOdd|colorEven).
     * Q-layout uses background ColorOdd|ColorEven depending on the number of question.
     * !!! Method works fine if NUMBER_OF_QUESTIONS<10 !!!
     *
     * @param linearLayoutIdName   // = "layout_Qn" where  n=1,2,3...(NUMBER_OF_QUESTIONS)
     */
    private void setActionBarForQLayout(String linearLayoutIdName) {

        /**     ================================================================ ViewGroupTree: ====
         *      View Class: (ScrollView)    (LinearLayout)     (LinearLayout)
         *      view_id:    scroll_view --> parent_layout  ---|---> layout_Q1
         *                                                    |---> layout_Q2
         *                                                    |..............
         *                                                    |---> layout_Qn
         *      n = NUMBER_OF_QUESTIONS
         *                                           THIS IS QLayout^
         */


        char questionNumber = linearLayoutIdName.charAt(8); //    |012345678| 8 = 9th char
        //   ^ works only when n<10 (one-digit number)            |layout_Qn|
        //     getting Question number from linearLayoutIdName    |        ^| gets n =1,2...

        LinearLayout qLayout = (LinearLayout) findViewById(getResources().getIdentifier(linearLayoutIdName, "id", getPackageName()));
        ScrollView scrollView = (ScrollView) findViewById(getResources().getIdentifier("scroll_view", "id", getPackageName()));

        //scrollView.updateViewLayout(qLayout, scrollView);


       // qLayout.forceLayout();

        //int xyCoordinates[] = {0, 0};
        //qLayout.getLocationOnScreen(int[] xyCoordinates);

        int[] location = new int[2];
        qLayout.getLocationOnScreen(location);
        Log.d("UWAGA!","left: "+ location[0]+", top: "+ location[1]);



        //Log.v("Mainactivity","Name: "+ "(x: "+xyCoordinates[0]+", y: "+xyCoordinates[1]+")" );

       // printOnTextView("Question1" , "left: "+ location[0]+", top: "+ location[1]);


        //BEGIN actionBar:
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

     // actionBar.setTitle("Quiz App / Question #1"+":");                 //example      //    for question #1.
     // actionBar.setTitle("Quiz App / "+getString(R.string.Q1)+":");     // strings.xml , unfortunatelly cannot use layout string dynamically.                          //[1]                                                     //[1]
        actionBar.setTitle(Html.fromHtml("<font color='#000000'>"+"Quiz App / Question #"+ questionNumber +":</font>")); //deprecated in API 24, but still works:) //[2]
     // actionBar.setTitle("Quiz App / Question #"+ questionNumber +":");  // this works perfect, but does not changes the color of actionBarTitle to BLACK              //[3]

        // if actionBar.setTitle[2] is not O.K., use actionBar.setTitle[1] then "spannable" for actionBarTitle colour.

        // changes the color of actionBarTitle to BLACK
        //actionBar.getTitle();
        //SpannableString spannableString = new SpannableString(actionBar.getTitle());
        //spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, actionBar.getTitle().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //getSupportActionBar().setTitle(spannableString);

        //Changes ActionBar background color due to question-layout color/number (colorOdd|colorEven).
        if ((questionNumber & 1) == 0) // if [questionNumber is even] then colorEven else colorOdd (R.color)
        { actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.colorEven)));  }
        else
        { actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.colorOdd)));   }
        //END actionBar:
    };


    /**
     * . Method
     * setAllQLayoutsHeight calls setLinearLayoutHeight for all Q-layouts
     */
     private void setAllQLayoutsHeight() {
        for(int i=1; i<=NUMBER_OF_QUESTIONS; i++) {setLinearLayoutHeight("layout_Q"+i);}
     // Q-layout view Id ^is "layout_Qn" where  n=1,2,3...(NUMBER_OF_QUESTIONS)  ^
    }


    /**
     * . Method
     * setLinearLayoutHeight sets the height of a the LinearLayout to dp-height of the device screen.
     * With no ScrollView it would be not needed, all Q-Layouts would have equal heights by "match_parent".
     *
     * @param linearLayoutIdName   //  (xml-named "linearLayoutIdName")
     */
    private void setLinearLayoutHeight(String linearLayoutIdName) {

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

        // linearLayoutHeight set:
        LinearLayout linearLayout=(LinearLayout)findViewById(getResources().getIdentifier(linearLayoutIdName,"id",getPackageName()));
        android.view.ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();
        layoutParams.height = linearLayoutHeight;
        linearLayout.setLayoutParams(layoutParams);
    }

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setAllQLayoutsHeight();
// till here ok
// working on:
        setActionBarForQLayout("layout_Q1");
    }

}

/** CREATIVE BIN: :P
 *
 *
 *
 * This method checks vertical position of Q-layout in the scrollView
 *
 *
 */
/** private void SpotQuestionLayoutVertPos() {
 scrollView.getViewTreeObserver().addOnScrollChangedListener(new OnScrollChangedListener() {
@Override
public void onScrollChanged() {
int scrollY = rootScrollView.getScrollY(); // For ScrollView
int scrollX = rootScrollView.getScrollX(); // For HorizontalScrollView
// DO SOMETHING WITH THE SCROLL COORDINATES
}


});
 }
 **/

/**scrollView.getViewTreeObserver().addOnScrollChangedListener(new OnScrollChangedListener() {
@Override
public void onScrollChanged() {
        int scrollY = rootScrollView.getScrollY(); // For ScrollView
        int scrollX = rootScrollView.getScrollX(); // For HorizontalScrollView
        // DO SOMETHING WITH THE SCROLL COORDINATES
        }
        });*/