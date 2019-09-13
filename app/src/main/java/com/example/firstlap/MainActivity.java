package com.example.firstlap;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements View.OnClickListener , TextWatcher
{
    final String ERROR_TEXT = "Enter player name!";

    @BindView(R.id.startButton) Button startButton;
    @BindView(R.id.editText2) EditText firstPlayerName;
    @BindView(R.id.editText3) EditText secondPlayerName;
    @BindView(R.id.textView) TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        startButton.setOnClickListener(this);
        firstPlayerName.addTextChangedListener(this);
        secondPlayerName.addTextChangedListener(this);
    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.startButton:
                String firstName = firstPlayerName.getText().toString();
                String secondName = secondPlayerName.getText().toString();
                if (firstName.isEmpty() || secondName.isEmpty())
                {
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText(ERROR_TEXT);
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                intent.putExtra("firstPlayerName", firstName);
                intent.putExtra("secondPlayerName", secondName);
                startActivity(intent);
                break;

                default: throw new IllegalArgumentException("This button have no handlers");
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {

    }


    @Override
    public void afterTextChanged(Editable s)
    {

    }


    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        errorText.setVisibility(View.INVISIBLE);
    }


}
