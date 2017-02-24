package com.framgia.beemusic.data.source.local.album;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.framgia.beemusic.data.model.Album;
import com.framgia.beemusic.data.source.DataSource;
import com.framgia.beemusic.data.source.local.DataHelper;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func0;

/**
 * Created by beepi on 19/02/2017.
 */
public class AlbumLocalDataSource extends DataHelper implements DataSource<Album> {
    private static AlbumLocalDataSource sAlbumLocalHandler;

    private AlbumLocalDataSource(Context context) {
        super(context);
    }

    public static AlbumLocalDataSource getInstant(Context context) {
        if (sAlbumLocalHandler == null) {
            sAlbumLocalHandler = new AlbumLocalDataSource(context);
        }
        return sAlbumLocalHandler;
    }

    @Override
    public List<Album> getModel(String selection, String[] args) {
        List<Album> albums = null;
        try {
            openDatabase();
            albums = null;
            String sortOrder = AlbumSourceContract.AlbumEntry.COLUMN_NAME + " ASC";
            Cursor cursor =
                mDatabase
                    .query(AlbumSourceContract.AlbumEntry.TABLE_ALBUM_NAME, null, selection, args,
                        null, null, sortOrder);
            if (cursor != null && cursor.moveToFirst()) {
                albums = new ArrayList<>();
                do {
                    albums.add(new Album(cursor));
                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabse();
        }
        return albums;
    }

    @Override
    public int save(Album model) {
        int count = -1;
        try {
            openDatabase();
            ContentValues contentValues = convertFromAlbum(model);
            if (contentValues == null) return -1;
            count = (int) mDatabase
                .insert(AlbumSourceContract.AlbumEntry.TABLE_ALBUM_NAME, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabse();
        }
        return count;
    }

    @Override
    public int update(Album model) {
        int count = -1;
        try {
            ContentValues contentValues = convertFromAlbum(model);
            if (contentValues == null) return count;
            openDatabase();
            mDatabase.beginTransaction();
            count = mDatabase.update(AlbumSourceContract.AlbumEntry.TABLE_ALBUM_NAME, contentValues,
                AlbumSourceContract.AlbumEntry.COLUMN_ID_ALBUM + " = ?",
                new String[]{String.valueOf(model.getId())});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabse();
        }
        return count;
    }

    @Override
    public int delete(int id) {
        int count = -1;
        try {
            openDatabase();
            count = mDatabase.delete(AlbumSourceContract.AlbumEntry.TABLE_ALBUM_NAME,
                AlbumSourceContract.AlbumEntry.COLUMN_ID_ALBUM + " = ?",
                new String[]{String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabse();
        }
        return count;
    }

    @Override
    public void deleteAlls() {
        try {
            openDatabase();
            mDatabase.delete(AlbumSourceContract.AlbumEntry.TABLE_ALBUM_NAME, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabse();
        }
    }

    @Override
    public boolean checkExistModel(int id) {
        if (id == -1) return false;
        String selection = AlbumSourceContract.AlbumEntry.COLUMN_ID_ALBUM + " = ?";
        List<Album> albums = getModel(selection, new String[]{String.valueOf(id)});
        if (albums == null) return false;
        return true;
    }

    @Override
    public Observable<Album> getDataObservable(final List<Album> models) {
        return Observable.defer(new Func0<Observable<Album>>() {
            @Override
            public Observable<Album> call() {
                return Observable.from(models);
            }
        });
    }

    private ContentValues convertFromAlbum(Album album) {
        if (album == null) return null;
        ContentValues contentValues = new ContentValues();
        contentValues.put(AlbumSourceContract.AlbumEntry.COLUMN_NAME, album.getName());
        contentValues.put(AlbumSourceContract.AlbumEntry.COLUMN_IMAGE_LINK, album.getImageLink());
        contentValues.put(AlbumSourceContract.AlbumEntry.COLUMN_COUNT, album.getCount());
        if (album.getId() > -1) {
            contentValues.put(AlbumSourceContract.AlbumEntry.COLUMN_ID_ALBUM, album.getId());
        }
        return contentValues;
    }
}
