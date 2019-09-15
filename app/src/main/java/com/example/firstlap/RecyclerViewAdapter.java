package com.example.firstlap;

import android.annotation.SuppressLint;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


interface IGameEndable
{
    void TryEndGame(int newPosition, int oldPosition);
}


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>
        implements ItemMoveCallback.ItemTouchHelperContract, IGameEndable
{
    private ArrayList<CellItem> data;
    private final OnMoveEndListener listener;
    private int step = 0;

    public RecyclerViewAdapter(ArrayList<CellItem> data, OnMoveEndListener onMoveEndListener, int spanCount)
    {
        this.data = data;
        listener = onMoveEndListener;
        step = spanCount;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position)
    {
        holder.mTitle.setText(data.get(position).getText());
        holder.rowView.setBackgroundColor(data.get(position).getBackgroundColor());
    }


    @Override
    public int getItemCount()
    {
        return data.size();
    }


    @Override
    public void onRowMoved(int fromPosition, int toPosition)
    {
        if (fromPosition < toPosition)
        {
            for (int i = fromPosition; i < toPosition; i++)
            {
                Collections.swap(data, i, i + 1);
            }
        }
        else
        {
            for (int i = fromPosition; i > toPosition; i--)
            {
                Collections.swap(data, i, i - 1);
            }
        }

        notifyItemMoved(fromPosition, toPosition);
    }


    @Override
    public void onRowSelected(MyViewHolder myViewHolder)
    {
        if (myViewHolder != null)
        {
            myViewHolder.rowView.setBackgroundColor(Color.GRAY);
            myViewHolder.rowView.setAlpha(0.5f);
        }
    }

    @Override
    public void onRowClear(MyViewHolder myViewHolder)
    {
        if (myViewHolder != null)
        {
            int color = data.get(myViewHolder.getAdapterPosition()).getBackgroundColor();
            myViewHolder.rowView.setBackgroundColor(color);
            myViewHolder.rowView.setAlpha(1f);
        }
    }


    @Override
    public void TryEndGame(int newPosition, int oldPosition)
    {
        AbstractMap.SimpleEntry newPositionResults = IsContainItemsWithSameColorInRowOrColumn(newPosition);
        int firstValue = (int) newPositionResults.getValue();
        int secondValue = (int) newPositionResults.getKey();

        if (firstValue == step || secondValue == step)
        {
            listener.OnPlayerWin();
            return;
        }

        newPositionResults = IsContainItemsWithSameColorInRowOrColumn(oldPosition);
        firstValue = (int) newPositionResults.getValue();
        secondValue = (int) newPositionResults.getKey();

        if (firstValue == step || secondValue == step)
        {
            listener.OnPlayerWin();
        }
        else
        {
            listener.OnNeedRefreshPlayerInfo();
        }
    }

    AbstractMap.SimpleEntry IsContainItemsWithSameColorInRowOrColumn(int startPosition)
    {
        int sameColorInColumnCount = 0;
        int sameColorInRowCount = 0;
        int startVerticalPosition = startPosition % step;
        int startHorizontalPosition = startPosition - startVerticalPosition;
        int color = data.get(startPosition).getBackgroundColor();

        for (int i = startVerticalPosition, j = startHorizontalPosition; i < getItemCount(); i+= step , j++)
        {
            CellItem verticalCell = data.get(i);
            if (verticalCell.getBackgroundColor() == color)
            {
                sameColorInColumnCount++;
            }

            CellItem horizontalCell = data.get(j);
            if (horizontalCell.getBackgroundColor() == color)
            {
                sameColorInRowCount++;
            }
        }

        return new AbstractMap.SimpleEntry(sameColorInColumnCount, sameColorInRowCount);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView mTitle;
        View rowView;

        public MyViewHolder(View itemView)
        {
            super(itemView);

            rowView = itemView;
            mTitle = itemView.findViewById(R.id.txtTitle);
        }
    }

    public interface OnMoveEndListener
    {
        void OnPlayerWin();
        void OnNeedRefreshPlayerInfo();
    }
}

