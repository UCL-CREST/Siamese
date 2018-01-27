from matplotlib import pyplot as plt
import numpy as np
import matplotlib


x1 = [ 22, 50, 178, 423, 1723, 6601, 28030, 111190, 442403, 1771183]
y1 = [ 4.13, 2.95, 4.62, 8.4, 11.94, 36.22, 172.90, 614.90, 2077.04, 9089]

# x2 = [ 423, 6601, 111190, 1771183]
# y2 = [ 10, 40.22, 704.90, 9989]

fig = plt.figure()
ax = fig.add_subplot(111)

ax.plot(x1, y1, c="b", marker="s", label="Siamese")
# ax.plot(x2, y2, c="r", marker="x", label="SourcererCC")
plt.xscale('log', basex=2)
plt.xlabel("No. of methods")
plt.ylabel("Indexing time (s)")
plt.legend(loc=2)
plt.show()

fig = ax.get_figure()
fig.savefig('index.pdf', bbox_inches='tight')