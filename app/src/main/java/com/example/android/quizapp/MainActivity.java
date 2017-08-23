package com.example.android.quizapp;

import android.app.Service;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.support.v7.app.ActionBar;
import android.widget.Toast;

import static com.example.android.quizapp.MainActivity.Constants.NUMBER_OF_QUESTIONS;
import static com.example.android.quizapp.MainActivity.Constants.SUBMIT_BUTTON_ACTIVE;
import static com.example.android.quizapp.MainActivity.Constants.SUBMIT_BUTTON_ACTIVE_FOR_LAST_QUESTION;
import static com.example.android.quizapp.MainActivity.Constants.SUBMIT_BUTTON_ALL_QUESTIONS_ANSWERED;
import static com.example.android.quizapp.MainActivity.Constants.SUBMIT_BUTTON_DISABLED;
import static com.example.android.quizapp.MainActivity.Constants.SUBMIT_BUTTON_EXIT;
import static com.example.android.quizapp.MainActivity.Constants.SUBMIT_BUTTON_INVISIBLE;
import static com.example.android.quizapp.R.id.chkbox_table;
import static com.example.android.quizapp.R.id.edit_text_2;
import static com.example.android.quizapp.R.id.edit_text_4;
import static com.example.android.quizapp.R.id.empty_bottomview_1;
import static com.example.android.quizapp.R.id.empty_topview_1;
import static com.example.android.quizapp.R.id.parent_of_qlayouts;
import static com.example.android.quizapp.R.id.question_textview_1;
import static com.example.android.quizapp.R.id.rbutton_table;
import static com.example.android.quizapp.R.id.scroll_view;
import static com.example.android.quizapp.R.id.submit_button;
import static com.example.android.quizapp.R.layout.activity_main;
import static com.example.android.quizapp.R.string.button_text_active_P;
import static com.example.android.quizapp.R.string.button_text_disabled_P;
import static com.example.android.quizapp.R.string.button_text_end_P;
import static com.example.android.quizapp.R.string.button_text_exit_P;
import static com.example.android.quizapp.R.string.button_text_lastq_P;
import static java.lang.Integer.parseInt;

/**
 * Pawel Zygmunciak
 * QuizApp Android Basics Udacity UE scholarship (Project (3/10))
 */

public class MainActivity extends AppCompatActivity {
/*********************** CONSTANTS AND GLOBAL $variables: *****************************************/
    public class Constants {
        public static final int NUMBER_OF_QUESTIONS = 4;
        public static final int SUBMIT_BUTTON_DISABLED = 0;
        public static final int SUBMIT_BUTTON_ACTIVE = 1;
        public static final int SUBMIT_BUTTON_INVISIBLE = 2;
        public static final int SUBMIT_BUTTON_ACTIVE_FOR_LAST_QUESTION = 3;
        public static final int SUBMIT_BUTTON_ALL_QUESTIONS_ANSWERED = 4;
        public static final int SUBMIT_BUTTON_EXIT = 5;

}
    public  static final boolean[] FALSEx4 =   {false,false,false,false};   // Q1&Q3
    public  static final boolean[] FALSEx1 =   {false};                     // Q2&Q4
    public  static final boolean[] TRUEx1 =    {true};                      // Q2&Q4

    public  static final  int[] SCORES_Q1 = {1,1,1,1};//4 checkboxes, 4 right answers
    public  static final  int[] SCORES_Q2 = {3};    //1 right answers editable
    public  static final  int[] SCORES_Q3 = {0,3,0,0};//4 radiobuttons, 1 right answer
    public  static final  int[] SCORES_Q4 = {3};    //1 right answers editable

    public  Question QUESTION_1 = new Question(SCORES_Q1,FALSEx4,false);
    public  Question QUESTION_2 = new Question(SCORES_Q2,FALSEx1,false);
    public  Question QUESTION_3 = new Question(SCORES_Q3,FALSEx4,false);
    public  Question QUESTION_4 = new Question(SCORES_Q4,FALSEx1,false);

    public  Question[] ALL_QUESTIONS = {QUESTION_1,QUESTION_2,QUESTION_3,QUESTION_4};

    public  Quiz INIT_QUIZ= new Quiz(ALL_QUESTIONS);

    public static final Rect INIT_RECT = new Rect(0,0,0,0);

    // GLOBAL VARIABLES ($var) of Main Activity:

    public Rect $rect = INIT_RECT; // all zeros
    //          $rect = (0   ,0  , $theWidth, theHeight)
    //          $rect = (left,top, right    , bottom   )
    //
    //          $rect.width() = THE_WIDTH ; $rect.height() = THE_HEIGHT (scrollView)

    public int $questionNo; //$questionNo is a current Question#, depends on scrollView position.

    public final Quiz $quiz=INIT_QUIZ; //gets score values and all boolean values FALSE
    // classes:
    //
    // Quiz = (Question[questionNumber]) represented by $quiz
    // Question = (int[answerNumber] score, boolean[answerNumber] isSelected, boolean isAnswered)

    public static boolean $quizSummaryDone = false;

    public class Question {
        //fields:
        private int[] score;
        private boolean[] isSelected;
        private boolean isAnswered;
        //constructor:
        public Question(int[] score, boolean[] isSelected, boolean isAnswered) {
            this.score = score;
            this.isSelected = isSelected;
            this.isAnswered = isAnswered;
        }
        //getters:
        public int          numOfAnswers()                  {return score.length;}
        public int          questionScore(){
            int x = 0;
            for(int i=0;i<numOfAnswers();i++) x=x+(isSelected[i]?1:0)*score[i];
            return x;
        }
        public int          maxScore(){
            int x = 0;
            for(int i=0;i<numOfAnswers();i++) if(score[i]>x) x=score[i];
            return x;
        }
        public int          sumScore(){
            int x = 0;
            for(int i=0;i<numOfAnswers();i++) x=x+score[i];
            return x;
        }
        //setters:
        public void         setSelection(   boolean[] newSelection){
            //isSelected = newSelection;
            for (int i=0;i<numOfAnswers();i++) isSelected[i]=newSelection[i];
        }
        public void         setSelection(   int     index,
                                            boolean newSelection){
            isSelected[index]=newSelection;
        }
        // toggle selection:    one answer selection cancels the others:
        public void         toggleSelection( int    index){
            for(int i=0;i<numOfAnswers();i++) isSelected[index]=(i==index);
        }
    }//END_OF Question

