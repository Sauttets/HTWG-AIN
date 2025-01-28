#include "ulstack.h"
#include <stdlib.h>
#include <stdio.h>

#define SIZE_ULONG sizeof (unsigned long)

static int size = 10;

void ulStackNew (ulstack * s){
    s->elementPtr = malloc(SIZE_ULONG * size);
    if(s == NULL){
        printf("Allocation error!");
        exit(1);
    }
    s->stackSize = 0;
    s->allocSize = size;
}
void ulStackDispose (ulstack * s){
    s->stackSize = 0;
    s->allocSize = 0;
    free(s->elementPtr);
}
void ulStackPush (ulstack * s, unsigned long value){
    if ((s->stackSize * SIZE_ULONG + SIZE_ULONG) > s->allocSize){
        size_t size_incr = s->allocSize << 1;
        unsigned long* new_el_ptr = realloc(s->elementPtr, size_incr);
        if(new_el_ptr == NULL){
            printf("Reallocation error!");
            exit(1);
        }
        s->elementPtr = new_el_ptr;
        s->allocSize = size_incr;
    }
    s->elementPtr[s->stackSize] = value;
    s->stackSize++;

}
unsigned long ulStackPop (ulstack * s){
    if(s->stackSize < 1){
        printf("Can't pop from empty stack!");
        exit(1);
    }
    return s->elementPtr[--s->stackSize];

}
unsigned int ulStackSize (ulstack * s){
    return s->stackSize;
}
