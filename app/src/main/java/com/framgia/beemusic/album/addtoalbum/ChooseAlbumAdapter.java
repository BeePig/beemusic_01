package com.framgia.beemusic.album.addtoalbum;

import android.databinding.ObservableField;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.framgia.beemusic.data.model.Album;
import com.framgia.beemusic.databinding.ItemChooseAlbumBinding;
import com.framgia.beemusic.util.draganddrop.DragAndDrop;
import com.framgia.beemusic.util.draganddrop.ItemTouchHelperAdapter;
import java.util.Collections;
import java.util.List;

/**
 * Created by beepi on 16/05/2017.
 */

public class ChooseAlbumAdapter extends RecyclerView.Adapter<ChooseAlbumAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {
    private List<Album> mAlbums;
    private LayoutInflater mLayoutInflater;
    private ChooseAlbumContract.Presenter mPresenter;
    private DragAndDrop.OnDragListener mOnDragListener;

    public ChooseAlbumAdapter(List<Album> albums, ChooseAlbumContract.Presenter presenter,
            DragAndDrop.OnDragListener listener) {
        mAlbums = albums;
        mPresenter = presenter;
        mOnDragListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) mLayoutInflater = LayoutInflater.from(parent.getContext());
        ItemChooseAlbumBinding binding =
                ItemChooseAlbumBinding.inflate(mLayoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mAlbums == null ? 0 : mAlbums.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < 0
                || fromPosition >= mAlbums.size() && toPosition < 0
                || toPosition >= mAlbums.size()) {
            return false;
        }

        Collections.swap(mAlbums, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemSwipe(int position) {

    }

    public List<Album> getAlbums() {
        return mAlbums;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemChooseAlbumBinding mBinding;
        private Album mAlbum;
        private ObservableField<Boolean> mIsChecked = new ObservableField<>(false);

        public ViewHolder(ItemChooseAlbumBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        private void bind(int pos) {
            mAlbum = mAlbums.get(pos);
            if (mAlbum == null) return;
            mBinding.setHolder(this);
            mBinding.setListener(mOnDragListener);
            mBinding.setPresenter(mPresenter);
            mBinding.executePendingBindings();
        }

        public Album getAlbum() {
            return mAlbum;
        }

        public ObservableField<Boolean> getIsChecked() {
            return mIsChecked;
        }
    }
}
