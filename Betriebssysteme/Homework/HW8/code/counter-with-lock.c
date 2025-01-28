#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/time.h>

#define NUM_THREADS 4
#define NUM_INCREMENTS 10000000

typedef struct {
    int counter;
    pthread_mutex_t lock;
} ConcurrentCounter;

void* incrementCounter(void* arg) {
    ConcurrentCounter* counter = (ConcurrentCounter*)arg;
    for (int i = 0; i < NUM_INCREMENTS; i++) {
        pthread_mutex_lock(&(counter->lock));
        counter->counter++;
        pthread_mutex_unlock(&(counter->lock));
    }
    pthread_exit(NULL);
}

int main() {
    ConcurrentCounter counter;
    counter.counter = 0;
    pthread_mutex_init(&(counter.lock), NULL);

    struct timeval start, end;
    double elapsed_time;

    int num_threads = 1;  // Start with one thread

    for (int iteration = 1; iteration <= NUM_THREADS; iteration++) {
        pthread_t threads[NUM_THREADS];  // Create an array of threads

        gettimeofday(&start, NULL);

        for (int i = 0; i < num_threads; i++) {
            pthread_create(&threads[i], NULL, incrementCounter, (void*)&counter);
        }

        for (int i = 0; i < num_threads; i++) {
            pthread_join(threads[i], NULL);
        }

        gettimeofday(&end, NULL);
        elapsed_time = (end.tv_sec - start.tv_sec) + (end.tv_usec - start.tv_usec) / 1000000.0;
        printf("Number of Threads: %d\tCounter Value: %d\tElapsed Time: %.6f seconds\n", num_threads, counter.counter, elapsed_time);

        num_threads++;  // Increase the number of threads for the next iteration

        // Reset the counter for the next iteration
        counter.counter = 0;
    }

    pthread_mutex_destroy(&(counter.lock));

    return 0;
}
