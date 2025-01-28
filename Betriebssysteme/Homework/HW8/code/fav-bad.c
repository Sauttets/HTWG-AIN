#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/time.h>

#define NUM_THREADS 4
#define NUM_INSERTS 1000000
    
pthread_mutex_t rootLock = PTHREAD_MUTEX_INITIALIZER;
int* randomValues;
int globalThreadCount = 0;

typedef struct Node {
    int data;
    struct Node* left;
    struct Node* right;
} Node;

Node* createNode(int data) {
    Node* newNode = (Node*)malloc(sizeof(Node));
    newNode->data = data;
    newNode->left = NULL;
    newNode->right = NULL;
    return newNode;
}

void insertNode(Node * root, int data) {
    if (data < root->data) {
        if (root->left == NULL) {
            root->left = createNode(data);
        }
        else insertNode(root->left, data);
    } else if (data > root->data) {
        if (root->right == NULL) {
            root->right = createNode(data);
        } else insertNode(root->right, data);
    }  
}



int* generateUniqueRandomValues(int size) {
    int* values = malloc(size * sizeof(int));
    if (values == NULL) {
        // Error handling: memory allocation failed
        return NULL;
    }

    // Initialize the array with sequential values
    for (int i = 0; i < size; i++) {
        values[i] = i;
    }

    // Shuffle the array using Fisher-Yates algorithm
    srand(time(NULL));
    for (int i = size - 1; i > 0; i--) {
        int j = rand() % (i + 1);
        int temp = values[i];
        values[i] = values[j];
        values[j] = temp;
    }

    return values;
}


void fillBinaryTree(Node* root) {
    if (randomValues == NULL) {
        printf("Error: memory allocation failed\n");
        return;
    }

    pthread_mutex_lock(&rootLock);
    //uint64_t tid;
    //pthread_threadid_np(NULL, &tid);
    //printf("Thread id : %llu\n", tid);
    for (int i = 0; i < NUM_INSERTS; i++) {
        insertNode(root, randomValues[i+globalThreadCount*NUM_INSERTS]);
    }
    globalThreadCount++;
    pthread_mutex_unlock(&rootLock); 
  
}


void* threadFunction(void* arg) {
    Node* root = (Node*)arg;
    fillBinaryTree(root);
    return NULL;
}

void dropTree(Node* root) {
    if (root == NULL) {
        return;
    }
    
    dropTree(root->left);
    root->left = NULL;
    dropTree(root->right);
    root->right = NULL;
    
    free(root);
}


int main() {

    //Generate an global array of random values
    randomValues = generateUniqueRandomValues(NUM_INSERTS * NUM_THREADS);
    struct timeval start, end;
    double elapsed_time;
    Node* root = createNode(randomValues[0]);
    
    pthread_t threads[NUM_THREADS];

    for(int thread_count = 1; thread_count <= NUM_THREADS; thread_count++){
        gettimeofday(&start, NULL);
        for (int i = 0; i < thread_count; i++) {
            pthread_create(&threads[i], NULL, threadFunction, root);
        }
        for (int i = 0; i < thread_count; i++) {
            pthread_join(threads[i], NULL);
        }
        gettimeofday(&end, NULL);
        elapsed_time = (end.tv_sec - start.tv_sec) + (end.tv_usec - start.tv_usec) / 1000000.0;
        printf("Number of Threads: %d, Elapsed Time: %.6f s Size: %d\n",thread_count, elapsed_time, NUM_INSERTS * thread_count);
        elapsed_time = 0;
        globalThreadCount = 0;
    }
        free(randomValues);
        dropTree(root);
    return 0;
 
 
}