    public static void transfer(File src, File dest, boolean removeSrc) throws FileNotFoundException, IOException {
        Log.warning("source: " + src);
        Log.warning("dest: " + dest);
        if (!src.canRead()) {
            throw new IOException("can not read src file: " + src);
        }
        if (!dest.getParentFile().isDirectory()) {
            if (!dest.getParentFile().mkdirs()) {
                throw new IOException("failed to make directories: " + dest.getParent());
            }
        }
        FileInputStream fis = new FileInputStream(src);
        FileOutputStream fos = new FileOutputStream(dest);
        FileChannel fcin = fis.getChannel();
        FileChannel fcout = fos.getChannel();
        Log.warning("starting transfer from position " + fcin.position() + " to size " + fcin.size());
        fcout.transferFrom(fcin, 0, fcin.size());
        Log.warning("closing streams and channels");
        fcin.close();
        fcout.close();
        fis.close();
        fos.close();
        if (removeSrc) {
            Log.warning("deleting file " + src);
            src.delete();
        }
    }
