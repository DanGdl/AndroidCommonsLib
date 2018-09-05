package com.mdgd.commons.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

/**
 * Created by Owner
 * on 08/07/2018.
 */
abstract class BasicPrefsImpl {

    protected final Context ctx;

    BasicPrefsImpl(Context ctx) {
        this.ctx = ctx;
    }

    abstract String getDefaultPrefsFileName();

    protected SharedPreferences getPrefs() {
        return getPrefs(getDefaultPrefsFileName());
    }

    protected SharedPreferences getPrefs(String fileName){
        return ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    protected SharedPreferences.Editor getEditor(){
        return getEditor(getDefaultPrefsFileName());
    }

    protected SharedPreferences.Editor getEditor(String fileName){
        return getPrefs(fileName).edit();
    }



    protected String get(String fileName, String key, String defaultVal) {
        return getPrefs(fileName).getString(key, defaultVal);
    }

    protected String get(String key, String defaultVal){
        return getPrefs(getDefaultPrefsFileName()).getString(key, defaultVal);
    }

    protected void put(String fileName, String key, String value) {
        getEditor(fileName).putString(key, value).apply();
    }

    protected void put(String key, String value){
        getEditor(getDefaultPrefsFileName()).putString(key, value).apply();
    }



    protected int get(String fileName, String key, int defaultVal) {
        return getPrefs(fileName).getInt(key, defaultVal);
    }

    protected int get(String key, int defaultVal){
        return getPrefs(getDefaultPrefsFileName()).getInt(key, defaultVal);
    }

    protected void put(String fileName, String key, int value) {
        getEditor(fileName).putInt(key, value).apply();
    }

    protected void put(String key, int value){
        getEditor(getDefaultPrefsFileName()).putInt(key, value).apply();
    }



    protected boolean get(String fileName, String key, boolean defaultVal) {
        return getPrefs(fileName).getBoolean(key, defaultVal);
    }

    protected boolean get(String key, boolean defaultVal){
        return getPrefs(getDefaultPrefsFileName()).getBoolean(key, defaultVal);
    }

    protected void put(String fileName, String key, boolean value) {
        getEditor(fileName).putBoolean(key, value).apply();
    }

    protected void put(String key, boolean value){
        getEditor(getDefaultPrefsFileName()).putBoolean(key, value).apply();
    }



    protected long get(String fileName, String key, long defaultVal) {
        return getPrefs(fileName).getLong(key, defaultVal);
    }

    protected long get(String key, long defaultVal){
        return getPrefs(getDefaultPrefsFileName()).getLong(key, defaultVal);
    }

    protected void put(String fileName, String key, long value) {
        getEditor(fileName).putLong(key, value).apply();
    }

    protected void put(String key, long value){
        getEditor(getDefaultPrefsFileName()).putLong(key, value).apply();
    }



    protected float get(String fileName, String key, float defaultVal) {
        return getPrefs(fileName).getFloat(key, defaultVal);
    }

    protected float get(String key, float defaultVal){
        return getPrefs(getDefaultPrefsFileName()).getFloat(key, defaultVal);
    }

    protected void put(String fileName, String key, float value) {
        getEditor(fileName).putFloat(key, value).apply();
    }

    protected void put(String key, float value){
        getEditor(getDefaultPrefsFileName()).putFloat(key, value).apply();
    }



    protected Set<String> get(String fileName, String key, Set<String> defaultVal) {
        return getPrefs(fileName).getStringSet(key, defaultVal);
    }

    protected Set<String> get(String key, Set<String> defaultVal){
        return getPrefs(getDefaultPrefsFileName()).getStringSet(key, defaultVal);
    }

    protected void put(String fileName, String key, Set<String> value) {
        getEditor(fileName).putStringSet(key, value).apply();
    }

    protected void put(String key, Set<String> value){
        getEditor(getDefaultPrefsFileName()).putStringSet(key, value).apply();
    }



    protected Map<String, ?> get(String fileName) {
        return getPrefs(fileName).getAll();
    }

    protected Map<String, ?> get(){
        return getPrefs(getDefaultPrefsFileName()).getAll();
    }
}
