#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
int main(){
    int id = fork();
    if (id < 0 ){
        fprintf(stderr, "forking fail");
        return 1;
    } else if(id == 0){
        printf("I am the child before closing!\n");
        close(STDOUT_FILENO);
        printf("I am the child after closing!\n");
        for (int i = 0; i < 100000; i++){
            printf("Hello World");
        }
    } else {
        wait(NULL);
        printf("I am the parent!\n");
    }

    return 0;
}

/*
Q7: Write a program that creates a child process, and then in the child
closes standard output (STDOUT FILENO). What happens if the child calls
printf() to print some output after closing the descriptor?

after closing the stdout the child can no longer print anyting to the terminal.
The parent can write to the Terminal since the child only closed it in its own process
*/