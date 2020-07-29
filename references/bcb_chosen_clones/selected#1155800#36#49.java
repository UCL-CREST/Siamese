    static ScaledRAFile newScaledRAFile(String name, boolean readonly, int multiplier, int type) throws FileNotFoundException, IOException {
        if (type == DATA_FILE_RAF) {
            return new ScaledRAFile(name, readonly, multiplier);
        } else {
            try {
                Class.forName("java.nio.MappedByteBuffer");
                Class c = Class.forName("org.hsqldb.NIOScaledRAFile");
                Constructor constructor = c.getConstructor(new Class[] { String.class, boolean.class, int.class });
                return (ScaledRAFile) constructor.newInstance(new Object[] { name, new Boolean(readonly), new Integer(multiplier) });
            } catch (Exception e) {
                return new ScaledRAFile(name, readonly, multiplier);
            }
        }
    }
