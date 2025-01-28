import random

numbers = [random.randint(1, 10) for _ in range(100)]

with open("no-locality.txt", "w") as file:
    # Schreibe jede Zahl in eine neue Zeile
    for number in numbers:
        file.write(str(number) + "\n")
