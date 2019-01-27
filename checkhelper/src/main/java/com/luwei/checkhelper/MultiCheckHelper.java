package com.luwei.checkhelper;

import android.support.v7.widget.RecyclerView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Mr_Zeng
 *
 * @date 2019/1/3
 */
public class MultiCheckHelper extends CheckHelper {

    protected HashMap<Class, Set<?>> mMap;
    private boolean hasDefault = false;

    public MultiCheckHelper() {
        mMap = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public void add(Object d) {
        Set<Object> set = (Set<Object>) mMap.get(d.getClass());
        if (set == null) {
            set = new HashSet<>();
            mMap.put(d.getClass(), set);
        }
        set.add(d);
    }

    public void remove(Object d) {
        Set set = mMap.get(d.getClass());
        if (set != null) {
            set.remove(d);
            if (set.size() == 0) {
                mMap.remove(d.getClass());
            }
        }
    }

    @Override
    public void select(Object d, RecyclerView.ViewHolder v, boolean toCheck) {
        update(d, toCheck);
        super.select(d, v, toCheck);
    }

    @Override
    public void bind(Object d, RecyclerView.ViewHolder v, boolean toCheck) {
        if (hasDefault) {
            update(d, toCheck);
        }
        super.bind(d, v, toCheck);
    }

    protected void update(Object d, boolean toCheck) {
        if (toCheck) {
            add(d);
        } else {
            remove(d);
        }
    }

    /**
     * 选择全部
     *
     * @param map 需要选择的 D,V 的Map
     */
    public void checkAll(Map<?, RecyclerView.ViewHolder> map) {
        for (Map.Entry<?, RecyclerView.ViewHolder> entry : map.entrySet()) {
            bind(entry.getKey(), entry.getValue(), true);
        }
    }

    /**
     * 选择全部
     *
     * @param dList 数据列表
     */
    @SuppressWarnings("unchecked")
    public void checkAll(List<?> dList, RecyclerView.Adapter adapter) {
        for (Object o : dList) {
            Set set = mMap.get(o.getClass());
            set = checkNoNull(o.getClass(), set);
            set.add(o);
        }
        adapter.notifyDataSetChanged();
    }

    private <T> Set checkNoNull(Class<T> clazz, Set<T> set) {
        if (set == null) {
            set = new HashSet<>();
            mMap.put(clazz, set);
        }
        return set;
    }

    /**
     * 取消全部选择
     */
    public void unCheckAll(RecyclerView.Adapter adapter) {
        if (mMap.size() == 0) {
            return;
        }
        mMap.clear();
        adapter.notifyDataSetChanged();
    }

    @SuppressWarnings("unchecked")
    public <T> void unCheckAll(RecyclerView.Adapter adapter, Class<T> clazz, List<T> list) {
        Set<T> set = (Set<T>) mMap.get(clazz);
        if (set == null) {
            return;
        }
        set.removeAll(list);
        adapter.notifyDataSetChanged();
    }

    public void unCheckAll(RecyclerView.Adapter adapter, Class clazz) {
        mMap.remove(clazz);
        adapter.notifyDataSetChanged();
    }

    @SuppressWarnings("unchecked")
    public <T> Set<T> getSelected(Class<T> type) {
        return (Set<T>) mMap.get(type);
    }

    public Set<Object> getSelected() {
        Set<Object> set = new HashSet<>();
        for (Set set1 : mMap.values()) {
            set.addAll(set1);
        }
        return set;
    }

    @Override
    public boolean isChecked(Object d, RecyclerView.ViewHolder v) {
        Set set = mMap.get(d.getClass());
        return set != null && set.contains(d);
    }

    @Override
    public boolean hasChecked() {
        return mMap.size() != 0;
    }

    public boolean isAllChecked(List<?> list) {
        return getSelected().containsAll(list);
    }


    public <T> boolean isAllChecked(List<?> list, Class<T> clazz) {
        Set<T> set = getSelected(clazz);
        if (set == null) {
            return false;
        }
        for (Object o : list) {
            if (o.getClass() != clazz) {
                continue;
            }
            if (!set.contains(o)) {
                return false;
            }
        }
        return true;
    }

    public boolean hasDefault() {
        return hasDefault;
    }

    public void setHasDefault(boolean hasDefault) {
        this.hasDefault = hasDefault;
    }
}
