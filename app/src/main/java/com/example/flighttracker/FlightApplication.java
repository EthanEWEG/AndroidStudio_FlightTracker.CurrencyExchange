package com.example.flighttracker;

import android.app.Application;

import com.couchbase.lite.Collection;
import com.couchbase.lite.CouchbaseLite;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;

/**
 * custom application for setting database
 */
public class FlightApplication extends Application {
    /**
     * The constant DATABASE.
     */
//database name
    public static final String DATABASE = "flight_db";
    /**
     * The constant COLLECTION.
     */
//collection name
    public static final String COLLECTION = "flights";
    /**
     * The constant KEY_DATA.
     */
//data key for save flight
    public static final String KEY_DATA = "data";
    /**
     * The constant KEY_ID.
     */
    public static final String KEY_ID = "id";
    /**
     * The constant KEY_DOC_ID.
     */
    public static final String KEY_DOC_ID = "doc_id";
    private static Database database;
    private static Collection collection;

    public void onCreate() {
        super.onCreate();
        //init database
        CouchbaseLite.init(this);
        try {
            database = new Database(DATABASE);
            collection = database.createCollection(COLLECTION);
        } catch (CouchbaseLiteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * get instance of database
     *
     * @return Database database
     */
    public static Database getDatabase() {
        return database;
    }

    /**
     * get data collection
     *
     * @return collection collection
     */
    public static Collection getCollection() {
        return collection;
    }
}
