#include "genstacklib.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <assert.h>

static int SIZE = 10;

void genStackNew(genStack *s, int elementSize, void (*freefn)(void*)) {
    s->elementPtr = malloc(elementSize * SIZE);
    if (s->elementPtr == NULL) {
        printf("Allocation error!");
        exit(1);
    }
    s->stackSize = 0;
    s->allocSize = SIZE * elementSize;  // Initialisiere allocSize
    s->elementSize = elementSize;
    s->freefn = freefn;
}

void genStackDispose (genStack *s){
    if (s->freefn)
        s->freefn(&s->elementPtr);
    else
        free(s->elementPtr);
    s->stackSize = 0;
    s->allocSize = 0;
    s->elementPtr = NULL;
}


void genStackPush (genStack *s, const void *elementAddr){
    if ((s->stackSize * s->elementSize + s->elementSize) > s->allocSize){
        size_t size_incr = s->allocSize << 1;
        void* new_el_ptr = realloc(s->elementPtr, size_incr);
        if(new_el_ptr == NULL){
            printf("Reallocation error!");
            exit(1);
        }
        s->elementPtr = new_el_ptr;
        s->allocSize = size_incr;
    }
    void* target = (char*)s->elementPtr + (s->stackSize * s->elementSize);
    memcpy(target, elementAddr, s->elementSize);
    s->stackSize++;
}

void genStackPop (genStack *s, void *elementAddr){
    assert(s->stackSize > 0);
    void* source = (char*)s->elementPtr + ((s->stackSize - 1) * s->elementSize);
    memcpy(elementAddr, source, s->elementSize);
    s->stackSize--;
}


bool genStackIsEmpty (const genStack *s){
    return s->stackSize == 0;
}

unsigned int genStackSize (genStack * s){
    return s->stackSize;
}
