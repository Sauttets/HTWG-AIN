#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/time.h>

// Structure for each node in the binary search tree
typedef struct Node {
    int key;
    struct Node* left;
    struct Node* right;
    pthread_mutex_t lock;
} Node;

typedef struct {
    Node* root;
} BinarySearchTree;

// Function to create a new node
struct Node* createNode(int key) {
    struct Node* newNode = (struct Node*)malloc(sizeof(struct Node));
    newNode->key = key;
    newNode->left = NULL;
    newNode->right = NULL;
    pthread_mutex_init(&newNode->lock, NULL);
    return newNode;
}

// Function to insert a key into the binary search tree
struct Node* insert(struct Node* root, int key) {
    if (root == NULL) {
        pthread_mutex_lock(&root->lock);  // Lock the root node
        return createNode(key);
    }
    pthread_mutex_lock(&root->lock);  // Lock the current node

    if (key < root->key) {
        root->left = insert(root->left, key);
    } else if (key > root->key) {
        root->right = insert(root->right, key);
    } else {
        int rnd = rand() % 1000;  // Generate a random value
        insert(root, key + rnd);  // Insert a modified key as an example
    }

    pthread_mutex_unlock(&root->lock);  // Unlock the current node
    return root;
}

// Function to calculate the size of the binary search tree
int getSize(struct Node* root) {
    if (root == NULL) {
        return 0;
    } else {
        return getSize(root->left) + 1 + getSize(root->right);
    }
}

int main() {
    BinarySearchTree bst;
    bst.root = NULL;

    for (int i = 0; i < 1000; i++) {
        pthread_mutex_lock(&bst.root->lock);  // Lock the entire binary search tree
        bst.root = insert(bst.root, i);
        pthread_mutex_unlock(&bst.root->lock);  // Unlock the binary search tree
    }

    // Getting the size of the binary search tree
    int size = getSize(bst.root);
    printf("Size of the binary search tree: %d\n", size);

    return 0;
}
