    private static void zipRecursively(ZipOutputStream zip, File file, String parentName) throws IOException {
        if (file.isFile()) {
            byte[] buf = new byte[1024];
            FileInputStream in = new FileInputStream(file);
            zip.putNextEntry(new ZipEntry(parentName + File.separator + file.getName()));
            int len;
            while ((len = in.read(buf)) > 0) {
                zip.write(buf, 0, len);
            }
            in.close();
        } else {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    zipRecursively(zip, f, parentName + File.separator + file.getName());
                }
            }
        }
    }
