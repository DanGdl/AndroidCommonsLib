package com.mdgd.commons;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

public abstract class DefMatcherImp<T> implements Matcher<T> {

    @Override
    public boolean matches(Object item) {
        return match((T) item);
    }

    protected abstract boolean match(T item);

    @Override
    public void describeMismatch(Object item, Description mismatchDescription) {

    }

    @Override
    public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {

    }

    @Override
    public void describeTo(Description description) {

    }
}
