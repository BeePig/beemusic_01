package com.framgia.beemusic.data.source.local.songalbum;

import com.framgia.beemusic.data.source.local.BaseColumn;

/**
 * Created by beepi on 28/02/2017.
 */
public class SongAlbumSourceContract {
    private SongAlbumSourceContract() {
    }

    public static final class SongAlbumEntry extends BaseColumn {
        public static final String TABLE_SONG_ALBUM_RELATIONSHIP_NAME = "Song_album_relationship";
    }
}
