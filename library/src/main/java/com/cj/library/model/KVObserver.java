package com.cj.library.model;

import android.util.Pair;

import androidx.lifecycle.Observer;

/**
 Create by chenjiao at 2019/11/27 0027
 描述：
 */
abstract class KVObserver<K, V> implements Observer<Pair<K, V>> {
    @Override
    public void onChanged(Pair<K, V> kvPair) {
        if (kvPair == null)
            return;
        K key = kvPair.first;
        V value = kvPair.second;
        onChanged(key, value);
    }

    abstract void onChanged(K key, V v);
}
