package com.jn.sqlhelper.common.ddl.model.internal;

import com.jn.langx.Converter;

public class SortTypeConverter implements Converter<String, SortType> {
    @Override
    public SortType apply(String input) {
        return SortType.ofCode(input);
    }
}
