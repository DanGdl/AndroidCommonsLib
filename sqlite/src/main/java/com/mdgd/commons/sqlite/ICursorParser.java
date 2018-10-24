package com.mdgd.commons.sqlite;

import android.content.ContentValues;
import android.database.Cursor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ICursorParser<T> {

    @NotNull
    T formCursor(@NotNull Cursor c);

    @NotNull
    ContentValues toContentValues(@NotNull T item);

    @NotNull
    ContentValues toContentValues(@Nullable ContentValues cv, @NotNull T item);
}
