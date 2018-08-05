    public static byte[] gzipInput(String file) {
        try {
            FileInputStream fin = new FileInputStream(file);
            GZIPInputStream in = new GZIPInputStream(fin);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            while (true) {
                int bytesRead = in.read(buffer);
                if (bytesRead == -1) break;
                out.write(buffer, 0, bytesRead);
            }
            return out.toByteArray();
        } catch (IOException e) {
            System.err.println(e);
        }
        return null;
    }
