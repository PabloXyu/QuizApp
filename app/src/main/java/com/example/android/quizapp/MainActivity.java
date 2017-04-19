package com.example.android.quizapp;

import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import static java.security.AccessController.getContext;


public class MainActivity extends AppCompatActivity {

    class Constants {
        public static final int NUM_QUESTIONS = 4;
    }


    // prints the "text" of a TextView (xml-named "textViewIdName")
    private void printOnTextView(String textViewIdName, String text) {
        TextView orderSummaryTextView = (TextView)findViewById(getResources().getIdentifier(textViewIdName, "id", getPackageName()));
        orderSummaryTextView.setText(text);
    }

    /**
     * This method sets the height of a LinearLayout (xml-named "linearLayoutIdName")
     * to dip height of the device screen.
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetLinearLayoutHeight("layout_Q1");
        SetLinearLayoutHeight("layout_Q2");
        SetLinearLayoutHeight("layout_Q3");
        SetLinearLayoutHeight("layout_Q4");

    }


}

//      Gets linearLayout of linearLayoutIdName
//     LinearLayout linearLayout = (LinearLayout)findViewById(getResources().getIdentifier(linearLayoutIdName, "id", getPackageName()));
// Gets the linearLayout LayoutParams that allow to resize the linearLayout


//      DisplayMetrics displayMetrics = new DisplayMetrics();








//LayoutParams layoutParams = container.linearLayout.getLayoutParams();
// LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(container.getLayoutParams().width = width);
//  linearLayout.setLayoutParams(
//LayoutParams layoutParams = linearLayout.getLayoutParams();
//LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams()
//LayoutParams layoutParams = (LayoutParams)(LayoutParams.WRAP_CONTENT, 50));
// linearLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, 50));

//   RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
// layoutParams.height = 100;
//linearLayout.setLayoutParams();

// textView.setText(newText);

//Display display = getActivity().getWindowManager().getDefaultDisplay();


