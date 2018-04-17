    public static void unzipAndRemove(final String file) {
        String destination = file.substring(0, file.length() - 3);
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new GZIPInputStream(new FileInputStream(file));
            os = new FileOutputStream(destination);
            byte[] buffer = new byte[8192];
            for (int length; (length = is.read(buffer)) != -1; ) os.write(buffer, 0, length);
        } catch (IOException e) {
            System.err.println("Fehler: Kann nicht entpacken " + file);
        } finally {
            if (os != null) try {
                os.close();
            } catch (IOException e) {
            }
            if (is != null) try {
                is.close();
            } catch (IOException e) {
            }
        }
        deleteFile(file);
    }
