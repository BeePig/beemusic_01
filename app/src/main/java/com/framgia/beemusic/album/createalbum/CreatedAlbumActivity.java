package com.framgia.beemusic.album.createalbum;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.framgia.beemusic.R;
import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.data.source.AlbumRepository;
import com.framgia.beemusic.data.source.SingerRepository;
import com.framgia.beemusic.data.source.SongAlbumRepository;
import com.framgia.beemusic.data.source.SongRepository;
import com.framgia.beemusic.data.source.SongSingerRepository;
import com.framgia.beemusic.databinding.ActivityCreatedAlbumBinding;
import java.util.List;
import rx.subscriptions.CompositeSubscription;

public class CreatedAlbumActivity extends AppCompatActivity
        implements CreatedAlbumContract.View, SearchView.OnQueryTextListener {
    private final static int REQUEST_PICK_IMAGE = 1;
    private final static String NUMBER_ZERO = "0";
    private ActivityCreatedAlbumBinding mBinding;
    private CreatedAlbumContract.Presenter mPresenter;
    private ObservableField<String> mFilePath = new ObservableField<>();
    private CreatedAlbumAdapter mAdapter;
    private ObservableField<String> mNumberOfSong = new ObservableField<>(NUMBER_ZERO);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_created_album);
        mBinding.setActivity(this);
        mBinding.searchview.setOnQueryTextListener(this);
        initPresenter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                mPresenter.onCompletedCreation(mBinding.editTextNameAlbum.getText().toString(),
                        Integer.valueOf(mNumberOfSong.get()));
                break;
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_created_album, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public CreatedAlbumContract.Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void onPickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, getResources().getString(R.string.title_select_image)),
                REQUEST_PICK_IMAGE);
    }

    @Override
    public void initRecycleview(List<Song> songs, List<String> singers) {
        mAdapter = new CreatedAlbumAdapter(songs, singers, mPresenter);
        mBinding.setAdapter(mAdapter);
    }

    @Override
    public void onCheckBox(Song song) {
        mAdapter.onChecked(song);
    }

    @Override
    public void addSong() {
        int value = Integer.valueOf(mNumberOfSong.get()) + 1;
        mNumberOfSong.set(String.valueOf(value));
    }

    @Override
    public void removeSong() {
        int value = Integer.valueOf(mNumberOfSong.get()) - 1;
        mNumberOfSong.set(String.valueOf(value));
    }

    @Override
    public void onCompletedCreation() {
        onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Snackbar.make(findViewById(android.R.id.content), R.string.error_not_pick_image,
                        Snackbar.LENGTH_LONG).show();
                return;
            }
            mFilePath.set(mPresenter.getFilePathPickedImage(data.getDataString()));
        }
    }

    private void initPresenter() {
        mPresenter = new CreatedAlbumPresenter(this, AlbumRepository.getInstant(this),
                new CompositeSubscription(), SongRepository.getInstant(this),
                SingerRepository.getInstant(this), SongAlbumRepository.getInstant(this),
                SongSingerRepository.getInstant(this));
        if (mPresenter != null) mPresenter.subcribe();
    }

    public ObservableField<String> getFilePath() {
        return mFilePath;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubcribe();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unsubcribe();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (mAdapter == null) return false;
        mAdapter.onSearch(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (mAdapter == null) return false;
        mAdapter.onSearch(newText);
        return true;
    }

    public ObservableField<String> getNumberOfSong() {
        return mNumberOfSong;
    }
}
