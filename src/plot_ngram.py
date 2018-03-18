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

max = 20
size = list(range(1, max + 1))
# size = list(range(1, 51)) + [60, 70]
# cloplagm = [0.6745, 0.7626, 0.7972, 0.8039, 0.8050, 0.8062, 0.8105, 0.8116, 0.8134, 0.8125,
#             0.8113, 0.8098, 0.8119, 0.8075, 0.8053, 0.8022, 0.7981, 0.7937, 0.7818, 0.7788, 0.7737]
# cloplagf = [0.7207, 0.8644, 0.8727, 0.8642, 0.8712, 0.8796, 0.8831, 0.8850, 0.8890, 0.8963,
#             0.9013, 0.9000, 0.8988, 0.8919, 0.8852, 0.8875, 0.8824, 0.8774, 0.8738, 0.8725, 0.8721]
# soco = [0.6992, 0.8946, 0.9327, 0.9456, 0.9517, 0.9590, 0.9620, 0.9656, 0.9701, 0.9740,
#         0.9776, 0.9791, 0.9822, 0.9835, 0.9851, 0.9851, 0.9855, 0.9874, 0.9874, 0.9873, 0.9858]
bellon = [0.009732360097323601, 0.07785888077858881, 0.09975669099756691, 0.10948905109489052, 0.12895377128953772,
          0.1411192214111922, 0.1411192214111922, 0.1411192214111922, 0.1411192214111922, 0.1386861313868613,
          0.145985401459854, 0.145985401459854, 0.145985401459854, 0.14841849148418493, 0.13381995133819952,
          0.13138686131386862, 0.1386861313868613, 0.1362530413625304, 0.1386861313868613, 0.1386861313868613]
          # 0.1944209636517329, 0.18765849535080303]

fig = plt.figure()
ax = fig.add_subplot(111)
# ax.plot(size, cloplagm, c="b", marker="s", label="Ragkhitwetsagul (Method)")
# ax.plot(size, cloplagm, c="b", marker="s", label="Ragkhitwetsagul (Method)")
# ax.plot(size, cloplagf, c="r", marker="x", label="Ragkhitwetsagul (File)")
ax.plot(size, bellon, c="black", marker=".", label="Bellon's")
# ax.plot(size, soco, c="g", marker="o", label="SOCO (File)")
# plt.yscale('log', basey=10)
# plt.xscale('log', basex=10)
plt.xticks(list(arange(0, max + 1, 2)))
# plt.xticks(list(arange(1, 51, 2)) + [60, 70])
plt.yticks(arange(0.0, 0.19, 0.04))
plt.xlabel("n-gram size")
plt.ylabel("MRR")
# plt.legend(loc=4)
plt.show()

fig = ax.get_figure()
fig.set_size_inches(6, 3)
fig.savefig('ngram_mrr.pdf', bbox_inches='tight')