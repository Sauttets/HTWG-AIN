#include <stdlib.h>
#include <stdio.h>

int main() {
    int* ptr = NULL; //point to nothing
    int v = *ptr; //dereference the pointer
    printf("value of v: %d", v);
    return 0;
}


/*
dereferencing a NULL-pointer is undefined and results in a segmentation fault.
(This means you fucked up big time with your memory allocation)
The compiler can do anything it wants, also crashing the program.
*/