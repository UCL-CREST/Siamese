from matplotlib import pyplot as plt
from numpy import arange


max = 40
size = list(range(1, max + 1))
decimal = 3

# extract time from the indexing time file using
# cat t2.txt | grep "wall clock" | sed -e s/Elapsed (wall clock) time (h:mm:ss or m:ss): //g

bellon_t1_2 = [
    0.48506527797917526, 0.539530414354991, 0.562222514659963, 0.5609855410602803, 0.5617102366189244,
    0.5552495181655861, 0.5576114346418937, 0.5655830857869764, 0.5676623922943271, 0.5693615464611934,
    0.5713079892449491, 0.5666722707300037, 0.5701131201534009, 0.5738031570537699, 0.5736494055162544,
    0.5708818778409779, 0.5671918409406089, 0.5647346118228632, 0.5622643371201161, 0.5638018524952699,
    0.5658006224829698, 0.5648051235169912, 0.5611150866166222, 0.5598850743164991, 0.5564820402861589,
    0.5583270587363434, 0.5479905625142385, 0.5460071676802901, 0.543547143080044, 0.543547143080044,
    0.5375508331169445, 0.5348448060566738, 0.5291252488611018, 0.5249142655747985, 0.526759284024983,
    0.5221467378995217, 0.5226676842854561, 0.5172248798574118, 0.5187213948225614, 0.5207742108052669
]

bellon_t1_index = [20.08, 21.16, 22.55, 23.19, 24.42, 25.50, 26.63, 27.53, 27.96, 29.12,
                   29.73, 30.45, 31.43, 31.65, 32.76, 33.78, 33.98, 35.03, 35.61, 36.14,
                   37.23, 37.86, 38.79, 39.41, 39.94, 40.44, 41.04, 41.66, 42.77, 43.25,
                   43.87, 44.80, 45.32, 46.05, 46.12, 47.04, 48.12, 48.33, 49.20, 49.54]

# JUST A DOUBLE CHECK
# bellon_t1_index2 = [20.00, 21.06, 22.53, 23.36, 24.73, 25.67, 26.07, 27.34, 28.04, 29.40,
#                     29.55, 30.54, 31.39, 31.76, 32.84, 33.47, 33.93, 34.30, 35.58, 36.25,
#                     37.02, 37.86, 38.34, 38.75, 39.63, 40.54, 41.29, 41.75, 42.62, 42.84,
#                     43.89, 43.97, 45.02, 45.80, 46.20, 46.91, 47.12, 48.30, 49.03, 49.47]

fig = plt.figure()
ax = fig.add_subplot(111)
plt.scatter(bellon_t1_index, bellon_t1_2, c='green')
plt.title('Representation 1')

i = 2
ax.annotate(str(i+1) + '-gram ('
            + str(round(bellon_t1_index[i], decimal)) + 's, '
            + str(round(bellon_t1_2[i], decimal)) + ')',
            xy=(bellon_t1_index[i], bellon_t1_2[i]),
            xytext=(bellon_t1_index[i], bellon_t1_2[i] - 0.05),
            arrowprops=dict(arrowstyle="->", connectionstyle="arc3"))

i = 3
ax.annotate(str(i+1) + '-gram ('
            + str(round(bellon_t1_index[i], decimal)) + 's, '
            + str(round(bellon_t1_2[i], decimal)) + ')',
            xy=(bellon_t1_index[i], bellon_t1_2[i]),
            xytext=(bellon_t1_index[i] + 3, bellon_t1_2[i] - 0.03),
            arrowprops=dict(arrowstyle="->", connectionstyle="arc3"))

i = 4
ax.annotate(str(i+1) + '-gram ('
            + str(round(bellon_t1_index[i], decimal)) + 's, '
            + str(round(bellon_t1_2[i], decimal)) + ')',
            xy=(bellon_t1_index[i], bellon_t1_2[i]),
            xytext=(bellon_t1_index[i] + 5, bellon_t1_2[i] - 0.02),
            arrowprops=dict(arrowstyle="->", connectionstyle="arc3"))
plt.xlabel('Index time (s)')
plt.ylabel('MRR')
# plt.show()
fig = ax.get_figure()
fig.set_size_inches(7, 5)
fig.savefig('ngram_mrr-time_1.pdf', bbox_inches='tight')

