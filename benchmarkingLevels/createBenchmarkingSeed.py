import random


# This script was used to generate a random seed to select the benchmarking levels from 1-1000.

random_numbers = random.sample(range(1, 1001), 50)
random_numbers.sort()
print(random_numbers)