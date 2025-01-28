#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/time.h>

#define NUM_THREADS 4
#define INITIAL_SIZE 1000000
#define RESIZE_FACTOR 2

// Define the structure for the data
typedef struct {
    int id;
    int age;
} Data;

// Define the structure for the hash table
typedef struct {
    int size;
    int count;
    Data** array;
    pthread_mutex_t lock;
} HashTable;

// Hash function
int hashFunction(int key, int size) {
    return key % size;
}

// Initialize the hash table
void initializeHashTable(HashTable* hashTable) {
    hashTable->size = INITIAL_SIZE;
    hashTable->count = 0;
    hashTable->array = (Data**)malloc(sizeof(Data*) * hashTable->size);
    for (int i = 0; i < hashTable->size; i++) {
        hashTable->array[i] = NULL;
    }
    pthread_mutex_init(&(hashTable->lock), NULL);
}

// Resize the hash table
void resizeHashTable(HashTable* hashTable) {
    pthread_mutex_lock(&(hashTable->lock));

    int newSize = hashTable->size * RESIZE_FACTOR;
    Data** newArray = (Data**)malloc(sizeof(Data*) * newSize);
    for (int i = 0; i < newSize; i++) {
        newArray[i] = NULL;
    }

    // Rehash existing elements
    for (int i = 0; i < hashTable->size; i++) {
        if (hashTable->array[i] != NULL) {
            int newIndex = hashFunction(hashTable->array[i]->id, newSize);
            while (newArray[newIndex] != NULL) {
                newIndex = (newIndex + 1) % newSize;
            }
            newArray[newIndex] = hashTable->array[i];
        }
    }

    free(hashTable->array);
    hashTable->array = newArray;
    hashTable->size = newSize;

    pthread_mutex_unlock(&(hashTable->lock));
}

// Insert an element into the hash table
void insert(HashTable* hashTable, int id, int age) {
    pthread_mutex_lock(&(hashTable->lock));
    int index = hashFunction(id, hashTable->size);
    int originalIndex = index;

    while (hashTable->array[index] != NULL) {
        if (hashTable->array[index]->id == id) {
            pthread_mutex_unlock(&(hashTable->lock));
            return;  // Key already exists, no need to insert
        }
        index = (index + 1) % hashTable->size;
        if (index == originalIndex) {
            pthread_mutex_unlock(&(hashTable->lock));
            return;  // Hash table is full, unable to insert
        }
    }

    Data* data = (Data*)malloc(sizeof(Data));
    data->id = id;
    data->age = age;
    hashTable->array[index] = data;
    hashTable->count++;

    // Check if the hash table needs resizing
    if (hashTable->count >= hashTable->size * 0.8) {
        pthread_mutex_unlock(&(hashTable->lock));
        resizeHashTable(hashTable);
    } else {
        pthread_mutex_unlock(&(hashTable->lock));
    }
}

void* fillHashTable(void* arg) {
    HashTable* hashTable = (HashTable*)arg;
    for (int i = 0; i < INITIAL_SIZE; i++) {
        insert(hashTable, i, rand() % 100);
    }
    return NULL;
}

// Free the memory allocated for the hash table
void freeHashTable(HashTable* hashTable) {
    pthread_mutex_lock(&(hashTable->lock));
    for (int i = 0; i < hashTable->size; i++) {
        if (hashTable->array[i] != NULL) {
            free(hashTable->array[i]);
        }
    }
    free(hashTable->array);
    pthread_mutex_unlock(&(hashTable->lock));
    pthread_mutex_destroy(&(hashTable->lock));
}

// Main function
int main() {
    HashTable hashTable;
    initializeHashTable(&hashTable);
    srand(time(NULL));      // rand init
    struct timeval start, end;
    double elapsed_time;

    for (int num_threads = 1; num_threads <= NUM_THREADS; num_threads++) {
        printf("Number of Threads: %d\n", num_threads);
        pthread_t threads[num_threads];

        gettimeofday(&start, NULL);
        for (int i = 0; i < num_threads; i++) {
            pthread_create(&threads[i], NULL, fillHashTable, (void*)&hashTable);
        }

        for (int i = 0; i < num_threads; i++) {
            pthread_join(threads[i], NULL);
        }
        gettimeofday(&end, NULL);
        elapsed_time = (end.tv_sec - start.tv_sec) + (end.tv_usec - start.tv_usec) / 1000000.0;
        printf("Number of Threads: %d\tCounter Value: %d\tElapsed Time: %.6f seconds\n", num_threads, hashTable.count, elapsed_time);
        // Reset the counter for the next iteration
        hashTable.count = 0;
    }

    printf("Size: %d\n", hashTable.size);
    // Free the memory allocated for the hash table
    freeHashTable(&hashTable);

    return 0;
}
