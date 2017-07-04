package com.example.android.quizapp;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.Html;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import android.support.v7.app.ActionBar;

import static com.example.android.quizapp.MainActivity.Constants.NUMBER_OF_CB_ANSWERS;
import static com.example.android.quizapp.MainActivity.Constants.NUMBER_OF_ET_ANSWERS;
import static com.example.android.quizapp.MainActivity.Constants.NUMBER_OF_QUESTIONS;
import static com.example.android.quizapp.MainActivity.Constants.SUBMIT_BUTTON_ACTIVE;
import static com.example.android.quizapp.MainActivity.Constants.SUBMIT_BUTTON_ACTIVE_FOR_LAST_QUESTION;
import static com.example.android.quizapp.MainActivity.Constants.SUBMIT_BUTTON_ALL_QUESTIONS_ANSWERED;
import static com.example.android.quizapp.MainActivity.Constants.SUBMIT_BUTTON_DISABLE;
import static com.example.android.quizapp.MainActivity.Constants.SUBMIT_BUTTON_INVISIBLE;
import static com.example.android.quizapp.R.id.button_text_id;
import static com.example.android.quizapp.R.id.chkbox_table;
import static com.example.android.quizapp.R.id.parent_of_qlayouts;
import static com.example.android.quizapp.R.id.question_textview_1;
import static com.example.android.quizapp.R.id.rbutton_table;
import static com.example.android.quizapp.R.layout.activity_main;

/**
 *      TODO: SUBMIT BUTTON MUST DISSAPEAR WHEN TEXT ENTERED AND KEYBOARD APPEARS
         THEN QUESTION TEXT VIEW MUST GO UP
 */

// TODO: q-TEXT VIEw


/**
 * todo: NON-REALISTIC
 * Pawel Zygmunciak
 * QuizApp Android Basics Udacity UE scholarship (Project (3/10))
 * Started:               12.04.2017
 * Estimated finish time: 04.05.2017
 */

public class MainActivity extends AppCompatActivity {
    public class Answers {
        //fields:
        private int[] score;
        private boolean[] isSelected;
        //constructor:
        public Answers(int[] score, boolean[] isSelected) {
            this.score = score;
            this.isSelected = isSelected;
        }
        //methods:
        //getters:
        public int[]        getScores()                     {return score;}
        public boolean[]    getSelections()                 {return isSelected;}
        public int          getScore(       int index)      {return score[index];}
        public boolean      getSelection(   int index)      {return isSelected[index];}
        public int          questionScore(){
            int x = 0;
            for(int i=0;i<getSelections().length;i++) x=x+(getSelection(i)?1:0)*getScore(i);
                                                             return x;}
        //setters:
        public void         setSelection(   int     index,
                                            boolean newSelection
        ){isSelected[index] = newSelection;}

        // sets isSelected=TRUE for Answers[index]
        // sets for other indexes'  isSelected=FALSE
        public void         toggleSelection(int    index)
        {for(int i=0;i<isSelected.length ;i++) setSelection(i,(i==index));}
    }

    public class Question {
        //fields:
        private Answers[] answers;
        //constructor:
        public Question(Answers[] answers) {
            this.answers = answers;
        }
        //methods:
        //getters:
        public Answers      getAnswers(     int index)  {return answers[index];}
        public int          questionScore(  int index)  {return (answers[index].questionScore());}
        public int          totalScore()             {
            int x = 0;
            for(int i=0;i<NUMBER_OF_QUESTIONS;i++) x=x+questionScore(i);
            return x;}
    }
    class Constants {
        public static final int NUMBER_OF_QUESTIONS = 4;
        public static final int NUMBER_OF_ET_ANSWERS = 2; //ET = Edit Text
        public static final int NUMBER_OF_CB_ANSWERS = 4; //CB = Compound Button
        public static final int SUBMIT_BUTTON_DISABLE = 0;
        public static final int SUBMIT_BUTTON_ACTIVE = 1;
        public static final int SUBMIT_BUTTON_INVISIBLE = 2;
        public static final int SUBMIT_BUTTON_ACTIVE_FOR_LAST_QUESTION = 3;
        public static final int SUBMIT_BUTTON_ALL_QUESTIONS_ANSWERED = 4;
    }
    public static final int[] SCORES_Q1 = {1,1,1,1};//4 checkboxes, 4 right answers
    public static final int[] SCORES_Q2 = {2,2};    //2 right answers editable
    public static final int[] SCORES_Q3 = {0,0,3,0};//4 radiobuttons, 1 right answer
    public static final int[] SCORES_Q4 = {2,2};    //2 right answers editable

