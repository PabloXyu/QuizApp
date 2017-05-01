package com.example.android.quizapp;


import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Configuration;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.lang.reflect.Method;

import static android.R.attr.numColumns;
import static android.R.attr.width;
import static com.example.android.quizapp.MainActivity.Constants.NUMBER_OF_QUESTIONS;
import static com.example.android.quizapp.R.id.chkbox_table;
import static com.example.android.quizapp.R.string.Q1;


public class MainActivity extends AppCompatActivity {

    class Constants {
        public static final int NUMBER_OF_QUESTIONS = 4;
    }

    /** List of Private Methods (alphabetically):
     **********************************************************************************************
     * . changeParamsDueToOrientation()
     * . changeTableLayoutToLandscape()
     * . isOrientationLandscape()
     * . printTextOnTextView()
     * . printTextNameOnTextView()
     * . setActionBarForQLayout()
     * . setAllQLayoutsDims()
     * . setLayoutsDims()
     * . setTextSizeOnTextView()
     *
     ======================================= ViewGroupTree: ========================================
     *
     * View Class: (RelativeLayout)     (ScrollView)      (LinearLayout)     (LinearLayout)
     * view_id:     root_layout ---|---> scroll_view ----> parent_layout ---|---> layout_Q1
     *                             |                                        |---> layout_Q2
     *                             |---> submit_button                      ...............
     *                                  (LinearLayout)                      ...............
     *                                                                      |---> layout_Qn
     *            n = NUMBER_OF_QUESTIONS                                                ^
     *                                          THAT IS Question Layout|QLayout|Q-Layout:^
     */


    /**
     * . Method
     * changeParamsDueToOrientation , OnStart setups parameters for Q-layouts,
     * if device orientation is portrait or landscape mode.
     *
     */
    private void changeParamsDueToOrientation() {
    /******************************* Q-Layout Logical Structure:***********************************
     *
     *  0. SubmitButtonLayout [xml: submit_button]              (not in "physical" layout in )
     *  1. CheckBoxLayout/RadioButtonLayout
     *  2. HintLayout (optional)
     *  3. QuestionTextLayout [xml: questionTextView_N]             N= 1,2 ... NUMBER_OF_QUESTIONS
     *  4. EmptyLayout (the same dim background fit with SubmitButton)
     **********************************************************************************************
     */
    //  if the device is rotated to Landscape Mode
        if (isOrientationLandscape()) {
            printTextNameOnTextView("button_text_id","button_text_L");       //0. SubmitButton
         // ^ longer button text
            changeTableLayoutToLandscape("chkbox_table");                     //1. CheckBoxLayout
         // ^ [SEE METHOD DESCRIPTION]
            setTextSizeOnTextView("question_textview_1","xlarge");            //3. QuestionTextLayout
        //  ^ setText to xlarge
            printTextNameOnTextView("question_textview_1","question_text1_L");//3. QuestionTextLayout
         // ^ longer question text
        };
    }

