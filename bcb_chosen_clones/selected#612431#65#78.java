    public static List<Holder> getOrdering(double[][] s, Holder[][] h) {
        List<Holder> list = new ArrayList<Holder>();
        for (int i = 0; i < s.length - 1; i++) {
            for (int j = i + 1; j < s.length; j++) {
                h[i][j] = new Holder(i, j, s[i][j]);
                h[j][i] = h[i][j];
                list.add(h[i][j]);
            }
        }
        Collections.sort(list);
        for (Holder ho : list) {
        }
        return list;
    }
