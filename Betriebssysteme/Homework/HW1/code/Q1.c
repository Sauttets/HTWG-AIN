#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

int main(){
    int x;
    x = 100;
    printf("Value of x: %d  (PID: %d)\n" ,x, (int) getpid());
    int id = fork();
    if (id < 0 ){
        fprintf(stderr, "forking fail");
        return 1;
    } else if(id == 0){
        x = 50;
        printf("Child PID: %d\n" , (int) getpid());
    } else {
        x = 20;
        printf("Parent PID: %d\n" , (int) getpid());
    }
    printf("Value of x is: %d  (PID:%d)\n", x, (int) getpid());
    x = x*3;
    printf("Value of x is: %d  (PID:%d)\n", x, (int) getpid());  
    return 0;
}

/*
Q1: Writeaprogramthatcallsfork().Beforecallingfork(),havethe main process 
access a variable (e.g., x) and set its value to some- thing (e.g., 100). 
What value is the variable in the child process? What happens to the 
variable when both the child and parent change the value of x?

The child and parent process take the value 100 and calculate with this,
regardless of what the other process is doing, since they run in paral
*/