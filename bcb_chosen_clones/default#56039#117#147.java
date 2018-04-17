    public static void httpOnLoad(String fileName, String urlpath) throws Exception {
        URL url = new URL(urlpath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        int responseCode = conn.getResponseCode();
        System.err.println("Code : " + responseCode);
        System.err.println("getResponseMessage : " + conn.getResponseMessage());
        if (responseCode >= 400) {
            return;
        }
        int threadSize = 3;
        int fileLength = conn.getContentLength();
        System.out.println("fileLength:" + fileLength);
        int block = fileLength / threadSize;
        int lastBlock = fileLength - (block * (threadSize - 1));
        conn.disconnect();
        File file = new File(fileName);
        RandomAccessFile randomFile = new RandomAccessFile(file, "rw");
        randomFile.setLength(fileLength);
        randomFile.close();
        for (int i = 2; i < 3; i++) {
            int startPosition = i * block;
            if (i == threadSize - 1) {
                block = lastBlock;
            }
            RandomAccessFile threadFile = new RandomAccessFile(file, "rw");
            threadFile.seek(startPosition);
            new TestDownFile(url, startPosition, threadFile, block).start();
        }
    }
