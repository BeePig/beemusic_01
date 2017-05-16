package com.framgia.beemusic.util;

import android.app.SearchManager;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import com.framgia.beemusic.BeeApplication;
import com.framgia.beemusic.R;
import com.framgia.beemusic.album.AlbumAdapter;
import com.framgia.beemusic.album.addtoalbum.ChooseAlbumActivity;
import com.framgia.beemusic.album.createalbum.CreatedAlbumActivity;
import com.framgia.beemusic.data.model.Album;
import com.framgia.beemusic.displaysong.DisplaySongActivity;
import com.framgia.beemusic.main.MainActivity;
import com.framgia.beemusic.util.draganddrop.DragAndDrop;
import java.io.File;

/**
 * Created by beepi on 27/02/2017.
 */
public class BindingAdapterUtil {
    private static int VERTICAL_ITEM_SPACE = 20;

    @BindingAdapter({ "toolbar", "activity" })
    public static void setUpDrawerListener(final DrawerLayout drawlayout, Toolbar toolbar,
            final AppCompatActivity appCompatActivity) {
        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(appCompatActivity, drawlayout, toolbar,
                        R.string.msg_open_drawer, R.string.msg_close_drawer);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawlayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @BindingAdapter({ "title", "color", "activity" })
    public static void setToolBar(Toolbar toolbar, String title, int color,
            AppCompatActivity activity) {
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(color);
        if (activity instanceof MainActivity) {
            toolbar.inflateMenu(R.menu.toolbar_main);
            initSearchView(toolbar.getMenu(), (MainActivity) activity);
            return;
        }
        if (activity instanceof CreatedAlbumActivity || activity instanceof ChooseAlbumActivity) {
            toolbar.inflateMenu(R.menu.toolbar_created_album);
            activity.setSupportActionBar(toolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar == null) return;
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private static void initSearchView(Menu menu, final MainActivity activity) {
        MenuItem itemSearch = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) itemSearch.getActionView();
        SearchManager searchManager =
                (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity.getComponentName()));
        ImageView searchIcon = (ImageView) searchView.findViewById(R.id.search_button);
        searchIcon.setImageResource(R.drawable.ic_action_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                activity.onSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                activity.onSearch(newText);
                return true;
            }
        });
    }

    @BindingAdapter("layoutManager")
    public static void setLayoutManager(RecyclerView view,
            LayoutManager.LayoutManagerFactory factory) {
        view.setLayoutManager(factory.create(view));
        view.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
    }

    @BindingAdapter("adapter")
    public static void setAdapter(RecyclerView view, RecyclerView.Adapter adapter) {
        if (adapter == null) return;
        view.setAdapter(adapter);
    }

    @BindingAdapter({ "title", "color", "stop" })
    public static void setPlaySongActionBar(Toolbar toolbar, String title, int color,
            final DisplaySongActivity activity) {
        toolbar.inflateMenu(R.menu.toolbar_display_song);
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(color);
        MenuItem menuItem = toolbar.getMenu().findItem(R.id.action_remove);
        if (menuItem == null) return;
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                activity.finish();
                return true;
            }
        });
    }

    @BindingAdapter("rotate")
    public static void setAnimation(ImageView imageView, int typeAnimation) {
        Animation animation =
                AnimationUtils.loadAnimation(BeeApplication.getInstant(), typeAnimation);
        imageView.startAnimation(animation);
    }

    @BindingAdapter("listener")
    public static void onSeekBarChangeListener(SeekBar seekBar,
            SeekBar.OnSeekBarChangeListener listener) {
        if (listener == null) return;
        seekBar.setOnSeekBarChangeListener(listener);
    }

    @BindingAdapter({ "touchListener", "holder" })
    public static void setOnTouchListener(final View view,
            final DragAndDrop.OnDragListener listener, final RecyclerView.ViewHolder holder) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEventCompat.getActionMasked(motionEvent) == MotionEvent.ACTION_DOWN) {
                    listener.onStartDrag(holder);
                }
                return false;
            }
        });
        if (holder instanceof AlbumAdapter.AlbumViewHolder) {
            final AlbumAdapter.AlbumViewHolder albumViewHolder =
                    ((AlbumAdapter.AlbumViewHolder) holder);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (albumViewHolder.isTransparent.get()) {
                        albumViewHolder.isTransparent.set(false);
                        return false;
                    }
                    albumViewHolder.isTransparent.set(true);
                    return true;
                }
            });
        }
    }

    @BindingAdapter("path")
    public static void updateUriImage(ImageView image, String pathImg) {
        if (pathImg == null) return;
        File file = new File(pathImg);
        if (!file.exists()) return;
        image.setImageURI(Uri.fromFile(file));
    }

    @BindingAdapter("image")
    public static void setImageAlbum(ImageView image, Album album) {
        if (album == null || album.getImageLink() == null) return;
        File imgFile = new File(album.getImageLink());
        if (!imgFile.exists()) return;
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        image.setImageBitmap(myBitmap);
    }
}
