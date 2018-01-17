package com.example.seevoid.tp_devoir.communication.models;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by seevoid on 14/01/18.
 */

public class SWModelList<T> implements Serializable {
    public int count;
    public String next;
    public String previous;
    public ArrayList<T> results;

    public boolean hasMore() {
        return !TextUtils.isEmpty(next);
    }

    public int size() {
        return this.results.size();
    }

    public T get(int index) {
        return results.get(index);
    }
}
