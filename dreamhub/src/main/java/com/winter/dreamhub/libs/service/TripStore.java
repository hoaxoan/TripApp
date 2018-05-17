package com.winter.dreamhub.libs.service;

import android.content.ContentValues;
import android.content.Context;
import android.content.Entity;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

/**
 * Created by hoaxoan on 8/11/2017.
 */

public class TripStore {
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://com.winter.dreamhub.TripContentProvider/");
    public static final Uri TRIPS_URI = Uri.parse(String.valueOf(BASE_CONTENT_URI) +  String.valueOf("trips"));


    private static final String[] EVENT_COUNT_PROJECTION = {"IFNULL(COUNT(_id), 0) AS _count"};
    private static final String[] EVENT_PROJECTION = new String[]{"_id", "title", "backgroundImageUrl", "colorId", "start", "end", "etag", "updated"};

    private Context mContext;
    private final SQLiteDatabase mDatabase;
    private static TripStore sStore;
    private static final Object STORE_HOLDER_LOCK = new Object();

    public TripStore(Context context) {
        this.mDatabase = new TripStore.DatabaseHelper(context).getWritableDatabase();
        this.mContext = context;
    }

    public static TripStore getInstance() {
        if (sStore == null) {
            throw new IllegalStateException("You have to call initialize() first");
        }
        return sStore;
    }

    public static void initialize(Context context) {
        synchronized (STORE_HOLDER_LOCK) {
            if (sStore == null) {
                sStore = new TripStore(context);
            }
        }
    }

    private ContentValues validateAndGetEventValues(Entity entity) {
        ContentValues values = entity.getEntityValues();
        if (values.containsKey("_id")) {
            throw new IllegalArgumentException("Entity contains forbidden column: _id.");
        }
        return values;
    }

    private Entity getEventEntityFromCursor(Cursor cursor) {
        ContentValues values = new ContentValues(cursor.getColumnCount());
        DatabaseUtils.cursorRowToContentValues(cursor, values);
        return new Entity(values);
    }

    public void beginTransaction() {
        this.mDatabase.beginTransaction();
    }

    public void endTransaction() {
        this.mDatabase.endTransaction();
    }

    public void setTransactionSuccessful() {
        this.mDatabase.setTransactionSuccessful();
    }

