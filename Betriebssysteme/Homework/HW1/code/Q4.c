#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>


int main(int argc, char* argv[]){
    int id = fork();
    if (id < 0 ){
        fprintf(stderr, "forking fail");
        return 1;
    } else if(id == 0){
        wait(NULL);
        char* const argv[] = {"ls", NULL};
        char* const envp[] = {NULL};
        //execl("/bin/ls", "/ls", NULL);
        execlp("ls", "ls", NULL);
        //execvp("ls", argv);
        //execv("/bin/ls", argv);
        //execle("/bin/ls", "ls", NULL, envp);
        //execvpe("ls", argv, envp); // not working idk why
        printf("\n");
    } else {
        printf("all files in your current folder: \n");
    }

    return 0;
}

/*

Write a program that calls fork() and then calls some form of exec() 
to run the program /bin/ls. See if you can try all of the variants of 
exec(), including (on Linux) execl(), execle(), execlp(), execv(), execvp()
and execvpe(). Why do you think there are so many variants of the same basic call?

------------- no clue ---------------------


from stackOverflow:
l : arguments are passed as a list of strings to the main()
v : arguments are passed as an array of strings to the main()
p : path/s to search for the new running program
e : the environment can be specified by the caller

int execl(const char *path, const char *arg, ...);
int execlp(const char *file, const char *arg, ...);
int execle(const char *path, const char *arg, ..., char * const envp[]);
int execv(const char *path, char *const argv[]);
int execvp(const char *file, char *const argv[]);
int execvpe(const char *file, char *const argv[], char *const envp[]);

*/