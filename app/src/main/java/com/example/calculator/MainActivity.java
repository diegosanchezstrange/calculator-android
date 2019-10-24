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
        String last = inputLen == 0 ? "" : this.input.substring(inputLen-1);

        if (Pattern.matches("^(-?[\\dE]*(\\.[\\d]*)?(?<=[\\d]|\\.)[*\\/^+-]?)+$", strInput + textBtn))
        {
            String[] nums = this.input.split("[+\\-/^*]");

            if(!textBtn.equals(".") || !nums[nums.length-1].contains(".") ||
                (OPERATION_SIMBOLS + "-").contains(last))
            {
                this.input += textBtn;
                this.tvInput.setText(this.input);
                return true;
            }

        }else if((OPERATION_SIMBOLS + "-").contains(inputLen == 0 ? "" : this.input.substring(inputLen-1)) &&
                OPERATION_SIMBOLS.contains(textBtn) &&
                inputLen != 0)
        {
            //When input is [+*/^] and the last thing is [+-*/^]
            String lastTwo = inputLen == 0 ? "" : this.input.substring(inputLen-2);

            if(!Pattern.matches("[+/\\-^*]{2}", lastTwo))
            {
                this.input = this.input.substring(0, inputLen-1) + textBtn;
                this.tvInput.setText(this.input);
                return true;
            }
        }else if(textBtn.equals("-"))
        {
            //When input is -
            if(inputLen == 0)
            {
                //When input is - and text is empty
                this.input += textBtn;
                this.tvInput.setText(this.input);
                return true;
            }else if((OPERATION_SIMBOLS.contains(last) || Pattern.matches("\\d", last)))
            {
                //When input is - and the thing before is a number or [+*/^]
                this.input += textBtn;
                this.tvInput.setText(this.input);
                return true;
            }
        }else if(textBtn.equals("ANS") && !this.ans.equals("") )
        {
            if((OPERATION_SIMBOLS + "-").contains(last) || inputLen == 0)
            {
                this.input += this.ans;
                this.tvInput.setText(this.input);
                return true;
            }
        }else if(textBtn.equals("="))
        {
            try
            {
                String calculation = Double.toString(calculate(input));
                this.tvInput.setText(calculation);
                this.ans = calculation;
                this.input = calculation;
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
