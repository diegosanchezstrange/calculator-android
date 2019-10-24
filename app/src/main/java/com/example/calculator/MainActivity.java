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

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    private String input = "";
    private TextView tvInput;
    private TextView tvResult;
    private String ans = "";
    private final String OPERATION_SIMBOLS = "/*+^";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.tvInput = findViewById(R.id.textViewInput);
        this.tvResult  = findViewById(R.id.textViewResult);

        addButtonEffect((ViewGroup) this.findViewById(R.id.linearLayout));

        this.findViewById(R.id.buttonDel).setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v) {
                tvInput.setText("");
                tvResult.setText("");
                input = "";
                return true;
            }
        });
    }


    @Override
    public void onClick(View v)
    {
        Button button = (Button)v;

        this.addInput(button);

        try
        {
            String calculation = Double.toString(calculate(input));
            this.tvResult.setText(calculation);
        }catch(IllegalArgumentException e)
        {
            this.tvResult.setText("");
        }catch(ArithmeticException e)
        {
            this.tvResult.setText("");
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


    public boolean addInput(View v)
    {
        String strInput = (String) this.tvInput.getText();
        Button button = (Button)v;
        String textBtn = (String) button.getText();
        int inputLen = this.input.length();

        if (Pattern.matches("^(-?\\d*(\\.\\d*)?(?<=\\d|\\.)[*\\/^+-]?)+$", strInput + textBtn))
        {
            this.input += textBtn;
            this.tvInput.setText(this.input);
            return true;
        }else if((OPERATION_SIMBOLS + "-").contains(inputLen == 0 ? "" : this.input.substring(inputLen-1)) &&
                OPERATION_SIMBOLS.contains(textBtn) &&
                inputLen != 0)
        {
            this.input = this.input.substring(0, inputLen-1) + textBtn;
            this.tvInput.setText(this.input);
            return true;
        }else if(textBtn.equals("-"))
        {
            String last = inputLen == 0 ? "" : this.input.substring(inputLen-1);
            if(inputLen == 0)
            {
                this.input += textBtn;
                this.tvInput.setText(this.input);
                return true;
            }else if(OPERATION_SIMBOLS.contains(last) || Pattern.matches("\\d", last))
            {
                this.input += textBtn;
                this.tvInput.setText(this.input);
                return true;
            }
        }else if(textBtn.equals("ANS") && !this.ans.equals("") )
        {
            this.input += this.ans;
            this.tvInput.setText(this.input);
            return true;
        }else if(textBtn.equals("="))
        {
            try
            {
                String calculation = Double.toString(calculate(input));
                this.tvInput.setText(calculation);
                this.ans = calculation;
                this.input = "";
            }catch(IllegalArgumentException e)
            {
                this.tvResult.setText("");
            }catch(ArithmeticException e)
            {
                this.tvResult.setText("");
            }
        }else if (textBtn.equals("DEL"))
        {
            //Boton DEL
            this.input = this.input.length() == 0 ? "" : this.input.substring(0, inputLen - 1);
            this.tvInput.setText(this.input);
            return true;
        }
        return false;
    }


    public double calculate(String string) throws ArithmeticException, IllegalArgumentException
    {
        Expression calc = new ExpressionBuilder(string).build();
        return calc.evaluate();
    }
}
