    private void zip(File srcFile, FilenameFilter filter, ZipEntry pentry, String prefix) throws IOException {
        ZipEntry entry;
        if (srcFile.isDirectory()) {
            if (pentry == null) {
                entry = new ZipEntry(srcFile.getName());
            } else {
                entry = new ZipEntry(pentry.getName() + "/" + srcFile.getName());
            }
            File[] files = srcFile.listFiles(filter);
            for (File f : files) {
                zip(f, filter, entry, prefix);
            }
        } else {
            if (pentry == null) {
                entry = new ZipEntry(prefix + srcFile.getName());
            } else {
                entry = new ZipEntry(pentry.getName() + "/" + prefix + srcFile.getName());
            }
            FileInputStream in;
            try {
                log.debug("读取文件：{}", srcFile.getAbsolutePath());
                in = new FileInputStream(srcFile);
                try {
                    zipOut.putNextEntry(entry);
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        zipOut.write(buf, 0, len);
                    }
                    zipOut.closeEntry();
                } finally {
                    in.close();
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException("制作压缩包时，源文件不存在：" + srcFile.getAbsolutePath(), e);
            }
        }
    }
