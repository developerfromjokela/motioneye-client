/*
 * Copyright (c) 2019. MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licenced with MIT:
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 *  and associated documentation files (the "Software"), to deal in the Software without restriction,including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *   The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *    SOFTWARE.
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
