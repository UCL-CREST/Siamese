    public static boolean writeZipFile(File zipFile, byte[][] data, String[] names) {
        if (zipFile == null || data == null || names == null) {
            return false;
        }
        if (data.length < 1 || data.length != names.length) {
            return false;
        }
        ZipOutputStream outStream;
        try {
            outStream = new ZipOutputStream(new FileOutputStream(zipFile));
            for (int i = 0; i < data.length; i++) {
                if (data[i] != null && !StringUtil.isEmpty(names[i])) {
                    ZipEntry entry = new ZipEntry(names[i]);
                    entry.setSize(data[i].length);
                    entry.setTime(System.currentTimeMillis());
                    outStream.putNextEntry(entry);
                    outStream.write(data[i]);
                    outStream.closeEntry();
                }
            }
            outStream.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