    /**{@linkplain #initAnswers(int[], int)}
     *
     * @param SCORES_Qn
     * @param NUMBER_OF_xx_ANSWERS
     * @return
     *
     * initiate Answers class table where:
     * - score is SCORES_Qn table
     * - all of isSelected table elements are FALSE
     * Answers.length = NUMBER_OF_xx_ANSWERS
     * xx= CB|ET  //                                          //ET = Edit Text, CB = Compound Button
     * n=1..NUMBER_OF_QUESTIONS ; for CB n=1,3 ; for ET n=2,4
     */
    public Answers initAnswers(int[] SCORES_Qn,int NUMBER_OF_xx_ANSWERS ) {
        boolean[] isSelected = new boolean[NUMBER_OF_xx_ANSWERS];
        for (int i=0; i<NUMBER_OF_xx_ANSWERS; i++) {isSelected[i]=false;}
        return new Answers(SCORES_Qn,isSelected);
    }
    // initialisation of Answers tables filling in with score point values
    // all isSelected elements set to FALSE.
    public Answers answerQ1 = initAnswers(SCORES_Q1,NUMBER_OF_CB_ANSWERS);
    public Answers answerQ2 = initAnswers(SCORES_Q2,NUMBER_OF_ET_ANSWERS);
    public Answers answerQ3 = initAnswers(SCORES_Q3,NUMBER_OF_CB_ANSWERS);
    public Answers answerQ4 = initAnswers(SCORES_Q4,NUMBER_OF_ET_ANSWERS);

    public  Answers[] question = {initAnswers(SCORES_Q1,NUMBER_OF_CB_ANSWERS),
                                  initAnswers(SCORES_Q2,NUMBER_OF_ET_ANSWERS),
                                  initAnswers(SCORES_Q3,NUMBER_OF_CB_ANSWERS),
                                  initAnswers(SCORES_Q4,NUMBER_OF_ET_ANSWERS)};


    /*====================================== ViewGroupTree: ========================================
     *
     * View Class: (RelativeLayout)     (ScrollView) [1]   (LinearLayout)     (LinearLayout)
     * view_id:    "root_layout" --|---> scroll_view ----> parent_Qlayout ---|---> Qlayout_1
     *                             |                                         |---> Qlayout_2
     *                             |---> submit_button                       ...............
     *                                  (LinearLayout) [0]                   ...............
     *                                                                       |---> Qlayout_n
     *            n = NUMBER_OF_QUESTIONS                                                  ^
     *                                            THAT IS Question Layout/QLayout/Q-Layout:^
     *
     *  Q-Layout is also Qlayout_0 = W-Layout|A-Layout (Welcome/Answer)
     *  W/A-Layout is displayed in the scrollview as first one.
     *==============================================================================================
     */

    /*================================== Q-Layout Structure: =======================================
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
     *==============================================================================================
     */

    /*======================= CheckBox & RadioButton Table Layout Structure ========================
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
     *
     *   P = Portrait
     *   L = Landscape
     *
     *
     * 0. Portrait default (defined in activity_main.xml): (BEFORE CHANGE TO LANDSCAPE)
     * --------------------------------------------------------------------------------
     *
     * 0A. All columns are shrunk and all tableLayout grandchildren have layout_width="wrap_content"
     * 0B. All tableRows have the same height weight, to have equal horizontal spacing.
     * 0C. The table is vertically centered.
     * 0D. 2nd column #1 is invisible by <android:collapseColumns="1"/>
     *
     *   This helps "1x4"-portrait-mode table nice vertical alignment in the center of the screen.
     *
     !! When columns are shrunk, it is needed to getWidth of A (old width)
     !! using calculateAndSetQlayoutDims() BEFORE changing to Landscape.
     !! THIS ID DONE IN calculateAndSetQlayoutDims()
     *
     *
     *      |<aaaaaaaaaaaaaaaaa/>| Col #0 text, text = oldAwidth = col #0 width before LANDSCAPE
     *      |<bbbbb/>|             Col #1 text  ================================================
     *
     *      |<aaaaaaaaaaaaaaaaa/>|<bbbbb/>| both columns shrunk.
     *
     *
     *  1. WHEN IN LANDSCAPE MODE : Placement of columns in the table.
     *  --------------------------------------------------------------
     *
     *  #TableRow = int indexOfChild(TableLayout)
     *
     *  1A. make visible  column   #1   (second column)
     *  1B. remove        tableRow #2   (third row)     (tableLayout.getChildAt(2))
     *  1C. remove        tableRow #3   (fourth row)    (tableLayout.getChildAt(3))
     *
     ******************************************************************************
     *  All 2 (chkBoxes|rButtons) (A11&A13 | A31&A33) belonging to col#0 have to have newWidth set.
     *
     *    TableRow# |0:     |1:  |<--Column
     *    ------------------------
     *        0:    |<A11/> |A12L|          id: A11 or A31      A1 = CheckBoxTable
     *        1:    |<A13/> |A14L|          id: A13 or A33      A3 = RadioButtonTable
     *
     *  2. WHEN IN LANDSCAPE MODE : Alignment of columns in the table.
     *  --------------------------------------------------------------
     *
     *  Two shrunk columns of chkBoxes/rButtons are too close to each other,
     *  so in Landscape mode, leftCol#0 is stretched and rightCol#1 is still shrunk.
     *  Smart left column (#0) alignment is needed: get new A-width.
     *
     *  tableLayout.setColumnStretchable(0,true);   // 1st column (#0) streched
     *
     *      |<aaaaaaaaaaaaaaaaa/>| Col #0 text (A=width of col#0)
     *      |<bbbbb/>|             Col #1 text (B=width of col#1)
     *
     *      |<-------------------------------------------------------/>|
     *      |stretched-A---------------------------------------|shrunk-B|
     *      |<------------------ <Q-layout width = W/> ---------------->|
     *      |<----------------------<col_0/>-----------------/>|<col_1 >|   A= 1st Col #0
     *      |<aaaaaaaaaaaaaaaaa/>|---------------------------->|<bbbbb/>|   B= 2nd Col #1
     *      |<--------------------------------------------------------->|
     *
     *      ^this stretched or shrunk col A doesn't look nice, space between is needed:
     *
     *
     *  AFTER setting new Width to col #0 (A) texts in both columns will have equal space m:
     *
     *      |<------------------ <Q-layout width = W/> ---------------->|
     *                |<----------<col_0/>---------/>|<col_1>|
     *      |--- m -->|<aaaaaaaaaaaaaaaaa/>|--- m -->|<bbbbb/>|<-- m ---|
     *
     *      3m+A+B=W; m=(W-A-B)/3; A=A+m; A+=m;  new A width = old A width + m :)
     *
     *      |<------------------ <Q-layout width = W/> ---------------->|
     *                |<----------<col_0/>---------/>|<oldB/ >|
     *      |--- m -->|<-----<oldA/>------>|--- m -->|<bbbbb/>|<-- m ---|
     *                |<-----------<newA/>---------/>|<newB />|
     *
     *  equations for m:
     *  m = (Q.layoutWidth_L - col_0_Width_P - col_1_Width_LP)/3;
     *
     *  col_0_Width_L = col_0_Width_P + m;
     *
     *  Two widths of columns for TableLayout are counted:
     */

