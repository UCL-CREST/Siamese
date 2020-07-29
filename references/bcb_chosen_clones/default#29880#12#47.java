    public static boolean downloadFile(String url, String destination) throws Exception {
        BufferedInputStream bi = null;
        BufferedOutputStream bo = null;
        File destfile;
        byte BUFFER[] = new byte[100];
        java.net.URL fileurl;
        URLConnection conn;
        fileurl = new java.net.URL(url);
        conn = fileurl.openConnection();
        long fullsize = conn.getContentLength();
        long onepercent = fullsize / 100;
        MessageFrame.setTotalDownloadSize(fullsize);
        bi = new BufferedInputStream(conn.getInputStream());
        destfile = new File(destination);
        if (!destfile.createNewFile()) {
            destfile.delete();
            destfile.createNewFile();
        }
        bo = new BufferedOutputStream(new FileOutputStream(destfile));
        int read = 0;
        int sum = 0;
        long i = 0;
        while ((read = bi.read(BUFFER)) != -1) {
            bo.write(BUFFER, 0, read);
            sum += read;
            i += read;
            if (i > onepercent) {
                i = 0;
                MessageFrame.setDownloadProgress(sum);
            }
        }
        bi.close();
        bo.close();
        MessageFrame.setDownloadProgress(fullsize);
        return true;
    }
