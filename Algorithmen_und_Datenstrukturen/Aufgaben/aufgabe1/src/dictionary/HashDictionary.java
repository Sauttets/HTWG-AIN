package dictionary;

import java.util.Iterator;
import java.util.LinkedList;

public class HashDictionary<K,V> implements Dictionary<K, V>{
    private LinkedList<Entry<K, V>>[] data;
    private int size = 0;
    private static final int loadFactor = 2;

    public HashDictionary(int s){
        size = 0;
        data = new LinkedList[s];
    }
    @Override
    public V insert(K key, V value) {
        int idx = searchIDX(key);
        if (idx >= 0){
            V v = search(key);
            data[h(key)].get(idx).setValue(value);
            return v;
        }
        else {
            if (data[h(key)] == null){
                data[h(key)] = new LinkedList<>();
            }
            data[h(key)].add(new Entry<>(key, value));
            size++;
        }
        if (size > loadFactor * data.length)
            resize();
        return null;

    }

    /*
   @Override
    public V insert(K key, V value) {
        if (data[h(key)] != null) {
            for (Entry<K, V> entry : data[h(key)]) {
                if (entry.getKey().equals(key)) {
                    V v = entry.getValue();
                    entry.setValue(value);
                    return v;
                }
            }
        } else {
            data[h(key)] = new LinkedList<>();
        }
        data[h(key)].add(new Entry<>(key, value));
        size++;
        if (size > loadFactor * data.length)
            resize();
        return null;
    }



     */

    public void resize(){
        HashDictionary tmp = new HashDictionary(nextPrime(size));
        for(Entry<K, V> e : this){
            tmp.insert(e.getKey(), e.getValue());
        }
        data = tmp.data;
    }

    public static int nextPrime(int n) {
        // Check if n is less than 2
        if (n < 2) {
            return 2;
        }
        // Increment n to the next odd number
        n = (n % 2 == 0) ? n + 1 : n + 2;

        while (true) {
            // Check if n is prime
            boolean isPrime = true;
            for (int i = 3; i <= Math.sqrt(n); i += 2) {
                if (n % i == 0) {
                    isPrime = false;
                    break;
                }
            }
            if (isPrime) {
                return n;
            }
            // Increment n by 2 to get the next odd number
            n += 2;
        }
    }
    @Override
     public V search(K key) {
        if (searchIDX(key) < 0){
            return null;
        } else {
            return data[h(key)].get(searchIDX(key)).getValue();
        }
    }

    public int searchIDX(K key){
        if(data[h(key)] == null){
            return -1;
        }else {
            for(int i = 0; i < data[h(key)].size(); i++){
                if(data[h(key)].get(i).getKey().equals(key)){
                    return i;
                }
            }
            return -1;
        }
    }

    @Override
    public V remove(K key) {
        int idx = searchIDX(key);
        if(idx > -1 ){
            V v = search(key);
            data[h(key)].remove(idx);
            size--;
            return v;
        }else {
            return null;
        }
    }


    @Override
    public int size() {
        return this.size;
    }

    public int h(K key) {
        int l = data.length;
        int hashValue = key.hashCode();
        if(hashValue < 0)
            hashValue = hashValue * -1;
        return hashValue % l;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new Iterator<>() {

            private int currentIndex = 0;
            private int currentDataIndex = 0;

            @Override
            public boolean hasNext() {
                if (currentDataIndex < data.length) {
                    if (data[currentDataIndex] == null) {
                        currentDataIndex++;
                        return this.hasNext();
                    } else {
                        return currentIndex < data[currentDataIndex].size();
                    }
                }
                return false;
            }

            @Override
            public Entry<K, V> next() {
                //if array index is empty (not containing a linked list)
                if (data[currentDataIndex] == null && ++currentDataIndex < data.length) {
                    return this.next();
                }
                //save next entry to return later
                Entry<K, V> entry = data[currentDataIndex].get(currentIndex++);

                //when at the end of the current linked list of current array index set array index to the next entry
                if (currentIndex >= data[currentDataIndex].size()) {
                    currentDataIndex++;
                    currentIndex = 0;
                }

                return entry;
            }
        };
    }
}
