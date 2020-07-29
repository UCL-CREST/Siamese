    public static void zipDirectory(String directory, String zipName) {
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(zipName);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            int endpos = directory.lastIndexOf('/');
            byte data[] = new byte[ZIP_BUFFER];
            File f = new File(directory);
            String files[] = getRecursiveFileList(f);
            for (int i = 0; i < files.length; i++) {
                logger.debug("Adding: " + files[i]);
                FileInputStream fi = new FileInputStream(files[i]);
                origin = new BufferedInputStream(fi, ZIP_BUFFER);
                String fileName = files[i];
                String relativeName = fileName.substring(endpos + 1, fileName.length());
                relativeName = relativeName.replace('\\', '/');
                ZipEntry entry = new ZipEntry(relativeName);
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, ZIP_BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
            out.close();
        } catch (Exception e) {
            logger.error("zipDirectory: " + e, e);
        }
    }