    public final static class Quiz {
        //fields:
        private Question[]  question;
        //constructor:
        public Quiz(Question[] question) {
            this.question = question;
        }
        //getters:
        public int          numOfQuestions()            {return question.length;}
        public int          numOfAnswers(   int index)  {return question[index].numOfAnswers();}//possible answers
        public boolean      isAnswered(     int index)  {return question[index].isAnswered;}

        public int          getScore(       int index){
            return question[index].questionScore();
        }
        public int          getMaxScore(    int index){
            if (index==0)
                return question[0].sumScore(); //Question #1 more than one answer to choose
            else
                return question[index].maxScore();
        }
        public int          getScore(){//total
            int x = 0;
            for(int i=0;i<numOfQuestions();i++) x=x+this.getScore(i);
            return x;
        }
        public int          getMaxScore(){//total
            int x = 0;
            for(int i=0;i<numOfQuestions();i++) x=x+this.getMaxScore(i);
            return x;
        }
        public boolean      allAnswered(){
            boolean x = true;
            for (int i=0;i<numOfQuestions();i++) x=x&question[i].isAnswered;
            return x;
        }
        private boolean lastQuestionLeft() {
            // TRUE if all (N-1)questions of N are answered.
            int x = 0;
            int numberOfQuestions = this.numOfQuestions();
            for (int i=0;i<numberOfQuestions;i++) if (question[i].isAnswered) x++;
            return (x==(numberOfQuestions-1));
        }
        //setters:
        public void         setRight(       int     questionNumber,
                                            boolean isRight){
          //question[questionNumber].setRight(isRight);
            question[questionNumber].isSelected[0]=isRight;
        }
        public void         setSelection(   int     questionNumber,
                                            boolean[] newSelection){
            //  question[questionNumber].setSelection(index,newSelection);
            question[questionNumber].setSelection(newSelection);
        }
        public void         setSelection(   int     questionNumber,
                                            int     index,
                                            boolean newSelection){
         // question[questionNumber].setSelection(index,newSelection);
            question[questionNumber].isSelected[index]=newSelection;
        }
        public void         toggleSelection(int     questionNumber,
                                            int     index){
            question[questionNumber].toggleSelection(index);
        }
        public void         setIsAnswered(    int     index)      {question[index].isAnswered=true;}
        //test methods:
        //
        // shows boolean values for questionNumber in pattern "{N:XXXX.Y}" | "{XX.Y}" or
        // where XXX are boolean values for answer selection and Y is IsAnswered value.
        // e.g.:"{0:TFFT:T}"
        //        ^ ^  ^ ^     T=TRUE, F=FALSE.
        //        | |  | |---- the question is Answered
        //        | |  |------ Answer #4 (3) is chosen
        //        | |--------- Answer #1 (2) is chosen
        //        |----------- Question #1 (0)
        // : answer #1 & #4 are chosen from [TFFT], question #1 [0:] is answered.
        public String printStatus(int i) { // question number
            String theString="{";
            if (i!=-1) theString=theString+i+":";
            for (int j=0; j<(numOfAnswers(i)); j++) {
                theString =theString+(question[i].isSelected[j]?"T":"F");
            }
            theString=theString+":"+(question[i].isAnswered?"T":"F")+"}";
            return theString;
        }
        public String printStatus() { // all questions
            String theString="";
            for (int i=0; i<(numOfQuestions()); i++) theString=theString+printStatus(-1);
            return theString;
        }

    }//END_OF Quiz

    /******************* OVERALL LAYOUT DESIGN AND DIMENTIONING CONCEPT: **************************/

