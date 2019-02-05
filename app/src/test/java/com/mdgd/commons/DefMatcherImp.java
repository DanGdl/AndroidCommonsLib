package com.mdgd.commons;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public abstract class DefMatcherImp<T> extends BaseMatcher<T> {

    private final String msg;

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
        System.out.println(msg + item);
    }

    @Override
    public void describeTo(Description description) {}
}
