    public int createData(String type) {
        try {
            Class col = ClassLoader.getSystemClassLoader().loadClass(type);
            Constructor colcon = col.getConstructor(new Class[] {});
            data[dataidx] = colcon.newInstance(new Object[] {});
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
        return dataidx++;
    }
