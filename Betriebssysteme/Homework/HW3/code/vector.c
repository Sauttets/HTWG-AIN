#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

// v is the vector
// value is the value to be added
// size is the size of the vector
// cap is the max capacity of the vector

typedef struct{
    int *value;
    size_t size;
    size_t cap;
} Vector;

void vector_add(Vector *v, int value){ //takes in the value to be added and the 
    if(v->size == v->cap){
        //calc new cap for the Vector
        size_t new_cap;        
        if(v->cap == 0){  //init case
            new_cap = 1;
        }else{
            new_cap = v->cap * 2;
        }        
        //resize the memory of the Vector
        size_t new_size = new_cap * sizeof(int);
        int *new_value = realloc(v->value, new_size);
        //value is not allowed to be NULL
        if(new_value == NULL){
            fprintf(stderr, "Memory allocation fail!\n");
            exit(1);
        }
        v->value = new_value;
        v->cap = new_cap;
    }
    //assign value and increase size
    v->value[v->size] = value;
    v->size++;
}

void vector_init_clear(Vector *v, bool clear){
    v->value = NULL;
    v->size = 0;
    v->cap = 0;
    if(clear){
        free(v->value);
    }
}

int main(){
    Vector v; // new vector
    vector_init_clear(&v, false); //init the vector with no clear
    for (int i = 0; i < 10; i++) {
        vector_add(&v, i);
        //printf("%d added to vector\n", i); 
    }
    for (int i = 0; i < v.size; i++) { //iterate through the vector and print
        printf("%d ", v.value[i]);
    }
    vector_init_clear(&v, true);
    return 0;
}
