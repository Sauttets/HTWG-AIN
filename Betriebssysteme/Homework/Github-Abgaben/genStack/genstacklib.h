typedef struct {
	void *elementPtr; 
	int elementSize; 
	int stackSize; 
	int allocSize;
	void (*freefn)(void*);
} genStack;

typedef int bool;
 
void genStackNew (genStack *s, int elementSize, void (*freefn)(void*));
void genStackDispose (genStack *s);
bool genStackIsEmpty (const genStack *s);
void genStackPush (genStack *s, const void *elementAddr); 
void genStackPop (genStack *s, void *elementAddr);
unsigned int genStackSize (genStack * s);
