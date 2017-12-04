count = 0;
for d in range(2):
    for j in range(2):
        for k in range(2):
            for p in range(2):
                for s in range(2):
                    for v in range(2):
                        for w in range(2):
                            print(count)
                            print('\"', end='')
                            if d == 1:
                                print('d', end='')
                            if j == 1:
                                print('j', end='')
                            if k == 1:
                                print('k', end='')
                            if p == 1:
                                print('p', end='')
                            if s == 1:
                                print('s', end='')
                            if v == 1:
                                print('v', end='')
                            if w == 1:
                                print('w', end='')
                            print('\", ', end='')
                            count += 1
                            print()