bellon_t2_2 = [
    0.20926548788012778, 0.2783360389627968, 0.3111033385169024, 0.32130126251700986, 0.3299005733759096,
    0.32924941694243365, 0.3285375401275418, 0.33184918022255677, 0.31768634570431026, 0.314620512196714,
    0.3189862262201814, 0.3156439272081704, 0.3161189530955977, 0.3215898760670963, 0.32101532680522193,
    0.3201347529487471, 0.33357301152789187, 0.3320540148319341, 0.33286037116912437, 0.3327460576800442,
    0.3324568966132416, 0.3331996926781877, 0.3150938846760374, 0.33410175420412036, 0.332505826419809,
    0.32833996334865834, 0.3274625365879889, 0.32622071285555215, 0.32308319574659466, 0.320611275520204,
    0.32030886852939255, 0.3211198665371589, 0.32086682105050046, 0.3201988442402352, 0.3212364559940088,
    0.3184693036585782, 0.31057660055621594, 0.3067445707959212, 0.28649715632610034, 0.2743999502301123
]

bellon_t2_index = [	19.31, 20.37, 20.37, 20.77, 20.92, 21.71, 22.15, 22.25, 22.81, 23.01,
                    23.35, 23.93, 24.23, 24.77, 25.03, 25.50, 25.51, 25.63, 26.11, 26.28,
                    26.32, 26.80, 27.33, 27.46, 27.59, 28.08, 28.13, 28.12, 28.48, 28.55,
                    28.62, 29.30, 29.41, 29.50, 30.12, 30.04, 30.23, 30.45, 30.60, 30.58]

# JUST A DOUBLE CHECK
# bellon_t2_index2 = [19.51, 19.73, 20.04, 20.81, 21.14, 21.52, 21.92, 22.40, 22.96, 23.19,
#                     23.50, 23.94, 24.25, 24.87, 25.02, 25.06, 25.50, 25.68, 25.90, 26.16,
#                     26.47, 26.92, 27.03, 27.30, 27.46, 27.48, 28.14, 28.45, 28.50, 28.40,
#                     29.19, 28.93, 29.54, 29.60, 29.51, 30.07, 30.23, 30.44, 30.62, 30.63]

fig = plt.figure()
ax = fig.add_subplot(111)
plt.scatter(bellon_t2_index, bellon_t2_2, c='blue')
plt.title('Representation 2')

# for i in range(0, 40):
#     if i in {2, 3, 4, 5, 6, 7}:
i = 3
ax.annotate(str(i+1) + '-gram ('
            + str(round(bellon_t2_index[i], decimal)) + 's, '
            + str(round(bellon_t2_2[i], decimal)) + ')',
            xy=(bellon_t2_index[i], bellon_t2_2[i]),
            xytext=(bellon_t2_index[i], bellon_t2_2[i] - 0.08),
            arrowprops=dict(arrowstyle="->", connectionstyle="arc3"))
i = 4
ax.annotate(str(i+1) + '-gram ('
            + str(round(bellon_t2_index[i], decimal)) + 's, '
            + str(round(bellon_t2_2[i], decimal)) + ')',
            xy=(bellon_t2_index[i], bellon_t2_2[i]),
            xytext=(bellon_t2_index[i] + 1.5, bellon_t2_2[i] - 0.06),
            arrowprops=dict(arrowstyle="->", connectionstyle="arc3"))

i = 5
ax.annotate(str(i+1) + '-gram ('
            + str(round(bellon_t2_index[i], decimal)) + 's, '
            + str(round(bellon_t2_2[i], decimal)) + ')',
            xy=(bellon_t2_index[i], bellon_t2_2[i]),
            xytext=(bellon_t2_index[i] + 3, bellon_t2_2[i] - 0.04),
            arrowprops=dict(arrowstyle="->", connectionstyle="arc3"))

plt.xlabel('Index time (s)')
plt.ylabel('MRR')
# plt.show()
fig = ax.get_figure()
fig.set_size_inches(7, 5)
fig.savefig('ngram_mrr-time_2.pdf', bbox_inches='tight')

