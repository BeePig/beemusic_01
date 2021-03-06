package com.framgia.beemusic.data.source;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.framgia.beemusic.data.source.local.DataHelper;
import com.framgia.beemusic.data.source.local.song.SongSourceContract;
import com.framgia.beemusic.data.source.local.songsinger.SongSingerSourceContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beepi on 20/02/2017.
 */
public class SongSingerRepository extends DataHelper implements DataSourceRelationship {
    private static SongSingerRepository sSongSingerRepository;

    private SongSingerRepository(Context context) {
        super(context);
    }

    public static SongSingerRepository getInstant(Context context) {
        if (sSongSingerRepository == null) {
            sSongSingerRepository = new SongSingerRepository(context);
        }
        return sSongSingerRepository;
    }

    @Override
    public List<Integer> getListId(int idSong) {
        String selection = SongSingerSourceContract.SongSingerEntry.COLUMN_ID_SONG + " = ?";
        List<Integer> idSingers = new ArrayList<>();
        int idSinger;
        Cursor cursor = getCursor(selection,
            new String[]{String.valueOf(idSong)});
        if (cursor == null || cursor.getCount() == 0) return null;
        while (cursor.moveToNext()) {
            idSinger = cursor.getInt(cursor.getColumnIndex(
                SongSingerSourceContract
                    .SongSingerEntry.COLUMN_ID_SINGER));
            idSingers.add(idSinger);
        }
        closeCursor(cursor);
        closeDatabse();
        return idSingers;
    }

    @Override
    public Cursor getCursor(String selection, String[] args) {
        Cursor cursor = null;
        try {
            openDatabase();
            String sortOrder = SongSourceContract.SongEntry.COLUMN_ID_SONG + " ASC";
            cursor = mDatabase.query(SongSingerSourceContract.SongSingerEntry
                    .TABLE_SONG_SINGER_RELATIONSHIP_NAME, null,
                selection, args,
                null, null, sortOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    @Override
    public int save(int idSong, int idSinger) {
        int count = -1;
        try {
            openDatabase();
            ContentValues contentValues = createContentValue(idSong, idSinger);
            if (contentValues == null) return -1;
            count = (int) mDatabase
                .insert(
                    SongSingerSourceContract.SongSingerEntry.TABLE_SONG_SINGER_RELATIONSHIP_NAME,
                    null,
                    contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabse();
        }
        return count;
    }

    @Override
    public int delete(String selection, String[] args) {
        int count = -1;
        try {
            openDatabase();
            count = mDatabase
                .delete(
                    SongSingerSourceContract.SongSingerEntry.TABLE_SONG_SINGER_RELATIONSHIP_NAME,
                    selection, args);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabse();
        }
        return count;
    }

    @Override
    public int delete(int idSong) {
        String selection = SongSingerSourceContract.SongSingerEntry.COLUMN_ID_SONG + " = ?";
        return delete(selection, new String[]{String.valueOf(idSong)});
    }

    @Override
    public void deleteAlls() {
        try {
            openDatabase();
            mDatabase
                .delete(
                    SongSingerSourceContract.SongSingerEntry.TABLE_SONG_SINGER_RELATIONSHIP_NAME,
                    null,
                    null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabse();
        }
    }

    @Override
    public List<Integer> getListIdSong(int idSinger) {
        String selection = SongSingerSourceContract.SongSingerEntry.COLUMN_ID_SINGER + " = ?";
        List<Integer> idSongs = new ArrayList<>();
        int idSong;
        Cursor cursor = getCursor(selection,
            new String[]{String.valueOf(idSinger)});
        if (cursor == null || cursor.getCount() == 0) return null;
        while (cursor.moveToNext()) {
            idSong = cursor.getInt(cursor.getColumnIndex(
                SongSingerSourceContract
                    .SongSingerEntry.COLUMN_ID_SONG));
            idSongs.add(idSong);
        }
        closeCursor(cursor);
        closeDatabse();
        return idSongs;
    }

    private ContentValues createContentValue(int idSong, int idSinger) {
        if (idSinger == -1 && idSong == -1) return null;
        ContentValues contentValues = new ContentValues();
        contentValues.put(SongSingerSourceContract.SongSingerEntry.COLUMN_ID_SONG, idSong);
        contentValues.put(SongSingerSourceContract.SongSingerEntry.COLUMN_ID_SINGER, idSinger);
        return contentValues;
    }
}
