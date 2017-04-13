package com.framgia.beemusic.album.createalbum;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.framgia.beemusic.BR;
import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.databinding.ItemCreatedAlbumAdapterBinding;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by beepi on 04/05/2017.
 */

public class CreatedAlbumAdapter extends RecyclerView.Adapter<CreatedAlbumAdapter.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private List<Song> mSongList;
    private List<String> mSingerList;
    private CreatedAlbumContract.Presenter mPresenter;
    private List<Song> mTempSongList = new ArrayList<>();
    private HashMap<Integer, Boolean> mChecked = new HashMap<>();

    CreatedAlbumAdapter(List<Song> songs, List<String> singers,

            CreatedAlbumContract.Presenter presenter) {
        mSongList = songs;
        mSingerList = singers;
        mPresenter = presenter;
        mTempSongList.addAll(mSongList);
        setChecked(mSongList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) mLayoutInflater = LayoutInflater.from(parent.getContext());
        ItemCreatedAlbumAdapterBinding binding =
                ItemCreatedAlbumAdapterBinding.inflate(mLayoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position < 0) return;
        holder.bind(position, mChecked.get(mSongList.get(position).getId()));
    }

    @Override
    public int getItemCount() {
        return mSongList == null ? 0 : mSongList.size();
    }

    public void onSearch(String nameSong) {
        List<Song> results = new ArrayList<>();
        for (Song song : mTempSongList) {
            if (song.getName().toLowerCase().contains(nameSong.toLowerCase())) results.add(song);
        }
        mSongList.clear();
        mSongList.addAll(results);
        notifyDataSetChanged();
    }

    public void onChecked(Song song) {
        if (mChecked.get(song.getId())) {
            mChecked.put(song.getId(), false);
            return;
        }
        mChecked.put(song.getId(), true);
    }

    private void setChecked(List<Song> songs) {
        if (mSongList == null) return;
        for (Song song : songs)
            mChecked.put(song.getId(), false);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemCreatedAlbumAdapterBinding mBinding;
        private BindingModel mModel;

        public ViewHolder(ItemCreatedAlbumAdapterBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(int pos, boolean isChecked) {
            Song song = mSongList.get(pos);
            if (song == null) return;
            mModel = new BindingModel(song, mSingerList.get(pos), mPresenter, isChecked, pos);
            mBinding.setModel(mModel);
            mBinding.executePendingBindings();
        }

        public class BindingModel extends BaseObservable {
            private Song mSong;
            private String mSinger;
            private CreatedAlbumContract.Presenter mBindPresenter;
            private int mPostion;
            private boolean mIsCheck;

            public BindingModel(Song song, String singer, CreatedAlbumContract.Presenter presenter,
                    boolean isCheck, int postion) {
                mSong = song;
                mSinger = singer;
                mBindPresenter = presenter;
                mIsCheck = isCheck;
                mPostion = postion;
            }

            @Bindable
            public Song getSong() {
                return mSong;
            }

            public void setSong(Song song) {
                mSong = song;
                notifyPropertyChanged(BR.song);
            }

            @Bindable
            public String getSinger() {
                return mSinger;
            }

            public void setSinger(String singer) {
                mSinger = singer;
                notifyPropertyChanged(BR.singer);
            }

            public int getPostion() {
                return mPostion;
            }

            public CreatedAlbumContract.Presenter getBindPresenter() {
                return mBindPresenter;
            }

            public void setBindPresenter(CreatedAlbumContract.Presenter bindPresenter) {
                mBindPresenter = bindPresenter;
            }

            public boolean isCheck() {
                return mIsCheck;
            }
        }
    }
}