    /*
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
     *  TableView is a grandparent of CheckBox/RadioButton.
     *
     *  |[ 0]|[ 1]| <-- widths of those chkboxes/rbuttons will be taken as Col #0,1
     */

    /*==============================================================================================
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
    /* TODO: delete the comment below? Replace that with Welcome/Answer Layout?
    /*   A12P & A12L        <android:onClick="onCheckBoxClicked"/>
     *   A14P & A14L        <android:onClick="onCheckBoxClicked"/>
     *
     *   A32P & A32L        <android:onClick="onRadioButtonClicked"/>
     *   A34P & A34L        <android:onClick="onCheckBoxClicked_A34"/>
     *==============================================================================================
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

    /*============================ Q-Layouts Quiz Basic Assumptions ================================
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
     */

    /* List of Private Methods (alphabetically):
     *========================= List of Private Methods (alphabetically): ==========================
     *
     * @see #isDeviceLandscape()                                                @returns boolean
     * @see #printTextOnTextView(String textView_id_name, String text)
     * @see #printTextNameOnTextView(String textView_id_name, String text_name)
     * @see #calculateAndSetQlayoutDims()                                                      @returns Rect
     * @see #setMyActionBar(String linearLayoutIdName)
     * @see #setAllViewDims()
     * @see #setTableLayoutOnLandscape(String tableLayout_id_name, Rect rect)
     * @see #setTextSizeOnTextView(String textView_id_name, String dimen_name)
     * @see #setViewDimsOnLandscape(Rect rect)
     */

    /**{@linkplain #isDeviceLandscape
     * @returns TRUE if the device orientation mode is Landscape.
     }
     */
    private boolean isDeviceLandscape() {
        return (getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT);
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
     * @param textView_id      // layout xml: <android:id="@+id/textView_id_name">
     * @param text_name             // string xml: <string name="text_name">="text"</string>
     */
    private void printTextNameOnTextView(int textView_id, String text_name) {
        TextView textView = (TextView)findViewById(textView_id);
        textView.setText( getResources().getIdentifier( text_name, "string", getPackageName() ) );
    }

    /**{@linkplain #calculateAndSetQlayoutDims() calculates and sets dimensions for a all Q-layouts in scrollView}
     *
     *  A weight method for proportional children under ScrollView not possible.
     *  ScrollView must have one child, Q-layouts (of the same size) are grandchildren.
     *  After calculation equal height for each Q-layout is set. 
     *  Later, the width of Q-layout in Rect is used to set othe views values in landscape mode. 
     */
    private Rect calculateAndSetQlayoutDims() {
        //
        // 1.: Rect (0,
        // 2.:       0,
        // 3.:       Q-layout width,
        // 4.:       Q-layout height)
        //
        // Q-layout WITH THOSE DIMENSIONS (WITHOUT LATERAL PADDING) FITS EXACTLY
        // TO THE DEVICE SCREEN VERTICALLY BETWEEN ACTION BAR & STATUS BAR !!!

        // initialization. display screen: displayMetrics.widthPixels x displayMetrics.heightPixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // THEY SAY IT'S IMPORTANT
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        // Scrollview child & Q-layout's parent (needed for left/right padding dims:
        LinearLayout linearLayout = (LinearLayout) findViewById(parent_of_qlayouts); // better and simpler.

        // calculates actionBarHeight  (the top of the screen):
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true);
        int actionBarHeight = getResources().getDimensionPixelSize(typedValue.resourceId);

        // calculates statusBarHeight (the bottom of the screen):
        int statusBarHeight = getResources().getDimensionPixelSize(getResources().getIdentifier("status_bar_height", "dimen", "android"));

        int qLayoutWidth  = displayMetrics.widthPixels  - linearLayout.getPaddingLeft()- linearLayout.getPaddingRight();
        int qLayoutHeight = displayMetrics.heightPixels - actionBarHeight - statusBarHeight;

        // sets the height  for all layouts within the scrollview
        for (int i=0; i<linearLayout.getChildCount(); i++) {
            android.view.ViewGroup.LayoutParams layoutParams = linearLayout.getChildAt(i).getLayoutParams();
            layoutParams.height = qLayoutHeight;                        // gets the height
            linearLayout.getChildAt(i).setLayoutParams(layoutParams);   // sets the height
        }
        return new  Rect(0,0, qLayoutWidth, qLayoutHeight);
    }


