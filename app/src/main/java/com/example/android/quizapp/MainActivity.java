package com.example.android.quizapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    int displayHeight;

    // prints the "text" of a TextView (xml-named "textViewIdName")
    private void printOnTextView(String textViewIdName, String text) {
        TextView orderSummaryTextView = (TextView)findViewById(getResources().getIdentifier(textViewIdName, "id", getPackageName()));
        orderSummaryTextView.setText(text);
    }

    /**
     * This method sets the height of a LinearLayout (xml-named "linearLayoutIdName")
     * to dip height of the device screen.
     */
    private void SetDisplayHeightToLinearLayout(String linearLayoutIdName) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayHeight = displayMetrics.heightPixels;
        printOnTextView("header_Q1",""+displayHeight);
        LinearLayout linearLayout=(LinearLayout)findViewById(getResources().getIdentifier(linearLayoutIdName,"id",getPackageName()));

        android.view.ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();
        layoutParams.height = displayHeight;
        linearLayout.setLayoutParams(layoutParams);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetDisplayHeightToLinearLayout("layout_Q1");
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


