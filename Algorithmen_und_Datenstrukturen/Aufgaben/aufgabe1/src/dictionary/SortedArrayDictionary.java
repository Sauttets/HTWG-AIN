package dictionary;

import java.util.Arrays;
import java.util.Iterator;

public class SortedArrayDictionary<K extends Comparable<? super K>, V> implements Dictionary<K, V> {
    private int size = 0;
    private static final int DEF_CAPACITY = 32;
    private Entry<K,V>[] data;

    public SortedArrayDictionary() {
        data = new Entry[DEF_CAPACITY];
    }
    @Override
    public V insert(K key, V value) {
        if(search(key) == null){
            if(data.length == size){
                data = Arrays.copyOf(data, data.length * 2);
            }
            int i = size - 1;
            while (i >= 0 && data[i].getKey().compareTo(key) > 0) {
                data[i + 1] = data[i];
                i--;
            }

            data[i + 1] = new Entry<>(key, value);
            size++;
            return null;
        } else {
            V v = search(key);
            data[binaryIDXSearch(key)].setValue(value);
            return v;
        }
    }

    @Override
    public V remove(K key) {
        int idx = binaryIDXSearch(key);
        if(search(key) != null){
            V v = search(key);
            data[idx] = null;
            while(data[idx + 1] != null){
                data[idx] = data[idx + 1];
                idx++;
            }
            //Arrays.sort(data, 0, size); //doesnt really sort, just moves null to the end
            size--;
            return v;
        } else {
            return null;
        }
    }

    public int binaryIDXSearch(K key) {
        int li = 0;
        int re = size - 1;
        while (re >= li) {
            int m = (li + re)/2;
            if (key.compareTo(data[m].getKey()) < 0){
                re = m - 1;
            } else if (key.compareTo(data[m].getKey()) > 0){
                li = m + 1;
            } else {
                return m; //key found
            }
        }
        return -1; //key not found
    }

    @Override
    public V search(K key) {
        int search = binaryIDXSearch(key);

        if (search > -1){
            return data[search].getValue();
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new Iterator<>() { // anonymous (inner class) to implement Iterator
            int i = 0; // index of next element

            @Override
            public boolean hasNext() { // true if there is a next element
                if(i < size){
                    return true;
                }else {
                    return false;
                }
            }
            @Override
            public Entry<K, V> next() { // return next element
                return data[i++];

            }

        };
    }
}