    //NAMING CONVENTION: all numbers preceded with hush are described as  #N are non-zero indexed
    // e.g: Col #2 is second column but this is column 1 zero-indexed.

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
     *  CheckBox A14P = Question #4, for Question #1 in Q1-Layout Checkable in Portrait Mode
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
     *  This helps "1x4"-portrait-mode table nice vertical alignment in the center of the screen.
     *
     !! When columns are shrunk, it is needed to getWidth of A (old width)
     !! using calculateAndSetQlayoutDims() BEFORE changing to Landscape.
     !! THIS ID DONE IN calculateAndSetQlayoutDims()
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
     *  1A. make visible  column   1   (second column #2)
     *  1B. remove        tableRow 2   (third row)     (tableLayout.getChildAt(2))
     *  1C. remove        tableRow 3   (fourth row)    (tableLayout.getChildAt(3))
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
     *  RadioButton A32L = Question #2, for Question #3 in Q1-Layout Checkable in Landscape Mode
     *  TableView is a grandparent of CheckBox/RadioButton.
     *
     *  |[ 0]|[ 1]| <-- widths of those chkboxes/rbuttons will be taken as Col #0,1
     */

    /*==============================================================================================
     * x=1,3 (1:chkbox_table,3:rbutton_table) Q-layout #1&3 | A-layout: textview_table
     *----------------------------------------------------------------------------------------------
     *    TableRow# |0:  |1:  |<--Column ||                 | TableRow# |0:  |1:  |<--Column
     *    ---------------------          ||1x4:    2x2:     | ---------------------
     *        0:    |Ax1 |Ax2L|          ||    P       LL   |     0:    |AA1 |SA1 |
     *        1:    |Ax3 |Ax4L|          ||    P  (or) LL   |     1:    |AA2 |SA2 |
     *        2:    |Ax2P|               ||    P            |     2:    |AA3 |SA3 |
     *        3:    |Ax4P|               ||    P            |     3:    |AA4 |SA4 |
     *----------------------------------------------------------------------------------------------
     *  CheckBox A14P = Question #4, for Question #1 in Q1-Layout Checkable in Portrait Mode
     *
     *   A12P & A12L        <android:onClick="onCheckBoxClicked"/>
     *   A14P & A14L        <android:onClick="onCheckBoxClicked"/>
     *
     *   A32P & A32L        <android:onClick="onRadioButtonClicked"/>
     *   A34P & A34L        <android:onClick="onRadioButtonClicked"/>
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
     *  RadioButton A32L = Question #2, for Question #3 in Q1-Layout Checkable in Landscape Mode
     * TableView is a grandparent of CheckBox/RadioButton.
     *
     * |[ 0]|[ 1]| <-- widths of those chkboxes/rbuttons will be taken as Col #0,1
     *
     *
     *============================ Q-Layouts Quiz Basic Assumptions ================================
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
     * Question Options
     * Right Question #
     * Possible Input #
     * Weight Score for Answer
     * Possible Score
     *
     * AnswerView-------AV
     * HintView---------HT
     * QuestionTextView-QT
     *
     ****************   List of Methods for DESIGN & DIMENTIONING:    *****************************/

    /**{@linkplain #isDeviceLandscape
     * @returns TRUE if the device orientation mode is Landscape.
     }
     */
    private boolean isDeviceLandscape() {
        return (getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT);
    }

    /**
     * {@linkplain #landscapeEnable(int)}
     * @param portraitText
     * @return
     *
     * gets intId of R.string button text in portrait mode,
     * looks for adequate string for landscape mode
     * which ends with _L (portrait ends with _P)
     * then returns intId for landscape R.string
     *
     * used only in for submitButton text change on Landscape
     */
    private int landscapeEnable(int portraitText){
        final char oChar; // orientation letter character = L|P

        if (isDeviceLandscape()){
            String s = getResources().getResourceEntryName(portraitText);
            String landscapeTextString = s.substring(0,s.length()-1)+"L";
            return getResources().getIdentifier(landscapeTextString,"string",getPackageName());
        }
        else  return portraitText;
    }

    /**{@linkplain #calculateAndSetQlayoutDims()
     *  calculates and sets dimensions for a all Q-layouts in scrollView}
     *
     *  A weight method for proportional children under ScrollView not possible.
     *  ScrollView must have one child, Q-layouts (of the same size) are grandchildren.
     *  After calculation equal height for each Q-layout is set. 
     *  Later, the width of Q-layout in $rect is used to set other view values in landscape mode.
     */
    private Rect calculateAndSetQlayoutDims() {
        //
        // 1.: $rect 0,
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
        // sets Left/Right margins in A-layout:
        int AnswerLayoutMargin = qLayoutWidth/6;
        ViewGroup.MarginLayoutParams layoutMarginParams = (ViewGroup.MarginLayoutParams) (findViewById(R.id.textview_table)).getLayoutParams();
        layoutMarginParams.setMargins(AnswerLayoutMargin,0,AnswerLayoutMargin,0);
        return new  Rect(0,0, qLayoutWidth, qLayoutHeight);
    }

    /**{@linkplain #setTableLayoutOnLandscape(int, Rect)
     * the argument is Resource ID of the tableLayout and Q-layout width in $rect.
     * calculates and sets new value of 1st column width.
     * unhides 2nd column of the table
     * removes 3rd and 4th row
     * That makes table dim as 1x4 --> 2x2
     */
    private void setTableLayoutOnLandscape(int tableLayoutId, Rect $rect) {
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
                                             +  $rect.width()
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

    /**{@linkplain #setViewDimsOnLandscape(Rect)}
     * OnCreate setups parameters for Q-layouts,
     * if device orientation is in landscape mode.}
     */
    private void setViewDimsOnLandscape(Rect $rect){
        // IN ORDER OF APPERANCE IN ROOT LAYOUT:
        //                                                Q-Layout Structure -------|
        //                                                Q-Layout--------------> Q.V. childView:
        //                                                ==============================================
        setTableLayoutOnLandscape(chkbox_table,$rect);                          //1.1. CheckBoxLayout
        setTableLayoutOnLandscape(rbutton_table,$rect);                         //3.1. RadioButtonLayout
        //  ^ [SEE METHOD DESCRIPTION]
        TextView questionTextView1 =(TextView)findViewById(question_textview_1);//1.3. QuestionTextView
        questionTextView1.setText(R.string.question_text1_L);
        View emptyBottomView1 =findViewById(empty_bottomview_1);                //1.3. QuestionTextView
        emptyBottomView1.getLayoutParams().height=(int)getResources().getDimension(R.dimen.bottom_view_height_L);
        View emptyTopView1 =findViewById(empty_topview_1);                      //1.3. QuestionTextView
        emptyTopView1.getLayoutParams().height=(int)getResources().getDimension(R.dimen.top_view_height_L);
    }

    /************************* Action Bar Design & Colour Methods *********************************/

    /**{@linkplain #setActionBarParams(int, int, String)}
     * sets text&background color of the actionBar, adds in titleBar to AppName Q-layout name like:
     * e.g.: "Question #1","Welcome!" or "YOUR SCORE"
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
        actionBar.setTitle(Html.fromHtml(htmlText)); //<---depreciated but works :)
        actionBar.setBackgroundDrawable(new ColorDrawable(backgroundColor));
    }

    private int setQuestionColor(int questionNo) {
        //questionNo #0 is "Welcome"
        if(questionNo%2==0)
            return  ResourcesCompat.getColor(getResources(), R.color.color_even, null);
        else
            return  ResourcesCompat.getColor(getResources(), R.color.color_odd, null);
    }

    private int colourBetween(int color0_ResId, int color1_ResId, float propotion) {
        //calculates the colour between color0 and color1
        // depending on propotion  and gives integer value of colour between.
        // propotion is float between 0 and 1.
        int color0 = ResourcesCompat.getColor(getResources(), color0_ResId, null);
        int color1 = ResourcesCompat.getColor(getResources(), color1_ResId, null);
        // min propotion=0 ==> colourBetween = color0;
        // max propotion=1 ==> colourBetween = color1;
        return Color.rgb((int) ((1 - propotion) * Color.red(color0) + propotion * Color.red(color1)),
                (int) ((1 - propotion) * Color.green(color0) + propotion * Color.green(color1)),
                (int) ((1 - propotion) * Color.blue(color0) + propotion * Color.blue(color1))
        );
    }
    private void setQlayoutColor() {
        int colorId;
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.parent_of_qlayouts);
        for (int i=0;i<linearLayout.getChildCount();i++) {
            if (i < 2 || i % 2 != 0) colorId = R.color.color_even; else colorId = R.color.color_odd;
            linearLayout.getChildAt(i).setBackgroundColor(ResourcesCompat.getColor(getResources(),colorId,null));
        }
    }
    private void setSubmitButtonColor(int color) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.submit_button);
        linearLayout.setBackgroundColor(color);
    }
    //For ActionBar: second part of Quiz App title.(Question #N|"Welcome!"|"YOUR SCORES")
    private String setActionBarTitle(int questionNo){
        if (questionNo>0)
            return "Question #"+questionNo;
        else {
            if($quiz.allAnswered())
                return getResources().getString(R.string.QA);
            else
                return getResources().getString(R.string.Q0);
        }
    }
    private void changeWelcomeToAnswerLayout() {
        findViewById(R.id.Qlayout_0).setVisibility(View.GONE);
        findViewById(R.id.Qlayout_A).setVisibility(View.VISIBLE);
    }
    /**END OF Action Bar Design & Colour Methods **************************************************/

    /********** Build-in Listeners: ***************************************************************/
    //  The List of Listeners:
    //
    //  0. setScrollViewListener
    //  1. onCheckboxClicked(View)
    //  2. onRadioButtonClicked(View)
    //  3. editTextOnClick(View)
    //  4. submitButtonOnClick
    /*==========================================> setScrollViewListener
     * This is the most important listener:
     * =========================================
     * Due to the scrollView position:
     * - number of the question is chosen
     * - the colour Action Bar  is changed
     * - appearance, visibility and function of the Submit Button is set
     */
    private void setScrollViewListener(){
        // This Listener on scrollview's height determines:
        // - color of actionBar
        // - number of question (Q-layout) for test answer process
        // - state of submitButton (active, blocked, invisible etc.)

        final ScrollView scrollView = (ScrollView) findViewById(scroll_view);

        final int theHeight = $rect.height(); // the same height for each Q-layout: calculated before by calculateAndSetQlayoutDims().

        final int visibleQLayoutQuantity = ((LinearLayout)scrollView.getChildAt(0)).getChildCount()- 1; // ONE LESS!
        //from all Q-layouts (W+A + n*Q ) one Welcome, one Answer/(Results), n Questions  (layouts) [n = number of questions]
        // there is always one layout invisible: At the beginning answer-layout, then at the end - welcome-layout.

        final int theDenominator = visibleQLayoutQuantity - 1; //(Scrolling Y-coordinate position have always offset=-theHeight)
        // Here the denominator is always equal to number of questions.(only because one Welcome/Question layout is always GONE/VISIBLE)
        // This case: 6 vertical layouts in scrollview: only 5 always visible, but counted from 0 to 4.
        // 6 = getChildCount(), 5=visibleQLayoutQuantity, 4=theDenominator

        // height of the lowest, bottom view of Q-layout(@+id/empty_view_xB) being "a background" for submit_button.
        final int emptyBottomViewHeightPx = (int)getResources().getDimension(R.dimen.bottom_view_height_L);

        //Setting ScrollView Listener:
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            public void showAllQlayouts() {
                ScrollView scrollView = (ScrollView) findViewById(scroll_view);
                LinearLayout linearLayout = (LinearLayout)scrollView.getChildAt(0);
                // 0 = Answer  Qlayout   A
                // 1 = Welcome Qlayout   0
                // i = Qlayout Question #N --> index = N+1 (childView no)
                // 5 = Qlayout Question #4
                // ^                     ^------Question Number
                // |--------------------------- index if a childView.
                for (int i=1;i<NUMBER_OF_QUESTIONS+2;i++)
                {(linearLayout.getChildAt(i)).setVisibility(View.VISIBLE);}
                // all views except  A-layout are VISIBLE !.
            }
            //stops Q-layout when scrolling, to focus on Q-layout
            private void stopScrolling(){
                if  (!$quiz.isAnswered($questionNo - 1)) {//if question is not answered then scroll stop
                    //Q-layout stop when scrolling for EditText:
                    focusViewOnQuestion($questionNo);
                    //additional Q-layout stop when scrolling for EditText(s) (Q#1 & Q#3)
                    if($questionNo%2 != 0) scrollView.smoothScrollBy(0,0);
                }
            }

            boolean readyToExit=false;
            @Override
            public void onScrollChanged() {

                int verticalPosition = scrollView.getScrollY();
                // $questionNo is a question number depending on the scrollview position:
                // OFFSET: the question number [n] changes to higher number [n+1] earlier
                // when scrolled up Q[n]-layout have still emptyBottomView visible on the screen top
                $questionNo =(int)Math.floor((verticalPosition+emptyBottomViewHeightPx)/theHeight);

                //   $questionFloat = <0;N> "continuous question number" ; N=number of questions =4
                     float $questionFloat = (float)verticalPosition/theHeight;
                //    relativePos   = <0;1>      (relative Scrollview position)
                float relativePos   = $questionFloat/theDenominator; //x=  0...1
                float sawWave       = $questionFloat -(int)Math.floor($questionFloat);//x - floor(x)
                //    triangleWave  : 0 for Even, 1 for Odd but continuously.
                float triangleWave  = 1 - Math.abs(($questionFloat%2) - 1);      //  1-|(x mod 2)-1|
                ////////////////////////////////////////////////////////////////////////////////////
                //  triangleWave:                        //  sawWave:                             //
                //  ^                                    //  ^                                    //
                // 1|   1     3     5     7              // 1|   /|   /|   /|   /|                //
                //  |  /\    /\    /\    /\              //  |  / |  / |  / |  / |  /             //
                //  | /  \  /  \  /  \  /  \  /          //  | /  | /  | /  | /  | /              //
                //  |/    \/    \/    \/    \/           //  |/   |/   |/   |/   |/               //
                // 0|----------------------------->      // 0|----------------------------->      //
                //  0     2     4|    6     8            //  0    1    2    3    4|               //
                //                                       //                                       //
                ////////////////////////////////////////////////////////////////////////////////////
                float minOdd  = 0.05f;
                float minEven = 0.15f;
                float maxOdd  = 0.90f;//for Even Questions (EditText)
                float maxEven = 0.60f;//for Odd  Questions (CompoundButton)

                float min;      if($questionNo%2 == 0) min=minEven; else min=minOdd;
                float max;      if($questionNo%2 == 0) max=maxEven; else max=maxOdd;

                boolean isInvisible = (sawWave>min)&&(sawWave<max);
                boolean disableForWelcome = ($questionFloat<=0.9f);

                setActionBarParams( 0,                                                   // text color = #00000000 = black = 0
                        colourBetween(R.color.color_even,R.color.color_odd,triangleWave),// action bar color
                        setActionBarTitle($questionNo)                                   // "Quiz App: Question #N
                        //" :"+String.format("%.2f",sawWave)+                            // further text
                        //" :"+verticalPosition+
                        //"/" +theHeight+
                        //":"+String.format("%.2f",$questionFloat)+
                        //":"+$quiz.printStatus(1)+$quiz.printStatus(3)
                        // ^ LAST LINE FOR SHOW UP VAR ON ACTION BAR (+string)
                );
                setSubmitButtonColor(colourBetween(R.color.color_odd,R.color.color_even,triangleWave));
                // Submit Button Appearance Rules:
                if ($quiz.allAnswered()) {//if  all questions are answered:
                    changeSubmitButtonState(SUBMIT_BUTTON_ALL_QUESTIONS_ANSWERED);// arrow down to to scroll up to first "Answer"-layout

                    quizSummary();//executed once
                    if ( ($questionNo==0) || readyToExit) {//initially readyToExit is FALSE
                        changeSubmitButtonState(SUBMIT_BUTTON_EXIT);//final quiz review with EXIT button which appeares when scroll down to A-layout
                        readyToExit=true;
                    }
                }
                else { //if not all questions are answered:
                    if (disableForWelcome) { //arrow up to to scroll down from first "Welcome"-layout to Q-layouts
                        changeSubmitButtonState(SUBMIT_BUTTON_DISABLED);
                    }
                    else { //Submit Button disappears when not interupting with question visibility or  when the question is answered.
                        if (isInvisible||($quiz.isAnswered($questionNo - 1))) {
                            changeSubmitButtonState(SUBMIT_BUTTON_INVISIBLE);
                        }
                        else { //Submit Button is ACTIVE, ready to submit the answer
                            stopScrolling(); //stop Q-layout when scrolling to focus on Q-layout
                            if ($quiz.lastQuestionLeft())//when the last question is to answer SubmitButton changes for LAST_QUESTION
                                changeSubmitButtonState(SUBMIT_BUTTON_ACTIVE_FOR_LAST_QUESTION);
                            else
                                changeSubmitButtonState(SUBMIT_BUTTON_ACTIVE);
                        }
                    }

                }

            }//END_OF onScrollChanged()
        });
    }//END_OF setScrollViewListener()

