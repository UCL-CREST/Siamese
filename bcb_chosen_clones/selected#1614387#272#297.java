    public boolean renameTo(File dest) throws IOException {
        if (dest == null) {
            throw new NullPointerException("dest");
        }
        if (!file.renameTo(dest)) {
            FileInputStream inputStream = new FileInputStream(file);
            FileOutputStream outputStream = new FileOutputStream(dest);
            FileChannel in = inputStream.getChannel();
            FileChannel out = outputStream.getChannel();
            long destsize = in.transferTo(0, size, out);
            in.close();
            out.close();
            if (destsize == size) {
                file.delete();
                file = dest;
                isRenamed = true;
                return true;
            } else {
                dest.delete();
                return false;
            }
        }
        file = dest;
        isRenamed = true;
        return true;
    }
