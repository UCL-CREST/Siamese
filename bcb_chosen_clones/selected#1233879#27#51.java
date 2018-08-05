    public static ByteArrayOutputStream packFiles(ArrayList<File> files, long time) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zip_out = new ZipOutputStream(baos);
        try {
            for (File file : files) {
                byte[] data = CFile.readData(file);
                if (data != null) {
                    ZipEntry entry = new ZipEntry(file.getName());
                    entry.setTime(time);
                    try {
                        zip_out.putNextEntry(entry);
                        zip_out.write(data);
                    } catch (Exception err) {
                        err.printStackTrace();
                    }
                }
            }
        } finally {
            try {
                zip_out.close();
            } catch (IOException e) {
            }
        }
        return baos;
    }
