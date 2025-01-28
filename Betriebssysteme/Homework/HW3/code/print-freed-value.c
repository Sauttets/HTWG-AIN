#include <stdlib.h>
#include <stdio.h>

int main(){
    int *array = (int*) malloc(100* sizeof(int));
    array[100] = 0;
    array[42] = 42;
    free(array);
    printf("%d\n", array[42]);
    return 0;
}