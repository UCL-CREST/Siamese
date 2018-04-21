from matplotlib import pyplot as plt
import numpy as np
import matplotlib

# Index time plot

methods = [22, 50, 178, 423, 1723, 6601, 28030, 111190, 442403, 1771183, 4870113]
nicad_methods = [22, 50, 178, 423, 1723, 6601, 28030, 111190]
iclones_methods = [22, 50, 178, 1723, 6601, 28030]
jplag_methods = [22, 50, 178, 423, 1723, 6601, 28030]
simian_methods = [22, 50, 178, 423, 1723, 6601, 28030]
deckard_methods = [22, 50, 178, 423, 1723, 6601, 28030, 111190]
ccfx_methods = [22, 50, 178, 423, 1723, 6601, 28030]

siamese = [4.8, 5.26, 9.54, 19.21, 23.16, 97.07, 576.78, 1999.95, 6195, 25699, 74304]
scc = [(0.58 + 2.03), (0.88 + 0.68), (2.68 + 0.98), (3.81 + 1.37), (9.18 + 2.15),
       (28.40 + 4.96), (110.09 + 16.78), (432.52 + 60.96), (1694.23 + 219.8), (6786 + 870.08),
       (18606 + 2348.9)]
nicad = [0.34, 0.66, 2.21, 7.89, 26.50, 84.25, 574.91, 6992]
iclones = [0.59, 0.67, 3.06, 4.82, 14.97, 166.95]
jpag = [0.37, 0.91, 0.89, 1.02, 3.83, 57.92, 890.08]
simian = [0.25, 0.30, 0.47, 2.14, 25.90, 401.93, 6506]
deckard = [1.56, 3.51, 8.29, 18.15, 109.66, 1159.29, 26051.83, 0]
ccfx = [0.18, 0.21, 0.34, 1.62, 5.72, 35.04, 2769.78]

# # seconds
# fig = plt.figure()
# ax = fig.add_subplot(111)
# ax.plot(methods, siamese, c="b", marker="s", label="Siamese")
# ax.plot(methods, scc, c="r", marker="x", label="SourcererCC")
# ax.plot(methods, nicad, c="g", marker="o", label="NiCad")
# plt.yscale('log', basey=10)
# plt.xscale('log', basex=10)
# plt.xlabel("No. of methods")
# plt.ylabel("Indexing time (s)")
# plt.ylim(ymax=100000)
# plt.legend(loc=2)
# plt.show()
#
# fig = ax.get_figure()
# fig.savefig('index.pdf', bbox_inches='tight')

# minutes
siamese_m = [x / 60 for x in siamese]
scc_m = [x / 60 for x in scc]
nicad_m = [x / 60 for x in nicad]
iclones_m = [x / 60 for x in iclones]
jplag_m = [x / 60 for x in jpag]
simian_m = [x / 60 for x in simian]
deckard_m = [x / 60 for x in deckard]
ccfx_m = [x / 60 for x in ccfx]

fig = plt.figure()
# ax = fig.add_subplot(111)
plt.plot(methods, siamese_m, c="b", marker="s", label="Siamese")
plt.plot(methods, scc_m, c="r", marker="x", label="SourcererCC")
plt.plot(ccfx_methods, ccfx_m, c="#5B2C6F", marker="p", linestyle=":", label="CCFinderX")
plt.plot(deckard_methods, deckard_m, c="k", marker=">", linestyle=":", label="Deckard")
plt.plot(iclones_methods, iclones_m, c="c", marker="v", linestyle=":", label="iClones")
plt.plot(jplag_methods, jplag_m, c="m", marker="^", linestyle=":", label="JPlag")
plt.plot(nicad_methods, nicad_m, c="g", marker="o", linestyle=":", label="NiCad")
plt.plot(simian_methods, simian_m, c="y", marker="<", linestyle=":", label="Simian")
plt.yscale('log', basey=10)
plt.xscale('log', basex=10)
plt.xlabel("No. of methods")
plt.ylabel("Indexing time (m)")
# plt.ylim(ymax=1500)
plt.legend(loc=2)
# plt.show()
# fig = ax.get_figure()
plt.savefig('../index_m.pdf', bbox_inches='tight')


# Query time plot

methods = [22, 50, 178, 423, 1723, 6601, 28030, 111190, 442403, 1771183, 4870113]
# siamese = [1.539, 1.5405, 1.5526, 1.5244, 1.5818, 1.6382, 1.71, 1.8626, 1.9711, 2.3409, 3.30]
siamese = [1.7297, 1.7503, 1.7412, 1.7682, 1.7966, 2.06, 2.5026, 2.6474, 5.3371, 5.5473, 9.2843]
scc = [1.3006, 1.3773, 1.3937, 1.3412, 1.4536, 1.4913, 2.0718, 3.3621, 9.2613, 28.3169, 60.9813]
scc_m = [x + 0.2354 for x in scc] # add avg tokenisation time

print(scc_m)

fig = plt.figure()
ax = fig.add_subplot(111)
ax.plot(methods, siamese, c="b", marker="s", label="Siamese")
ax.plot(methods, scc_m, c="r", marker="x", label="SourcererCC")
plt.yscale('log', basey=10)
plt.xscale('log', basex=10)
plt.ylim(ymax=100)
plt.xlabel("No. of methods in the index")
plt.ylabel("Average query response time (s)")
plt.legend(loc=2)
plt.show()

fig = ax.get_figure()
fig.savefig('../query.pdf', bbox_inches='tight')