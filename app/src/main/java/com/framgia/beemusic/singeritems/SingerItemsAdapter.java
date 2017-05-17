package com.framgia.beemusic.singeritems;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.databinding.ItemSingerItemAdapterBinding;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by beepi on 17/05/2017.
 */

public class SingerItemsAdapter extends RecyclerView.Adapter<SingerItemsAdapter.ViewHolder> {
    private List<Song> mSongs;
    private SingerItemsContract.Presenter mPresenter;
    private LayoutInflater mLayoutInflater;
    private List<Song> mTempSongs = new ArrayList<>();

    public SingerItemsAdapter(List<Song> songs, SingerItemsContract.Presenter presenter) {
        mSongs = songs;
        mPresenter = presenter;
        mTempSongs.addAll(mSongs);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) mLayoutInflater = LayoutInflater.from(parent.getContext());
        ItemSingerItemAdapterBinding binding =
                ItemSingerItemAdapterBinding.inflate(mLayoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mSongs == null ? 0 : mSongs.size();
    }

    public void onSearch(String nameSong) {
        List<Song> results = new ArrayList<>();
        for (Song song : mTempSongs) {
            if (song.getName().toLowerCase().contains(nameSong.toLowerCase())) results.add(song);
        }
        mSongs.clear();
        mSongs.addAll(results);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemSingerItemAdapterBinding mBinding;
        private Song mSong;

        public ViewHolder(ItemSingerItemAdapterBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        private void bind(int pos) {
            mSong = mSongs.get(pos);
            if (mSong == null) return;
            mBinding.setHolder(this);
            mBinding.setPresenter(mPresenter);
            mBinding.executePendingBindings();
        }

        public Song getSong() {
            return mSong;
        }
    }
}
