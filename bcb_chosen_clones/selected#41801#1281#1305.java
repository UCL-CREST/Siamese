    @J2SIgnore
    public static SimpleSerializable parseInstance(String str, int start, SimpleFilter filter) {
        if (str == null || start < 0) return null;
        int length = str.length() - start;
        if (length <= 7 || !("WLL".equals(str.substring(start, start + 3)))) return null;
        int index = str.indexOf('#', start);
        if (index == -1) return null;
        String clazzName = str.substring(start + 6, index);
        if (filter != null) {
            if (!filter.accept(clazzName)) return null;
        }
        try {
            Class<?> runnableClass = Class.forName(clazzName);
            if (runnableClass != null) {
                Constructor<?> constructor = runnableClass.getConstructor(new Class[0]);
                Object obj = constructor.newInstance(new Object[0]);
                if (obj != null && obj instanceof SimpleSerializable) {
                    return (SimpleSerializable) obj;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return UNKNOWN;
    }
