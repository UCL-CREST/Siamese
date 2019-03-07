from matplotlib import pyplot as plt
from numpy import arange


max = 15
size = list(range(1, max + 1))
labels = ['0', '1', '2', '3', '01', '02', '03', '12', '13', '23', '012', '013', '023', '123', '0123']
cloplag_m = [0.5703, 0.6944, 0.6993, 0.7386, 0.6275,
             0.6977, 0.6936, 0.7435, 0.7557, 0.7361,
             0.6953, 0.6985, 0.7222, 0.7614, 0.7198]
fig = plt.figure()
ax = fig.add_subplot(111)
# ax.plot(size, cloplagm, c="b", marker="s", label="Ragkhitwetsagul (Method)")
# ax.plot(size, cloplagm, c="b", marker="s", label="Ragkhitwetsagul (Method)")
# ax.plot(size, cloplagf, c="r", marker="x", label="Ragkhitwetsagul (File)")
ax.plot(size, cloplag_m, c="red", linestyle="-", label="Rag. (M)")
ax.set_xticklabels(labels)
# ax.plot(size, bellon_t2, c="green", linestyle="-", label="renamed")
# ax.plot(size, bellon_t3, c="blue", linestyle="-", label="full renamed")
# ax.plot(size, soco, c="g", marker="o", label="SOCO (File)")
# plt.yscale('log', basey=10)
# plt.xscale('log', basex=10)
# plt.xticks(list(arange(0, max + 1, 2)))
# plt.xticks(list(arange(1, 51, 2)) + [60, 70])
# plt.yticks(arange(0.0, 0.41, 0.05))
plt.xlabel("Combination")
plt.ylabel("ARP")
plt.legend(loc=4)
plt.show()

fig = ax.get_figure()
# fig.set_size_inches(12, 3)
fig.savefig('arp.pdf', bbox_inches='tight')