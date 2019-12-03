package com.cj.library.model;

import android.util.Pair;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

/**
 Create by chenjiao at 2019/11/27 0027
 描述：
 */
public class MutableKVLiveData<K, V> extends LiveData<Pair<K, V>> implements Map<K, V> {
    private HashMap<K, V> mMap = new HashMap();

    public void observeKey(LifecycleOwner owner, final Observer<Object> observer) {
        observe(owner, new Observer<Pair<K, V>>() {
            @Override
            public void onChanged(Pair<K, V> kvPair) {
                if (kvPair == null) {
                    observer.onChanged(null);
                    return;
                }
                observer.onChanged(kvPair.second);
            }
        });
    }

    @Override
    public int size() {
        return mMap.size();
    }

    @Override
    public boolean isEmpty() {
        return mMap.isEmpty();
    }

    @Override
    public boolean containsKey(@Nullable Object o) {
        return mMap.containsKey(o);
    }

    @Override
    public boolean containsValue(@Nullable Object o) {
        return mMap.containsValue(o);
    }

    @Nullable
    @Override
    public V get(@Nullable Object o) {
        return mMap.get(o);
    }

    @Nullable
    @Override
    public V put(K k, V v) {
        V result = mMap.put(k, v);
        this.setValue(new Pair(k, v));
        return result;
    }

    @Nullable
    @Override
    public V remove(@Nullable Object o) {
        V result = mMap.remove(o);
        this.setValue(new Pair(o, null));
        return result;
    }

    @Override
    public void putAll(@NonNull Map<? extends K, ? extends V> map) {
        mMap.putAll(map);
        for (K k : map.keySet()) {
            V v = map.get(k);
            this.setValue(new Pair(k, v));
        }
    }

    @Override
    public void clear() {
        mMap.clear();
        this.setValue(null);
    }

    @NonNull
    @Override
    public Set<K> keySet() {
        return mMap.keySet();
    }

    @NonNull
    @Override
    public Collection<V> values() {
        return mMap.values();
    }

    @NonNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        return mMap.entrySet();
    }

    public void observeKey(LifecycleOwner owner, final K key, final Observer<V> observer) {
        observe(owner, new Observer<Pair<K, V>>() {
            @Override
            public void onChanged(Pair<K, V> kvPair) {
                if (kvPair == null) {
                    observer.onChanged(null);
                }
                if (kvPair.first == key) {
                    observer.onChanged(kvPair.second);
                }

            }
        });
    }

    public void putOrRemove(K k, V v) {
        if (this == null) {
            return;
        }

        if (v == null) {
            remove(k);
        } else {
            put(k, v);
        }

    }
}
