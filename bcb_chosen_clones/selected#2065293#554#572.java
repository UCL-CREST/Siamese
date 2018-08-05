            public Object[] toArray(Object[] a) {
                Object[] base;
                if (a.length == 0) {
                    base = a;
                } else {
                    base = (Object[]) (Array.newInstance(a.getClass().getComponentType(), a.length));
                }
                base = entrySet.toArray(base);
                for (int i = 0; i < base.length; i++) {
                    base[i] = new EntryView((Map.Entry) base[i]);
                }
                if (base.length > a.length) {
                    a = base;
                } else {
                    System.arraycopy(base, 0, a, 0, base.length);
                    if (base.length < a.length) a[base.length] = null;
                }
                return a;
            }
