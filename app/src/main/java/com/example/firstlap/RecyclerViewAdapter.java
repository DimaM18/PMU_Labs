package com.example.firstlap;

import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>
        implements ItemMoveCallback.ItemTouchHelperContract
{
    private ArrayList<CellItem> data;
    private final OnMoveEndListener listener;


    public RecyclerViewAdapter(ArrayList<CellItem> data, OnMoveEndListener onMoveEndListener)
    {
        this.data = data;
        listener = onMoveEndListener;
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
        }
    }

    @Override
    public void onRowClear(MyViewHolder myViewHolder)
    {
        if (myViewHolder != null)
        {
            int color = data.get(myViewHolder.getAdapterPosition()).getBackgroundColor();
            myViewHolder.rowView.setBackgroundColor(color);
        }
    }

    @Override
    public void CheckEndGame(int position)
    {
        int sameColorInColumnCount = 0;
        int sameColorInRowCount = 0;
        int startVerticalPosition = position % 4;
        int startHorizontalPosition = position - startVerticalPosition;
        int color = data.get(startVerticalPosition).getBackgroundColor();

        for (int i = startVerticalPosition, j = startHorizontalPosition; i < getItemCount(); i+=4 , j++)
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

        if (sameColorInRowCount == 4 || sameColorInColumnCount == 4)
        {
            listener.OnPlayerWin();
        }
        else
        {
            listener.OnNeedRefreshPlayerInfo();
        }
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

