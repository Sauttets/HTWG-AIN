//cat /dev/zero | pv -r | ./test_stack 1>/dev/null
//cat test_stack.c | pv -ar | valgrind ./test_stack
#include <stdio.h>
#include <stdlib.h>
#include "genstacklib.h"
#include <ctype.h>
#include <unistd.h>

struct person
{
        char *prename;
        char *lastname;
};

typedef struct person person;
typedef struct person *person_pt;

void freestruct (void *struc);

void
freestruct (void *struc)
{
        free (*(person_pt *) struc);
}

int
main(int argc, char **argv)
{
        int oscillate = 0;
        int do_not_empty_stack_in_the_end = 0;
        int stack_count = 1000;

        int c;
        opterr = 0;
        while ((c = getopt(argc, argv, "oec:")) != -1)
        {
                switch (c)
                {
                        case 'o':
                                oscillate = 1;
                                break;
                        case 'e':
                                do_not_empty_stack_in_the_end = 1;
                                break;
                        case 'c':
                                stack_count = atoi(optarg);
                                break;
                }
        }
        char buffer[5];

        person_pt person_a, person_b;
        person_a = malloc (sizeof (person));

        person_a->prename = "Elon";
        person_a->lastname = "Musk";

        void (*freefn) (void *) = NULL;
        freefn = freestruct;

        genStack ptStack;
        genStackNew (&ptStack, sizeof (person_pt), freefn);

        int current_pushed = 0;

        genStackPush (&ptStack, &person_a);
        if (genStackSize(&ptStack) != 1)
        {
                printf("\nstacksize error\n");
                return 1;
        }
        genStackPop (&ptStack, &person_b);
        if (person_b->prename != person_a->prename)
        {
                printf("\npushed is not popped\n");
                return 1;
        }
        if (!genStackIsEmpty (&ptStack))
        {
            printf("\nStack should be empty!\n");
                return 1;
        }
        genStackDispose (&ptStack);
	free(person_a);
        
        //std in test
        unsigned long value;
        genStackNew (&ptStack, sizeof (long unsigned int), NULL);
        
        while (fgets(buffer, sizeof buffer, stdin) != NULL)
        {
                char *ptr;
                long unsigned int ret;

                ret = strtoul(buffer, &ptr, 10);
                genStackPush(&ptStack, &ret);
                current_pushed++;
                if (oscillate)
                {
                        genStackPop(&ptStack, &value);
                        if (value != ret)
                        {
                                printf("\npushed is not popped\n");
                                return 1;
                        }
                }
                else if (current_pushed != genStackSize(&ptStack))
                {
                        printf("\nstacksize error\n");
                        return 1;
                }
                if (current_pushed == stack_count)
                {
                        if (do_not_empty_stack_in_the_end) break;
                        else
                        {
                                while (genStackSize(&ptStack) != 0) genStackPop(&ptStack, &value);
                                break;
                        }
                }
        }
        genStackDispose (&ptStack);
        return 0;
}
