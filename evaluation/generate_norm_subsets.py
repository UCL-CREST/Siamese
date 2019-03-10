from pprint import pprint as pp
from itertools import chain, combinations


def powerset(iterable):
    " powerset([1,2,3]) --> () (1,) (2,) (3,) (1,2) (1,3) (2,3) (1,2,3) "
    s = list(iterable)
    return chain.from_iterable(combinations(s, r) for r in range(len(s)+1))


pset = list(set(powerset({'d', 'j', 'k', 'o', 'p', 's', 'v', 'w'})))

pset_list = list()
for s in pset:
    n = ''
    for e in s:
        n += e
    pset_list.append(n)

print(len(pset_list))

for x in sorted(pset_list):
    print("\"" + x + "\"", end=', ')


