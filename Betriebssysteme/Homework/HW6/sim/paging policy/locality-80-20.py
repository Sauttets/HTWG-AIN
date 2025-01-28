import random

frequent_numbers = [random.randint(1, 5) for _ in range(80)]
infrequent_numbers = [random.randint(6, 10) for _ in range(20)]
numbers = frequent_numbers + infrequent_numbers
random.shuffle(numbers)
with open("locality-80-20.txt", "w") as file:
    # Schreibe jede Zahl in eine neue Zeile
    for number in numbers:
        file.write(str(number) + "\n")
