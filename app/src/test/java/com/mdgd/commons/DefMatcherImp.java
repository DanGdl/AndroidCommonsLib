package com.mdgd.commons;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public abstract class DefMatcherImp<T> extends BaseMatcher<T> {

    private final String msg;
    protected T expected;

    public DefMatcherImp(String msg) {
        this.msg = msg;
    }

    @Override
    public boolean matches(Object item) {
        return match((T) item);
    }

    protected abstract boolean match(T item);

    @Override
    public void describeMismatch(Object item, Description mismatchDescription) {
        mismatchDescription.appendText("" + item);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("" + expected);
    }
}
