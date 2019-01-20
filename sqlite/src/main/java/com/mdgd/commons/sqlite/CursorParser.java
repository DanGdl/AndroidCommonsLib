package com.mdgd.commons.sqlite;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Max
 * on 05/09/2018.
 */
public abstract class CursorParser<T> implements ICursorParser<T> {

    protected String get(Cursor c, String columnName, String defValue) {
        final int columnIndex = c.getColumnIndex(columnName);
        return columnIndex == -1 ? defValue : c.getString(columnIndex);
    }

    protected int get(Cursor c, String columnName, int defValue) {
        final int columnIndex = c.getColumnIndex(columnName);
        return columnIndex == -1 ? defValue : c.getInt(columnIndex);
    }

    protected float get(Cursor c, String columnName, float defValue) {
        final int columnIndex = c.getColumnIndex(columnName);
        return columnIndex == -1 ? defValue : c.getFloat(columnIndex);
    }

    protected double get(Cursor c, String columnName, double defValue) {
        final int columnIndex = c.getColumnIndex(columnName);
        return columnIndex == -1 ? defValue : c.getDouble(columnIndex);
    }

    protected long get(Cursor c, String columnName, long defValue) {
        final int columnIndex = c.getColumnIndex(columnName);
        return columnIndex == -1 ? defValue : c.getLong(columnIndex);
    }

    protected short get(Cursor c, String columnName, short defValue) {
        final int columnIndex = c.getColumnIndex(columnName);
        return columnIndex == -1 ? defValue : c.getShort(columnIndex);
    }

    protected byte[] get(Cursor c, String columnName, byte[] defValue) {
        final int columnIndex = c.getColumnIndex(columnName);
        return columnIndex == -1 ? defValue : c.getBlob(columnIndex);
    }

    @Override
    public ContentValues toContentValues(T item) {
        return toContentValues(item, null);
    }
}