    /**
     * . Method
     * changeTableLayoutToLandscape changes the tableLayout shape
     * from 1x4 vertical rectangle to 2x2 square.
     *
     * Used in Q-layout for Checkbox table and RadioButton table
     *
     * @param tableLayout_id_name      // layout xml: android:id="@+id/tableLayout_id_name"
     *
     *  tableLayout xml names: chkbox_table, rButtonTable
     *  -------------------------------------------------------------------------------------------
     *
     *  CheckBox A14P = Answer #4, for Question #1 in Q1-Layout Checkable in Portrait Mode
     *
     *    TableRow# |0:  |1:  |<--Column
     *    ---------------------           1x4:    2x2:
     *        0:    |A11 |A12L|               P       LL
     *        1:    |A13 |A14L|               P  (or) LL
     *        2:    |A12P|                    P
     *        3:    |A14P|                    P
     *
     *  CheckBox A14P = Answer #4, for Question #1 in Q1-Layout Checkable in Portrait Mode
     *
     *   A12P & A12L        <android:onClick="onCheckBoxClicked_A12"/>
     *   A14P & A14L        <android:onClick="onCheckBoxClicked_A14"/>
     *
     *  0. Portrait default (xml): (BEFORE CHANGE TO LANDSCAPE)
     *  -----------------------------------------------------------------
     *
     * 0A. All columns are shrunk and all tableLayout granchildren have layout_width="wrap_content".
     * 0B. All tableRows have the same height weight, to have equal horizontal spacing.
     *
     *   This helps "1x4"-portrait-mode table nice horizontal alignment in the center of the screen.
     *
     *
     *  1. changing to Landscape: Placement of columns in the table
     *  -----------------------------------------------------------
     *                                                     #TableRow = int indexOfChild(TableLayout)
     *  1A. unhide        column   #1
     *  1B. remove        tableRow #2  (tableLayout.getChildAt(2))
     *  1C. remove        tableRow #3  (tableLayout.getChildAt(3))
     *
     *  #TableRow = int indexOfChild(TableLayout)
     *
     *
     *  2. changing to Landscape: Alignment of columns in the table
     *  -----------------------------------------------------------
     *                                 
     *   THAT IS PERFORMED IN setLayoutsDims()
     *
     * Two shrunk columns of chkBoxes/rButtons are too close to each other,
     * the table is vertically centered.
     * 
     * Smart left column (#0) alignment is needed.
     *
     *      |<----m---------|<---------A-------->|<--B-->|---------m---->|
     *      |<------------------  Q-layout width = W  ------------------>|   A= 1st Col #0
     *         d=(W-A-B)/3                                                   B= 2nd Col #1
     *      |<---d-|<---------A-------->|<--d-->|<------->|<--B-->|-d--->|   W= Q-layout width
     *
     *      |<----n------|<-----------A+d---------->|<--B-->|------n---->|
     *
     *       A += d; new width of A (first column); A = A + d;
     * 
     *  newWidth = (2A-B-W)/3
     *
     *  All 2 chkBoxes/rButtons A11&A13 belonging to col#0 have to have newWidth set.
     *
     *
     */
    private void changeTableLayoutToLandscape(String tableLayout_id_name){
        TableLayout tableLayout = (TableLayout)findViewById(getResources().getIdentifier( tableLayout_id_name, "id", getPackageName() ) );
        // 1. Placement of columns in the table:
        tableLayout.setColumnCollapsed(1,false);                                //unhide column   #1
        tableLayout.getChildAt(2).setVisibility(tableLayout.getChildAt(2).GONE);//remove tableRow #2
        tableLayout.getChildAt(3).setVisibility(tableLayout.getChildAt(3).GONE);//remove tableRow #3
        // 2. Alignment of columns in the table:

       // View view = findViewById(getResources().getIdentifier( "empty_view", "id", getPackageName() ) );
        // the width of that view is the same as all childrens' of Q-layout.
        //int newWidth  = (2*tableLayout.getChildAt(0).getWidth() - tableLayout.getChildAt(1).getWidth() - view.getWidth())/3;
        //int newWidth  = 2*  ((tableLayout.getChildAt(0).getWidth() +
               //             tableLayout.getChildAt(1).getWidth()
               //             ) +
                 //           view.getWidth())/3;
        //  newWidth  = (2A-B-W)/3;

        int width0=tableLayout.getChildAt(0).getMeasuredWidth();
        int width1=tableLayout.getChildAt(1).getMeasuredWidth();

        //Log.d("DIMS:",newWidth+"");
        Log.d("DIMS:",width0+"");
        Log.d("DIMS:",width1+"");

       // LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(15,50);
       //tableLayout.setLayoutParams(layoutParams);

      //  LinearLayout.LayoutParams layoutParams

        //tableLayout.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams())));
    }


