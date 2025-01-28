CC = gcc
CFLAGS = -pthread

all: aprox-counter hand-over-hand-ll counter-with-lock fav-good fav-bad

aprox-counter: aprox-counter.c
	$(CC) -o aprox-counter aprox-counter.c $(CFLAGS)

hand-over-hand-ll: hand-over-hand-ll.c
	$(CC) -o hand-over-hand-ll hand-over-hand-ll.c $(CFLAGS)

counter-with-lock: counter-with-lock.c
	$(CC) -o counter-with-lock counter-with-lock.c $(CFLAGS)

fav-good: fav-good.c
	$(CC) -o fav-good fav-good.c $(CFLAGS)

fav-bad: fav-bad.c
	$(CC) -o fav-bad fav-bad.c $(CFLAGS)

clean:
	rm -f aprox-counter hand-over-hand-ll counter-with-lock fav-good fav-bad
