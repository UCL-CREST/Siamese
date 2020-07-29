    public static void zipFile(File destFile, File[] fileA) throws Exception {
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(destFile));
            ZipEntry entry;
            FileInputStream fis = null;
            for (File file : fileA) {
                entry = new ZipEntry(file.getName());
                zos.putNextEntry(entry);
                try {
                    fis = new FileInputStream(file);
                    int read = 0;
                    byte[] bytes;
                    while (read != -1) {
                        bytes = new byte[1024];
                        read = fis.read(bytes);
                        if (read != -1) {
                            zos.write(bytes);
                        }
                    }
                    zos.flush();
                } finally {
                    if (fis != null) {
                        fis.close();
                    }
                }
            }
            zos.flush();
        } finally {
            if (zos != null) {
                zos.close();
            }
        }
    }
