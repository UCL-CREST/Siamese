    private static void zip(String baseDir, File source, ZipOutputStream zout) throws IOException {
        ZipEntry entry = null;
        if (baseDir == null || baseDir.equals(".") || baseDir.equals("./")) {
            baseDir = "";
        }
        if (source.isDirectory()) {
            entry = new ZipEntry(baseDir + source.getName() + "/");
        } else {
            entry = new ZipEntry(baseDir + source.getName());
        }
        zout.putNextEntry(entry);
        if (!source.isDirectory()) {
            byte data[] = new byte[BUFFER];
            FileInputStream fis = new FileInputStream(source);
            BufferedInputStream origin = new BufferedInputStream(fis, BUFFER);
            int count;
            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                zout.write(data, 0, count);
            }
            fis.close();
            origin.close();
        } else {
            File files[] = source.listFiles();
            for (int i = 0; i < files.length; i++) {
                zip(entry.getName(), files[i], zout);
            }
        }
    }
