    public FieldMetaData findEmbeddedFmd(String name) {
        if (embeddedFmds == null) {
            return null;
        }
        int low = 0;
        int high = embeddedFmds.length - 1;
        name = this.name + "/" + name;
        while (low <= high) {
            int mid = (low + high) / 2;
            FieldMetaData midVal = embeddedFmds[mid];
            String name2 = midVal.name;
            int cmp = name2.compareTo(name);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return midVal;
            }
        }
        return null;
    }