    /**
     * .Method
     * isOrientationLandscape returns TRUE if the device
     * orientation is Landscape otherwise returns FALSE
     * when device orientation is Portrait.
     */
    private boolean isOrientationLandscape() {
        return !(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
    }


     /**
     * . Method
     * printTextOnTextView prints the "text" of a TextView
     *
     * @param textView_id_name     <android:id="@+id/textView_id_name">
     * @param text                 text to display
     */
    private void printTextOnTextView(String textView_id_name, String text) {
        TextView textView = (TextView)findViewById( getResources().getIdentifier( textView_id_name, "id", getPackageName() ) );
        textView.setText(text);
    }


    /**
     * . Method
     * printTextNameOnTextView prints the textName from string Resources
     *
     * @param textView_id_name      // layout xml: <android:id="@+id/textView_id_name">
     * @param text_name             // string xml: <string name="text_name">="text"</string>
     */
    private void printTextNameOnTextView(String textView_id_name, String text_name) {
        TextView textView = (TextView)findViewById( getResources().getIdentifier( textView_id_name, "id", getPackageName() ) );
        textView.setText( getResources().getIdentifier( text_name, "string", getPackageName() ) );
    }


    /**
     * . Method
     * setActionBarForQLayout changes ActionBar Theme due to the Question Layout Id name.
     * Changes "Quiz App" title to "Quiz App / Question #n".
     * Changes ActionBar background color due to question-layout color/number (colorOdd|colorEven).
     * Q-layout uses background ColorOdd|ColorEven depending on the number of question.
     * !!! Method works fine if NUMBER_OF_QUESTIONS<10 !!!
     *
     * @param linearLayoutIdName   // = "layout_Qn" where  n=1,2,3...(NUMBER_OF_QUESTIONS)
     */
    private void setActionBarForQLayout(String linearLayoutIdName) {

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

     // actionBar.setTitle("Quiz App / Question #1"+":");                 //example for question #1.
     // actionBar.setTitle("Quiz App / "+getString(R.string.Q1)+":");     // strings.xml , unfortunatelly cannot use layout string dynamically.                    //[1]                                                     //[1]
        actionBar.setTitle(Html.fromHtml("<font color='#000000'>"+"Quiz App / Question #"+ questionNumber +":</font>")); //deprecated in API 24, but still works:) //[2]
     // actionBar.setTitle("Quiz App / Question #"+ questionNumber +":");  // this works perfect, but does not changes the color of actionBarTitle to BLACK        //[3]

     // Color by spannable, later or never here :)

        //Changes ActionBar background color due to question-layout color/number (colorOdd|colorEven).
        if ((questionNumber & 1) == 0)
    //  if questionNumber is even
        { actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.colorEven))); }
    //  then actionBar has colorEven
        else
    //  else actionBar has colorOdd
        { actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.colorOdd)));  }
        //END actionBar:
    };


    /**
     * . Method
     * setAllQLayoutsDims calls setLayoutsDims for all Q-layouts
     */
     private void setAllQLayoutsDims() {
        for(int i=1; i<=NUMBER_OF_QUESTIONS; i++) {setLayoutsDims("layout_Q"+i);}

     // Q-layout view Id is "layout_Qn" where  n=1,2,3...(NUMBER_OF_QUESTIONS)
    }


    /**
     * . Method
     * setLayoutsDims sets the height of a the LinearLayout to dp-height of the device screen.
     * With no ScrollView it would be not needed, all Q-Layouts would have equal heights by "match_parent".
     *
     * @param linearLayoutIdName   //  (xml-named "linearLayoutIdName")
     */
    private void setLayoutsDims(String linearLayoutIdName) {

        //  ALL DIMENSION VALUES IN PIXELS

        // displayHeight & displayWidth of the screen counted:
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth  = displayMetrics.widthPixels;

        // actionBarHeight counted (the top of the screen):
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true);
        int actionBarHeight = getResources().getDimensionPixelSize(typedValue.resourceId);

        // statusBarHeight counted (the bottom of the screen):
        int statusBarHeight = getResources().getDimensionPixelSize(getResources().getIdentifier("status_bar_height", "dimen", "android"));

        // linearLayoutHeight calculated:
        int linearLayoutHeight = displayHeight - actionBarHeight - statusBarHeight;

        // linearLayoutHeight set:
        LinearLayout linearLayout=(LinearLayout)findViewById(getResources().getIdentifier(linearLayoutIdName,"id",getPackageName()));
        android.view.ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();
        layoutParams.height = linearLayoutHeight;
        linearLayout.setLayoutParams(layoutParams);
    }


    /**
     * . Method
     * setTextSizeOnTextView sets the textView text size to value of dimen_name
     *
     * The value of dimen_name in dimens.xml must be scaled pixel !!!
     *
     * @param textView_id_name      // layout xml: <android:id="@+id/textView_id_name">
     * @param dimen_name            // dimen xml: <dimen name="dimen_name">"#0.0sp"</dimen>
     *
     *  The values of dimen_name in "dimens.xml" are int, but can be float.
     */
    private void setTextSizeOnTextView(String textView_id_name, String dimen_name) {
        TextView textView = (TextView)findViewById(  getResources().getIdentifier( textView_id_name,"id",getPackageName() )  );

        String x = getString(getResources().getIdentifier( dimen_name, "dimen", getPackageName() ));// format "#0.0sp"
        x = x.substring(0,x.length()-2); // gets rid of last two chars ("sp")                       // format "#0.0"
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(x));                      //converted to float
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        setAllQLayoutsDims();

// till here ok
// working on:
        changeParamsDueToOrientation();
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