    //OnClick Listeners methods defined in activity_main: (EditText & Compound Buttons):

    public void onCheckboxClicked(View checkBox) {
        // A2L=A2P, A4L=A4P synchronisation of compound buttons for Question #2 & #4:
        // Two compound buttons for Question #2 & #4 each are seen only once in the table,
        // but for changing orientation they have to have the same state
        // (there is 4 answers, but 6 compound buttons)
        // ------------------------------------------------------------------------------------
        // |1|2|     |1|     |1|2|
        // |3|4| ==> |3|  or |3|4|@Landscape
        // |2|       |2|
        // |4|       |4|@Portrait
        // ------------------------------------------------------------------------------------

        boolean[] answerArray= $quiz.question[0].isSelected;
        // Is the view now checked?
        boolean isSelected = ((CheckBox) checkBox).isChecked();
        // Check which checkbox was clicked
        switch (checkBox.getId()) {
            case R.id.A11:  answerArray[0]=isSelected;break;
            case R.id.A12P: answerArray[1]=isSelected;break;
            case R.id.A12L: answerArray[1]=isSelected;break;
            case R.id.A13:  answerArray[2]=isSelected;break;
            case R.id.A14P: answerArray[3]=isSelected;break;
            case R.id.A14L: answerArray[3]=isSelected;break;
        }
        //Synchronisation with twin button (see method description)
        twinCompoundButtonSelectionSynchro(checkBox);
        //$quiz.setSelection(0,answerArray);
        $quiz.question[0].isSelected=answerArray;
    // The method onCheckboxClicked() above works if:
    // - is public
    // - returns void
    // - defines a View as its only parameter (any of compound button that was clicked)
    }//END_OF onCheckboxClicked()

