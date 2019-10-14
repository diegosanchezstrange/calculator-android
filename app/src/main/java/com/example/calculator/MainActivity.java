package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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

        addButtonEffect((ViewGroup) this.findViewById(R.id.linearLayout));

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
        else if (textBtn.equals("DEL"))
        {
            result = result.substring(0, resultLength-1);
            tvResult.setText(result);
        } else
        {
            if (SIMBOLS.contains(result.substring(resultLength-1)))
            {
                tvResult.setText(result);
            } else
            {
                result += textBtn;
                tvResult.setText(result);
            }


        }
    }

    private static void addButtonEffect(ViewGroup v)
    {
        for (int i = 0; i < v.getChildCount() ; i++)
        {
           View currentChild = v.getChildAt(i);

           if(currentChild instanceof Button)
           {
                buttonEffect(currentChild);
           }else if(currentChild instanceof LinearLayout)
           {
              addButtonEffect((ViewGroup) currentChild);
           }
        }
    }

    private static void buttonEffect(View button)
    {
        button.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                    {
                        v.getBackground().setColorFilter(0xe066300E, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }

}
