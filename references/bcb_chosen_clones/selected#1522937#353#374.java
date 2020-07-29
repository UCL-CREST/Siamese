    protected int findNamePoint(String name, int start) {
        int i = 0;
        if (nodes != null) {
            int first = start;
            int last = nodes.size() - 1;
            while (first <= last) {
                i = (first + last) / 2;
                int test = name.compareTo(((Node) (nodes.elementAt(i))).getNodeName());
                if (test == 0) {
                    return i;
                } else if (test < 0) {
                    last = i - 1;
                } else {
                    first = i + 1;
                }
            }
            if (first > i) {
                i = first;
            }
        }
        return -1 - i;
    }
