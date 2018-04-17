    private byte[] readFile(String path) throws IOException {
        File f = new File(path);
        if (f.exists()) {
            ByteArrayOutputStream result = new ByteArrayOutputStream((int) f.length());
            InputStream is = null;
            try {
                is = new FileInputStream(path);
                byte[] buf = new byte[10240];
                int i;
                while ((i = is.read(buf)) != -1) {
                    result.write(buf, 0, i);
                }
                is.close();
                is = null;
                return result.toByteArray();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                    }
                }
            }
        } else {
            throw new IOException("File " + path + " does not exist or is not accessible");
        }
    }
