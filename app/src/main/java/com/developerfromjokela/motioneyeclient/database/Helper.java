/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licensed with MIT
 */

package com.developerfromjokela.motioneyeclient.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public final class Helper extends SQLiteOpenHelper {

    /**
     * The name of the database containing device's details.
     */
    public static final String DATABASE_NAME = "Devices.db";

    public static final String TABLE_DEVICE = "DEVICES";

    /**
     * The current version of the database containing devices.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * The command when first creating the database.
     */
    private static final String TABLE_CREATE_DEVICES = "create table DEVICES(ID integer primary key autoincrement,DEVICENAME text,DEVICEURL text,USERNAME text,PASSWORD text, CAMERAS text, WLAN text, LOCALPORT text, DDNSPORT text, MOTIONEYEVER text, MOTIONVER text, OSVER text, DDNSURL text)";

    public Helper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public final void onCreate(final SQLiteDatabase database) {
        database.execSQL(TABLE_CREATE_DEVICES);
    }

    @Override
    public final void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        // Upgrade from first to third version

    }
}
