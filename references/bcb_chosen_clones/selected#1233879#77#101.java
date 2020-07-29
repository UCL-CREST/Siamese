    public static ByteArrayOutputStream packStreams(ArrayList<Pair<InputStream, String>> files, long time) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zip_out = new ZipOutputStream(baos);
        try {
            for (Pair<InputStream, String> file : files) {
                byte[] data = CIO.readStream(file.getKey());
                if (data != null) {
                    ZipEntry entry = new ZipEntry(file.getValue());
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
