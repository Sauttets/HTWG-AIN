#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <fcntl.h>
#include <string.h>

int main(){
    int fdsc = open("test.txt", O_RDWR | O_APPEND | O_TRUNC	| O_CREAT);
    if (fdsc < 0){
        fprintf(stderr, "filedescriptor fail");
        return 1;
    }
    int id = fork();
    if (id < 0 ){
        fprintf(stderr, "forking fail");
        return 1;
    } else if(id == 0){
        for(int i = 0; i < 100; i++){
            write(fdsc, "test from child\n", 16);    
        }
    } else {
        for(int i = 0; i < 100; i++){
            write(fdsc, "test from parent\n", 17);
        }
    }
    return 0;
}

/*
Q2: Write a program that opens a file (with the open() system call) and 
then calls fork() to create a new process. Can both the child and 
parent access the file descriptor returned by open()? What happens 
when they are writing to the file concurrently, i.e., at the same time?

It seems to be possible to write with parallel processes on one file, but
as always with the fork() syscall the order of the print to the file is random.
*/