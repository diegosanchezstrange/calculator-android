package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    private String result = "";
    private final String SIMBOLS = "/*-+.=";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvResult = (TextView)findViewById(R.id.textViewResult);

        Button buttonParOpen = (Button)findViewById(R.id.buttonParOpen);
        Button buttonParClose = (Button)findViewById(R.id.buttonParClose);
        Button buttonDiv = (Button)findViewById(R.id.buttonDiv);
        Button buttonMult = (Button)findViewById(R.id.buttonMult);
        Button buttonMinus = (Button)findViewById(R.id.buttonMinus);
        Button buttonPlus = (Button)findViewById(R.id.buttonPlus);
        Button buttonEquals = (Button)findViewById(R.id.buttonEquals);
        Button buttonDel = (Button)findViewById(R.id.buttonDel);
        Button button1 = (Button)findViewById(R.id.button1);
        Button button2 = (Button)findViewById(R.id.button2);
        Button button3 = (Button)findViewById(R.id.button3);
        Button button4 = (Button)findViewById(R.id.button4);
        Button button5 = (Button)findViewById(R.id.button5);
        Button button6 = (Button)findViewById(R.id.button6);
        Button button7 = (Button)findViewById(R.id.button7);
        Button button8 = (Button)findViewById(R.id.button8);
        Button button9 = (Button)findViewById(R.id.button9);
        Button button0 = (Button)findViewById(R.id.button0);
        Button buttonDot = (Button)findViewById(R.id.buttonDot);

    }


    @Override
    public void onClick(View v)
    {
        TextView tvResult = findViewById(R.id.textViewResult);
        Button button = (Button)v;
        String textBtn = (String) button.getText();
        int resultLength = result.length();

        if (Pattern.matches( "\\d", textBtn))
        {
            result += textBtn;
            tvResult.setText(result);
        }
        else if (SIMBOLS.contains(result.substring(resultLength-2)))
        {
            result += textBtn;
            tvResult.setText(result);
            return;
        }
        else
        {
            result = result.substring(0, resultLength-1);
            tvResult.setText(result);
        }
    }
}
