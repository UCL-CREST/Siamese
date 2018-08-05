    public static void makeArchiveFromFolder(@NotNull File folder, File toZipFile, int method) throws IOException, IllegalArgumentException {
        if (folder == null) {
            throw new IllegalArgumentException("the folder can't be null!");
        }
        if (!folder.exists()) {
            throw new IllegalArgumentException("the folder = " + folder.getAbsolutePath() + " don't exist!");
        }
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("the folder = " + folder.getAbsolutePath() + " is a file and not a folder!");
        }
        BufferedInputStream origin = null;
        FileOutputStream dest = new FileOutputStream(toZipFile);
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
        out.setMethod(method);
        byte data[] = new byte[BUFFER];
        File files[] = folder.listFiles();
        for (int i = 0; i < files.length; i++) {
            FileInputStream fi = new FileInputStream(files[i]);
            origin = new BufferedInputStream(fi, BUFFER);
            ZipEntry entry = new ZipEntry(files[i].getName());
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            origin.close();
        }
        out.close();
    }
