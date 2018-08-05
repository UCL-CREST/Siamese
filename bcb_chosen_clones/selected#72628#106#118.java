    private int searchField(int aCol, int aRow) {
        int kk = ivCrt.toLinearPos(aCol, aRow);
        int min = 0;
        int max = ivFields.size();
        int med = (max + min) / 2;
        XI5250Field field;
        while ((med < ivFields.size()) && (min <= max)) {
            field = ivFields.get(med);
            if (field.getSortKey() == kk) return med; else if (field.getSortKey() > kk) max = med - 1; else min = med + 1;
            med = (max + min) / 2;
        }
        return -(min + 1);
    }
