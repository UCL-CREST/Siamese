    public static Vendor getVendor(String oui, String hardwareClass, String hardwareVersion) {
        try {
            Class c = vendors.get(oui);
            Vendor v;
            if (c == null) {
                v = new Vendor();
            } else {
                Constructor<?> m = c.getConstructor();
                v = (Vendor) m.newInstance();
            }
            v.hwversion = hardwareVersion;
            return v;
        } catch (Exception ex) {
            Logger.getLogger(Vendor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Vendor();
    }
