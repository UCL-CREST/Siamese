    public static void unzipModel(String filename, String tempdir) throws EDITSException {
        try {
            BufferedOutputStream dest = null;
            FileInputStream fis = new FileInputStream(filename);
            int BUFFER = 2048;
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                int count;
                byte data[] = new byte[BUFFER];
                FileOutputStream fos = new FileOutputStream(tempdir + entry.getName());
                dest = new BufferedOutputStream(fos, BUFFER);
                while ((count = zis.read(data, 0, BUFFER)) != -1) dest.write(data, 0, count);
                dest.flush();
                dest.close();
            }
            zis.close();
        } catch (Exception e) {
            throw new EDITSException("Can not expand model in \"" + tempdir + "\" because:\n" + e.getMessage());
        }
    }
