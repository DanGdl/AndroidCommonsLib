package com.mdgd.commons.sqlite;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Max
 * on 05/09/2018.
 */
public interface ICursorParser<T> {

    T formCursor(Cursor c);

    ContentValues toContentValues(T item);

    ContentValues toContentValues(ContentValues cv, T item);
}
