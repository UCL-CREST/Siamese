from matplotlib import pyplot as plt
import numpy as np
import matplotlib

# Index time plot

methods = [22, 50, 178, 423, 1723, 6601, 28030, 111190, 442403, 1771183, 4870113]
siamese = [4.13, 2.95, 4.62, 8.4, 11.94, 36.22, 172.90, 614.90, 2077.04, 9089, 23606]
scc = [(0.58 + 2.03), (0.88 + 0.68), (2.68 + 0.98), (3.81 + 1.37), (9.18 + 2.15), (28.40 + 4.96),
       (110.09 + 16.78), (432.52 + 60.96), (1694.23 + 219.8), (6786 + 870.08), (18606 + 2348.9)]
# seconds
fig = plt.figure()
ax = fig.add_subplot(111)
ax.plot(methods, siamese, c="b", marker="s", label="Siamese")
ax.plot(methods, scc, c="r", marker="x", label="SourcererCC")
plt.yscale('log', basey=10)
plt.xscale('log', basex=10)
plt.xlabel("No. of methods")
plt.ylabel("Indexing time (s)")
plt.ylim(ymax=100000)
plt.legend(loc=2)
plt.show()