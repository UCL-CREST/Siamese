     */
    public static boolean addZipEntry(String srcZipFile, String newEntryFile, String dstFile) {
        java.util.zip.ZipOutputStream zout = null;
        InputStream is = null;
        try {
            if (new File(newEntryFile).isDirectory() && !newEntryFile.substring(newEntryFile.length() - 1).equals(File.separator)) {
                newEntryFile = newEntryFile + File.separator;
            }
            System.err.println("============");
            File fn = new File(dstFile);
            if (!fn.exists()) {
                fn.createNewFile();
            }
            zout = new java.util.zip.ZipOutputStream(new FileOutputStream(dstFile));
            ZipFile zipfile = new ZipFile(srcZipFile);
            ZipEntry entry = null;
            Enumeration e = zipfile.entries();
            byte[] buffer = new byte[1024];
            while (e.hasMoreElements()) {
                entry = (ZipEntry) e.nextElement();
                System.err.println(entry.getName());
                zout.putNextEntry(entry);
                is = new BufferedInputStream(zipfile.getInputStream(entry));
                int count;
                while ((count = is.read(buffer, 0, 1024)) != -1) {
                    zout.write(buffer, 0, count);
                    zout.flush();
                }
                is.close();
                zout.closeEntry();
            }
            zipFile(null, newEntryFile, "*.*", zout);
            zout.close();
            return true;
        } catch (java.io.IOException ioex) {
            LogUtil.getLogger().error(ioex.getMessage(), ioex);
            ioex.printStackTrace();
            return false;
        } finally {
            try {
                if (zout != null) {
                    zout.close();
                }
            } catch (Exception ex) {
            }
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception ex) {
            }
        }
