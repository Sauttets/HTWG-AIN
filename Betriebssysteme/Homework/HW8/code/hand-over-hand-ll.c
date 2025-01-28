#include <stdlib.h>
#include <stdio.h>
#include <pthread.h>
#include <sys/time.h>

#define INSERTS 10000

typedef struct node_t {
    struct node_t *next;
    int key;
    pthread_mutex_t node_lock;
} node_t;

typedef struct list_t {
    node_t *head;
    pthread_mutex_t list_lock;
} list_t;

void init(list_t *list) {
    node_t *head = malloc(sizeof (node_t));
    head->key = 0;
    list->head = head;
    pthread_mutex_init(&list->list_lock, NULL);
}

void insert(list_t *list, int key) {
    node_t *node = malloc(sizeof (node_t));
    if (node == NULL) {
        perror("malloc failed");
        return;
    }
    node->key = key;
    node_t *prev, *curr;

    pthread_mutex_lock(&list->list_lock);

    prev = list->head;
    curr = list->head->next;

    pthread_mutex_lock(&prev->node_lock);
    pthread_mutex_unlock(&list->list_lock);
    if (curr != NULL) {
        pthread_mutex_lock(&curr->node_lock);
    }

    while (curr != NULL) {
        if (curr->key > key) {
            break;
        }
        node_t  *old_prev = prev;
        prev = curr;
        curr = curr->next;
        pthread_mutex_unlock(&old_prev->node_lock);
        if (curr != NULL) {
            pthread_mutex_lock(&curr->node_lock);
        }
    }

    node->next = curr;
    prev->next = node;

    pthread_mutex_unlock(&prev->node_lock);
    if (curr != NULL) {
        pthread_mutex_unlock(&curr->node_lock);
    }
}

void *worker(void *arg){
    list_t *list = (list_t*) arg;
    for (int i = 0; i < INSERTS; i++) {
        insert(list, i);
    }
    return NULL;
}

int main(int argc, char *argv[]) {

    int max_threads = 4;
    pthread_t threads[max_threads];
    struct timeval start, end;
    double elapsed_time;
    list_t* list = malloc(sizeof (list_t));
    init(list);

    for (int num_threads = 1; num_threads <= max_threads; num_threads++) {
        gettimeofday(&start, NULL);

        for (int i = 0; i < num_threads; i++) {
            pthread_create(&threads[i], NULL, worker, list);
        }
        for (int i = 0; i < num_threads; i++) {
            pthread_join(threads[i], NULL);
        }

        gettimeofday(&end, NULL);
        elapsed_time = (end.tv_sec - start.tv_sec) + (end.tv_usec - start.tv_usec) / 1000000.0;
        printf("Threads: %d, Duration: %.6f seconds\n", num_threads, elapsed_time);
    }
    free(list);
    return 0;
}