    public void onRadioButtonClicked(View radioButton) {
        // A2L=A2P, A4L=A4P synchronisation of compound buttons for Question #2 & #4:
        // Two compound buttons for Question #2 & #4 each are seen only once in the table,
        // but for changing orientation they have to have the same state
        // (there is 4 answers, but 6 compound buttons)
        // ------------------------------------------------------------------------------------
        // |1|2|     |1|     |1|2|
        // |3|4| ==> |3|  or |3|4|@Landscape
        // |2|       |2|
        // |4|       |4|@Portrait
        // ------------------------------------------------------------------------------------

        boolean[] answerArray= new boolean[$quiz.numOfAnswers(0)];
        // Is the view now checked?
        boolean isSelected = ((RadioButton) radioButton).isChecked();
        // Check which radiobutton was clicked
        uncheckOtherRadioButtons(radioButton);
        switch (radioButton.getId()) {
            case R.id.A31:  answerArray[0]=isSelected;break;
            case R.id.A32P: answerArray[1]=isSelected;break;
            case R.id.A32L: answerArray[1]=isSelected;break;
            case R.id.A33:  answerArray[2]=isSelected;break;
            case R.id.A34P: answerArray[3]=isSelected;break;
            case R.id.A34L: answerArray[3]=isSelected;break;
            // Unfortunately setup of the RadioGroup on TableLayout including RadioButtons is impossible. :(
            // RadioButtons here are grandchildren of layout.
        }
        //Synchronisation with twin button (see method description)
        twinCompoundButtonSelectionSynchro(radioButton);
        //$quiz.setSelection(2,answerArray);
        $quiz.question[2].isSelected=answerArray;
        // The method onRadioButtonClicked() above works if:
    // - is public
    // - returns void
    // - defines a View as its only parameter (any of compound button that was clicked)
    }//END_OF onRadioButtonClicked()

