package com.wechat.files.cleaner.videoclean.bean;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractExpandableItem<T> implements IExpandable<T> {
    protected boolean mExpandable = false;
    protected List<T> mSubItems;

    public boolean isExpanded() {
        return this.mExpandable;
    }

    public void setExpanded(boolean z) {
        this.mExpandable = z;
    }

    public List<T> getSubItems() {
        return this.mSubItems;
    }

    public boolean hasSubItem() {
        return this.mSubItems != null && this.mSubItems.size() > 0;
    }

    public void setSubItems(List<T> list) {
        this.mSubItems = list;
    }

    public T getSubItem(int i) {
        if (!hasSubItem() || i >= this.mSubItems.size()) {
            return null;
        }
        return this.mSubItems.get(i);
    }

    public int getSubItemPosition(T t) {
        return this.mSubItems != null ? this.mSubItems.indexOf(t) : -1;
    }

    public void addSubItem(T t) {
        if (this.mSubItems == null) {
            this.mSubItems = new ArrayList();
        }
        this.mSubItems.add(t);
    }

    public void addSubItem(int i, T t) {
        if (this.mSubItems == null || i < 0 || i >= this.mSubItems.size()) {
            addSubItem(t);
        } else {
            this.mSubItems.add(i, t);
        }
    }

    public boolean contains(T t) {
        return this.mSubItems != null && this.mSubItems.contains(t);
    }

    public boolean removeSubItem(T t) {
        return this.mSubItems != null && this.mSubItems.remove(t);
    }

    public boolean removeSubItem(int i) {
        if (this.mSubItems == null || i < 0 || i >= this.mSubItems.size()) {
            return false;
        }
        this.mSubItems.remove(i);
        return true;
    }
}
