from matplotlib import pyplot as plt
from numpy import arange

# size = list(range(1, 22))
# cloplagm = [0.6078, 0.6887, 0.7247, 0.7263, 0.7198, 0.7222, 0.7345, 0.7353, 0.7361, 0.7345,
#             0.7328, 0.7328, 0.7304, 0.7198, 0.7222, 0.7132, 0.7132, 0.7083, 0.6920, 0.6871, 0.6838]
# cloplagf = [0.6030, 0.7790, 0.7910, 0.7860, 0.7970, 0.8000, 0.8030, 0.8000, 0.7960, 0.8060,
#             0.8140, 0.8160, 0.8230, 0.8130, 0.8040, 0.8070, 0.7990, 0.7910, 0.7810, 0.7790, 0.7770]
# soco = [0.6520, 0.8640, 0.9043, 0.9290, 0.9333, 0.9464, 0.9507, 0.9551, 0.9551, 0.9638,
#         0.9681, 0.9681, 0.9724, 0.9768, 0.9768, 0.9768, 0.9768, 0.9812, 0.9812, 0.9812, 0.9768]
#
# fig = plt.figure()
# ax = fig.add_subplot(111)
# ax.plot(size, cloplagm, c="b", marker="s", label="Ragkhitwetsagul (Method)")
# ax.plot(size, cloplagf, c="r", marker="x", label="Ragkhitwetsagul (File)")
# ax.plot(size, soco, c="g", marker="o", label="SOCO (File)")
# # plt.yscale('log', basey=10)
# # plt.xscale('log', basex=10)
# plt.xticks(arange(22))
# plt.xlabel("n-gram size")
# plt.ylabel("ARP")
# plt.legend(loc=4)
# # plt.show()
#
# fig = ax.get_figure()
# fig.savefig('ngram_arp.pdf', bbox_inches='tight')

size = list(range(1, 21))
# cloplagm = [0.6745, 0.7626, 0.7972, 0.8039, 0.8050, 0.8062, 0.8105, 0.8116, 0.8134, 0.8125,
#             0.8113, 0.8098, 0.8119, 0.8075, 0.8053, 0.8022, 0.7981, 0.7937, 0.7818, 0.7788, 0.7737]
# cloplagf = [0.7207, 0.8644, 0.8727, 0.8642, 0.8712, 0.8796, 0.8831, 0.8850, 0.8890, 0.8963,
#             0.9013, 0.9000, 0.8988, 0.8919, 0.8852, 0.8875, 0.8824, 0.8774, 0.8738, 0.8725, 0.8721]
# soco = [0.6992, 0.8946, 0.9327, 0.9456, 0.9517, 0.9590, 0.9620, 0.9656, 0.9701, 0.9740,
#         0.9776, 0.9791, 0.9822, 0.9835, 0.9851, 0.9851, 0.9855, 0.9874, 0.9874, 0.9873, 0.9858]
bellon = [0.052409129332206254, 0.13693998309382924, 0.17751479289940827, 0.19188503803888418, 0.19864750633981404,
          0.2037193575655114, 0.2037193575655114, 0.20710059171597633, 0.21048182586644126, 0.21301775147928995,
          0.2147083685545224, 0.21301775147928995, 0.21386306001690616, 0.21555367709213863, 0.2172442941673711,
          0.21555367709213863, 0.21386306001690616, 0.21132713440405748, 0.21132713440405748, 0.21132713440405748]

fig = plt.figure()
ax = fig.add_subplot(111)
# ax.plot(size, cloplagm, c="b", marker="s", label="Ragkhitwetsagul (Method)")
# ax.plot(size, cloplagm, c="b", marker="s", label="Ragkhitwetsagul (Method)")
# ax.plot(size, cloplagf, c="r", marker="x", label="Ragkhitwetsagul (File)")
ax.plot(size, bellon, c="b", marker="s", label="Bellon's")
# ax.plot(size, soco, c="g", marker="o", label="SOCO (File)")
# plt.yscale('log', basey=10)
# plt.xscale('log', basex=10)
plt.xticks(arange(21))
plt.xlabel("n-gram size")
plt.ylabel("MRR")
# plt.legend(loc=4)
# plt.show()

fig = ax.get_figure()
fig.savefig('ngram_mrr.pdf', bbox_inches='tight')