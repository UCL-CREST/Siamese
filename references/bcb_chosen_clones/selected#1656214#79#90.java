    public String getAttributeValue(String attName) {
        int min = 0;
        int max = attributes.length - 1;
        int curr;
        int cr;
        while (min <= max) {
            curr = (min + max) / 2;
            cr = attName.compareTo(attributes[curr].getKey());
            if (cr == 0) return attributes[curr].getValue(); else if (cr < 0) max = curr - 1; else min = curr + 1;
        }
        return null;
    }
