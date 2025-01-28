#include <stdlib.h>
#include<stdio.h>

int main(){
    int *array = malloc(100* sizeof(int));
    array = array+20;
    free(array);
    return 0;
}