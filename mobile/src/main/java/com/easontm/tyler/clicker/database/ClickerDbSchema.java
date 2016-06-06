package com.easontm.tyler.clicker.database;

/**
 * Created by drink on 6/5/2016.
 */
public class ClickerDbSchema {

    public static final class ClickerTable {
        public static final String NAME = "clickers";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String COUNT = "count";
            public static final String GOAL = "goal";

        }
    }

    public static final class ClickTable {
        public static final String NAME = "clicks";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String PARENT_ID = "parent_id";
            public static final String TIMESTAMP = "timestamp";
            public static final String LOCATION = "location";

        }
    }


}
