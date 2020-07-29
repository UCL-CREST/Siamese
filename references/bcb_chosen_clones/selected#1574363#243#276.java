    public static byte[] getSystemStateHash() {
        MessageDigest sha1;
        try {
            sha1 = MessageDigest.getInstance("SHA1");
        } catch (Exception e) {
            throw new Error("Error in RandomSeed, no sha1 hash");
        }
        sha1.update((byte) System.currentTimeMillis());
        sha1.update((byte) Runtime.getRuntime().totalMemory());
        sha1.update((byte) Runtime.getRuntime().freeMemory());
        sha1.update(stackDump(new Throwable()));
        try {
            Properties props = System.getProperties();
            Enumeration names = props.propertyNames();
            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();
                sha1.update(name.getBytes());
                sha1.update(props.getProperty(name).getBytes());
            }
        } catch (Throwable t) {
            sha1.update(stackDump(t));
        }
        sha1.update((byte) System.currentTimeMillis());
        try {
            sha1.update(InetAddress.getLocalHost().toString().getBytes());
        } catch (Throwable t) {
            sha1.update(stackDump(t));
        }
        sha1.update((byte) System.currentTimeMillis());
        Runtime.getRuntime().gc();
        sha1.update((byte) Runtime.getRuntime().freeMemory());
        sha1.update((byte) System.currentTimeMillis());
        return sha1.digest();
    }
