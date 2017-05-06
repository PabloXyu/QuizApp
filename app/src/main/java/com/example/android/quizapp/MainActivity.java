package com.example.android.quizapp;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.Html;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.lang.reflect.Method;

import static android.R.attr.numColumns;
import static android.R.attr.opacity;
import static android.R.attr.paddingLeft;
import static android.R.attr.value;
import static android.R.attr.width;
import static android.R.attr.x;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.example.android.quizapp.MainActivity.Constants.NUMBER_OF_QUESTIONS;
import static com.example.android.quizapp.R.id.chkbox_table;
import static com.example.android.quizapp.R.id.rbutton_table;
import static com.example.android.quizapp.R.layout.activity_main;
import static com.example.android.quizapp.R.string.Q1;
import static java.security.AccessController.getContext;

/**
 *      TODO: SUBMIT BUTTON MUST DISSAPEAR WHEN TEXT ENTERED AND KEYBOARD APPEARS
         THEN QUESTION TEXT VIEW MUST GO UP
 */

// TODO: q-TEXT VIEw


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
    * @see #calculateNewQLayoutDims()                                       @returns Rect
    * @see #calculateTableLayoutFirstColWidth()                             @returns Rect
    * @see #isLandscape()                                                   @returns boolean
    * @see #printTextOnTextView(String textView_id_name, String text)
    * @see #printTextNameOnTextView(String textView_id_name, String text_name)
    * @see #setActionBarForQLayout(String linearLayoutIdName)
    * @see #setAllViewDims()
    * @see #setTableLayoutToLandscape(String tableLayout_id_name, Rect rect)
    * @see #setTextSizeOnTextView(String textView_id_name, String dimen_name)
    * @see #setViewDimsOnLandscape(Rect rect)
    */

    /**{@linkplain #calculateNewQLayoutDims() calculates new width and height for a Q-layout.
     }
     *
     * Part 1.:
     * 
     *  A weight method for propotional children under ScrollView not possble.
     *  ScrollView must have one child, Q-layouts are grandchildren.
     *
     *  Part 2.:
     *
     *  Two widths of columns for TableLayout are counted:
     *
     *
     *=========================================== TableLayout: =====================================
     * TableLayout: grandchildrenView indexes: |                  |TableLayout: n = question number.
     *==============================================================================================
     * TableRow viewIndex |0:  |1:  |<--Column |                  | TableRow# |0:  |1:  |<--Column
     *-------------------------------          | 1x4:    2x2:     | ---------------------
     *        0:          |[ 0]|[ 1]|          |     P       LL   |     0:    |An1 |An2L|
     *        1:          |  0 |  1 |          |     P  (or) LL   |     1:    |An3 |An4L|
     *        2:          |  0 |----           |     P            |     2:    |An2P|----
     *        3:          |  0 |----           |     P            |     3:    |An4P|----
     *----------------------------------------------------------------------------------------------
     *  RadioButton A32L = Answer #2, for Question #3 in Q1-Layout Checkable in Landscape Mode
     * TableView is a grandparent of CheckBox/RadioButton.
     *
     * |[ 0]|[ 1]| <-- widths of those chkboxes/rbuttons will be taken as Col #0,1
     *
     *
     */
    private Rect calculateNewQLayoutDims() {
        //1. Calculate new Height and Width for Q-layout

        // initialization. display screen: displayMetrics.widthPixels x displayMetrics.heightPixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // THEY SAY IT'S IMPORTANT
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        // Scrollview child & Q-layout's parent (needed for left/right padding dims:
        LinearLayout linearLayout = (LinearLayout) findViewById(getResources().getIdentifier("parent_of_qlayouts", "id", getPackageName()));
        
        // actionBarHeight counted (the top of the screen):
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true);
        int actionBarHeight = getResources().getDimensionPixelSize(typedValue.resourceId);

        // statusBarHeight counted (the bottom of the screen):
        int statusBarHeight = getResources().getDimensionPixelSize(getResources().getIdentifier("status_bar_height", "dimen", "android"));

        Rect rect = new Rect(
                0,
                0,
                displayMetrics.widthPixels-linearLayout.getPaddingLeft()-linearLayout.getPaddingRight(),
                displayMetrics.heightPixels-actionBarHeight-statusBarHeight);

         return rect;
   }

    /**{@linkplain #calculateTableLayoutFirstColWidth
     * gets width IN PIXELS of given TableLayout.
     * Cannot be run just OnCreate, otherwise all dims=0;
     }
     *==============================================================================================
     *   (CheckBoxTable)[chkbox_table],  |                |(RadioButtonTable)[rbutton_table]
     *----------------------------------------------------------------------------------------------
     *    TableRow# |0:  |1:  |<--Column |                | TableRow# |0:  |1:  |<--Column
     *    ---------------------          |1x4:    2x2:    | ---------------------
     *        0:    |A11 |A12L|          |    P       LL  |     0:    |A31 |A32L|
     *        1:    |A13 |A14L|          |    P  (or) LL  |     1:    |A33 |A34L|
     *        2:    |A12P|               |    P           |     2:    |A32P|
     *        3:    |A14P|               |    P           |     3:    |A34P|
     *----------------------------------------------------------------------------------------------
     *  CheckBox A14P = Answer #4, for Question #1 in Q1-Layout Checkable in Portrait Mode
     *
     *   A12P & A12L        <android:onClick="onCheckBoxClicked_A12"/>
     *   A14P & A14L        <android:onClick="onCheckBoxClicked_A14"/>
     *
     *   A32P & A32L        <android:onClick="onCheckBoxClicked_A32"/>
     *   A34P & A34L        <android:onClick="onCheckBoxClicked_A34"/>
     *==============================================================================================
     *
     * 0. Portrait default (defined in ativity_main.xml): (BEFORE CHANGE TO LANDSCAPE)
     * -------------------------------------------------------------------------------
     *
     *  0A. All columns are shrunk and all tableLayout grandchildren have layout_width="wrap_content"
     *  0B. All tableRows have the same height weight, to have equal horizontal spacing.
     *  0C. The table is vertically centered.
     *  0D. 2nd column #1 is invisible by <android:collapseColumns="1"/>
     *  This helps "1x4"-portrait-mode table nice vertical alignment in the center of the screen.
     *
    !! When columns are shrunk, it is needed to getWidth of A (old width)
    !! by displayMetrics  in  calculateNewQLayoutDims() BEFORE changing to Landscape.
    !! THIS ID DONE IN calculateNewQLayoutDims()
     *
     *
     *      |<aaaaaaaaaaaaaaaaa/>| Col #0 text, text = oldAwidth = col #0 width before LANDSCAPE
     *      |<bbbb/>|              Col #1 text  ================================================
     *
     *      |<aaaaaaaaaaaaaaaaa/>|<bbbb/>| both columns shrunk.
     *
     *
     *  1. WHEN IN LANDSCAPE MODE : Placement of columns in the table.
     *  --------------------------------------------------------------
     *
     *  #TableRow = int indexOfChild(TableLayout)
     *
     *  1A. make visible  column   #1
     *  1B. remove        tableRow #2  (tableLayout.getChildAt(2))
     *  1C. remove        tableRow #3  (tableLayout.getChildAt(3))
     *
     *
     *  2. WHEN IN LANDSCAPE MODE : Alignment of columns in the table.
     *  --------------------------------------------------------------
     *
     *  Two shrunk columns of chkBoxes/rButtons are too close to each other,
     *  so in Landscape mode, leftCol#0 is stretched and rightCol#1 is still shrunk.
     *  Smart left column (#0) alignment is needed: get newAwidth.
     *
     *  tableLayout.setColumnStretchable(0,true);   // 1st column (#0) streched
     *
     *      |<aaaaaaaaaaaaaaaaa/>| Col #0 text
     *      |<bbbb/>|              Col #1 text
     *
     *      |<-------------------------------------------------------/>|
     *      |stretched-A---------------------------------------|Bshrunk|
     *      |<------------------ <Q-layout width = W/> --------------->|
     *      |<----------------------<col_0/>-----------------/>|<col_1>|   A= 1st Col #0
     *      |<aaaaaaaaaaaaaaaaa/>|---------------------------->|<bbbb/>|   B= 2nd Col #1
     *      |<-------------------------------------------------------->|
     *
     *  AFTER setting newWidth to col #0:
     *
     *      |<------------------ <Q-layout width = W/> --------------->|
     *                |<----------<col_0/>---------/>|<col_1>|
     *      |--- m -->|<aaaaaaaaaaaaaaaaa/>|--- m -->|<bbbb/>|<-- m ---|
     *
     *      3m+A+B=W; m=(W-A-B)/3; A=A+m; A+=m;  newAwidth=oldAwidth+m       :)
     *
     *      |<------------------ <Q-layout width = W/> --------------->|
     *                |<----------<col_0/>---------/>|<oldB/>|
     *      |--- m -->|<-----<oldA/>------>|--- m -->|<bbbb/>|<-- m ---|
     *                |<-----------<newA/>---------/>|<newB/>|
     *
     *
     *  All 2 (chkBoxes|rButtons) (A11&A13 | A31&A33) belonging to col#0 have to have newWidth set.
     *
     *    TableRow# |0:     |1:  |<--Column
     *    --------------------- --
     *        0:    |<A11/> |A12L|          id: A11 or A31      A1 = CheckBoxTable
     *        1:    |<A13/> |A14L|          id: A13 or A33      A3 = RadioButtonTable
     *
     *
     *=========================================== TableLayout: =====================================
     * TableLayout: grandchildrenView indexes: |                  |TableLayout: n = question number.
     *==============================================================================================
     * TableRow viewIndex |0:  |1:  |<--Column |                  | TableRow# |0:  |1:  |<--Column
     *-------------------------------          | 1x4:    2x2:     | ---------------------
     *        0:          |[ 0]|[ 1]|          |     P       LL   |     0:    |An1 |An2L|
     *        1:          |  0 |  1 |          |     P  (or) LL   |     1:    |An3 |An4L|
     *        2:          |  0 |----           |     P            |     2:    |An2P|----
     *        3:          |  0 |----           |     P            |     3:    |An4P|----
     *----------------------------------------------------------------------------------------------
     *  RadioButton A32L = Answer #2, for Question #3 in Q1-Layout Checkable in Landscape Mode
     * TableView is a grandparent of CheckBox/RadioButton.
     *
     * |[ 0]|[ 1]| <-- widths of those chkboxes/rbuttons will be taken as Col #0,1
     *
     */



    /**
     Finally width&height are possible to count as :

      The offset is not used , always here stated is(0,0), so it is possible to use it
     for getting widht values of first column #0 (shrunk on Portrait Mode) like:

     Rect rect = new Rect(col#0-chkbox_table,col#0-rbutton_table, Q-Layout-width, Q-Layout-height)

     @see #setTableLayoutToLandscape(String tableLayout_id_name, Rect rect) description

     So let's do it:
     */
    private Rect calculateTableLayoutFirstColWidth() {

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //View view = layoutInflater.inflate(R.layout.activity_main, null, false);
       // View view = layoutInflater.inflate(R.layout.activity_main, activity_main, false);

        //LayoutInflater layoutInflater = LayoutInflater.from(activity_main.this); // 1
        //View theInflatedView = layoutInflater.inflate(R.layout.your_layout, null); // 2 and 3


        //TableLayout checkBoxtable    = (TableLayout)findViewById( getResources().getIdentifier(chkbox_table+"","id",  getPackageName()));
       // TableLayout radioButtonTable = (TableLayout)findViewById( getResources().getIdentifier(rbutton_table+"","id", getPackageName()));
        //  First column before OnResume is shrunk and wraps content - the longest text

    //    container.post(new Runnable(){
    //        public void run(){
    //            int height = container.getHeight();
    //        }
     //   });



        //view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        //TableLayout checkBoxtable = (TableLayout) view;

        //RelativeLayout item = (RelativeLayout) view.findViewById(R.id.item);


        //int width = view.getMeasuredWidth();

        //int height = view.getMeasuredHeight();

        //printTextOnTextView("question_textview_1","width: " +width+",height:" +height );
//Log.d("XXX","width: " +width+",height:" +height );


        Rect rect = new Rect(
                0,
                0,
                0,
                0);
        return rect;
    }




    /**{@linkplain #isLandscape
     * @returns TRUE if the device orientation mode is Landscape.
     }
     */
    private boolean isLandscape() {
        return !(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
    }


     /**{@linkplain #printTextOnTextView prints the "text" of a TextView
     }
     * @param textView_id_name     <android:id="@+id/textView_id_name">
     * @param text                 text to display
     */
    private void printTextOnTextView(String textView_id_name, String text) {
        TextView textView = (TextView)findViewById( getResources().getIdentifier( textView_id_name, "id", getPackageName() ) );
        textView.setText(text);
    }


    /**{@linkplain #printTextNameOnTextView prints the textName from string Resources
     }
     * @param textView_id_name      // layout xml: <android:id="@+id/textView_id_name">
     * @param text_name             // string xml: <string name="text_name">="text"</string>
     */
    private void printTextNameOnTextView(String textView_id_name, String text_name) {
        TextView textView = (TextView)findViewById( getResources().getIdentifier( textView_id_name, "id", getPackageName() ) );
        textView.setText( getResources().getIdentifier( text_name, "string", getPackageName() ) );
    }


    /**{@linkplain #setActionBarForQLayout
     * sets ActionBar Theme text due to the Question Layout Id name like "Quiz App / Question #n".
     * sets background ColorOdd|ColorEven depending on the number of question.
     * TODO: !!! WORKS FINE ONLY WHEN  NUMBER_OF_QUESTIONS<10 !!!
     * TODO: Changes ActionBar background color due to question-layout color/number (colorOdd|colorEven).
     }
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


        //int[] location = new int[2];
        //qLayout.getLocationOnScreen(location);
        //Log.d("UWAGA!","left: "+ location[0]+", top: "+ location[1]);

        // 2. Action Bar

        // 2a. setting ActionBar title font&colour

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

     // actionBar.setTitle("Quiz App / Question #1"+":");                 //example for question #1.
     // actionBar.setTitle("Quiz App / "+getString(R.string.Q1)+":");     // strings.xml , unfortunatelly cannot use layout string dynamically.                    //[1]                                                     //[1]
        actionBar.setTitle(Html.fromHtml("<font color='#000000'>"+"Quiz App / Question #"+ questionNumber +":</font>")); //deprecated in API 24, but still works:) //[2]
     // actionBar.setTitle("Quiz App / Question #"+ questionNumber +":");  // this works perfect, but does not changes the color of actionBarTitle to BLACK        //[3]

     // TODO: Action Bar Color by spannable methods , later or never here :), anyway, it works :)

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



    /**{@linkplain #setAllViewDims is called Onstart and sets new height values  for all Q-layouts.
     * If the device is landscape-rotated it sets new params for Q-layouts and their children views
     * like buttton dims, textView heights and sets the widths of TableLayouts including
     * CheckboxViews or RadioButtonViews.
     }
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
     *============================ Q-Layouts Quiz Basic Aassumptions================================
     *
     *Q#|CI|Q-L.outId|Answ#|RightAnsw.|PossibleA|W|M  |QuestionView|Q-L grandChild#:|AV|HT|QT|<L-Chng
     *==|==|=========|=====|=======  =|=========|=|===|============|================|==|==|==|
     * 1| 0|Qlayout_1|    4|         4|        4|1|0:4|TableLayout |CheckBox    6[4]| Y| N| Y|
     * 2| 1|Qlayout_2|    1|         2|      any|3|0|3|EditText    |   -          1 | ?| -| ?|
     * 3| 2|Qlayout_3|    4|         1|        1|3|0|3|TableLayout |RadioButton 6[4]| Y| -| N|
     * 4| 3|Qlayout_4|    1|         2|      any|2|0|2|EditText    |   -          1 | ?| -| ?|
     * ^
     * QuestION #
     * ViewIndexId
     * Q-LayoutName
     * Answer Options
     * Right Answer #
     * Possible Input #
     * Weight Score for Answer
     * Possible Score
     *
     * AnswerView-------AV
     * HintView---------HT
     * QuestionTextView-QT
     *
     *================================== Q-Layout Structure: =======================================
     *
     *  # |childView#|common name       |View Class          |Qlayout_N (xml-view name)
     * ---------------------------------------------------------------------------------------------
     *  1.|   0|    0|AnswerView        |TableLayout/EditText|chkbox_table/rbutton_table/edit_text_N
     *  2.|   1|    -|HintText          |TextView            |hint_view_N                 (optional)
     *  3.|   2|    1|QuestionText      |TextView            |question_textview_N
     *  4.|   3|    2|EmptyView         |View                |empty_view_N
     *  5.|   -|    -|SubmitButtonLayout|LinearLayout        |submit_button         (logically only)
     *        ^     ^
     *        |     |
     *        |     ----with HintViews optionally existing in all Q-Layouts.                [1]
     *        ----------with HintViews existing in all Q-Layouts that optionally are GONE.  [2]
     *                                                                      OPTION CHOSEN:  [2]
     *
     * EmptyView has fixed dimensions as background to SubmitButtonLayout.
     * QuestionView: CheckBox TableLayout, RadioButton TableLayout or EditText.
     */
    private void setAllViewDims() {
        
    //  1. Q-layout heights setup.    
    //  --------------------------      
    // Weight method for propotional children under ScrollView not possble.
    // ScrollView must have one child, Q-layouts are grandchildren.

    //  1a. gets width&height of Q-layout.
        Rect rect = calculateNewQLayoutDims();
     // 1b. Sets height, loop for all Q-layouts:
        for (int i=1; i<=NUMBER_OF_QUESTIONS; i++) {
            LinearLayout linearLayout = (LinearLayout) findViewById(getResources().getIdentifier("Qlayout_"+i, "id", getPackageName()));
            android.view.ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();
            layoutParams.height = rect.height();        // gets the height
            linearLayout.setLayoutParams(layoutParams); // sets the height
        };

    //  2. Landscape Mode: sets new dimensions for all Q-layouts, specially TableLayouts.
    //  ---------------------------------------------------------------------------------
        if (isLandscape()) {setViewDimsOnLandscape(rect);};
    };


    /**{@linkplain #setTableLayoutToLandscape
     * changes the tableLayout shape from 1x4 vertical rectangle to 2x2 square.
     *
     * Used in Q-layout for Checkbox table and RadioButton table
     }
     * @param tableLayout_id_name  // layout xml: chkbox_table or rbutton_table
     * @param rect                 // rect: (chkbox:A11-width,rbutton:A31-width,display-w,display-h)
     *
     *==============================================================================================
     *   (CheckBoxTable)[chkbox_table],  |                |(RadioButtonTable)[rbutton_table]
     *----------------------------------------------------------------------------------------------
     *    TableRow# |0:  |1:  |<--Column |                | TableRow# |0:  |1:  |<--Column
     *    ---------------------          |1x4:    2x2:    | ---------------------
     *        0:    |A11 |A12L|          |    P       LL  |     0:    |A31 |A32L|
     *        1:    |A13 |A14L|          |    P  (or) LL  |     1:    |A33 |A34L|
     *        2:    |A12P|               |    P           |     2:    |A32P|
     *        3:    |A14P|               |    P           |     3:    |A34P|
     *----------------------------------------------------------------------------------------------
     *  CheckBox A14P = Answer #4, for Question #1 in Q1-Layout Checkable in Portrait Mode
     *
     *   A12P & A12L        <android:onClick="onCheckBoxClicked_A12"/>
     *   A14P & A14L        <android:onClick="onCheckBoxClicked_A14"/>
     *
     *   A32P & A32L        <android:onClick="onCheckBoxClicked_A32"/>
     *   A34P & A34L        <android:onClick="onCheckBoxClicked_A34"/>
     *==============================================================================================
     *
     * 0. Portrait default (defined in ativity_main.xml): (BEFORE CHANGE TO LANDSCAPE)
     * -------------------------------------------------------------------------------
     *
     *  0A. All columns are shrunk and all tableLayout grandchildren have layout_width="wrap_content"
     *  0B. All tableRows have the same height weight, to have equal horizontal spacing.
     *  0C. The table is vertically centered.
     *  0D. 2nd column #1 is invisible by <android:collapseColumns="1"/>
     *  This helps "1x4"-portrait-mode table nice vertical alignment in the center of the screen.
     *
     !! When columns are shrunk, it is needed to getWidth of A (old width)
     !! by displayMetrics  in  calculateNewQLayoutDims() BEFORE changing to Landscape.
     !! THIS ID DONE IN calculateNewQLayoutDims()
     *
     *
     *      |<aaaaaaaaaaaaaaaaa/>| Col #0 text, text = oldAwidth = col #0 width before LANDSCAPE
     *      |<bbbb/>|              Col #1 text  ================================================
     *
     *      |<aaaaaaaaaaaaaaaaa/>|<bbbb/>| both columns shrunk.
     *
     *
     *  1. WHEN IN LANDSCAPE MODE : Placement of columns in the table.
     *  --------------------------------------------------------------
     *
     *  #TableRow = int indexOfChild(TableLayout)
     *
     *  1A. make visible  column   #1
     *  1B. remove        tableRow #2  (tableLayout.getChildAt(2))
     *  1C. remove        tableRow #3  (tableLayout.getChildAt(3))
     *
     *
     *  2. WHEN IN LANDSCAPE MODE : Alignment of columns in the table.
     *  --------------------------------------------------------------
     *
     *  Two shrunk columns of chkBoxes/rButtons are too close to each other,
     *  so in Landscape mode, leftCol#0 is stretched and rightCol#1 is still shrunk.
     *  Smart left column (#0) alignment is needed: get newAwidth.
     *
     *  tableLayout.setColumnStretchable(0,true);   // 1st column (#0) streched
     *
     *      |<aaaaaaaaaaaaaaaaa/>| Col #0 text
     *      |<bbbb/>|              Col #1 text
     *
     *      |<-------------------------------------------------------/>|
     *      |stretched-A---------------------------------------|Bshrunk|
     *      |<------------------ <Q-layout width = W/> --------------->|
     *      |<----------------------<col_0/>-----------------/>|<col_1>|   A= 1st Col #0
     *      |<aaaaaaaaaaaaaaaaa/>|---------------------------->|<bbbb/>|   B= 2nd Col #1
     *      |<-------------------------------------------------------->|
     *
     *  AFTER setting newWidth to col #0:
     *
     *      |<------------------ <Q-layout width = W/> --------------->|
     *                |<----------<col_0/>---------/>|<col_1>|
     *      |--- m -->|<aaaaaaaaaaaaaaaaa/>|--- m -->|<bbbb/>|<-- m ---|
     *
     *      3m+A+B=W; m=(W-A-B)/3; A=A+m; A+=m;  newAwidth=oldAwidth+m       :)
     *
     *      |<------------------ <Q-layout width = W/> --------------->|
     *                |<----------<col_0/>---------/>|<oldB/>|
     *      |--- m -->|<-----<oldA/>------>|--- m -->|<bbbb/>|<-- m ---|
     *                |<-----------<newA/>---------/>|<newB/>|
     *
     *
     *  All 2 (chkBoxes|rButtons) (A11&A13 | A31&A33) belonging to col#0 have to have newWidth set.
     *
     *    TableRow# |0:     |1:  |<--Column
     *    --------------------- --
     *        0:    |<A11/> |A12L|          id: A11 or A31      A1 = CheckBoxTable
     *        1:    |<A13/> |A14L|          id: A13 or A33      A3 = RadioButtonTable
     */
    private void setTableLayoutToLandscape(String tableLayout_id_name, Rect rect){
        TableLayout tableLayout = (TableLayout)findViewById(getResources().getIdentifier( tableLayout_id_name, "id", getPackageName() ) );

        tableLayout.setColumnCollapsed(1,false);                                //unhide column   #1
        tableLayout.getChildAt(2).setVisibility(tableLayout.getChildAt(2).GONE);//rm 3rd tableRow #2
        tableLayout.getChildAt(3).setVisibility(tableLayout.getChildAt(3).GONE);//rm 4th tableRow #3
        //TODO: Remember to strech COLUMN #0 !!!!
        // calculateNewQLayoutDims() calculates TableLayout Col Widths before...
        // TODO:Changing col widths here.
   }


    /**{@linkplain #setTextSizeOnTextView sets the textView text size to value of dimen_name.
     *
     *  THE VALUE of dimen_name IN dimens.xml MUST BE IN SCALED PIXEL !!!
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


    /**{@linkplain #setViewDimsOnLandscape , OnCreate setups parameters for Q-layouts,
     * if device orientation is in landscape mode.
     }
     * @param rect // (A11_width1, A31_width, Q-layout_width, Q-layout_height)
     *  Ax1 WIDTHS IN PORTRAIT MODE !!!
     *
     * This method is a part of {@linkplain #setAllViewDims}.
     */
    private void setViewDimsOnLandscape(Rect rect){
    //                                 see^          Q-Layout Structure -------|
    //                                                         Q-Layout----> Q.V. childView:
    //                                                                     =========================
        setTableLayoutToLandscape("chkbox_table",rect);                    //1.1. CheckBoxLayout
    //  ^ [SEE METHOD DESCRIPTION]
        setTextSizeOnTextView("question_textview_1","xlarge");             //1.3. QuestionTextView
    //  ^ setText to xlarge
        printTextNameOnTextView("question_textview_1","question_text1_L"); //1.3. QuestionTextView
    //  ^ longer question text
        printTextNameOnTextView("button_text_id","button_text_L");         //1.5. SubmitButtonLayout
    //  ^ longer button text
        setTableLayoutToLandscape("rbutton_table",rect);                   //3.1. CheckBoxLayout
        //  ^ [SEE METHOD DESCRIPTION]
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);
        calculateTableLayoutFirstColWidth();
        setAllViewDims();
// working on:
        setActionBarForQLayout("Qlayout_1");

    }


}
/** CREATIVE BIN: :P
 *
 protected void onStart(Bundle savedInstanceState) {
 super.onCreate(savedInstanceState);
 setContentView(activity_main);

 calculateTableLayoutFirstColWidth();
 }
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
