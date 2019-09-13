package com.example.firstlap;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GameActivity extends AppCompatActivity implements RecyclerViewAdapter.OnMoveEndListener, View.OnClickListener
{
    final String GAME_IN_PROCESS_TEXT = "Current player - ";
    final String WIN_TEXT = "Win player - ";

    @BindView(R.id.RestartButton) Button restartButton;
    @BindView(R.id.CurrentPlayerTextView) TextView currentPlayerTextView;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    RecyclerViewAdapter mAdapter;
    ItemTouchHelper touchHelper;

    ArrayList<CellItem> dataArray = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    boolean isSecondPlayerMove = false;
    int span;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        names.add(getIntent().getStringExtra("firstPlayerName"));
        names.add(getIntent().getStringExtra("secondPlayerName"));
        span = getIntent().getIntExtra("boardSize", 4);

        InitGameplay();
    }

    private void InitGameplay()
    {
        currentPlayerTextView.setText(GAME_IN_PROCESS_TEXT + names.get(GetCurrentPlayerIndex()));
        restartButton.setOnClickListener(this);
        restartButton.setVisibility(View.INVISIBLE);

        InitCells();
        mAdapter = new RecyclerViewAdapter(dataArray,this);
        recyclerView.setAdapter(mAdapter);

        final GridLayoutManager layoutManager1 = new GridLayoutManager(getApplicationContext(), span);
        recyclerView.setLayoutManager(layoutManager1);

        ItemTouchHelper.Callback callback = new ItemMoveCallback(mAdapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }


    static int getRandomNumberInts(int min, int max){
        Random random = new Random();
        return random.ints(min,(max+1)).findFirst().getAsInt();
    }

    void InitCells()
    {
        ArrayList<Integer> colors = new ArrayList<Integer>()
        {
            {
                add(Color.BLUE);
                add(Color.RED);
                add(Color.GREEN);
            }
        };

        int count = span;
        for (int i = 0; i < count; i++)
        {
            for (int j = 0 ; j < count; j++)
            {
                int colorPos = getRandomNumberInts(0, (colors.size() - 1));
                if (!dataArray.isEmpty())
                {
                if (IsTwoItemsWithSameColorRow(dataArray.size(), colors.get(colorPos)) ||
                        IsTwoItemsWithSameColorColumn(dataArray.size(), colors.get(colorPos)))
                {
                    int color = colors.get(colorPos);
                    while (color == colors.get(colorPos))
                    {
                        colorPos = getRandomNumberInts(0, (colors.size() - 1));
                    }
                }
                }
                dataArray.add(new CellItem(GetColorName(colors.get(colorPos)), colors.get(colorPos)));
            }
        }
    }

    boolean IsTwoItemsWithSameColorRow(int lastPos, int color)
    {
        int startPost = lastPos - lastPos % 4;
        int count = 0;
        int endPos = startPost + 4 >= dataArray.size()? dataArray.size() : 4;
        for (int i = startPost; i < endPos; i++)
        {
            CellItem cell = dataArray.get(i);
            if (cell.getBackgroundColor() == color)
            {
                count++;
            }
        }

        if (count == 2)
        {
            return true;
        }

        return false;
    }


    boolean IsTwoItemsWithSameColorColumn(int lastPos, int color)
    {
        int startPost = lastPos % 4;
        int count = 0;
        for (int i = startPost; i < dataArray.size(); i+=4)
        {
            CellItem cell = dataArray.get(i);
            if (cell.getBackgroundColor() == color)
            {
                count++;
            }
        }

        if (count == 2)
        {
            return true;
        }

        return false;
    }

    String GetColorName(int color)
    {
        switch (color)
        {
            case Color.BLUE : return "Blue";
            case Color.RED : return "Red";
            case Color.GREEN: return "Green";

            default: throw new IllegalArgumentException();
        }
    }


    int GetCurrentPlayerIndex()
    {
        return isSecondPlayerMove ? 1 : 0;
    }


    @Override
    public void onClick(View view)
    {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }


    @Override
    public void OnPlayerWin()
    {
        restartButton.setVisibility(View.VISIBLE);
        recyclerView.setEnabled(false);
        recyclerView.setLayoutFrozen(true);
        currentPlayerTextView.setText(WIN_TEXT + names.get(GetCurrentPlayerIndex()));
    }


    @Override
    public void OnNeedRefreshPlayerInfo()
    {
        isSecondPlayerMove = !isSecondPlayerMove;
        currentPlayerTextView.setText(GAME_IN_PROCESS_TEXT + names.get(GetCurrentPlayerIndex()));
    }
}