    /**{@linkplain #setTableLayoutOnLandscape(int, Rect)}
     * the argument is Resource ID of the tableLayout and Q-layout width in rect.
     * calculates and sets new value of 1st column width.
     * unhides 2nd column of the table
     * removes 3rd and 4th row
     * That makes table dim as 1x4 --> 2x2
     */
    private void setTableLayoutOnLandscape(int tableLayoutId, Rect rect) {
         /*
         * equation for m: (see: 2. WHEN IN LANDSCAPE MODE : Alignment of columns in the table.)
         *
         *  m = (Q.layoutWidth - col_0_Width - col_1_Width)/3;
         *  [NEW]col_0_Width = [OLD]col_0_Width + m;
         *  [NEW]col_0_Width = (2*[OLD]col_0_Width - col_1_Width + Q.layoutWidth)/3
         *******************************************************************************************

        // 0. Preparing table layout for View measurement before drawing.
        //
        // viewNameBD = viewName BEFORE DRAWING !!!
        // viewNameAD = viewName AFTER  DRAWING !!!

        // 1. Calculating 1st Column Width:
        /******************** THIS GRAPH CONCERNES first Col (#0): *****************************
         *
         *  tableView --------> tableRow(0) ----> checkBox/radioButton(0)
         *               |----> tableRow(1) ----> checkBox/radioButton(0)
         *               |----> tableRow(2) ----> checkBox/radioButton(0)
         *               |        ...
         *               |----> tableRow(N) ----> checkBox/radioButton(0)
         *
         * checkBox/radioButton is a grandchild od tableView
         *
         * all grandchildren(0) belong to column(0) (Interesting is 1st Col only)
         * number of rows is the same as number of checkBoxes/radioButtons in the column.
         ***************************************************************************************
         */

        // tableLayout is not measured, but its grandchildren are.
        // without layout inflating getMeasure while OnCreate gives 0 :(
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = layoutInflater.inflate(R.layout.activity_main, null, false);
        // firstColUpperView is left upper checkbox/radiobutton/textview in the table
        TableLayout tableLayoutBD = (TableLayout)contentView.findViewById(tableLayoutId);
        // firstColUpperView is a grandchild of tableLayout:             1st row,       1st col
        View firstColUpperViewBD = (View)((TableRow)tableLayoutBD.getChildAt(0)).getChildAt(0);
        // View measurement to get width (START MEASURING CHILDREN, MEASURE THE PARENT LATER!)

        // secondColUpperView is a grandchild of tableLayout:             1st row,       2nd col
        // preparing for calculating firstColumnWidthOnLandscape OnLandscape
        firstColUpperViewBD.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        //see: [THE FORMULA]: till then firstColumnWidthOnLandscape gets value of col_0_Width_P

        // secondColUpperView is a grandchild of tableLayout:               1st row,       2nd col
        View secondColUpperView = (View)((TableRow)tableLayoutBD.getChildAt(0)).getChildAt(1);
        // View measurement to get width (START MEASURING CHILDREN, MEASURE THE PARENT LATER!)
        secondColUpperView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        //see: [THE FORMULA]: variable firstColumnWidthOnLandscape gets value of col_0_Width_P
        int firstColumnWidthOnLandscape =   (2* firstColUpperViewBD.getMeasuredWidth()
                                             -  secondColUpperView.getMeasuredWidth()
                                             +  rect.width()
                                            )/3;

        // 2. Setting 1st Column Width:

        // setting only that first-column-upper View (checkBox/radioButton/textView)
        // sets the entire column width
        TableLayout tableLayoutAD = (TableLayout)findViewById(tableLayoutId);
        View firstColUpperViewAD = (View)((TableRow)tableLayoutAD.getChildAt(0)).getChildAt(0);
        android.view.ViewGroup.LayoutParams layoutParams = firstColUpperViewAD.getLayoutParams();
        layoutParams.width = firstColumnWidthOnLandscape;       // gets the width
        firstColUpperViewAD.setLayoutParams(layoutParams);      // sets the width

        // 3. Changing table layout. (see: CheckBox & RadioButton Table Layout Structure)

        // 3.1  Unhiding 2nd Column (#1):
        tableLayoutAD.setColumnCollapsed(1,false);
        // 3.2  Removing 3rd and 4th Row (#2 & #3):
        tableLayoutAD.getChildAt(2).setVisibility(tableLayoutAD.getChildAt(2).GONE);
        tableLayoutAD.getChildAt(3).setVisibility(tableLayoutAD.getChildAt(3).GONE);
    }

