package com.example.firstlap;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstlap.RecyclerViewAdapter.MyViewHolder;


public class ItemMoveCallback extends ItemTouchHelper.Callback
{
    final ItemTouchHelperContract mAdapter;
    private boolean mOrderChanged = false;
    private boolean canDragItems = true;
    private int currentPosition = 0;


    public ItemMoveCallback(ItemTouchHelperContract adapter)
    {
        mAdapter = adapter;
    }


    @Override
    public boolean isLongPressDragEnabled()
    {
        return canDragItems;
    }

    @Override
    public boolean isItemViewSwipeEnabled()
    {
        return false;
    }



    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) { }


    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
    {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
    {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
        if (myViewHolder.getAdapterPosition() != myViewHolder.getOldPosition())
        {
            mOrderChanged = true;
            currentPosition = target.getAdapterPosition();
        }

        mAdapter.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }


    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState)
    {
        if (actionState == ItemTouchHelper.ACTION_STATE_IDLE && mOrderChanged)
        {
            canDragItems = false;
            checkEndGame(currentPosition);
            mOrderChanged = false;
        }
        else if (actionState == ItemTouchHelper.ACTION_STATE_DRAG)
        {
            RecyclerViewAdapter.MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
            mAdapter.onRowSelected(myViewHolder);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }


    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
    {
        super.clearView(recyclerView, viewHolder);

        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
        mAdapter.onRowClear(myViewHolder);
    }


    void checkEndGame(int fromPosition)
    {
        mAdapter.CheckEndGame(fromPosition);
        canDragItems = true;
    }


    public interface ItemTouchHelperContract
    {
        void onRowMoved(int fromPosition, int toPosition);
        void onRowSelected(MyViewHolder myViewHolder);
        void onRowClear(MyViewHolder myViewHolder);
        void CheckEndGame(int fromPosition);
    }
}
