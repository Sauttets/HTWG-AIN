#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/time.h>

#define MAX_THREADS 4
#define NUM_ITERATIONS 1000000
int LOCAL_THRESHOLD = 1024;

// Struktur für den Zähler
typedef struct {
    int globalCount;
    int localCount;
    pthread_mutex_t globalMutex;
    pthread_mutex_t localMutex;
} Counter;

Counter createCounter() {
    Counter counter;
    counter.globalCount = 0;
    counter.localCount = 0;
    pthread_mutex_init(&counter.globalMutex, NULL);
    pthread_mutex_init(&counter.localMutex, NULL);
    return counter;
}

// Funktion zum Inkrementieren des Zählers
void* incrementCounter(void* arg) {
    Counter* counter = (Counter*) arg;
    int i;
    for (i = 0; i < NUM_ITERATIONS; i++) {
        pthread_mutex_lock(&counter->localMutex);
        counter->localCount++;
        pthread_mutex_unlock(&counter->localMutex);

        if (counter->localCount >= LOCAL_THRESHOLD) {
            pthread_mutex_lock(&counter->globalMutex);
            counter->globalCount += counter->localCount;
            counter->localCount = 0;
            pthread_mutex_unlock(&counter->globalMutex);
        }
    }
    pthread_exit(NULL);
}

int main(int argc, char* argv[]) {
    if (argc < 2){
        LOCAL_THRESHOLD = 1024;
    }
    else{
        LOCAL_THRESHOLD = atoi(argv[1]);
    }

    int numThreads;

    for (numThreads = 1; numThreads <= MAX_THREADS; numThreads++) {
        pthread_t threads[MAX_THREADS];
        Counter counters[MAX_THREADS];
        int i;

        // Zeitmessung starten
        struct timeval start_time, end_time;
        gettimeofday(&start_time, NULL);

        // Erzeugen der Threads
        for (i = 0; i < numThreads; i++) {
            counters[i] = createCounter();
            pthread_create(&threads[i], NULL, incrementCounter, &counters[i]);
        }

        // Warten auf den Abschluss der Threads
        for (i = 0; i < numThreads; i++) {
            pthread_join(threads[i], NULL);
        }

        // Zeitmessung beenden
        gettimeofday(&end_time, NULL);
        double execution_time = (double)(end_time.tv_sec - start_time.tv_sec) +
                                (double)(end_time.tv_usec - start_time.tv_usec) / 1000000.0;

        // Berechnung der geschätzten Anzahl basierend auf der Anzahl der Iterationen
        int estimatedCount = 0;
        for (i = 0; i < numThreads; i++) {
            estimatedCount += counters[i].globalCount;
        }

        // Ausgabe der Schätzung des Zählerwerts und der Laufzeit
        printf("Threshold: %d Num threads: %d, Approximate count: %d, Execution time: %.6f seconds\n",
               LOCAL_THRESHOLD,numThreads, estimatedCount, execution_time);
    }

    return 0;
}
