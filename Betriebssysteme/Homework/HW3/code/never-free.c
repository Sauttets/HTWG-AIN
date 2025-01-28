#include <stdio.h>
#include <stdlib.h>

int main(){
    int *value = malloc(sizeof(int));
    *value = 42;
    printf("%d", *value);
    return 0;
}