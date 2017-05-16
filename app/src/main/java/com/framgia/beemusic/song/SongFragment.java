package com.framgia.beemusic.song;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.framgia.beemusic.R;
import com.framgia.beemusic.album.addtoalbum.ChooseAlbumActivity;
import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.databinding.FragmentSongBinding;

public class SongFragment extends Fragment implements SongContract.View {
    private SongContract.Presenter mPresenter;
    private FragmentSongBinding mBinding;
    private SongAdapter mAdapter;

    public SongFragment() {
    }

    public static SongFragment newInstance() {
        return new SongFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song, container, false);
        mBinding = DataBindingUtil.bind(view);
        if (mPresenter != null) mPresenter.subcribe();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setPresenter(SongContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void initRecycleview(ObservableArrayList<Song> songs,
            ObservableArrayList<String> singer) {
        mAdapter = new SongAdapter(songs, singer, mPresenter);
        mBinding.setAdapter(mAdapter);
    }

    @Override
    public void notifyItemRemove(int pos) {
        mAdapter.getSingerList().remove(pos);
        mAdapter.getSongList().remove(pos);
        mAdapter.notifyItemRemoved(pos);
    }

    @Override
    public void onAddToAnotherAlbum(Song song) {
        startActivity(ChooseAlbumActivity.getIntent(song));
    }

    @Override
    public void onSearch(String keySearch) {
        mPresenter.onSearch(keySearch);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mPresenter == null) return;
        mPresenter.unsubcribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter == null) return;
        mPresenter.unsubcribe();
    }
}