bellon_t3_1 = [
    0.024086272288553298, 0.14935615907137875, 0.1636409987699643, 0.17793268436448548, 0.19060799466217285,
    0.19273901367338866, 0.19452370741369837, 0.1998104097475886, 0.20227447621597477, 0.198855169159512,
    0.20369323744482448, 0.20506770406500857, 0.2068035724061262, 0.20687853303960477, 0.19953563242606231,
    0.1980600680590136, 0.195632031195025, 0.19791123712225886, 0.19771076787304281, 0.19565218085103583,
    0.19663163223519062, 0.19597158798122577, 0.19613769330478942, 0.19250580430825256, 0.19017366880632297,
    0.1896857471807844, 0.1900430725279835, 0.19170979457969717, 0.18999052787556792, 0.19151775180341657,
    0.18913022393479725, 0.18982512485719882, 0.18548769872474322, 0.18052398353403104, 0.17700067413680656,
    0.17194265031769396, 0.1721783335962553, 0.16815874601416286, 0.16543812310618006, 0.1635686738022792
]

bellon_t3_index = [19.14, 19.41, 19.84, 19.98, 20.40, 20.64, 20.87, 21.25, 21.53, 21.96,
                   22.05, 22.51, 22.53, 23.13, 23.01, 23.20, 23.46, 23.74, 24.00, 24.23,
                   24.67, 24.22, 24.80, 24.98, 25.06, 25.11, 25.32, 25.13, 25.44, 25.89,
                   25.92, 25.96, 26.21, 26.25, 26.47, 26.43, 26.77, 26.94, 27.27, 27.28]

# JUST A DOUBLE CHECK
# bellon_t3_index2 = [19.17, 19.65, 19.79, 20.14, 20.26, 20.62, 20.82, 21.19, 21.61, 21.72,
#                     21.92, 22.13, 22.36, 23.24, 23.25, 23.28, 23.29, 23.65, 23.94, 23.89,
#                     24.10, 24.32, 24.66, 24.36, 24.99, 24.89, 25.12, 25.16, 25.24, 25.58,
#                     25.53, 26.31, 25.89, 25.73, 26.21, 26.29, 26.49, 26.79, 27.02, 26.82]

fig = plt.figure()
ax = fig.add_subplot(111)
plt.scatter(bellon_t3_index, bellon_t3_1, c='purple')
plt.title('Representation 3')

# for i in range(10, 15):
#     ax.annotate('(' + str(i+1) + '-gram, '
#                 + str(round(bellon_t3_1[i], 1)) + ', '
#                 + str(round(bellon_t3_index[i], 1)) + ')', xy=(bellon_t3_1[i], bellon_t3_index[i]))

#     if i in {2, 3, 4, 5, 6, 7}:

i = 7
ax.annotate(str(i+1) + '-gram ('
            + str(round(bellon_t3_index[i], decimal)) + ', '
            + str(round(bellon_t3_1[i], decimal)) + ')',
            xy=(bellon_t3_index[i], bellon_t3_1[i]),
            xytext=(bellon_t3_index[i], bellon_t3_1[i] - 0.09),
            arrowprops=dict(arrowstyle="->", connectionstyle="arc3"))

i = 8
ax.annotate(str(i+1) + '-gram ('
            + str(round(bellon_t3_index[i], decimal)) + ', '
            + str(round(bellon_t3_1[i], decimal)) + ')',
            xy=(bellon_t3_index[i], bellon_t3_1[i]),
            xytext=(bellon_t3_index[i] + 0.7, bellon_t3_1[i] - 0.06),
            arrowprops=dict(arrowstyle="->", connectionstyle="arc3"))

i = 10
ax.annotate(str(i+1) + '-gram ('
            + str(round(bellon_t3_index[i], decimal)) + ', '
            + str(round(bellon_t3_1[i], decimal)) + ')',
            xy=(bellon_t3_index[i], bellon_t3_1[i]),
            xytext=(bellon_t3_index[i] + 1.2, bellon_t3_1[i] - 0.03),
            arrowprops=dict(arrowstyle="->", connectionstyle="arc3"))

plt.xlabel('Index time (s)')
plt.ylabel('MRR')
# plt.show()
fig = ax.get_figure()
fig.set_size_inches(7, 5)
fig.savefig('ngram_mrr-time_3.pdf', bbox_inches='tight')
