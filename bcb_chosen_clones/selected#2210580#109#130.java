    private static StringBuffer getMACAddressDescriptor(NetworkInterface ni) {
        byte[] haddr;
        try {
            haddr = ni.getHardwareAddress();
        } catch (Throwable t) {
            haddr = null;
        }
        StringBuffer b = new StringBuffer();
        if (haddr != null) {
            for (int i = 0; i < haddr.length; i++) {
                if (b.length() > 0) {
                    b.append("-");
                }
                String hex = Integer.toHexString(0xff & haddr[i]);
                if (hex.length() == 1) {
                    b.append('0');
                }
                b.append(hex);
            }
        }
        return b;
    }
