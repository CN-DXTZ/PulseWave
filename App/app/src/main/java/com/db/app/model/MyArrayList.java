package com.db.app.model;

import java.util.ArrayList;
import java.util.Iterator;

public class MyArrayList<E> extends ArrayList<E> {
    public MyArrayList(int initialCapacity) {
        super(initialCapacity);
    }

    // 去掉原ArrayList的toString的空格
    @Override
    public String toString() {
        Iterator<E> it = iterator();
        if (!it.hasNext())
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (; ; ) {
            E e = it.next();
            sb.append(e == this ? "(this Collection)" : e);
            if (!it.hasNext())
                return sb.append(']').toString();
            sb.append(',');
        }
    }
}
