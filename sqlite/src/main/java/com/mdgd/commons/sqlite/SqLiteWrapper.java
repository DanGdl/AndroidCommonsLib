package com.mdgd.commons.sqlite;

import android.content.ContentValues;

/**
 * Created by Max
 * on 05/09/2018.
 */
public abstract class SqLiteWrapper<T> extends CursorParser<T> {

    @Override
    public ContentValues toContentValues(T item) {
        return toContentValues(item, null);
    }
}
