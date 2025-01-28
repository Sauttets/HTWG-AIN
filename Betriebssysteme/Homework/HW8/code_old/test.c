#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/time.h>

#define NUM_THREADS 4
#define NUM_INSERTS 1000000

typedef struct Node {
    int data;
    struct Node* left;
    struct Node* right;
} Node;

typedef struct Tree {
    Node* root;
    pthread_mutex_t lock;
} Tree;

Node* createNode(int data) {
    Node* newNode = (Node*)malloc(sizeof(Node));
    newNode->data = data;
    newNode->left = NULL;
    newNode->right = NULL;
    return newNode;
}

Tree* createTree() {
    Tree* newTree = (Tree*)malloc(sizeof(Tree));
    newTree->root = NULL;
    pthread_mutex_init(&(newTree->lock), NULL);
    return newTree;
}

void insertNode(Tree* tree, int data) {
    pthread_mutex_lock(&(tree->lock));

    Node** root = &(tree->root);
    if (*root == NULL) {
        *root = createNode(data);
    } else {
        Node* currentNode = *root;
        while (1) {
            if (data < currentNode->data) {
                if (currentNode->left == NULL) {
                    currentNode->left = createNode(data);
                    break;
                } else {
                    currentNode = currentNode->left;
                }
            } else if (data > currentNode->data) {
                if (currentNode->right == NULL) {
                    currentNode->right = createNode(data);
                    break;
                } else {
                    currentNode = currentNode->right;
                }
            } else {
                break;
            }
        }
    }

    pthread_mutex_unlock(&(tree->lock));
}

int main() {
    Tree* tree = createTree();

    struct timeval start, end;
    double elapsed_time;

    int num_threads = 1;  // Start with one thread

    for (int iteration = 1; iteration <= NUM_THREADS; iteration++) {
        gettimeofday(&start, NULL);

        pthread_t threads[NUM_THREADS];  // Create an array of threads containing the thread IDs

        for (int t = 0; t < num_threads; t++) {
            pthread_create(&threads[t], NULL, (void* (*)(void*))insertNode, (void*)tree);
        }

        for (int i = 0; i < NUM_INSERTS; i++) {
            int data = rand() % (NUM_INSERTS * num_threads);
            insertNode(tree, data);
        }

        for (int t = 0; t < num_threads; t++) {
            pthread_join(threads[t], NULL);
        }

        gettimeofday(&end, NULL);

        elapsed_time = (end.tv_sec - start.tv_sec) + (end.tv_usec - start.tv_usec) / 1000000.0;

        printf("Number of Threads: %d\tElapsed Time: %.6f seconds\tSize: %d\n", num_threads, elapsed_time, NUM_INSERTS * num_threads);

        num_threads++;  // Increase the number of threads for the next iteration
    }

    return 0;
}
