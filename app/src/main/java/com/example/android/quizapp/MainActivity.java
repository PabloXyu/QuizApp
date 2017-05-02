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
import static android.R.attr.opacity;
import static android.R.attr.width;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.example.android.quizapp.MainActivity.Constants.NUMBER_OF_QUESTIONS;
import static com.example.android.quizapp.R.id.chkbox_table;
import static com.example.android.quizapp.R.string.Q1;

/**
 * Pawel Zygmunciak
 * QuizApp Android Basics Udacity UE scholarship (Project (3/10))
 * Started:               12.04.2017
 * Estimated finish time: 04.05.2017
 */

public class MainActivity extends AppCompatActivity {

    class Constants {
        public static final int NUMBER_OF_QUESTIONS = 4;
    }

    /** List of Private Methods (alphabetically):
     *========================= List of Private Methods (alphabetically): ==========================
     *
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
     *====================================== ViewGroupTree: ========================================
     *
     * View Class: (RelativeLayout)     (ScrollView)      (LinearLayout)     (LinearLayout)
     * view_id:    "root_layout" --|---> scroll_view ----> parent_Qlayout ---|---> Qlayout_1
     *                             |                                         |---> Qlayout_2
     *                             |---> submit_button                       ...............
     *                                  (LinearLayout)                       ...............
     *                                                                       |---> Qlayout_n
     *            n = NUMBER_OF_QUESTIONS                                                  ^
     *                                            THAT IS Question Layout/QLayout/Q-Layout:^
     *
     *==============================================================================================
     *================================== Q-Layout Structure: =======================================
     *
     *
     *  # |childView#|common name       |View Class          |Qlayout_N (xml-view name)
     * ---------------------------------------------------------------------------------------------
     *  1.|   0|    0|QuestionView      |TableLayout/EditText|chkbox_table/rbutton_table/edit_text_N
     *  2.|   1|    -|HintView          |TextView            |hint_view_N   (optional)
     *  3.|   2|    1|QuestionTextView  |TextView            |question_textview_N
     *  4.|   3|    2|EmptyView         |View                |empty_view_N
     *  5.|   -|    -|SubmitButtonLayout|LinearLayout        |submit_button (logical)
     *        ^     ^
     *        |     |
     *        |     ----with HintViews optionally existing in all Q-Layouts.                [1]
     *        ----------with HintViews existing in all Q-Layouts that optionally are GONE.  [2]
     *
     *        TODO: OPTION CHOSEN: [1] OR [2] ?
     *
     * EmptyView has fixed dimensions as background to SubmitButtonLayout.
     * QuestionView: CheckBox TableLayout, RadioButton TableLayout or EditText.
     *==============================================================================================
     *======== TableLayout (CheckBoxTable)[chkbox_table], (RadioButtonTable)[rbutton_table] ========
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
     *==============================================================================================
     */


