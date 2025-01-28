import random
import subprocess

# Array of terminal commands
commands = [
    "./fork.py -s10",
    "./fork.py -t",
    "./fork.py -A a+b,b+c,c+d,c+e,c-",
    "./process-run.py -l 4:100,1:0",
    "./process-run.py -l 5:100,5:100",
    "./mlfq.py -n 1 -q 10 -l 0,100,0:0,100,0:0,100,0",
    "./scheduler.py -p FIFO -j 3 -l 200,200,200",
    "./scheduler.py -p SJF -j 3 -l 200,200,200",
    "./scheduler.py -p RR -j 3 -l 200,200,200",
    "./scheduler.py -p RR -j 3 -l 100,200,300",
    "./relocation.py",
    "./relocation.py -s 1 -n 10 -l 100",
    "./segmentation.py -a 128 -p 512 -b 0 -l 20 -B 512 -L 20 -s 0 ",
    "./segmentation.py -a 128 -p 512 -b 0 -l 20 -B 512 -L 20 -s 1 ",
    "./malloc.py -n 10 -H 0 -p BEST -s 0",
    "./malloc.py -n 10 -H 0 -p WORST -s 0",
    "./malloc.py -n 10 -H 0 -p FIRST -s 0",
    "./paging-linear-translate.py -P 1k -a 16k -p 32k -v -u 0",
    "./paging-linear-translate.py -P 8 -a 32 -p 1024 -v -s 1",
    "./paging-multilevel-translate.py -s 1",
    "./paging-multilevel-translate.py -s 2",
    "./paging-policy.py -s 0 -n 10 -p FIFO",
    "./paging-policy.py -s 0 -n 10 -p LRU",
    "./paging-policy.py -s 0 -n 10 -p OPT",
    "./x86.py -p loop.s -t 1 -i 100 -R dx",
    "./x86.py -p loop.s -t 2 -i 100 -a dx=3,dx=3 -R dx",
    "./x86.py -p loop.s -t 2 -i 3 -r -a dx=3,dx=3 -R dx",
    "./x86.py -p flag.s -R ax,bx -M flag,count",
    "./x86.py -p flag.s -R ax,bx -M flag,count -a bx=2,bx=2",
    "./raid.py -L 0",
    "./raid.py -L 1",
    "./raid.py -L 4",
    "./raid.py -L 5",
    "./disk.py -a 0",
    "./disk.py -a 10,11,12,13",
    "./disk.py -R 0.5"
]

# Select a random command
random_command = random.choice(commands)

# Execute the command
subprocess.call("Python3 " + random_command, shell=True)
