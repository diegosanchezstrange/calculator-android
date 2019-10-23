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
    private final String ALL_SIMBOLS = "/*+=-.()";
    private final String OPERATION_SIMBOLS = "/*+-";
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
        TextView tvResult = this.findViewById(R.id.textViewResult);
        Button button = (Button)v;

        if(validateInput(button))
        {
            if (button.getText().equals("="))
                tvResult.setText("Caca pa ti");
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
        TextView tvInput = this.findViewById(R.id.textViewInput);
        String strInput = (String) tvInput.getText();
        Button button = (Button)v;
        String textBtn = (String) button.getText();
        int inputLen = input.length();

        if (Pattern.matches( "\\d", textBtn))
        {
            //Numeros
            input += textBtn;
            tvInput.setText(input);
            val = true;
        } else if (Pattern.matches("(^([-]?(\\d+)|(\\d+[-+*\\/]\\d+)+([-+*\\/]\\d?)?)+[-+*\\/]?$)*", strInput + textBtn))
        {
            input += textBtn;
            tvInput.setText(input);
            val = true;
        } else if (textBtn.equals("DEL"))
        {
            //Boton DEL
            input = input.length() == 0 ? "" : input.substring(0, inputLen - 1);
            tvInput.setText(input);
            val = true;
        } else if (textBtn.equals("="))
        {
            calculate(input);
        }


        /*

        }else if (OPERATION_SIMBOLS.contains(input.equals("") ? "" : input.substring(inputLen-1)) &&


                    input.length()>1 &&
                    OPERATION_SIMBOLS.contains(textBtn))
        {
            if(textBtn.equals("-") && !input.substring(inputLen-1).equals("-"))
            {
                input += textBtn;
                tvInput.setText(input);
                val = true;
            }else if(input.substring(inputLen-1).equals("-"))
            {
                input = input.substring(0, inputLen-2) + textBtn;
                tvInput.setText(input);
                val = true;
            }else
            {
                input = input.substring(0, inputLen-1) + textBtn;
                tvInput.setText(input);
                val = true;
            }

        }else if (OPERATION_SIMBOLS.contains(textBtn) &&
                    !OPERATION_SIMBOLS.contains(input.equals("") ? "" : input.substring(inputLen-1)) &&
                    !SPECIAL_SIMBOLS.contains(input.equals("") ? "" : input.substring(inputLen-1)) &&
                    !"(".equals(input.equals("") ? "" : input.substring(inputLen-1)))
        {
            //Operadores + * / =
            input += textBtn;
            tvInput.setText(input);
            val = true;
        }else if (input.equals("") && (SPECIAL_SIMBOLS.contains(textBtn) || textBtn.equals("(")))
        {
            input += textBtn;
            tvInput.setText(input);
            val = true;
        }else if (Pattern.matches( "[\\d+\\-/*]", input.equals("") ? "" : input.substring(inputLen-1)) &&
                    EVEN_MORE_SPECIAL_SIMBOLS.contains(textBtn))
        {
            if(textBtn.equals(")"))
            {

            }
            input += textBtn;
            tvInput.setText(input);

            val = true;
        }
        */

        return val;
    }


    public double calculate(String string)
    {
        Expression calc = new ExpressionBuilder(string).build();
        return calc.evaluate();
    }
}