    /**
     * . Method
     * changeParamsDueToOrientation , OnStart setups parameters for Q-layouts,
     * if device orientation is portrait or landscape mode.
     *
     */
    private void changeParamsDueToOrientation() {
        // SEE:^^^ Q-Layout Structure

    //  if the device is rotated to Landscape Mode
        if (isOrientationLandscape()) {
            printTextNameOnTextView("button_text_id","button_text_L");        //5. SubmitButtonLayout
         // ^ longer button text
            changeTableLayoutToLandscape("chkbox_table");                     //1. CheckBoxLayout
         // ^ [SEE METHOD DESCRIPTION]
            setTextSizeOnTextView("question_textview_1","xlarge");            //3. QuestionTextView
        //  ^ setText to xlarge
            printTextNameOnTextView("question_textview_1","question_text1_L");//3. QuestionTextView
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
     *==============================================================================================
     *    CheckBoxTable)[chkbox_table],                    (RadioButtonTable)[rbutton_table]
     *    --------------------------------------------------------------------------------------------
     *    TableRow# |0:  |1:  |<--Column                    TableRow# |0:  |1:  |<--Column
     *    ---------------------           1x4:    2x2:      ---------------------
     *        0:    |A11 |A12L|               P       LL        0:    |A31 |A32L|   
     *        1:    |A13 |A14L|               P  (or) LL        1:    |A33 |A34L|       
     *        2:    |A12P|                    P                 2:    |A32P|           
     *        3:    |A14P|                    P                 3:    |A34P|        
     *
     *  CheckBox A14P = Answer #4, for Question #1 in Q1-Layout Checkable in Portrait Mode
     *
     *   A12P & A12L        <android:onClick="onCheckBoxClicked_A12"/>
     *   A14P & A14L        <android:onClick="onCheckBoxClicked_A14"/>
     *                                 
     *   A32P & A32L        <android:onClick="onCheckBoxClicked_A32"/>
     *   A34P & A34L        <android:onClick="onCheckBoxClicked_A34"/>
     *==============================================================================================
     *
     *  0. Portrait default (defined in ativity_main.xml): (BEFORE CHANGE TO LANDSCAPE)
     *  -------------------------------------------------------------------------------
     *
     *  0A. All columns are shrunk and all tableLayout granchildren have layout_width="wrap_content"
     *  0B. All tableRows have the same height weight, to have equal horizontal spacing.
     *  0C. All two columns are shrunk. The table is vertically centered.
     *
     *  This helps "1x4"-portrait-mode table nice vertical alignment in the center of the screen.
     *
     *  when columns are shrunk, it is needed to getWidth of A & B
     *  by displayMetrics  in  setLayoutsDims() BEFORE changing to Landscape.
     *
     *      |<aaaaaaaaaaaaaaaaa/>| Col #0 text
     *      |<bbbb/>|              Col #1 text
     *
     *      |<aaaaaaaaaaaaaaaaa/>|<bbbb/>| both columns shrinked.
     *
     *
     *  1. Changing to Landscape: Placement of columns and rows in the table
     *  --------------------------------------------------------------------
     *
     *  #TableRow = int indexOfChild(TableLayout)
     *
     *  1A. unhide        column   #1
     *  1B. remove        tableRow #2  (tableLayout.getChildAt(2))
     *  1C. remove        tableRow #3  (tableLayout.getChildAt(3))
     *
     *
     *  2. Changing to Landscape: Alignment of columns in the table
     *  -----------------------------------------------------------
     *
     *  The table is vertically centered.
     *  Two shrunk columns of chkBoxes/rButtons like in Portrait Mode are too close to each other,
     *  Smart left column (#1) alignment is needed:
     *
     *  when columns are shrunk, it is needed to getWidth of A & B
     *  by displayMetrics  in  setLayoutsDims() BEFORE changing to Landscape.
     *
     *
     *         |<aaaaaaaaaaaaaaaaa/>| Col #0 text
     *         |<bbbb/>|              Col #1 text
     *
     *         before Portrait-->Landscape:
     *
     *         |<aaaaaaaaaaaaaaaaa/>|<bbbb/>| both columns shrinked.
     *
     *  In setLayoutsDims():
     *  A-text' & B-text width are calculated when both columns are shrunk.
     *
     *  AFTER stretching col #0:
     *
     *         |<------------------ <Q-layout width = W/> --------------->|
     *         |stretched-----------------------------------------|-shrunk|
     *         |<-------------------------------------------------------->|   A= 1st Col #0
     *         |<----------------------<col_0/>-----------------/>|<col_1>|   B= 2nd Col #1
     *         |<-------------------------------------------------------->|
     *         |<aaaaaaaaaaaaaaaaa/>|---------------------------->|<bbbb/>|   3m+A+B=W, TODO:find m
     *
     *  AFTER setting newWidth to col #0:
     *
     *         |<------------------ <Q-layout width = W/> --------------->|
     *                   |<----------<col_0/>---------/>|<col_1>|
     *         |--- m -->|<aaaaaaaaaaaaaaaaa/>|--- m -->|<bbbb/>|<-- m ---|
     *
     *
     *
     */
    private void changeTableLayoutToLandscape(String tableLayout_id_name){
        TableLayout tableLayout = (TableLayout)findViewById(getResources().getIdentifier( tableLayout_id_name, "id", getPackageName() ) );
    /**
     *  1. Placement of columns in the table:
     *  -----------------------------------------------------------
     *
     *  #TableRow = int indexOfChild(TableLayout)
     *
     *  1A. unhide        column   #1
     *  1B. remove        tableRow #2  (tableLayout.getChildAt(2))
     *  1C. remove        tableRow #3  (tableLayout.getChildAt(3))
     */
        tableLayout.setColumnCollapsed(1,false);                                //unhide column   #1
        tableLayout.getChildAt(2).setVisibility(tableLayout.getChildAt(2).GONE);//remove tableRow #2
        tableLayout.getChildAt(3).setVisibility(tableLayout.getChildAt(3).GONE);//remove tableRow #3
    /**
     *  2. changing to Landscape: Alignment of columns in the table
     *  -----------------------------------------------------------
     *
     * Two shrunk columns of chkBoxes/rButtons are too close to each other,
     * the table is vertically centered.
     * Smart left column (#1) alignment is needed: it is stretched. Right col is shrunk.
     */
     // tableLayout.setColumnStretchable(0,true);                      // 1st column (#1) streched
     /**
     * THEN, the width of col#1 is set in  setLayoutsDims():
     *
     */
    /**
     *      |<aaaaaaaaaaaaaaaaa/>| Col #0 text
     *      |<bbbb/>|              Col #1 text
     *
     *      |<-------------------------------------------------------/>|
     *      |stretched-A---------------------------------------|Bshrunk|
     *      |<------------------ <Q-layout width = W/> --------------->|   A= 1st Col #0
     *      |<----------------------<col_0/>-----------------/>|<col_1>|   B= 2nd Col #1
     *      |<aaaaaaaaaaaaaaaaa/>|---------------------------->|<bbbb/>|
     *      |<-------------------------------------------------------->|
     *
     *  AFTER setting newWidth to col #0:
     *
     *      |<------------------ <Q-layout width = W/> --------------->|
     *                |<----------<col_0/>---------/>|<col_1>|
     *      |--- m -->|<aaaaaaaaaaaaaaaaa/>|--- m -->|<bbbb/>|<-- m ---|
     *
     *      3m+A+B=W
     *
     *      |<------------------ <Q-layout width = W/> --------------->|
     *                |<----------<col_0/>---------/>|<col_1>|
     *      |--- m -->|<aaaaaaaaaaaaaaaaa/>|--- m -->|<bbbb/>|<-- m ---|
     *                |<----------- <new A --------/>|<oldB/>|
     *
     *  All 2 chkBoxes/rButtons A11&A13 belonging to col#0 have to have newWidth set.
     *
     *    TableRow# |0:     |1:  |<--Column
     *    --------------------- --
     *        0:    |<A11/> |A12L|          id: A11 or A31      A1 = CheckBoxTable
     *        1:    |<A13/> |A14L|          id: A13 or A33      A3 = RadioButtonTable
     */
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
     * @param linearLayoutIdName   // = "Qlayout_n" where  n=1,2,3...(NUMBER_OF_QUESTIONS)
     */
    private void setActionBarForQLayout(String linearLayoutIdName) {

        char questionNumber = linearLayoutIdName.charAt(8); //    |012345678| 8 = 9th char
        //   ^ works only when n<10 (one-digit number)            |Qlayout_n|
        //     getting Question number from linearLayoutIdName    |        ^| gets n =1,2...

        LinearLayout qLayout = (LinearLayout) findViewById(getResources().getIdentifier(linearLayoutIdName, "id", getPackageName()));
        ScrollView scrollView = (ScrollView) findViewById(getResources().getIdentifier("scroll_view", "id", getPackageName()));

        // TODO: 1. Gets location of Q-layout in scrollview/display window.

        //scrollView.updateViewLayout(qLayout, scrollView);


        // qLayout.forceLayout();

        //int xyCoordinates[] = {0, 0};
        //qLayout.getLocationOnScreen(int[] xyCoordinates);

        //Log.v("Mainactivity","Name: "+ "(x: "+xyCoordinates[0]+", y: "+xyCoordinates[1]+")" );

        // printOnTextView("Question1" , "left: "+ location[0]+", top: "+ location[1]);


        int[] location = new int[2];
        qLayout.getLocationOnScreen(location);
        Log.d("UWAGA!","left: "+ location[0]+", top: "+ location[1]);

        // 2. Action Bar

        // 2a. setting ActionBar title font&colour

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

     // actionBar.setTitle("Quiz App / Question #1"+":");                 //example for question #1.
     // actionBar.setTitle("Quiz App / "+getString(R.string.Q1)+":");     // strings.xml , unfortunatelly cannot use layout string dynamically.                    //[1]                                                     //[1]
        actionBar.setTitle(Html.fromHtml("<font color='#000000'>"+"Quiz App / Question #"+ questionNumber +":</font>")); //deprecated in API 24, but still works:) //[2]
     // actionBar.setTitle("Quiz App / Question #"+ questionNumber +":");  // this works perfect, but does not changes the color of actionBarTitle to BLACK        //[3]

     // TODO: Color by spannable, later or never here :), anyway, it works :)

        // 2b. setting ActionBar background colour


    //  TODO: Changes ActionBar background color due to Q-layout color/number (colorOdd|colorEven).

        if ((questionNumber & 1) == 0)
    //  if questionNumber is even
        { actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.colorEven))); }
    //  then actionBar has colorEven
        else
    //  else actionBar has colorOdd
        { actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.colorOdd)));  }

    // TODO: // 3b. setting ActionBar background colour with opacity
    };


    /**
     * . Method
     * setAllQLayoutsDims calls setLayoutsDims() for all Q-layouts
     */
     private void setAllQLayoutsDims() {
        for(int i=1; i<=NUMBER_OF_QUESTIONS; i++) {setLayoutsDims("Qlayout_"+i);}

     // Q-layout view Id is "Qlayout_n" where  n=1,2,3...(NUMBER_OF_QUESTIONS)
    }


    /**
     * . Method
     * setLayoutsDims sets the height of a the LinearLayout to dp-height of the device screen.
     * With no ScrollView it would be not needed, all Q-Layouts would have equal heights by "match_parent".
     *
     * @param linearLayoutIdName   //  (xml-named "linearLayoutIdName")
     */
    private void setLayoutsDims(String linearLayoutIdName) {

     // ALL DIMENSION VALUES IN PIXELS
        
     // 1. Calculation of the Q-layout Height (N vertical views vertically in scrollview:
     // ---------------------------------------------------------------------------------  
     // View-weight method not possible: scrollView must have one child, Q-layouts are grandchildren.

        // displayHeight & displayWidth of the screen counted:
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;

        // actionBarHeight counted (the top of the screen):
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true);
        int actionBarHeight = getResources().getDimensionPixelSize(typedValue.resourceId);

        // statusBarHeight counted (the bottom of the screen):
        int statusBarHeight = getResources().getDimensionPixelSize(getResources().getIdentifier("status_bar_height", "dimen", "android"));

        // qLayoutHeight calculated:
        int qLayoutHeight = displayHeight - actionBarHeight - statusBarHeight;

     // 2. Setting the Q-layout Height (N vertical views vertically in scrollview:
     // --------------------------------------------------------------------------
        LinearLayout linearLayout=(LinearLayout)findViewById(getResources().getIdentifier(linearLayoutIdName,"id",getPackageName()));
        android.view.ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();
        layoutParams.height = qLayoutHeight;
        linearLayout.setLayoutParams(layoutParams);

     // 2. Calculation of the Q-layout Dims for Landscape Mode:
     // -------------------------------------------------------
        if (isOrientationLandscape()) {

         // 2.1 Calculation the Width od Q-layout
            int W  = displayMetrics.widthPixels;

        };


        /** 2.2 TableLayout: Calculation of the TableLayout' Columns Width for Landscape Mode:
         *  ------------------------------------------------------------------------------
         *
         *         |<aaaaaaaaaaaaaaaaa/>|<bbbb/>| both columns shrinked.
         *
         *     In setLayoutsDims():
         *      A-text' & B-text width are calculated when both columns are shrunk.
         *
         *      AFTER stretching col #0:
         *
         *         |<------------------ <Q-layout width = W/> --------------->|
         *         |stretched-----------------------------------------|-shrunk|
         *         |<-------------------------------------------------------->|   A= 1st Col #0
         *         |<----------------------<col_0/>-----------------/>|<col_1>|   B= 2nd Col #1
         *         |<-------------------------------------------------------->|
         *         |<aaaaaaaaaaaaaaaaa/>|---------------------------->|<bbbb/>|   3m+A+B=W, TODO:find m
         *
         *  AFTER setting newWidth to col #0:
         *
         *         |<------------------ <Q-layout width = W/> --------------->|
         *                   |<----------<col_0/>---------/>|<col_1>|
         *         |--- m -->|<aaaaaaaaaaaaaaaaa/>|--- m -->|<bbbb/>|<-- m ---|
         *
         *
         *
         *
         *
         */
        //unhide column   #1

        // View view = findViewById(getResources().getIdentifier( "empty_view", "id", getPackageName() ) );
        // the width of that view is the same as all childrens' of Q-layout.

        //int newWidth  = (2*tableLayout.getChildAt(0).getWidth() - tableLayout.getChildAt(1).getWidth() - view.getWidth())/3;
        //int newWidth  = 2*  ((tableLayout.getChildAt(0).getWidth() +
        //             tableLayout.getChildAt(1).getWidth()
        //             ) +
        //           view.getWidth())/3;
        //  newWidth  = (2A-B-W)/3;

        //int width0=tableLayout.getChildAt(0).getMeasuredWidth();
        // int width1=tableLayout.getChildAt(1).getMeasuredWidth();

        //Log.d("DIMS:",newWidth+"");
        // Log.d("DIMS:",width0+"");
        //Log.d("DIMS:",width1+"");

        // LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(15,50);
        //tableLayout.setLayoutParams(layoutParams);

        //  LinearLayout.LayoutParams layoutParams

        //tableLayout.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams())));
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
        setActionBarForQLayout("Qlayout_1");

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