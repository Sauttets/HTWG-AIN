import matplotlib.pyplot as plt
import numpy as np
arr = [43.94, 79.59, 89.47, 92.43, 43.93, 79.59, 89.47, 92.43, 93.77, 95.19, 95.88, 97.08, 97.61, 97.93]
yplot = np.asarray(arr)
xplot = np.arange(len(yplot))
plt.plot(xplot, yplot)
plt.xlabel('Cache size in pages')
plt.ylabel('Hit rate in percent')
plt.ylim([0, 100])
plt.show()
