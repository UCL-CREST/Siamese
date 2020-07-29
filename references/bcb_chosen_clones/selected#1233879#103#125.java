    public static ByteArrayOutputStream packStream(InputStream is, String name, long time) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zip_out = new ZipOutputStream(baos);
        try {
            byte[] data = CIO.readStream(is);
            if (data != null) {
                ZipEntry entry = new ZipEntry(name);
                entry.setTime(time);
                try {
                    zip_out.putNextEntry(entry);
                    zip_out.write(data);
                } catch (Exception err) {
                    err.printStackTrace();
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
