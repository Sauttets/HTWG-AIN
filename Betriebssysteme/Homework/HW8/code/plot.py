import matplotlib.pyplot as plt

# Threshold values
thresholds = [1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384]

# Execution time values for each Num threads
num_threads_1 = [0.045131, 0.031960, 0.026888, 0.028876, 0.027648, 0.026868, 0.026509, 0.026117, 0.026426, 0.025052, 0.023641, 0.025916, 0.026281, 0.026476, 0.025655]
num_threads_2 = [0.040459, 0.030145, 0.026878, 0.023671, 0.023453, 0.022776, 0.022412, 0.022328, 0.021849, 0.021951, 0.023504, 0.021829, 0.021453, 0.022117, 0.022265]
num_threads_3 = [0.175124, 0.144227, 0.124350, 0.117629, 0.112811, 0.111477, 0.108854, 0.104343, 0.105584, 0.106239, 0.104228, 0.103815, 0.107925, 0.100558, 0.099451]
num_threads_4 = [0.196461, 0.159318, 0.140861, 0.126009, 0.126512, 0.120597, 0.117970, 0.118002, 0.119110, 0.120242, 0.122109, 0.116227, 0.117665, 0.121882, 0.118960]

# Plotting all curves in the same plot
plt.figure(figsize=(12, 8))

plt.plot(thresholds, num_threads_1, 'bo-', label='Num Threads: 1')
plt.plot(thresholds, num_threads_2, 'go-', label='Num Threads: 2')
plt.plot(thresholds, num_threads_3, 'ro-', label='Num Threads: 3')
plt.plot(thresholds, num_threads_4, 'co-', label='Num Threads: 4')

plt.xlabel('Threshold')
plt.ylabel('Execution Time (seconds)')
plt.title('Execution Time vs. Threshold')
plt.legend()

plt.show()
