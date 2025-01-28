#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>

int main(){
    int id = fork();
    //Failsafe
    if (id < 0 ){
        fprintf(stderr, "forking fail");
        return 1;
    } 
    //child
    else if(id == 0){
        //int rc_wait = wait(NULL);
        int n = 2;
         for (int i = 0; i < 10; i++ ){
            printf("  %d", i*n);
            n = n*2;
         }
         printf("\n");
    } 
    //parent
    else {
        int child_pid = wait(NULL);
        if (child_pid < 0) {
           printf("Failed");
           exit(1);
        }
        double n = 2.0;
         for (int i = 0; i < 10; i++ ){
            printf("  %.2f", i*n);
            n = n/2.0;
         }
         printf("\n");
    }

    return 0;
}

/*
Q5: Now write a program that uses wait()to wait for the child process to finish in the 
parent. What does wait() return? What happens if you use wait() in the child?

when you use wait in the child the child will wait until the parent is done.
*/