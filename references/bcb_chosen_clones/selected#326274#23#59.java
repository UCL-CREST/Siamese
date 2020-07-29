    private static void compressZip(String source, String dest) throws Exception {
        File baseFolder = new File(source);
        if (baseFolder.exists()) {
            if (baseFolder.isDirectory()) {
                List<File> fileList = getSubFiles(new File(source));
                ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(dest));
                zos.setEncoding("GBK");
                ZipEntry entry = null;
                byte[] buf = new byte[2048];
                int readLen = 0;
                for (int i = 0; i < fileList.size(); i++) {
                    File file = fileList.get(i);
                    if (file.isDirectory()) {
                        entry = new ZipEntry(getAbsFileName(source, file) + "/");
                    } else {
                        entry = new ZipEntry(getAbsFileName(source, file));
                    }
                    entry.setSize(file.length());
                    entry.setTime(file.lastModified());
                    zos.putNextEntry(entry);
                    if (file.isFile()) {
                        InputStream in = new BufferedInputStream(new FileInputStream(file));
                        while ((readLen = in.read(buf, 0, 1024)) != -1) {
                            zos.write(buf, 0, readLen);
                        }
                        in.close();
                    }
                }
                zos.close();
            } else {
                throw new Exception("Can not do this operation!.");
            }
        } else {
            baseFolder.mkdirs();
            compressZip(source, dest);
        }
    }
