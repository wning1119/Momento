package com.example.ningwang.momento;

import android.app.Application;
import android.provider.ContactsContract;

import com.example.sungyup.cs130.Database;

/**
 *
 * Created by Sungyup on 11/27/2016.
 */

public class Global extends Application {
    private Database database;

    public Database getDatabase(){
        return database;
    }

    public void setDatabase(Database d){
        database = d;
    }
}
