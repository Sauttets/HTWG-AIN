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
        int child_pid = waitpid(id, NULL, 0);
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
Q5: Write a slight modification of the previous program, this time using 
waitpid() instead of wait(). When would waitpid()be useful?

waitpid() is good if you have multiple child processes and you need to wait for a
specific process to be finished. with the pid you can exactly tell, 
which process you are waiting for
*/