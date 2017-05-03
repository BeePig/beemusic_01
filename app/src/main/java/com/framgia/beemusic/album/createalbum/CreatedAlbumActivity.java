package com.framgia.beemusic.album.createalbum;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.framgia.beemusic.R;
import com.framgia.beemusic.data.source.AlbumRepository;
import com.framgia.beemusic.databinding.ActivityCreatedAlbumBinding;

public class CreatedAlbumActivity extends AppCompatActivity implements CreatedAlbumContract.View {
    private final static int REQUEST_PICK_IMAGE = 1;
    private ActivityCreatedAlbumBinding mBinding;
    private CreatedAlbumContract.Presenter mPresenter;
    private ObservableField<String> mFilePath = new ObservableField<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCreatedAlbumBinding binding =
            DataBindingUtil.setContentView(this, R.layout.activity_created_album);
        binding.setActivity(this);
        initPresenter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                //todo create an album
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
        startActivityForResult(Intent.createChooser(intent, getResources()
            .getString(R.string.title_select_image)), REQUEST_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(this, R.string.error_not_pick_image, Toast.LENGTH_LONG).show();
                return;
            }
            mFilePath.set(mPresenter.getFilePathPickedImage(data.getDataString()));
        }
    }

    private void initPresenter() {
        mPresenter = new CreatedAlbumPresenter(this, AlbumRepository.getInstant(this));
    }

    public ObservableField<String> getFilePath() {
        return mFilePath;
    }
}
