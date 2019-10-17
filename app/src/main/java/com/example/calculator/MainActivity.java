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

    private String input = "";
    private final String ALL_SIMBOLS = "/*+=-.()";
    private final String SIMBOLS = "/*+=";
    private final String SPECIAL_SIMBOLS = "-.";
    private final String EVEN_MORE_SPECIAL_SIMBOLS = "()";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButtonEffect((ViewGroup) this.findViewById(R.id.linearLayout));

        this.findViewById(R.id.buttonDel).setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v) {
                TextView tvInput = findViewById(R.id.textViewInput);
                tvInput.setText("");
                input = "";
                return true;
            }
        });



    }



    @Override
    public void onClick(View v)
    {
        if(validateInput(v))
        {
            calculate(v);
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


    public boolean validateInput(View v)
    {
        boolean val = false;
        TextView tvInput = findViewById(R.id.textViewInput);
        Button button = (Button)v;
        String textBtn = (String) button.getText();
        int inputLen = input.length();

        if (Pattern.matches( "\\d", textBtn))
        {
            input += textBtn;
            tvInput.setText(input);
        }else if (textBtn.equals("DEL"))
        {
            input = input.length() == 0 ? "" : input.substring(0, inputLen-1);
            tvInput.setText(input);
        } else if ( SIMBOLS.contains(textBtn) && !SIMBOLS.contains(input.equals("") ? "" : input.substring(inputLen-1)))
        {
            input += textBtn;
            tvInput.setText(input);
        }else if (input.equals("") && (SPECIAL_SIMBOLS.contains(textBtn) || textBtn.equals("(")))
        {
            input += textBtn;
            tvInput.setText(input);
        }else if (Pattern.matches( "\\d", input.substring(inputLen-1)))
        {
            if(textBtn.equals(")"))
            {

            }
            input += textBtn;
            tvInput.setText(input);

            val = true;
        }

        return val;
    }


    public double calculate(View v)
    {
        Button button = (Button) v;
        String str = (String)button.getText();

        if(str.equals("="))
        {

        }
        return 0f;
    }
}
