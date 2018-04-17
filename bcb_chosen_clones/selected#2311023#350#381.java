        private Target randomize(Target t) {
            if (t != null && t.getNext() != null) {
                ArrayList list = new ArrayList();
                while (t != null) {
                    list.add(t);
                    t = t.getNext();
                }
                Target[] arr = (Target[]) list.toArray(new Target[list.size()]);
                if (true) {
                    Arrays.sort(arr, new Comparator() {

                        public int compare(Object lhs, Object rhs) {
                            return ((Target) rhs).name.compareTo(((Target) lhs).name);
                        }
                    });
                    for (int i = 0; i < arr.length; ++i) {
                        t = arr[i].setNext(t);
                    }
                }
                if (params.random != null) {
                    t = null;
                    Random r = params.random;
                    for (int i = arr.length; --i >= 1; ) {
                        int x = r.nextInt(i + 1);
                        t = arr[x].setNext(t);
                        arr[x] = arr[i];
                    }
                    t = arr[0].setNext(t);
                }
            }
            return t;
        }
