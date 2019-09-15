package com.example.firstlap;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstlap.RecyclerViewAdapter.MyViewHolder;


public class ItemMoveCallback extends ItemTouchHelper.Callback
{
    final ItemTouchHelperContract mAdapter;
    final IGameEndable iGameEndable;
    private boolean mOrderChanged = false;
    private boolean canDragItems = true;
    private int newPosition = 0;
    private int oldPosition = 0;


    public ItemMoveCallback(ItemTouchHelperContract adapter, IGameEndable iGameEndable)
    {
        mAdapter = adapter;
        this.iGameEndable = iGameEndable;
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
        newPosition = target.getAdapterPosition();
        oldPosition = viewHolder.getAdapterPosition();
        if (newPosition != oldPosition)
        {
            mOrderChanged = true;
        }

        mAdapter.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }


    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState)
    {
        if (actionState == ItemTouchHelper.ACTION_STATE_IDLE && mOrderChanged)
        {
            TryEndGame();
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


    void TryEndGame()
    {
        canDragItems = false;
        iGameEndable.TryEndGame(newPosition, oldPosition);
        canDragItems = true;
        mOrderChanged = false;
    }


    public interface ItemTouchHelperContract
    {
        void onRowMoved(int fromPosition, int toPosition);
        void onRowSelected(MyViewHolder myViewHolder);
        void onRowClear(MyViewHolder myViewHolder);
    }
}
