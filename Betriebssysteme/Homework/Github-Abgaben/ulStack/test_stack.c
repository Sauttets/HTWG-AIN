//cat /dev/zero | pv -r | ./test_stack 1>/dev/null
//cat test_stack.c | pv -ar | valgrind ./test_stack
#include <stdio.h>
#include <stdlib.h>
#include "ulstack.h"
#include <ctype.h>
#include <unistd.h>

int
main(int argc, char **argv)
{
	int oscillate = 0;
	int do_not_empty_stack_in_the_end = 0;
	int stack_count = 1000;
	unsigned long test_pop_data = 1337;

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

	ulstack stack;
	ulstack *ptStack = &stack;
	ulStackNew(ptStack);

	int current_pushed = 0;

	ulStackPush(ptStack, test_pop_data);
	if (ulStackSize(ptStack) != 1)
	{
		printf("\nstacksize error\n");
		return 1;
	}
	unsigned long test_pop = ulStackPop(ptStack);
	if (test_pop != test_pop_data)
	{
		printf("\npushed is not popped\n");
		return 1;
	}
	while (fgets(buffer, sizeof buffer, stdin) != NULL)
	{
		char *ptr;
		unsigned long ret;

		ret = strtoul(buffer, &ptr, 10);
		ulStackPush(ptStack, ret);
		current_pushed++;
		if (oscillate)
		{
			unsigned long data = ulStackPop(ptStack);
			if (data != ret)
			{
				printf("\npushed is not popped\n");
				return 1;
			}
		}
		else if (current_pushed != ulStackSize(ptStack))
		{
			printf("\nstacksize error\n");
			return 1;
		}
		if (current_pushed == stack_count)
		{
			if (do_not_empty_stack_in_the_end) break;
			else
			{
				while (ulStackSize(ptStack) != 0) ulStackPop(ptStack);
				break;
			}
		}
	}
	ulStackDispose(ptStack);
	return 0;
}
