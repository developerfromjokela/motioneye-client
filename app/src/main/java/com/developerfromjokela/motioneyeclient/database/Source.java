/*
 * Copyright (c) 2019. MotionEyeClient by Developer From Jokela, All Rights Reserved.
 * Licenced with MIT;
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.developerfromjokela.motioneyeclient.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiConfiguration;
import android.util.Log;

import com.developerfromjokela.motioneyeclient.classes.Camera;
import com.developerfromjokela.motioneyeclient.classes.CameraUser;
import com.developerfromjokela.motioneyeclient.classes.Device;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;

public final class Source {

    /**
     * All columns used in the database.
     */
    public static final String[] devicesColumns = {"ID", "DEVICENAME", "DEVICEURL", "USERNAME", "PASSWORD", "CAMERAS", "WLAN", "LOCALPORT", "DDNSPORT", "MOTIONEYEVER", "MOTIONVER", "OSVER", "DDNSURL"};


    private final Helper dbHelper;

    private SQLiteDatabase database;

    private Context context;


    public Source(final Context context) {
        dbHelper = new Helper(context);
        this.context = context;
    }


    public final void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }


    public final void close() {
        dbHelper.close();
    }

    /**
     * Adds a camera device.
     *
     * @param device The Device Class whcih will be saved in the database.
     */
    public final void createEntry(final Device device) throws Exception {

        open();

        final ContentValues values = new ContentValues();
        values.put(devicesColumns[1], AESCryptor.encrypt(device.getDeviceName()));
        values.put(devicesColumns[2], AESCryptor.encrypt(device.getDeviceUrl()));
        values.put(devicesColumns[12], AESCryptor.encrypt(device.getDdnsURL()));
        values.put(devicesColumns[3], AESCryptor.encrypt(device.getUser().getUsername()));
        values.put(devicesColumns[4], AESCryptor.encrypt(device.getUser().getPassword()));
        Log.e("Source", new Gson().toJson(device.getCamera()));
        values.put(devicesColumns[5], AESCryptor.encrypt(new Gson().toJson(device.getCamera())));
        values.put(devicesColumns[6], AESCryptor.encrypt(new Gson().toJson(device.getWlan())));
        values.put(devicesColumns[7], AESCryptor.encrypt(device.getLocalPort()));
        values.put(devicesColumns[8], AESCryptor.encrypt(device.getDDNSPort()));
        values.put(devicesColumns[9], AESCryptor.encrypt(device.getMotioneyeVersion()));
        values.put(devicesColumns[10], AESCryptor.encrypt(device.getMotionVersion()));
        values.put(devicesColumns[11], AESCryptor.encrypt(device.getOsVersion()));

        String insertId = "ID = " + database.insert("DEVICES", null, values);

        open();


        final Cursor cursor = database.query("DEVICES", devicesColumns, insertId, null,
                null, null, null);
        cursor.close();
        close();

    }

    /**
     * Adds a camera device.
     *
     * @param device The Device Class whcih will be saved in the database.
     */
    public final void editEntry(final Device device) throws Exception {


        final ContentValues values = new ContentValues();
        values.put(devicesColumns[1], AESCryptor.encrypt(device.getDeviceName()));
        values.put(devicesColumns[2], AESCryptor.encrypt(device.getDeviceUrl()));
        values.put(devicesColumns[12], AESCryptor.encrypt(device.getDdnsURL()));
        values.put(devicesColumns[3], AESCryptor.encrypt(device.getUser().getUsername()));
        values.put(devicesColumns[4], AESCryptor.encrypt(device.getUser().getPassword()));
        Log.e("Source", new Gson().toJson(device.getCamera()));
        values.put(devicesColumns[5], AESCryptor.encrypt(new Gson().toJson(device.getCamera())));
        values.put(devicesColumns[6], AESCryptor.encrypt(new Gson().toJson(device.getWlan())));
        values.put(devicesColumns[7], AESCryptor.encrypt(device.getLocalPort()));
        values.put(devicesColumns[8], AESCryptor.encrypt(device.getDDNSPort()));
        values.put(devicesColumns[9], AESCryptor.encrypt(device.getMotioneyeVersion()));
        values.put(devicesColumns[10], AESCryptor.encrypt(device.getMotionVersion()));
        values.put(devicesColumns[11], AESCryptor.encrypt(device.getOsVersion()));

        String insertId = "ID = ?";

        open();


        database.update("DEVICES", values, insertId, new String[]{device.getID()});
        close();

    }

    /**
     * Deletes an item in the database.
     *
     * @param where Row to delete.
     */
    public final void delete_item(final String where) {
        open();
        database.delete(Helper.TABLE_DEVICE, where, null);
        close();
    }

    /**
     * Returns an ArrayList containing Device classes.
     */
    public final ArrayList<Device> getAll() throws Exception {
        open();
        final ArrayList<Device> devices = new ArrayList<>();

        final Cursor cursor = database.query(Helper.TABLE_DEVICE, devicesColumns, "", null,
                null, null, null);
        cursor.moveToFirst();

        if (cursor.getCount() == 0)
            return devices;

        while (!cursor.isAfterLast()) {
            String ID = cursor.getString(cursor.getColumnIndex(devicesColumns[0]));
            String deviceName = AESCryptor.decrypt(cursor.getString(cursor.getColumnIndex(devicesColumns[1])));
            String deviceURL = AESCryptor.decrypt(cursor.getString(cursor.getColumnIndex(devicesColumns[2])));
            String ddnsURL = AESCryptor.decrypt(cursor.getString(cursor.getColumnIndex(devicesColumns[12])));

            String username = AESCryptor.decrypt(cursor.getString(cursor.getColumnIndex(devicesColumns[3])));
            String password = AESCryptor.decrypt(cursor.getString(cursor.getColumnIndex(devicesColumns[4])));
            String cameras_jsonstr = AESCryptor.decrypt(cursor.getString(cursor.getColumnIndex(devicesColumns[5])));
            String wlan = AESCryptor.decrypt(cursor.getString(cursor.getColumnIndex(devicesColumns[6])));
            String localPort = AESCryptor.decrypt(cursor.getString(cursor.getColumnIndex(devicesColumns[7])));
            String DDNSPort = AESCryptor.decrypt(cursor.getString(cursor.getColumnIndex(devicesColumns[8])));
            String motioneyeversion = AESCryptor.decrypt(cursor.getString(cursor.getColumnIndex(devicesColumns[9])));
            String motionversion = AESCryptor.decrypt(cursor.getString(cursor.getColumnIndex(devicesColumns[10])));
            String osversion = AESCryptor.decrypt(cursor.getString(cursor.getColumnIndex(devicesColumns[11])));


            ArrayList<Camera> cameras = new ArrayList<>();
            Log.e("Source", cameras_jsonstr);
            JSONArray cameras_json = new JSONArray(cameras_jsonstr);
            for (int i = 0; i < cameras_json.length(); i++) {
                cameras.add(new Gson().fromJson(cameras_json.getString(i), Camera.class));
            }

            WifiConfiguration wifiConfig = new Gson().fromJson(wlan, WifiConfiguration.class);
            devices.add(new Device(ID, deviceName, deviceURL, ddnsURL, localPort, DDNSPort, new CameraUser(username, password), motioneyeversion, motionversion, osversion, cameras, wifiConfig));

            cursor.moveToNext();
        }
        cursor.close();
        close();


        return devices;
    }

    public final Device get(String selectionID) throws Exception {
        open();
        String selection = devicesColumns[0] + " =  ?";
        String[] seletion_args = {selectionID};
        final Cursor cursor = database.query(Helper.TABLE_DEVICE, devicesColumns, selection, seletion_args,
                null, null, null);
        cursor.moveToFirst();

        if (cursor.getCount() == 0)
            return null;


        String ID = cursor.getString(cursor.getColumnIndex(devicesColumns[0]));
        String deviceName = AESCryptor.decrypt(cursor.getString(cursor.getColumnIndex(devicesColumns[1])));
        String deviceURL = AESCryptor.decrypt(cursor.getString(cursor.getColumnIndex(devicesColumns[2])));
        String ddnsURL = AESCryptor.decrypt(cursor.getString(cursor.getColumnIndex(devicesColumns[12])));

        String username = AESCryptor.decrypt(cursor.getString(cursor.getColumnIndex(devicesColumns[3])));
        String password = AESCryptor.decrypt(cursor.getString(cursor.getColumnIndex(devicesColumns[4])));
        String cameras_jsonstr = AESCryptor.decrypt(cursor.getString(cursor.getColumnIndex(devicesColumns[5])));
        String wlan = AESCryptor.decrypt(cursor.getString(cursor.getColumnIndex(devicesColumns[6])));
        String localPort = AESCryptor.decrypt(cursor.getString(cursor.getColumnIndex(devicesColumns[7])));
        String DDNSPort = AESCryptor.decrypt(cursor.getString(cursor.getColumnIndex(devicesColumns[8])));
        String motioneyeversion = AESCryptor.decrypt(cursor.getString(cursor.getColumnIndex(devicesColumns[9])));
        String motionversion = AESCryptor.decrypt(cursor.getString(cursor.getColumnIndex(devicesColumns[10])));
        String osversion = AESCryptor.decrypt(cursor.getString(cursor.getColumnIndex(devicesColumns[11])));


        ArrayList<Camera> cameras = new ArrayList<>();
        Log.e("Source", cameras_jsonstr);
        JSONArray cameras_json = new JSONArray(cameras_jsonstr);
        for (int i = 0; i < cameras_json.length(); i++) {
            cameras.add(new Gson().fromJson(cameras_json.getString(i), Camera.class));
        }

        WifiConfiguration wifiConfig = new Gson().fromJson(wlan, WifiConfiguration.class);
        cursor.close();
        close();
        return new Device(ID, deviceName, deviceURL, ddnsURL, localPort, DDNSPort, new CameraUser(username, password), motioneyeversion, motionversion, osversion, cameras, wifiConfig);


    }

    public final Cursor getCursor() {

        return database.query(Helper.TABLE_DEVICE, devicesColumns, null, null,
                null, null, null);


    }


}