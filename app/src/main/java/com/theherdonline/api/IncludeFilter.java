package com.theherdonline.api;

import java.util.ArrayList;

public class IncludeFilter<T> extends ArrayList<T> {


    public IncludeFilter(T ... string) {
        for (T i: string)
        {
            this.add(i);
        }
    }

    public String filterValue()
    {
        String filter = "";
        int len = this.size();
        Object [] array = this.toArray();
        for (int i = 0; i < len; i++)
        {
            filter += (String) array[i] + (i == len - 1 ? "" : ",");
        }
        return filter.equals("") ? null : filter;
    }

    @Override
    public String toString() {
        return filterValue();
    }
}
