package org.alessandrosalerno.jlome.tools;

import java.util.HashMap;

public class DefaultableHashMap<K, V> extends HashMap<K, V> {
    public V setDefault(K key, V defaultValue) {
        if (!this.containsKey(key)) {
            this.put(key, defaultValue);
            return defaultValue;
        }

        return this.get(key);
    }
}
