import subprocess
import platform
import argparse
import re
import matplotlib.pyplot as plt
import numpy as np

# Parse the command line argument
parser = argparse.ArgumentParser()
parser.add_argument('-p''--policy', type=str, help="Policy to use in the simulator")
parser.add_argument('-P', '--cachesize', type=int, help="Plot with cache size of x don't go over 10")
args = parser.parse_args()
policy = args.p__policy
plotsize = args.cachesize

print('Policy:', policy)
print('plotsize:', plotsize)

PAGE_SIZE = 4096

system = platform.system()

if system == 'Windows':
    import ctypes
    sysinfo = ctypes.windll.kernel32.GetSystemInfo()
    PAGE_SIZE = sysinfo.dwPageSize
elif system == 'Linux' or system == 'Darwin':
    import resource
    PAGE_SIZE = resource.getpagesize()
else:
    PAGE_SIZE = None
    print('Unknown operating system, PAGE_SIZE not set')

print('PAGE_SIZE:', PAGE_SIZE)

# Run the valgrind command and save the output to a file
subprocess.run("valgrind --tool=lackey --trace-mem=yes ls &> ls_trace.txt", shell=True)


# Convert virtual memory references to virtual page numbers in decimal and write to file
with open("ls_trace.txt", "r") as f:
    with open("ls_trace_vpn.txt", "w") as fout:
        for line in f:
            if not line.startswith("=") and "," in line:
                address = int(line[3:line.index(",")], 16)
                page_number = address // PAGE_SIZE
                fout.write(str(page_number) + "\n")


subprocess.run("rm ls_trace.txt", shell=True)

if plotsize is not None:
    x = 1
    with open('plot.txt', 'a') as p:
        while x <= plotsize:
            command = "python3 paging-policy.py -p " + policy + " -f ls_trace_vpn.txt -c -N -C " + str(x)
            subprocess.run(command + ' >> plot.txt', shell=True, stdout=p)
            x = x+1
else:
    command = "python3 paging-policy.py -p " + policy + " -f ls_trace_vpn.txt -c -N"
    subprocess.run(command, shell=True)

if plotsize is not None:
    hitrate_regex = re.compile(r'hitr[a-z]*te\s+(\d+\.\d+)')
    arr = []
    with open('plot.txt', 'r') as pp:
        contents = pp.read()

    code_blocks = contents.split('\n\n')
    for code_block in code_blocks:
        match = hitrate_regex.search(code_block)
        if match:
            hitrate = match.group(1)
            arr.append(hitrate)
            print(hitrate)

    if len(arr) != 0:
        f_arr = [float(numeric_string) for numeric_string in arr]
        yplot = np.asarray(f_arr)
        xplot = np.arange(len(yplot))
        plt.plot(xplot, yplot)
        plt.xlabel('Cache size in pages')
        plt.ylabel('Hit rate in percent')
        plt.ylim([0, 100])
        plt.title(policy)
        plt.show()

subprocess.run("rm ls_trace_vpn.txt", shell=True)
subprocess.run("rm plot.txt", shell=True)