package us.trigg.crumble;

public class Contract {

    public static final String DATABASE_NAME = "CRUMBLE";
    public static final String TABLE_USER = "User";
    public static final String TABLE_CRUMB = "Crumb";
    public static final String TABLE_DISCOVERED = "Discovered";
    public static final int DATABASE_VERSION = 1;


    public class Table_User{
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_USER_NAME = "user_name";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_FIRST_NAME = "first_name";
        public static final String COLUMN_LAST_NAME = "last_name";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + "(" +
                COLUMN_USER_NAME + " VARCHAR(20) NOT NULL" + ", " +
                COLUMN_PASSWORD + " VARCHAR(20)" + ", " +
                COLUMN_EMAIL + " VARCHAR(20)" + ", " +
                COLUMN_FIRST_NAME + " VARCHAR(20)" + ", " +
                COLUMN_LAST_NAME + " VARCHAR(20)" + ", " +
                COLUMN_USER_ID + " INT(10)" + " PRIMARY KEY AUTOINCREMENT" + ")" ;

        public static final String DELETE_TABLE_USER = "DROP TABLE IF EXISTS " + TABLE_USER;

    }

    public class Table_Crumb{
        public static final String COLUMN_CRUMB_ID = "crumb_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_CREATION_DATE = "creation_date";
        public static final String COLUMN_MESSAGE = "message";
        public static final String COLUMN_CREATOR_ID = "creator_id";
        public static final String COLUMN_TOTAL_DISCOVERED = "total_discovered";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RATINGS = "ratings";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CRUMB + "(" +
                COLUMN_CRUMB_ID + " INT(10)" + " PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LATITUDE + " VARCHAR(20" + ", " +
                COLUMN_LONGITUDE + " VARCHAR(20)" + ", " +
                COLUMN_MESSAGE + " TEXT" + ", " +
                COLUMN_TITLE + " VARCHAR(20)" + ", " +
                COLUMN_RATING + " DECIMAL(2,1)" + ", " +
                COLUMN_RATINGS + " INT(10)" + ", " +
                COLUMN_CREATION_DATE + " TIMESTAMP" + " NOT NULL, " +
                COLUMN_TOTAL_DISCOVERED + " INT(10)" + ", " +
                COLUMN_CREATOR_ID + " INT(10)" + ")";

        public static final String DELETE_TABLE_CRUMB = "DROP TABLE IF EXISTS " + TABLE_CRUMB;

    }


    public class Table_Discovered{
        public static final String COLUMN_ID = "u_id";
        public static final String COLUMN_TITLE = "c_id";


        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_DISCOVERED + "(" +
                COLUMN_ID + " INT(10)" + ", " +
                COLUMN_TITLE + " VARCHAR(20)" + ")";

        public static final String DELETE_TABLE_DISCOVERED = "DROP TABLE IF EXISTS " + TABLE_DISCOVERED;


    }


}