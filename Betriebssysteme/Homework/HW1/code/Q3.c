#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

int main(){
    int id = fork();
    if (id < 0 ){
        fprintf(stderr, "forking fail");
        return 1;
    } else if(id == 0){
        fflush(stdout);
        printf("hello  %d\n" , getpid());
    }
    sleep(1);
    printf("goodbye  %d\n" ,getpid());

    return 0;
}

/*
Q3: Write another program using fork(). The child process should 
print “hello”; the parent process should print “goodbye”. 
You should try to ensure that the child process always prints first;
can you do this without calling wait() in the parent?

The only way i foud to be working is by just stalling 
the parent until the child is finished
*/