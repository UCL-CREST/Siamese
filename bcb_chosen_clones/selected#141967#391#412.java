    private static void addRecursivelyToJar(File src, String prefix, ZipOutputStream jout) throws IOException {
        if (src.isDirectory()) {
            prefix = prefix + src.getName() + "/";
            ZipEntry entry = new ZipEntry(prefix);
            entry.setTime(src.lastModified());
            entry.setMethod(ZipEntry.STORED);
            entry.setSize(0L);
            entry.setCrc(0L);
            jout.putNextEntry(entry);
            jout.closeEntry();
            File files[] = src.listFiles();
            for (int i = 0; i < files.length; i++) addRecursivelyToJar(files[i], prefix, jout);
        } else {
            ZipEntry entry = new ZipEntry(prefix + src.getName());
            entry.setTime(src.lastModified());
            jout.putNextEntry(entry);
            FileInputStream in = new FileInputStream(src);
            IOUtil.transferStreamData(in, jout);
            in.close();
            jout.closeEntry();
        }
    }
