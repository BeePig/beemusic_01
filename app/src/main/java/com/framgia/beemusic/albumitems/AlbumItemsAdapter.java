package com.framgia.beemusic.albumitems;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.databinding.ItemAlbumItemAdapterBinding;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by beepi on 17/05/2017.
 */

public class AlbumItemsAdapter extends RecyclerView.Adapter<AlbumItemsAdapter.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private List<Song> mSongs;
    private List<String> mSingers;
    private AlbumItemsContract.Presenter mPresenter;
    private List<Song> mTempSongs = new ArrayList<>();

    public AlbumItemsAdapter(List<Song> songs, List<String> singers,
            AlbumItemsContract.Presenter presenter) {
        mSongs = songs;
        mSingers = singers;
        mPresenter = presenter;
        mTempSongs.addAll(mSongs);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) mLayoutInflater = LayoutInflater.from(parent.getContext());
        ItemAlbumItemAdapterBinding binding =
                ItemAlbumItemAdapterBinding.inflate(mLayoutInflater, parent, false);
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

    public void onSearch(String keySong) {
        List<Song> results = new ArrayList<>();
        for (Song song : mTempSongs) {
            if (song.getName().toLowerCase().contains(keySong.toLowerCase())) results.add(song);
        }
        mSongs.clear();
        mSongs.addAll(results);
        notifyDataSetChanged();
    }

    public List<Song> getSongs() {
        return mSongs;
    }

    public List<String> getSingers() {
        return mSingers;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemAlbumItemAdapterBinding mBinding;
        private Song mSong;
        private String mSinger;

        public ViewHolder(ItemAlbumItemAdapterBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        private void bind(int pos) {
            mSong = mSongs.get(pos);
            if (mSong == null) return;
            mSinger = mSingers.get(pos);
            mBinding.setHolder(this);
            mBinding.setPresenter(mPresenter);
            mBinding.executePendingBindings();
        }

        public Song getSong() {
            return mSong;
        }

        public String getSinger() {
            return mSinger;
        }
    }
}
