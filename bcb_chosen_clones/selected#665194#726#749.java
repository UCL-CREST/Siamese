    public RobotList<Percentage> sort_decr_Percentage(RobotList<Percentage> list, String field) {
        int length = list.size();
        Index_value[] distri = new Index_value[length];
        for (int i = 0; i < length; i++) {
            distri[i] = new Index_value(i, list.get(i).percent);
        }
        boolean permut;
        do {
            permut = false;
            for (int i = 0; i < length - 1; i++) {
                if (distri[i].value < distri[i + 1].value) {
                    Index_value a = distri[i];
                    distri[i] = distri[i + 1];
                    distri[i + 1] = a;
                    permut = true;
                }
            }
        } while (permut);
        RobotList<Percentage> sol = new RobotList<Percentage>(Percentage.class);
        for (int i = 0; i < length; i++) {
            sol.addLast(new Percentage(distri[i].value));
        }
        return sol;
    }
