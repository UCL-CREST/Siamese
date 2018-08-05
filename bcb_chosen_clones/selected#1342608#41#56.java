    public static FileChannel getFileChannel(Object o) throws IOException {
        Class c = o.getClass();
        try {
            Method m = c.getMethod("getChannel", null);
            return (FileChannel) m.invoke(o, null);
        } catch (IllegalAccessException x) {
        } catch (NoSuchMethodException x) {
        } catch (InvocationTargetException x) {
            if (x.getTargetException() instanceof IOException) throw (IOException) x.getTargetException();
        }
        if (o instanceof FileInputStream) return new MyFileChannelImpl((FileInputStream) o);
        if (o instanceof FileOutputStream) return new MyFileChannelImpl((FileOutputStream) o);
        if (o instanceof RandomAccessFile) return new MyFileChannelImpl((RandomAccessFile) o);
        Assert.UNREACHABLE(o.getClass().toString());
        return null;
    }