    // softKeyboard on-listener for EditText (Question 2&4):
    public void editTextOnClick(View view) {
        //gets resource id name string of the editText
        String viewName = getResources().getResourceEntryName(view.getId());
        // calculates the last char of it's name to know question number:
        String questionNum = viewName.substring(viewName.length()-1);// "2" or "4"
        int index =parseInt(questionNum)/2-1;// 1 or 2
        // finds question_textview_N where N is a question number
        final TextView questionTextView = (TextView) findViewById(getResources().getIdentifier("question_textview_" + questionNum, "id", getPackageName()));
        // finds rootView layout to measure the actual height of Q-layout which smaller than Q-layout height when the softKeyboard appeares.
        final View rootLayout = findViewById(android.R.id.content);
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // when the keyboard is ON the question text moves up, when it's off goes back to center alignment.
                if (rootLayout.getHeight()==$rect.height())                 //softKeyboard is off
                    questionTextView.setGravity(Gravity.CENTER);
                else                                                        //softKeyboard is on
                    questionTextView.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP);
            }
        });
    }//END_OF editTextOnClick

    public void submitButtonOnClick(View view) {
        // loops in submitButtonOnClick by return when no EditText or no RadioButton chosen.
        // that disables pressing submit button
        boolean thatHappened=false;
        switch ($questionNo) {
            case 2: thatHappened=(((EditText)findViewById(edit_text_2)).getText().toString().isEmpty());
                break;
            case 3: thatHappened=allCompoundButtonsUnchecked((TableLayout)findViewById(rbutton_table));
                break;
            case 4: thatHappened=(((EditText)findViewById(edit_text_4)).getText().toString().isEmpty());
                break;
        }//endswitch
        if (thatHappened) return;

        //Toast Info while submitting a question:
        if (!$quiz.allAnswered()) {
            Toast.makeText(
                    getApplicationContext(),
                    "the answer for Question #"
                    + $questionNo
                    + "\n has been submitted.",
                    Toast.LENGTH_SHORT
            ).show();
        }
        else android.os.Process.killProcess(android.os.Process.myPid());
            // for SUBMIT_BUTTON_EXIT // exits the application!!!
            // simply kills itself :)))))))

        //Checking INPUT TEXT (EditText):
        if ($questionNo%2==0 && stringAnswerIsCorrect($questionNo)) $quiz.question[$questionNo-1].isSelected=TRUEx1;//for question #2 or #4

        //if ($questionNo%2==0) $quiz.question[$questionNo-1].isSelected[0]=stringAnswerIsCorrect($questionNo);
        //^
        // before
        //      $quiz.question[$questionNo-1].isSelected=TRUEx1
        // I used
        //      $quiz.question[$questionNo-1].isSelected[0]=true
        //
        // unfortunatelly when I used the last one, $quiz.question[3].isSelected[0] changed its value at the same time as $quiz.question[1].isSelected[0]
        // WHY?????
        // If a person evaluating my script could help me with the answer... :)

        $quiz.setIsAnswered($questionNo - 1);
        // disabling the question view for editing:
        switch ($questionNo) {
            case 1: setAllCompoundButtonsEnabled(chkbox_table,false);   break;
            case 2: setEditViewEnabled(edit_text_2,false);              break;
            case 3: setAllCompoundButtonsEnabled(rbutton_table,false);  break;
            case 4: setEditViewEnabled(edit_text_4,false);              break;
        }//endswitch
    }//END_OF submitButtonOnClick

    /*END OF:** Build-in Listeners: ***************************************************************/

    /********** Scrollview Text, CompoundButton & EditText Input Methods **************************/

    //sets selection state of the compound button to its counterpart (twin) in other orientation mode

    private void twinCompoundButtonSelectionSynchro(View theCompoundButton) {
        // e.g.: when the state of A12L checkbox is changed (/un/checked) in landscape mode,
        // the same state is set for "twin" checkbox A12P visible in portrait mode.
        //
        // A2L=A2P, A4L=A4P synchronisation of compound buttons for Question #2 & #4:
        // Two compound buttons for Question #2 & #4 each are seen only once in the table,
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

    private boolean allCompoundButtonsUnchecked( TableLayout tableLayout) {
        boolean b=true;
        CompoundButton loopRadioButton;
        // Loops search through the grandchildren od the tableLayout:
        TableRow  loopTableRow;
        for (int i=0; i<tableLayout.getChildCount(); i++) {
            loopTableRow = (TableRow) tableLayout.getChildAt(i);
            for (int j=0; j<loopTableRow.getChildCount(); j++) {
                b=b&!((RadioButton)loopTableRow.getChildAt(j)).isChecked();
            }
        }
        return b;
    }

    private void setAllCompoundButtonsEnabled(int tableLayoutResId, boolean isEnabled) {
        // finds its parent and grandparent view:
        TableLayout tableLayout = (TableLayout) findViewById(tableLayoutResId);
        // Loops search through the grandchildren od the tableLayout:
        TableRow  loopTableRow;
        CompoundButton loopCompoundButton;
        for (int i=0; i<tableLayout.getChildCount(); i++) {
            loopTableRow = (TableRow) tableLayout.getChildAt(i);
            for (int j=0; j<loopTableRow.getChildCount(); j++) {
                loopCompoundButton = (CompoundButton) loopTableRow.getChildAt(j);
                //enables all compoundButtons in tableLayout.
                loopCompoundButton.setClickable(isEnabled);
            }
        }
    }

    private void setEditViewEnabled(int editTextResId, boolean isEnabled) {
        EditText editText = (EditText) findViewById(editTextResId);
        //set on/off softkeyboard listener:
        //RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.main_view); // root layout
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        if (isEnabled){
            //disables text-autocomplete not to suggest the answer
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            editText.setEnabled(true);
            inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
        else{
            editText.setEnabled(false);
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    public void disableAllAnsweredInputs() {
        // if  $quiz.isAnswered(N-1) is TRUE (question finally answered by pressing submit button)
        //     then editable part of Nth Q-layout cannot be edited/(un)checked/toggled
        setAllCompoundButtonsEnabled(chkbox_table,!$quiz.question[0].isAnswered);
        setEditViewEnabled(edit_text_2,!$quiz.question[1].isAnswered);
        setAllCompoundButtonsEnabled(rbutton_table,!$quiz.question[2].isAnswered);
        setEditViewEnabled(edit_text_4,!$quiz.question[3].isAnswered);
    }
    /**
     * {@linkplain #stringAnswerIsCorrect}
     * @param questionNum
     */
     private boolean stringAnswerIsCorrect(int questionNum) {

         String theStringN1=null;
         String theStringN2=null;

        //finds the EditText
        EditText editText = (EditText) findViewById(getResources().getIdentifier("edit_text_" + questionNum, "id", getPackageName()));
        //gets the input text
        String theAnswer =  editText.getText().toString();
        // trims left/right spaces and sets spacing between words to one space...
        String[] array = theAnswer.split("\\s+");
        theAnswer="";
        for(int i=0; i<array.length; i++) theAnswer = theAnswer + array[i] + " ";
        theAnswer = theAnswer.trim();
        editText.setText(theAnswer);
        //user answer rised to uppercase because the strings to compare are in uppercase too.
        theAnswer=theAnswer.toUpperCase();

        switch  (questionNum) {
            case 2: {
                 theStringN1 = getResources().getString(R.string.A21);
                 theStringN2 = getResources().getString(R.string.A22);
            };break;
            case 4: {
                 theStringN1 = getResources().getString(R.string.A41);
                 theStringN2 = getResources().getString(R.string.A42);
            };break;
        }
         return (theAnswer.equals(theStringN1)||theAnswer.equals(theStringN2));

    }//END_OF stringAnswerIsCorrect()

    /*END OF:***************** Scrollview Text & CompoundButton Input Methods: ********************/

    /********** The Submit Button Methods **********************************************************/
    private void changeSubmitButtonState(int buttonState){
        // 0 SUBMIT_BUTTON_DISABLED                     (no editing)(Even Numbers)
        // 1 SUBMIT_BUTTON_ACTIVE                       (Listener)  (Odd numbers)
        // 2 SUBMIT_BUTTON_INVISIBLE                    (no editing)
        // 3 SUBMIT_BUTTON_ACTIVE_FOR_LAST_QUESTION     (Listener)
        // 4 SUBMIT_BUTTON_ALL_QUESTIONS_ANSWERED       (no editing)
        // 5 SUBMIT_BUTTON_EXIT                         (Listener)
        //
        // SubmitButton IS LINEAR LAYOUT !!!
        //
        final LinearLayout submitButton = (LinearLayout)findViewById(submit_button);
        final TextView submitButtonText    =  (TextView) submitButton.getChildAt(0);
        final ImageView submitButtonImageTop  = (ImageView) submitButton.getChildAt(1);
        final ImageView submitButtonImageBottom  = (ImageView) submitButton.getChildAt(2);
        final int submitButtonPadding=getResources().getDimensionPixelSize(R.dimen.margin);
        final int submitButtonPaddingSmall=getResources().getDimensionPixelSize(R.dimen.small_margin);

        //setEnabled(false) makes a View non-clickable AND non-focusable = completely locked
        // padding: (Left, Top, Right, Bottom)

        // 1. Button locked/unlocked state:
        submitButton.setClickable(true);
        submitButtonImageTop.setVisibility(ImageView.VISIBLE);//recovering visibility after GONE
        submitButton.setPadding(submitButtonPadding,0,0,0); // with image, no R-padding

        submitButton.setClickable((buttonState!=SUBMIT_BUTTON_DISABLED)); //locked for that 0 state.

        if (buttonState==SUBMIT_BUTTON_INVISIBLE)
            submitButton.setVisibility(LinearLayout.GONE);
        else
            submitButton.setVisibility(LinearLayout.VISIBLE);

        if (buttonState==SUBMIT_BUTTON_ACTIVE_FOR_LAST_QUESTION){
            submitButton.setPadding(submitButtonPadding,0,submitButtonPadding,0);   // no arrow image, R-padding
            submitButton.setMinimumHeight(submitButton.getHeight());                //keeps the old height with no image.
            submitButtonImageTop.setVisibility(ImageView.GONE);
        }
        if (buttonState==SUBMIT_BUTTON_ALL_QUESTIONS_ANSWERED) {
            submitButton.setClickable(false);
            submitButtonImageTop.setVisibility(ImageView.GONE);
            submitButtonImageBottom.setVisibility(ImageView.VISIBLE);
        }
        if (buttonState==SUBMIT_BUTTON_EXIT) {
            submitButtonImageTop.setVisibility(ImageView.VISIBLE);
            submitButtonImageBottom.setVisibility(ImageView.GONE);
        }

        // 2. Button landscape orientation text change to longer strings:
        switch (buttonState) {
            case SUBMIT_BUTTON_DISABLED://0
                submitButtonText.setText(landscapeEnable(button_text_disabled_P));break;
            case SUBMIT_BUTTON_ACTIVE://1
                submitButtonText.setText(landscapeEnable(button_text_active_P));break;
            case SUBMIT_BUTTON_ACTIVE_FOR_LAST_QUESTION://3
                submitButtonText.setText(landscapeEnable(button_text_lastq_P));break;
            case SUBMIT_BUTTON_ALL_QUESTIONS_ANSWERED://4
                submitButtonText.setText(landscapeEnable(button_text_end_P));break;
            case SUBMIT_BUTTON_EXIT://5
                submitButtonText.setText(landscapeEnable(button_text_exit_P));break;
        }//endswitch
    }//END_OF changeSubmitButtonState()

    /*END OF:** The Submit Button Methods *********************************************************/

    /****************************Answer Layout Methods: *******************************************/
    private void fillInAnswerQlayoutTable() {
        // finds its parent and grandparent view:
        TableLayout tableLayout = (TableLayout) findViewById(R.id.textview_table);
        // Loops search through the grandchildren od the tableLayout:
        TableRow  loopTableRow; //i=row
        TextView loopTextView;
        for (int i=0; i<tableLayout.getChildCount(); i++) {
            loopTableRow = (TableRow) tableLayout.getChildAt(i);
            loopTextView = (TextView) loopTableRow.getChildAt(1);//2nd col
            //writes in TextViews in 2nd col of the tableLayout.
            $quiz.numOfAnswers(i);
            String theString =$quiz.getScore(i)+"/"+$quiz.getMaxScore(i);
            loopTextView.setText(theString);
            loopTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.color_ok, null));
            if ($quiz.getScore(i)==0) loopTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.color_bad, null));
            if ($quiz.getScore(i)==$quiz.getMaxScore(i)) loopTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.color_good, null));
        }
    }

    private void showQuizScores() {
        //counts and displays the score and max score for all answered questions.
        fillInAnswerQlayoutTable();
        // finds its parent and grandparent view:
        TextView textView = (TextView) findViewById(R.id.answer_textview_0);
        textView.setText(getResources().getString(R.string.answer_text0)+" "+$quiz.getScore()+"/"+$quiz.getMaxScore());
        textView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.color_ok, null));
        if ($quiz.getScore()==0) textView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.color_bad, null));
        if ($quiz.getScore()==$quiz.getMaxScore()) textView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.color_good, null));
    }

    private void showQuizAnswers(int questionNum) {

        if (questionNum%2==0) { //Edit Text Questions 2&4
            final String hexTextColorBad  = String.format("#%06X", (0xFFFFFF & ResourcesCompat.getColor(getResources(), R.color.color_bad, null)));
            final String hexTextColorGood = String.format("#%06X", (0xFFFFFF & ResourcesCompat.getColor(getResources(), R.color.color_good, null)));
            final String theHtmlArrowCode="&#9654";

            EditText editText = (EditText)findViewById(getResources().getIdentifier("edit_text_"+questionNum,"id",getPackageName())); //(R.id.edit_text_2) or (R.id.edit_text_4)
            final String rightAnswer = getString(getResources().getIdentifier("A"+questionNum+"r","string",getPackageName())); //(R.string.A2r) or (R.string.A4r)
            final String theAnswer = editText.getText().toString();

            String htmlText=null;

            if ($quiz.question[questionNum-1].isSelected[0]) { // The answer is correct.
                htmlText = "<font color=\'"+hexTextColorGood+"\'>"+rightAnswer+"</font>";
            }
            else { //The answer is false.
                htmlText =  "<font color=\'"+hexTextColorBad+"\'><strike>"+theAnswer+"</strike></font>" +
                        "<font color=#000000> "+theHtmlArrowCode+" </font>" +
                        "<font color=\'"+hexTextColorGood+"\'>"+rightAnswer+"</font>";
            }
            editText.setText(Html.fromHtml(htmlText));
        }
        else {
            switch (questionNum) {
                case 1:{
                    // finds its parent and grandparent view:
                    TableLayout tableLayout = (TableLayout) findViewById(chkbox_table);
                    // Loops search through the grandchildren od the tableLayout:
                    TableRow  loopTableRow;
                    CompoundButton loopCompoundButton;
                    for (int i=0; i<tableLayout.getChildCount(); i++) {
                        loopTableRow = (TableRow) tableLayout.getChildAt(i);
                        for (int j=0; j<loopTableRow.getChildCount(); j++) {
                            loopCompoundButton = (CompoundButton) loopTableRow.getChildAt(j);
                            //all compound buttons' texts get color_good
                            loopCompoundButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.color_good, null));
                        }
                    }
                }break;
                case 3:{
                    //
                    final String rightAnswer = getString(getResources().getIdentifier("A3r","string",getPackageName()));
                    // finds its parent and grandparent view:
                    TableLayout tableLayout = (TableLayout) findViewById(rbutton_table);
                    // Loops search through the grandchildren od the tableLayout:
                    TableRow  loopTableRow;
                    CompoundButton loopCompoundButton;
                    for (int i=0; i<tableLayout.getChildCount(); i++) {
                        loopTableRow = (TableRow) tableLayout.getChildAt(i);
                        for (int j=0; j<loopTableRow.getChildCount(); j++) {
                            loopCompoundButton = (CompoundButton) loopTableRow.getChildAt(j);
                            //all compound buttons' texts get color_bad except the one with the right answer
                            if (loopCompoundButton.getText().equals(rightAnswer)) {
                                loopCompoundButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.color_good, null));
                            }
                            else {
                                loopCompoundButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.color_bad, null));
                            }
                        }
                    }
                }break;
                case 0:{
                    //
                    final String rightAnswer = getString(getResources().getIdentifier("A3r","string",getPackageName()));
                    // finds its parent and grandparent view:
                    TableLayout tableLayout = (TableLayout) findViewById(rbutton_table);
                    // Loops search through the grandchildren od the tableLayout:
                    TableRow  loopTableRow;
                    CompoundButton loopCompoundButton;
                    for (int i=0; i<tableLayout.getChildCount(); i++) {
                        loopTableRow = (TableRow) tableLayout.getChildAt(i);
                        for (int j=0; j<loopTableRow.getChildCount(); j++) {
                            loopCompoundButton = (CompoundButton) loopTableRow.getChildAt(j);
                            //all compound buttons' texts get color_bad except the one with the right answer
                            if (loopCompoundButton.getText().equals(rightAnswer)) {
                                loopCompoundButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.color_good, null));
                            }
                            else {
                                loopCompoundButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.color_bad, null));
                            }

                        }
                    }
                }break;
            }

        }
    }

    void quizSummary() {
        if ($quizSummaryDone) return;
        changeWelcomeToAnswerLayout();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        //^ this locks orientation when all questions are answered,
        // otherwise with device orientation change $quiz resets its values (see AndroidManifest.xml note)
        showQuizScores();
        for (int i=1;i<=NUMBER_OF_QUESTIONS;i++) showQuizAnswers(i);
        $quizSummaryDone = true;
    }
    /*END OF: Answer Layout Methods: **************************************************************/

    /**
     * @param questionNo
     * stops scrollview on Question
     */
    private void focusViewOnQuestion(int questionNo) {
        if ($quiz.isAnswered($questionNo - 1)) return;
        String viewStringId = "Qlayout_" + questionNo;
        int resId = getResources().getIdentifier(viewStringId, "id", getPackageName());
        View targetView =findViewById(resId);
        targetView.getParent().requestChildFocus(targetView,targetView);
    }
    /****************************************** ON CREATE *****************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);
        setQlayoutColor();
        $rect = calculateAndSetQlayoutDims();  // $rect is width & height of every Q-layout.

        //Initial ActionBar design before scrollview move.
        setActionBarParams(
                ResourcesCompat.getColor(getResources(),R.color.actionBarText, null),//sets text color, can be set to 0 as black
                setQuestionColor($questionNo),                                       //sets background color
                setActionBarTitle($questionNo)                                       // "Quiz App: Welcome!",etc.
        );
        changeSubmitButtonState(SUBMIT_BUTTON_DISABLED);
        setSubmitButtonColor(colourBetween(R.color.color_odd,R.color.color_even,0));
        if (isDeviceLandscape()) {setViewDimsOnLandscape($rect);} // tableLayouts width recalculated
        onCheckboxClicked(findViewById(R.id.A11)); // Listener on chkbox_table (Question #1)
        disableAllAnsweredInputs();
        setScrollViewListener();

    }//END_OF OnCreate
}//END_OF MainActivity