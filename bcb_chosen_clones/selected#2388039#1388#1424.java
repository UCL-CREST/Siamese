        public T1[] adjust() throws E {
            init();
            int m, o, r = 0;
            Object[] ret = new Object[maxLength];
            for (o = 0; o < original.length && oMappings[o] < 0; o++) if (remove(o, -1, ret, r)) r++;
            for (m = 0; m < modifier.length; m++) {
                o = mMappings[m];
                if (o >= 0) {
                    if (set(o, m, ret, r)) r++;
                    for (o++; o < original.length && oMappings[o] < 0; o++) {
                        if (ret[r] != null) {
                            Object temp = ret[r];
                            for (int r2 = r; r < ret.length - 1 && temp != null; r2++) {
                                Object temp2 = ret[r2 + 1];
                                ret[r2 + 1] = temp;
                                temp = temp2;
                            }
                        }
                        if (remove(o, m + 1, ret, r)) r++;
                    }
                } else {
                    if (ret[r] != null) {
                        Object temp = ret[r];
                        for (int r2 = r; r < ret.length - 1 && temp != null; r2++) {
                            Object temp2 = ret[r2 + 1];
                            ret[r2 + 1] = temp;
                            temp = temp2;
                        }
                    }
                    if (add(m, ret, r)) r++;
                }
            }
            for (int i = 0; i < r; i++) if (ret[i] == NULL) ret[i] = null;
            T1[] actualRet = (T1[]) Array.newInstance(original.getClass().getComponentType(), r);
            System.arraycopy(ret, 0, actualRet, 0, r);
            return actualRet;
        }
