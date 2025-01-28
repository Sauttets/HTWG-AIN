#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/time.h>

#define NUM_THREADS 4
#define NUM_VALUES 10000

// Structure for each node in the binary search tree
typedef struct Node {
    int key;
    struct Node* left;
    struct Node* right;
    pthread_mutex_t lock;
} Node;

// Structure for the binary search tree
typedef struct {
    Node* root;
} BinarySearchTree;

// Function to create a new node
Node* createNode(int key) {
    Node* newNode = (Node*)malloc(sizeof(Node));
    newNode->key = key;
    newNode->left = NULL;
    newNode->right = NULL;
    pthread_mutex_init(&(newNode->lock), NULL); // Initialize the lock for the new node
    return newNode;
}

// Function to insert a key into the binary search tree
void insert(BinarySearchTree* tree, int key) {
    if (tree->root == NULL) {
        tree->root = createNode(key);
    } else {
        Node* currentNode = tree->root;
        Node* parentNode = NULL;

        while (1) {
            pthread_mutex_lock(&(currentNode->lock));  // Acquire lock for the current node

            if (key < currentNode->key) {
                if (currentNode->left == NULL) {
                    currentNode->left = createNode(key);
                    pthread_mutex_unlock(&(currentNode->lock));  // Release lock for the current node
                    break;
                } else {
                    parentNode = currentNode;
                    currentNode = currentNode->left;
                }
            } else if (key > currentNode->key) {
                if (currentNode->right == NULL) {
                    currentNode->right = createNode(key);
                    pthread_mutex_unlock(&(currentNode->lock));  // Release lock for the current node
                    break;
                } else {
                    parentNode = currentNode;
                    currentNode = currentNode->right;
                }
            } else {
                int rnd = rand() % 1000;  // Generate a random value
                pthread_mutex_unlock(&(currentNode->lock));  // Release lock for the current node
                insert(tree, (key + rnd));  // Insert a modified key as an example
                break;
            }
        }

        if (parentNode != NULL) {
            pthread_mutex_unlock(&(parentNode->lock));  // Release lock for the parent node
        }
    }
}

// Function to drop the entire tree
void dropTree(Node* node) {
    if (node == NULL) {
        return;
    }

    dropTree(node->left);
    dropTree(node->right);

    pthread_mutex_destroy(&(node->lock));  // Destroy the lock associated with the node

    free(node);
}

void* insertValues(void* arg) {
    BinarySearchTree* bst = (BinarySearchTree*)arg;
    int start = *((int*)bst);

    for (int i = start; i < start + NUM_VALUES; i++) {
        int randomValue = rand() % 100000;  // Generate a random value
        insert(bst, randomValue);
    }

    return NULL;
}

int getSize(Node* node) {
    if (node == NULL) {
        return 0;
    }

    return 1 + getSize(node->left) + getSize(node->right);
}

int main() {
    struct timeval start, end;
    double elapsedTime;

    for (int numThreads = 1; numThreads <= NUM_THREADS; numThreads++) {
        BinarySearchTree bst;
        bst.root = NULL;

        gettimeofday(&start, NULL);

        pthread_t threads[numThreads];
        int threadIds[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threadIds[i] = i * NUM_VALUES;
            pthread_create(&threads[i], NULL, insertValues, (void*)&bst);
        }

        for (int i = 0; i < numThreads; i++) {
            pthread_join(threads[i], NULL);
        }

        gettimeofday(&end, NULL);

        elapsedTime = (end.tv_sec - start.tv_sec) + (end.tv_usec - start.tv_usec) / 1000000.0;

        int totalValues = NUM_VALUES * numThreads;
        printf("Inserted %d values with %d thread(s). Time taken: %f seconds\n", totalValues, numThreads, elapsedTime);
        printf("Size of the tree: %d\n", getSize(bst.root));
        dropTree(bst.root);
        bst.root = NULL;
    }

    return 0;
}
