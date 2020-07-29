    public static void constructZip(File file, ZipOutputStream out, String dir) throws IOException {
        BufferedInputStream origin = null;
        FileInputStream fi = null;
        byte data[] = new byte[BUFFER];
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                constructZip(files[i], out, dir + files[i].getName() + "/");
                continue;
            }
            try {
                fi = new FileInputStream(files[i]);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(dir + files[i].getName());
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
            } finally {
                if (fi != null) {
                    try {
                        fi.close();
                    } catch (IOException ex) {
                    }
                }
                if (origin != null) {
                    try {
                        origin.close();
                    } catch (IOException ex) {
                        LOGGER.error(ex.getMessage());
                    }
                }
            }
        }
    }
