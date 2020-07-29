    private boolean streamDownload(URL url, File file) {
        try {
            InputStream in = url.openConnection().getInputStream();
            BufferedInputStream bis = new BufferedInputStream(in);
            OutputStream out = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(out);
            int chunkSize = 63 * 1024;
            byte[] ba = new byte[chunkSize];
            while (true) {
                int bytesRead = readBlocking(bis, ba, 0, chunkSize);
                if (bytesRead > 0) {
                    if (bos != null) bos.write(ba, 0, bytesRead);
                } else {
                    bos.close();
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error writing file " + file);
            return false;
        }
        System.out.println("OK writing file " + file);
        return true;
    }
