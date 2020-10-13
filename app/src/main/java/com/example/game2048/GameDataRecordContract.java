package com.example.game2048;

import android.provider.BaseColumns;

public final class GameDataRecordContract {
    private GameDataRecordContract(){}

    public static class GameBoardEntity implements BaseColumns {
        public static final String TABLE_NAME = "board";
        public static final String COLUMN_NAME_TITLE = "user_id";
        public static final String COLUMN_NAME_SECOND_TITLE = "take_time";
    }

    public static class Game2048Entity implements BaseColumns {
        public static final String TABLE_NAME = "game_2048";
        public static final String COLUMN_NAME_TITLE = "user_id";
        public static final String COLUMN_NAME_SECOND_TITLE = "score";
    }
}