    public int countEvents(String selection, String[] selectArguments) {
        Cursor cursor = this.mDatabase.query("events", EVENT_COUNT_PROJECTION, selection, selectArguments, null, null, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public Entity getEvent(String id) {
        Cursor cursor = this.mDatabase.query("events", EVENT_PROJECTION, "_id= ?", new String[]{id}, null, null, null);
        if (cursor.getCount() == 0) {
            cursor.close();
            return null;
        }

        cursor.moveToFirst();
        Entity entity = getEventEntityFromCursor(cursor);
        cursor.close();
        return entity;
    }

    public Entity[] queryEvents(String selection, String[] selectionArgs) {
        Cursor cursor = this.mDatabase.query("events", EVENT_PROJECTION, selection, selectionArgs, null, null, null);
        Entity[] entities = new Entity[cursor.getCount()];
        int i = 0;
        while (i < entities.length) {
            cursor.moveToPosition(i);
            entities[i] = getEventEntityFromCursor(cursor);
            i += 1;
        }
        cursor.close();
        return entities;
    }

    public Entity[] queryAllEvents() {
        Cursor cursor = this.mDatabase.query("events", EVENT_PROJECTION, null, null, null, null, null);
        Entity[] entities = new Entity[cursor.getCount()];
        int i = 0;
        while (i < entities.length) {
            cursor.moveToPosition(i);
            entities[i] = getEventEntityFromCursor(cursor);
            i += 1;
        }
        cursor.close();
        return entities;
    }

    public long insertEvent(Entity entity) {
        ContentValues values = validateAndGetEventValues(entity);
        return this.mDatabase.insertWithOnConflict("events", null, values, 5);
    }

    public int updateEvent(Entity entity) {
        ContentValues values = validateAndGetEventValues(entity);
        return this.mDatabase.updateWithOnConflict("events", values, "_id= ?", new String[]{entity.getEntityValues().getAsString("_id")}, 5);
    }

    public int deleteEvent(String eventId) {
        return this.mDatabase.delete("events", "_id=?", new String[]{eventId});
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "tripdata.db";
        private static final int DATABASE_VERSION = 1;
        private final Context mContext;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.mContext = context;
        }

        private static void createTable(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE Trips (_id TEXT NOT NULL PRIMARY KEY, TripName TEXT, StartDate INTEGER, EndDate INTEGER, PhotoData BLOB, TripStatus INTEGER DEFAULT 0, LastUpdateTimestamp INTEGER DEFAULT 0, TripProto BLOB NOT NULL);");
            db.execSQL("CREATE TABLE Moods (Id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, MoodId TEXT NOT NULL UNIQUE, MoodProto BLOB NOT NULL);");
            db.execSQL("CREATE TABLE Destinations (TripId TEXT NOT NULL, DestinationId TEXT NOT NULL, DestinationStatus INTEGER DEFAULT 0, LastUpdateTimestamp INTEGER DEFAULT 0, PRIMARY KEY (TripId, DestinationId) FOREIGN KEY (TripId) REFERENCES Trips(_id) ON DELETE CASCADE);");
            db.execSQL("CREATE TABLE EntityData (Mid TEXT NOT NULL PRIMARY KEY, EntityProto BLOB NOT NULL, RatingHistogram BLOB,IsAttraction INTEGER DEFAULT 1, DetailLevel INTEGER NOT NULL DEFAULT 3, LastUpdateTimestamp INTEGER DEFAULT 0);");
            db.execSQL("CREATE TABLE LandmarkSignals (Mid TEXT NOT NULL PRIMARY KEY, Name TEXT DEFAULT '', TouristAttractionScore REAL NOT NULL, LatE7 INTEGER, LngE7 INTEGER, StructuredOpenHoursProto BLOB, IndoorType INTEGER DEFAULT 0, FOREIGN KEY (Mid) REFERENCES EntityData(Mid) ON DELETE CASCADE);");
            db.execSQL("CREATE TABLE LandmarkContext (Mid TEXT NOT NULL PRIMARY KEY, LandmarkVisitDataProto BLOB, PersonalScore REAL NOT NULL DEFAULT -1, FOREIGN KEY (Mid) REFERENCES EntityData(Mid) ON DELETE CASCADE);");
            db.execSQL("CREATE TABLE Reviews (_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ReviewerName TEXT, ReviewerImageUrl TEXT, Text TEXT, StarRating INTEGER, Timestamp INTEGER, Mid TEXT NOT NULL,  FOREIGN KEY (Mid) REFERENCES EntityData(Mid) ON DELETE CASCADE);");
            db.execSQL("CREATE TABLE WeatherReports (DestinationId TEXT NOT NULL, ValidFrom INTEGER NOT NULL, ValidUntil INTEGER NOT NULL, WeatherPleasantness INTEGER NOT NULL, PRIMARY KEY (DestinationId, ValidFrom));");
            db.execSQL("CREATE TABLE StarredPlaces (Mid TEXT NOT NULL PRIMARY KEY, StarStatus INTEGER NOT NULL, Timestamp INTEGER DEFAULT 0,IsLocal INTEGER DEFAULT 1);");
            db.execSQL("CREATE VIRTUAL TABLE EntityFts USING fts4(Mid TEXT NOT NULL, Name TEXT);");
            db.execSQL("CREATE VIRTUAL TABLE CategoryFts USING fts4(Mid TEXT NOT NULL, CategoryMid TEXT, CategoryText TEXT);");
            db.execSQL("CREATE TABLE Tnt (Mid TEXT NOT NULL PRIMARY KEY, TntProto BLOB);");
            db.execSQL("CREATE TABLE VisitedPlaces (Mid TEXT NOT NULL PRIMARY KEY, VisitedPlacesProto BLOB);");
            db.execSQL("CREATE TABLE DestinationEntities (DestinationId TEXT NOT NULL, Mid TEXT NOT NULL, PRIMARY KEY (DestinationId, Mid), FOREIGN KEY (Mid) REFERENCES EntityData(Mid));");
            db.execSQL("CREATE TABLE DestinationMetadata (DestinationId TEXT NOT NULL, Mid TEXT NOT NULL, Position INTEGER, PRIMARY KEY (DestinationId, Mid));");
            db.execSQL("CREATE TABLE TopCategories (_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, DestinationId TEXT NOT NULL, CategoryMid TEXT, CategoryText TEXT);");
            db.execSQL("ALTER TABLE Trips ADD UserCreated INTEGER DEFAULT 0;");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            createTable(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            dropTable(db, "events");
            createTable(db);
        }

        private static void dropTable(SQLiteDatabase db, String tableName) {
            String sql = "DROP TABLE IF EXISTS ".concat(tableName);
            db.execSQL(sql);
        }
    }
}
