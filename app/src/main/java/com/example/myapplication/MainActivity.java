package com.example.myapplication;

import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private TextView result;
    private double firstNum = 0, secondNum = 0;
    private char action;
    private boolean isSecondNum = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        result = findViewById(R.id.textViewResult);
        result.setText("0");
    }

    public void numsFunc(View view) {
        String btnText = ((Button) view).getText().toString();

        Set<String> validStrings = Set.of("Error", "0", "+", "-", "x", "/");
        if (validStrings.contains(result.getText().toString()) && !btnText.equals("."))
            result.setText("");

        result.append(btnText);

        setNumber(result.getText().toString());

    }

    public void actionFunc(View view) {
        char curr = ((Button) view).getText().toString().charAt(0);

        if(curr == '=') {
            try {
                firstNum = calc();
                result.setText(String.valueOf(firstNum));
            }
            catch (IllegalArgumentException e) {
                handleError("Invalid Operator");
            } catch (ArithmeticException e) {
                handleError("Cannot Divide By Zero");
            }
        }
        else { // Handle operations: +, -, /, x
            action = curr;
            isSecondNum = true;
            result.setText(String.valueOf(action));
        }
    }

    public void resetFunc(View view) { reset(); }

    public void delFunc(View view) {
        String currentText = result.getText().toString();
        Set<String> validStrings = Set.of("+", "-", "x", "/");
        if(validStrings.contains(currentText)) return;

        if (currentText.equals("Error") || currentText.isEmpty()) {
            reset();
            return;
        }

        String updatedText = currentText.substring(0, currentText.length() - 1);

        if (updatedText.isEmpty()) {
            updatedText = "0";
        }

        result.setText(updatedText);

        setNumber(updatedText);
    }

    // Error Handling
    private void handleError(String errorMessage) {
        reset();
        System.out.println("Error: " + errorMessage);
        result.setText("Error");
    }

    // Help Functions
    public double calc() {
        switch (action){
            case '+':
                return add(firstNum,secondNum);
            case '-':
                return subtract(firstNum,secondNum);
            case 'x':
                return multiply(firstNum,secondNum);
            case '/':
                return divide(firstNum,secondNum);
            default:
                throw new IllegalArgumentException("Invalid operation: " + action);
        }
    }
    public double add(double num1, double num2) {
        return num1 + num2;
    }
    public double subtract(double num1, double num2) {
        return num1 - num2;
    }
    public double multiply(double num1, double num2) {
        return num1 * num2;
    }
    public double divide(double num1, double num2) {
        if (num2 == 0) {
            throw new ArithmeticException("Division by zero is not allowed.");
        }
        return num1 / num2;
    }
    public void setNumber(String text) {
        try {
            double num = Double.parseDouble(text);
            if (isSecondNum)
                secondNum = num;
            else {
                firstNum = num;
            }
        } catch (NumberFormatException e) {
            handleError("Unknown Error");
        }
    }
    public void reset() {
        firstNum = 0;
        secondNum = 0;
        isSecondNum = false;
        result.setText("0");
    }

}