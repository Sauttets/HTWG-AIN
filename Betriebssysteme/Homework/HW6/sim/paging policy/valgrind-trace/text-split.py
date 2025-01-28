import subprocess
import platform

PAGE_SIZE = 4096
import platform

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

subprocess.run("python3 paging-policy.py -f ls_trace_vpn.txt -c -N", shell=True)

