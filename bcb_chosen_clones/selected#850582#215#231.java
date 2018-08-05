    private void addToJar(JarOutputStream jos, String name, long size, long time, InputStream is) throws IOException {
        ZipEntry newEntry = new ZipEntry(name);
        if (size != -1) {
            newEntry.setSize(size);
        }
        newEntry.setTime(time);
        jos.putNextEntry(newEntry);
        byte buffer[] = new byte[1024];
        int copied;
        do {
            copied = is.read(buffer);
            if (copied > 0) {
                jos.write(buffer, 0, copied);
            }
        } while (copied > 0);
        jos.closeEntry();
    }
