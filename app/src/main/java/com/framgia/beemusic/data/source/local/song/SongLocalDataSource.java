package com.framgia.beemusic.data.source.local.song;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.data.source.DataSource;
import com.framgia.beemusic.data.source.local.DataHelper;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func0;

/**
 * Created by beepi on 17/02/2017.
 */
public class SongLocalDataSource extends DataHelper implements DataSource<Song> {
    private static String[] genresProjection = {
        MediaStore.Audio.Genres.NAME,
        MediaStore.Audio.Genres._ID
    };
    private static SongLocalDataSource sSongLocalDataSource;
    private ContentResolver mContentResolver;

    private SongLocalDataSource(Context context) {
        super(context);
        mContentResolver = context.getContentResolver();
    }

    public static SongLocalDataSource getInstant(Context context) {
        if (sSongLocalDataSource == null) {
            sSongLocalDataSource = new SongLocalDataSource(context);
        }
        return sSongLocalDataSource;
    }

    @Override
    public List<Song> getModel(String selection, String[] args) {
        List<Song> songs = null;
        try {
            openDatabase();
            songs = null;
            String sortOrder = SongSourceContract.SongEntry.COLUMN_NAME + " ASC";
            Cursor cursor =
                mDatabase.query(SongSourceContract.SongEntry.TABLE_SONG_NAME, null, selection, args,
                    null, null, sortOrder);
            if (cursor != null && cursor.moveToFirst()) {
                songs = new ArrayList<>();
                do {
                    songs.add(new Song(cursor));
                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabse();
        }
        return songs;
    }

    @Override
    public int save(Song song) {
        int count = -1;
        try {
            openDatabase();
            ContentValues contentValues = convertContentValueFromSong(song);
            if (contentValues == null) return -1;
            count = (int) mDatabase
                .insert(SongSourceContract.SongEntry.TABLE_SONG_NAME, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabse();
        }
        return count;
    }

    @Override
    public int update(Song song) {
        int count = -1;
        try {
            ContentValues contentValues = convertContentValueFromSong(song);
            if (contentValues == null) return count;
            openDatabase();
            mDatabase.beginTransaction();
            count = mDatabase.update(SongSourceContract.SongEntry.TABLE_SONG_NAME, contentValues,
                SongSourceContract.SongEntry.COLUMN_ID_SONG + " = ?",
                new String[]{String.valueOf(song.getId())});
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
            count = mDatabase.delete(SongSourceContract.SongEntry.TABLE_SONG_NAME,
                SongSourceContract.SongEntry.COLUMN_ID_SONG + " = ?",
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
            mDatabase.delete(SongSourceContract.SongEntry.TABLE_SONG_NAME, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabse();
        }
    }

    @Override
    public boolean checkExistModel(int id) {
        if (id == -1) return false;
        String selection = SongSourceContract.SongEntry.COLUMN_ID_SONG + " = ?";
        List<Song> songs = getModel(selection, new String[]{String.valueOf(id)});
        if (songs == null) return false;
        return true;
    }

    @Override
    public Observable<Song> getDataObservable(final List<Song> models) {
        return Observable.defer(new Func0<Observable<Song>>() {
            @Override
            public Observable<Song> call() {
                return Observable.from(models);
            }
        });
    }

    /**
     * convert {@link Song} to {@link ContentValues} object
     *
     * @param song
     * @return
     */
    private ContentValues convertContentValueFromSong(Song song) {
        if (song == null) return null;
        ContentValues contentValues = new ContentValues();
        contentValues.put(SongSourceContract.SongEntry.COLUMN_NAME, song.getName());
        contentValues.put(SongSourceContract.SongEntry.COLUMN_LINK, song.getLink());
        contentValues.put(SongSourceContract.SongEntry.COLUMN_IS_FAVORITE, song.getIsFavorite());
        contentValues.put(SongSourceContract.SongEntry.COLUMN_TYPE, song.getType());
        contentValues.put(SongSourceContract.SongEntry.COLUMN_DURATION, song.getDuration());
        contentValues.put(SongSourceContract.SongEntry.COLUMN_GENRE, song.getGenre());
        if (song.getId() > -1) {
            contentValues.put(SongSourceContract.SongEntry.COLUMN_ID_SONG, song.getId());
        }
        return contentValues;
    }
}
