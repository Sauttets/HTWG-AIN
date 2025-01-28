#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <unistd.h>


int main(int argc, char *argv[]){
    int* mem_array;
    int mem;
    int runtime;
    printf("PID: %d\n", getpid());
    if(atoi(argv[1]) > 0 && argc >= 3 && atoi(argv[2]) > 0){
        mem = atoi(argv[1]);
        runtime = atoi(argv[2]);
        printf("Programm runs for %d seconds with %d Megabytes of memory\n", runtime, mem);
    } else {
        printf("Parameters not correct!\n");
        return 1;
    }
    
    mem = mem * 1048576;        // calc from Byte to Megabyte
    mem_array = malloc(mem);    //malloc ammount of Megabyte
    time_t start = time(NULL);
    time_t end = start + (time_t)runtime;
    
    while(start < end){ 
        for(int i = 0; i < mem / sizeof(int); i++){
            start = time(NULL);
            mem_array[i] = mem_array[i] +1;
        }
    }
    free(mem_array);
    return 0;
}