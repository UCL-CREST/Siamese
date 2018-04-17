    public int getLine(Address address) {
        if (offsets.length <= 0) {
            return -1;
        }
        Address addr = address.newAddress(segment, 0);
        int offset = address.getLinearAddress() - addr.getLinearAddress();
        if (offset < (offsets[0] & 0xffff) || offset > (offsets[offsets.length - 1] & 0xffff)) {
            return -1;
        }
        int s = 0;
        int e = offsets.length;
        while (s < e) {
            int i = (e + s) / 2;
            int off = (offsets[i] & 0xffff);
            if (offset > off) {
                s = i + 1;
            } else if (offset < off) {
                e = i;
            } else {
                return lines[i];
            }
        }
        return -1;
    }
