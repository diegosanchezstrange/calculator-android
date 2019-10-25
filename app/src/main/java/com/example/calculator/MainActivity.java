package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.Random;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    private String input = "";
    private String ans = "";
    private TextView tvInput;
    private TextView tvResult;
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

        if(!this.addInput(button))
        {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            String[] mensajes = {"No.","No.","No.","No.","Casi.","Casi.","Casi.", "No te pases de listo.", "Venga crack.", "Va a ser que no.", "Bah.", " ", "Casi.", "¡QUE NO JODER QUE VAN 20 VECES YA DEJA DE DARLE AL BOTONCITO NO VES QUE NO FUNCIONA, A VER SI TE VOY A TENER QUE EXPLICAR COMO FUNCIONA UNA MALDITA CALCULADORA PEDAZO DE INUTIL QUE PARECES NUEVOS JODER QUE LA CALCULADORA ESTA TIENE 4 OPERACIONES Y AUN ASI TE LIAS JODER ES QUE NO PASASTE LA ESO O QUE!", "Casi figura."};
            Random r = new Random();
            Toast.makeText(this, mensajes[r.nextInt(mensajes.length)], Toast.LENGTH_SHORT).show();
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
        }

        try
        {
            String calculation = Double.toString(calculate(input));
            this.tvResult.setText(calculation);
        } catch (IllegalArgumentException e)
        {
            this.tvResult.setText("");
        } catch (ArithmeticException e)
        {
            this.tvResult.setText("");
        }
    }


    private static void addButtonEffect(ViewGroup v)
    {
        for (int i = 0; i < v.getChildCount() ; i++)
        {
           View currentChild = v.getChildAt(i);

           if( currentChild instanceof Button)
           {
                buttonEffect(currentChild);
           } else if (currentChild instanceof LinearLayout)
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
        boolean operationOrDeletion = textBtn.equals("DEL") || textBtn.contains("=");

        if ((inputLen == 30 && !operationOrDeletion) || (inputLen + this.ans.length() > 30 && textBtn.equals("ANS")))
        {
            return false;
        }

        if (!operationOrDeletion && Pattern.matches("^(-?[\\dE]*(\\.[\\d]*)?(?<=[\\d]|\\.)[*\\/^+-]?)+$", strInput + textBtn))
        {
            String[] nums = this.input.split("[+\\-/^*]");

            if (!textBtn.equals(".") || !nums[nums.length-1].contains(".") ||
                (OPERATION_SIMBOLS + "-").contains(last))
            {
                this.input += textBtn;
                this.tvInput.setText(this.input);
                return true;
            }

        } else if ((OPERATION_SIMBOLS + "-").contains(last) &&
                OPERATION_SIMBOLS.contains(textBtn) &&
                inputLen != 0)
        {
            //When input is [+*/^] and the last thing is [+-*/^]
            String lastTwo = inputLen < 2 ? "" : this.input.substring(inputLen-2);

            if (!Pattern.matches("[+/\\-^*]{2}", lastTwo) && inputLen != 1)
            {
                this.input = this.input.substring(0, inputLen-1) + textBtn;
                this.tvInput.setText(this.input);
                return true;
            }
        } else if (textBtn.equals("-"))
        {
            //When input is -
            if (inputLen == 0)
            {
                //When input is - and text is empty
                this.input += textBtn;
                this.tvInput.setText(this.input);
                return true;
            } else if ((OPERATION_SIMBOLS.contains(last) || Pattern.matches("\\d", last)))
            {
                //When input is - and the thing before is a number or [+*/^]
                this.input += textBtn;
                this.tvInput.setText(this.input);
                return true;
            }
        } else if (textBtn.equals("ANS") && !this.ans.equals("") )
        {
            //Boton ANS
            if ((OPERATION_SIMBOLS + "-").contains(last) || inputLen == 0)
            {
                if(this.ans.equals("Infinity"))
                {
                    return false;
                }
                this.input += this.ans;
                this.tvInput.setText(this.input);
                return true;
            }
        }else if (textBtn.equals("="))
        {
            //Boton EQUALS
            try
            {
                String calculation = Double.toString(calculate(input));
                this.tvInput.setText(calculation);
                this.ans = calculation;
                this.input = calculation;
                return true;
            } catch (IllegalArgumentException e)
            {
                this.tvResult.setText("");
                return false;
            } catch (ArithmeticException e)
            {
                this.tvResult.setText("");
                return false;
            }
        } else if (textBtn.equals("DEL"))
        {
            //Boton DEL
            if(this.input.equals("Infinity"))
            {
                this.input = "";
                this.tvInput.setText(this.input);
                return true;
            }
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
