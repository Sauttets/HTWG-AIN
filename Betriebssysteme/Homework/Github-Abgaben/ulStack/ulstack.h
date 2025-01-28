typedef struct {
	unsigned long *elementPtr;
	unsigned int stackSize;
	unsigned int allocSize;
} ulstack;

void ulStackNew (ulstack * s);
void ulStackDispose (ulstack * s);
void ulStackPush (ulstack * s, unsigned long value);
unsigned long ulStackPop (ulstack * s);
unsigned int ulStackSize (ulstack * s);
