#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>


int main(){
    int id1;
    int id2;
    int file_desc[2]; // file_desc[0] -> read file_desc[1] -> write
    if (pipe(file_desc) < 0){
        fprintf(stderr, "Pipe Failed!");
        return 1;
    }
    id1 = fork();
    if (id1 < 0 ){
        fprintf(stderr, "forking fail");
        return 1;
    } else if(id1 == 0){
        close(file_desc[0]);// Close reading end of pipe - not used
        int a;
        printf("Input integer: ");
        scanf("%d", &a);
        printf("Child 1 read value: %d  (PID %d)\n", a, getpid());
        write(file_desc[1], &a, sizeof(int));
        close(file_desc[1]); // Close writing end of pipe - after use
        exit(0);
    } 
    waitpid(id1, NULL, 0);

    id2 = fork();
    if (id2 < 0 ){
        fprintf(stderr, "forking fail");
        return 1;
    } else if(id2 == 0){
        close(file_desc[1]); // Close writing end of pipe - not used
        int b;
        read(file_desc[0], &b, sizeof(int));
        close(file_desc[0]); // Close reading end of pipe - after use
        printf("Child 2 recieved value: %d  (PID %d)\n", b, getpid());
        exit(0);
    } 

    return 0;
}

/*

*/