    /**{@linkplain #setTextSizeOnTextView sets the textView text size to value of dimen_name.
     *
     *  THE VALUE of dimen_name IN dimens.xml MUST BE IN SCALED PIXEL !!!
     *
     * @param textView_id_name      // layout xml: <android:id="@+id/textView_id_name">
     * @param dimen_name            // dimen xml:  <dimen name="dimen_name">"#0.0sp"</dimen>
     *
     *  The values of dimen_name in "dimens.xml" are int, but can be float.
     */
    private void setTextSizeOnTextView(int textView_id, String dimen_name) {
        TextView textView = (TextView)findViewById(textView_id);

        String x = getString(getResources().getIdentifier( dimen_name, "dimen", getPackageName() ));// format "#0.0sp"
        x = x.substring(0,x.length()-2); // gets rid of last two chars ("sp")                       // format "#0.0"
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(x));                      //converted to float
    }

    /**{@linkplain #setViewDimsOnLandscape(Rect)}  , OnCreate setups parameters for Q-layouts,
     * if device orientation is in landscape mode.
     }
     * @param rect // (A11_width1, A31_width, Q-layout_width, Q-layout_height)
     *  Ax1 WIDTHS IN PORTRAIT MODE !!!
     */
    private void setViewDimsOnLandscape(Rect rect){
        // IN ORDER OF APPERANCE IN ROOT LAYOUT:
        //                                              Q-Layout Structure -------|
        //                                                         Q-Layout----> Q.V. childView:
        //                                                                     =========================
        setTableLayoutOnLandscape(chkbox_table,rect);                    //1.1. CheckBoxLayout
        //  ^ [SEE METHOD DESCRIPTION]
        setTextSizeOnTextView(question_textview_1,"xlarge");             //1.3. QuestionTextView
        //  ^ setText to xlarge
        printTextNameOnTextView(question_textview_1,"question_text1_L"); //1.3. QuestionTextView
        //  ^ longer question text
        printTextNameOnTextView(button_text_id,"button_text_blocked_L"); //1.5. SubmitButtonLayout
        //  ^ longer button text
        setTableLayoutOnLandscape(rbutton_table,rect);                   //3.1. CheckBoxLayout
        //  ^ [SEE METHOD DESCRIPTION]
    }


    /**{@linkplain #setActionBarParams(int, int, String)}
     * sets text&background color of the actionBar, adds in titleBar to AppName Q-layout name like:
     * e.g.: "Question #1","Welcome!" or "Quiz results"
     * integer 8-hex-digit numbers of colors represent an alpha-color (transparency + RGB)
     */
    private void setActionBarParams(int textColor, int backgroundColor, String rTextAfterAppName) {
        ActionBar actionBar = getSupportActionBar();
      //String hexTextColorWithAlpha = "#"+Integer.toHexString(textColorWithAlpha);
        String hexTextColor = String.format("#%06X", (0xFFFFFF & textColor));
        String htmlText =   "<font color=\'"+hexTextColor+"\'>" +
                            getResources().getString(R.string.app_name) +
                            ": " + rTextAfterAppName +
                            "</font>";
        // htmlText:
        // <font color='#000000'>Quiz App: rTEXT</font>
        actionBar.setTitle(Html.fromHtml(htmlText));
        actionBar.setBackgroundDrawable(new ColorDrawable(backgroundColor));
    }
    private int colourBetween(int color0, int color1, float propotion){
        //calculates the colour between color0 and color1 depending on propotion  and gives integer value of colour between.
        // min propotion=0 ==> colourBetween = color0;
        // max propotion=1 ==> colourBetween = color1;
        return  Color.rgb(  (int)((1-propotion)*Color.red(color0)  + propotion*Color.red(color1)),
                            (int)((1-propotion)*Color.green(color0)+ propotion*Color.green(color1)),
                            (int)((1-propotion)*Color.blue(color0) + propotion*Color.blue(color1))
        );
    }
    private String qlayoutTitle(int questionNo){
        //shows second part of Quiz App title.
        if (questionNo==0)
            return getResources().getString(R.string.Q0);
        else
            return "Question #"+questionNo;
    }
    private void setScrollViewListener(final Rect rect){
        // This Listener on scrollview height determines:
        // - color of actionBar
        // - number of question (Q-layout) for test answer process
        // - state of submitButton (active, blocked, invisible etc.)

        // scrollView as a second grandchild of rootView:
        final ScrollView scrollView = (ScrollView)((ViewGroup)((ViewGroup)findViewById(android.R.id.content)).getChildAt(0)).getChildAt(1);

        final int theHeight= rect.height(); // the same height for each Q-layout: calculated before by calculateAndSetQlayoutDims().

        final int visibleQLayoutQuantity = ((LinearLayout)scrollView.getChildAt(0)).getChildCount()- 1; // ONE LESS!
        //from all Q-layouts (W+A + n*Q ) one Welcome, one Answer/(Results), n Questions  (layouts) [n = number of questions]
        // there is always one layout invisible: At the beginning answer-layout, then at the end - welcome-layout.

        final int theDenominator = visibleQLayoutQuantity - 1; //(Scrolling Y-coordinate position have always offset=-theHeight)
        // Here the denominator is always equal to number of questions.(only because one Welcome/Answer layout is always GONE/VISIBLE)
        // This case: 6 vertical layouts in scrollview: only 5 always visible, but counted from 0 to 4.
        // 6 = getChildCount(), 5=visibleQLayoutQuantity, 4=theDenominator

        // height of "pink" bottom view (@+id/empty_view_xB) being "a background" for submit_button.
        final int emptyBottomViewHeightPx = (int)getResources().getDimension(R.dimen.bottom_view_height);

        //Setting ScrollView Listener:
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
             public void onScrollChanged() {

                int questionNo =(int)Math.floor((scrollView.getScrollY()+emptyBottomViewHeightPx)/theHeight);// #question depending on the scrollview position
                float questionFloat = (float)scrollView.getScrollY()/theHeight;             //  <0;N> "continuous question number"  N=number of questions
                float relativePos =   questionFloat/theDenominator;                         // relativePos = <0;1> (relative position)
                float sawWave = questionFloat-(int)Math.floor(questionFloat);               // = x -floor(x)
                float triangleWave = 1-Math.abs((questionFloat%2) - 1);                     // = 1-|(x mod 2)-1| :::: 0 for Odd, 1 for Even but continuously.

                setActionBarParams( 0,                                                                                  // text color = #00000000 = black
                                    colourBetween(  ResourcesCompat.getColor(getResources(),R.color.colorEven, null),   // set background color
                                                    ResourcesCompat.getColor(getResources(),R.color.colorOdd, null),    // between colorEven & colorOdd
                                                    triangleWave                                                        // proportionally to triangleWave = <0;1>
                                    )
                                    ,qlayoutTitle(questionNo)                                                           // "Quiz App: Question #N
                                    //+" :"+String.format("%.2f",sawWave)
                );
                // Submit Button Appearance Rules
                if (questionFloat<=0.6f) changeSubmitButtonState(SUBMIT_BUTTON_DISABLE); //"Scroll up" info in "Welcome" Q-layout
                else {
                    if ((sawWave<0.95f)&&(sawWave>0.025f)) changeSubmitButtonState(SUBMIT_BUTTON_INVISIBLE); //Active button appeares only when Q-layout is well visible.
                        else
                            if (questionNo==NUMBER_OF_QUESTIONS)                                             // For the last question:
                                    changeSubmitButtonState(SUBMIT_BUTTON_ACTIVE_FOR_LAST_QUESTION);         // no arrow image & text changed
                            else    changeSubmitButtonState(SUBMIT_BUTTON_ACTIVE);                           //
                }
                if (scrollView.getHeight()!=theHeight) changeSubmitButtonState(SUBMIT_BUTTON_INVISIBLE); //button dissapears when keyboard appears
                //TODO: sth else ????
            }
        });
    }
    private void changeSubmitButtonState(int buttonState){
        // buttonState =
        // 0 SUBMIT_BUTTON_DISABLE
        // 1 SUBMIT_BUTTON_ACTIVE
        // 2 SUBMIT_BUTTON_INVISIBLE
        // 3 SUBMIT_BUTTON_ACTIVE_FOR_LAST_QUESTION
        // 4 SUBMIT_BUTTON_ALL_QUESTIONS_ANSWERED
        final LinearLayout submitButton = (LinearLayout)((ViewGroup)((ViewGroup)findViewById(android.R.id.content)).getChildAt(0)).getChildAt(0);
        final TextView submitButtonText  = (TextView) submitButton.getChildAt(0);
        final ImageView submitButtonImage  = (ImageView) submitButton.getChildAt(1);
        int submitButtonPadding=getResources().getDimensionPixelSize(getResources().getIdentifier("margin","dimen", getPackageName()));
        char oChar;
        if (isDeviceLandscape()) oChar='L'; else oChar='P';
        switch (buttonState) {
            case 0: submitButton.setClickable(false);
                    submitButton.setEnabled(true);
                    submitButton.setVisibility(View.VISIBLE);
                    submitButton.setPadding(submitButtonPadding,0,0,0); //like in activity_main.xml
                    submitButtonText.setText(getResources().getIdentifier("button_text_blocked_"+oChar,"string",getPackageName()));
                    submitButtonImage.setVisibility(View.VISIBLE);
                    submitButtonImage.setImageResource(R.mipmap.ic_vertical_align_top_black_24dp); //arrow down
                break;
            case 1: submitButton.setClickable(true);
                    submitButton.setEnabled(true);
                    submitButton.setVisibility(View.VISIBLE);
                    submitButton.setPadding(submitButtonPadding,0,0,0); //like in activity_main.xml
                    submitButtonText.setText(getResources().getIdentifier("button_text_active_"+oChar,"string",getPackageName()));
                    submitButtonImage.setVisibility(View.VISIBLE);
                    submitButtonImage.setImageResource(R.mipmap.ic_vertical_align_top_black_24dp); //arrow down
                break;
            case 2: submitButton.setClickable(false);
                    submitButton.setEnabled(false);
                    submitButton.setVisibility(View.GONE);
                    submitButton.setPadding(submitButtonPadding,0,0,0); //like in activity_main.xml
                    submitButtonImage.setVisibility(View.VISIBLE);
                    submitButtonImage.setImageResource(R.mipmap.ic_vertical_align_top_black_24dp); //arrow down
                break;
            case 3: submitButton.setClickable(true);
                    submitButton.setEnabled(true);
                    submitButton.setVisibility(View.VISIBLE);
                    submitButton.setPadding(submitButtonPadding,0,submitButtonPadding,0); // no arrow image
                    submitButtonText.setText(getResources().getIdentifier("button_text_lastq_"+oChar,"string",getPackageName()));
                    submitButtonImage.setVisibility(View.GONE); // no arrow image
                break;
            case 4: submitButton.setClickable(false);
                    submitButton.setEnabled(false);
                    submitButton.setVisibility(View.VISIBLE);
                    submitButton.setPadding(submitButtonPadding,0,0,0); //like in activity_main.xml
                    submitButtonText.setText(getResources().getIdentifier("button_text_end_"+oChar,"string",getPackageName()));
                    submitButtonImage.setVisibility(View.VISIBLE);
                    submitButtonImage.setImageResource(R.mipmap.ic_vertical_align_bottom_black_24dp); //arrow up
                break;
        }
    }
    private void allQuestionsAnswered() {
        changeSubmitButtonState(SUBMIT_BUTTON_ALL_QUESTIONS_ANSWERED);
    }
    private void setOnClickListenerOnActiveSubmitButton() {
        // buttonState =
        // 0 SUBMIT_BUTTON_DISABLE                      (ACTIVE)
        // 1 SUBMIT_BUTTON_ACTIVE                       (ACTIVE)
        // 2 SUBMIT_BUTTON_INVISIBLE
        // 3 SUBMIT_BUTTON_ACTIVE_FOR_LAST_QUESTION     (ACTIVE)
        // 4 SUBMIT_BUTTON_ALL_QUESTIONS_ANSWERED
        final LinearLayout submitButton = (LinearLayout) ((ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0)).getChildAt(0);
        final TextView submitButtonText = (TextView) submitButton.getChildAt(0);

        //char oChar; if (isDeviceLandscape())  oChar='L'; else  oChar='P'; //orientation letter L or P
        // String  myString = getString(getResources().getIdentifier("button_text_active_"+oChar,"string",getPackageName()));
        // All are active which have text "SUBMIT"
        if (((String) submitButtonText.getText()).contains("SUBMIT")) { // the button is ACTIVE
            // TODO sth
        }
    }

    // sets selection state of the compound button to its counterpart (twin) in other orientation mode
    // e.g.: when the state of A12L checkbox is changed (/un/checked) in landscape mode,
    // the same state is set for "twin" checkbox A12P visible in portrait mode.
    private void twinCompoundButtonSelectionSynchro(View theCompoundButton) {
        // A2L=A2P, A4L=A4P synchronisation of compound buttons for Answer #2 & #4:
        // Two compound buttons for Answer #2 & #4 each are seen only once in the table,
        // but for changing orientation they have to have the same state
        // (there is 4 answers, but 6 compound buttons)
        // ------------------------------------------------------------------------------------
        // |1|2|     |1|     |1|2|            2: (i,j): (0,1)->(2,0)
        // |3|4| ==> |3|  or |3|4|@Landscape  4: (i,j): (1,1)->(3,0)
        // |2|       |2|                      2: (i,j): (2,0)->(0,1)
        // |4|       |4|@Portrait             4: (i,j): (3,0)->(1,1)
        //                                              (i,j)-------->( (i+6)mod 4,(j+3)mod 2 )
        // METHOD FOR NON-DUPLICATED BUTTONS DOES NOTHING, SO IT CAN BE USED FOR ALL CB'S
        // ------------------------------------------------------------------------------------
        boolean isSelected = ((CompoundButton)theCompoundButton).isChecked();
        // finds its parent and grandparent view:
        TableRow tableRow = (TableRow) theCompoundButton.getParent();
        TableLayout tableLayout = (TableLayout) tableRow.getParent();

        int j = tableRow.indexOfChild(theCompoundButton);
        int i = tableLayout.indexOfChild(tableRow);

        if((i<2) && (j==0)) return; //buttons for answers #1 & #3 don't have duplicates

        TableRow tableRowO = (TableRow) tableLayout.getChildAt((i+6)%4);
        CompoundButton theCompoundButtonO = (CompoundButton) tableRowO.getChildAt((j+3)%2);
        theCompoundButtonO.setChecked(isSelected);
    }

    private void uncheckOtherRadioButtons(View theRadioButton) {
        // finds its parent and grandparent view:
        TableRow tableRow = (TableRow) theRadioButton.getParent();
        TableLayout tableLayout = (TableLayout) tableRow.getParent();
        // Loops search through the grandchildren od the tableLayout:
        TableRow  loopTableRow;
        RadioButton loopRadioButton;
        for (int i=0; i<tableLayout.getChildCount(); i++) {
            loopTableRow = (TableRow) tableLayout.getChildAt(i);
            for (int j=0; j<loopTableRow.getChildCount(); j++) {
                 loopRadioButton = (RadioButton) loopTableRow.getChildAt(j);
                //unchecks all other radioButtons than theRadioButton.
                loopRadioButton.setChecked((loopRadioButton == theRadioButton));
            }
        }
    }

    public void onCheckboxClicked(View view) {
        // A2L=A2P, A4L=A4P synchronisation of compound buttons for Answer #2 & #4:
        // Two compound buttons for Answer #2 & #4 each are seen only once in the table,
        // but for changing orientation they have to have the same state
        // (there is 4 answers, but 6 compound buttons)
        // ------------------------------------------------------------------------------------
        // |1|2|     |1|     |1|2|
        // |3|4| ==> |3|  or |3|4|@Landscape
        // |2|       |2|
        // |4|       |4|@Portrait
        // ------------------------------------------------------------------------------------

        // Is the view now checked?
        boolean isSelected = ((CheckBox) view).isChecked();
        //Toast.makeText(
        //        getApplicationContext(),
        //        "You "+ (isSelected ? "":"un")+"selected \""+
        //                ((CompoundButton) view).getText()+
        //                "\" answer",
        //        Toast.LENGTH_SHORT
        //).show();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.A11:  answerQ1.setSelection(0,isSelected);break;
            case R.id.A12P: answerQ1.setSelection(1,isSelected);break;
            case R.id.A12L: answerQ1.setSelection(1,isSelected);break;
            case R.id.A13:  answerQ1.setSelection(2,isSelected);break;
            case R.id.A14P: answerQ1.setSelection(3,isSelected);break;
            case R.id.A14L: answerQ1.setSelection(3,isSelected);break;
            // The method onCheckboxClicked() above works if:
            // - is public
            // - returns void
            // - defines a View as its only parameter (any of compound button that was clicked)
        }
        //Synchronisation with twin button (see method description)
        twinCompoundButtonSelectionSynchro(view);
    }
    public void onRadioButtonClicked(View view) {
        // A2L=A2P, A4L=A4P synchronisation of compound buttons for Answer #2 & #4:
        // Two compound buttons for Answer #2 & #4 each are seen only once in the table,
        // but for changing orientation they have to have the same state
        // (there is 4 answers, but 6 compound buttons)
        // ------------------------------------------------------------------------------------
        // |1|2|     |1|     |1|2|
        // |3|4| ==> |3|  or |3|4|@Landscape
        // |2|       |2|
        // |4|       |4|@Portrait
        // ------------------------------------------------------------------------------------

        // Is the view now checked?
        boolean isSelected = ((RadioButton) view).isChecked();
        // Check which radiobutton was clicked
        uncheckOtherRadioButtons(view);
        switch (view.getId()) {
            case R.id.A31:  answerQ3.toggleSelection(0);break;
            case R.id.A32P: answerQ3.toggleSelection(1);break;
            case R.id.A32L: answerQ3.toggleSelection(1);break;
            case R.id.A33:  answerQ3.toggleSelection(2);break;
            case R.id.A34P: answerQ3.toggleSelection(3);break;
            case R.id.A34L: answerQ3.toggleSelection(3);break;
            // Unfortunately setup of the RadioGroup on TableLayout including RadioButtons is impossible. :(
            // RadioButtons here are grandchildren of layout.

            // The method onCheckboxClicked() above works if:
            // - is public
            // - returns void
            // - defines a View as its only parameter (any of compound button that was clicked)
        }
        //Synchronisation with twin button (see method description)
        twinCompoundButtonSelectionSynchro(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // sets main_activity layout
        setContentView(activity_main);

        setActionBarParams( ResourcesCompat.getColor(getResources(),R.color.actionBarText, null),   //sets text color, can be set to 0 as black
                            ResourcesCompat.getColor(getResources(),R.color.colorEven, null),       //sets background color
                            getResources().getString(R.string.Q0) // "Quiz App: Welcome!"
        );
        Rect rect = calculateAndSetQlayoutDims();                   // rect is width & height of every Q-layout.
        if (isDeviceLandscape()) {setViewDimsOnLandscape(rect);}    // tableLayouts width recalculated
        // Listener On scrollview to calculate its height position and and use the scroll movement as main argument for other actions as choosing Q-layout or colors...
        setScrollViewListener(rect);
        //changeSubmitButtonState(SUBMIT_BUTTON_ALL_QUESTIONS_ANSWERED);
        //allQuestionsAnswered();
        onCheckboxClicked(findViewById(R.id.A11));      // Question #1
        onRadioButtonClicked(findViewById(R.id.A31));   // Question #3
    }
}
 /*

       ViewGroup.LayoutParams layoutParams = mDescriptionScrollView.getLayoutParams();
            layoutParams.height = scrollViewHeight;

rootLayout.getHeight() < rootLayout.getRootView().getHeight() - getStatusBarHeight()

       int questionNumber=1;
                if ((questionNumber & 1) == 0)
                //  if questionNumber is even
                { actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.colorEven))); }
                //  then actionBar has colorEven
                else
                //  else actionBar has colorOdd
                { actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.colorOdd)));  }
*/
