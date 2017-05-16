package com.framgia.beemusic.album.addtoalbum;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.framgia.beemusic.BeeApplication;
import com.framgia.beemusic.R;
import com.framgia.beemusic.data.model.Album;
import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.data.source.AlbumRepository;
import com.framgia.beemusic.data.source.SongAlbumRepository;
import com.framgia.beemusic.databinding.ActivityChooseAlbumBinding;
import com.framgia.beemusic.util.draganddrop.DragAndDrop;
import com.framgia.beemusic.util.draganddrop.ItemTouchHelperCallback;
import java.util.ArrayList;
import java.util.List;

public class ChooseAlbumActivity extends AppCompatActivity
        implements ChooseAlbumContract.View, DragAndDrop.OnDragListener {
    private static final String EXTRA_SONG = "EXTRA_SONG";
    private int mSpanCount;
    private ActivityChooseAlbumBinding mBinding;
    private ChooseAlbumContract.Presenter mPresenter;
    private ItemTouchHelper mItemTouchHelper;
    private ObservableField<ChooseAlbumAdapter> mAdapter = new ObservableField<>();
    private Song mSong;
    private List<Album> mAlbums = new ArrayList<>();

    public static Intent getIntent(Song song) {
        Intent intent = new Intent(BeeApplication.getInstant(), ChooseAlbumActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_SONG, song);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_choose_album);
        mSpanCount = getResources().getInteger(R.integer.span_count);
        mPresenter = new ChooseAlbumPresenter(this, AlbumRepository.getInstant(this),
                SongAlbumRepository.getInstant(this));
        mBinding.setActivity(this);
        getExtra();
        if (mPresenter != null) mPresenter.subcribe();
    }

    @Override
    public void onSuccessfullAddToAlbum() {

        Snackbar.make(findViewById(android.R.id.content), R.string.msg_successfull_add_album,
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onErrorAddToAlbum() {
        Snackbar.make(findViewById(android.R.id.content), R.string.msg_eror_add_album,
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                mPresenter.onCompleteAddToAlbum(mSong, mAlbums);
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

    @Override
    public void initRecycleview(List<Album> albums) {
        if (mPresenter == null) return;
        mAdapter.set(new ChooseAlbumAdapter(albums, mPresenter, this));
        initDragAndDrop();
    }

    @Override
    public void onAddToAlbum(Album album) {
        if (mSong == null || album == null) return;
        if (mAlbums.contains(album)) {
            mAlbums.remove(album);
            return;
        }
        mAlbums.add(album);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) mPresenter.subcribe();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPresenter == null) return;
        mPresenter.unsubcribe();
    }

    private void getExtra() {
        Intent intent = getIntent();
        if (intent == null) ;
        mSong = (Song) intent.getSerializableExtra(EXTRA_SONG);
    }

    private void initDragAndDrop() {
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(mAdapter.get());
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mBinding.recyclerView);
    }

    public int getSpanCount() {
        return mSpanCount;
    }

    public ObservableField<ChooseAlbumAdapter> getAdapter() {
        return mAdapter;
    }
}
