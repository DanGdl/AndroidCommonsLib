package com.mdgd.commons.sqlite;

import android.content.ContentValues;
import android.database.Cursor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Owner
 * on 05/09/2018.
 */
public abstract class CursorParser<T> implements ICursorParser<T> {

    protected String get(@NotNull Cursor c, @NotNull String columnName, @Nullable String defValue) {
        final int columnIndex = c.getColumnIndex(columnName);
        return columnIndex == -1 ? defValue : c.getString(columnIndex);
    }

    protected int get(@NotNull Cursor c, @NotNull String columnName, int defValue) {
        final int columnIndex = c.getColumnIndex(columnName);
        return columnIndex == -1 ? defValue : c.getInt(columnIndex);
    }

    protected float get(@NotNull Cursor c, @NotNull String columnName, float defValue) {
        final int columnIndex = c.getColumnIndex(columnName);
        return columnIndex == -1 ? defValue : c.getFloat(columnIndex);
    }

    protected double get(@NotNull Cursor c, @NotNull String columnName, double defValue) {
        final int columnIndex = c.getColumnIndex(columnName);
        return columnIndex == -1 ? defValue : c.getDouble(columnIndex);
    }

    protected long get(@NotNull Cursor c, @NotNull String columnName, long defValue) {
        final int columnIndex = c.getColumnIndex(columnName);
        return columnIndex == -1 ? defValue : c.getLong(columnIndex);
    }

    protected short get(@NotNull Cursor c, @NotNull String columnName, short defValue) {
        final int columnIndex = c.getColumnIndex(columnName);
        return columnIndex == -1 ? defValue : c.getShort(columnIndex);
    }

    protected byte[] get(@NotNull Cursor c, @NotNull String columnName, @Nullable byte[] defValue) {
        final int columnIndex = c.getColumnIndex(columnName);
        return columnIndex == -1 ? defValue : c.getBlob(columnIndex);
    }

    @Override
    @NotNull
    public ContentValues toContentValues(@NotNull T item) {
        return toContentValues(null, item);
    }
}
