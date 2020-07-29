    public static boolean isFileCorrect(File file, long crc32) throws Exception {
        long startTime = System.currentTimeMillis();
        Checksum checksum = new CRC32();
        InputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[16384];
        int readLen = -1;
        while ((readLen = fis.read(buffer)) != -1) checksum.update(buffer, 0, readLen);
        fis.close();
        long sum = checksum.getValue();
        boolean isCorrect = (crc32 == sum);
        int spend = (int) (System.currentTimeMillis() - startTime);
        Log.d(logTag, "Utility Check file: File index: " + file.getAbsolutePath() + ", length: " + file.length() + ", CRC32 check: " + ((isCorrect) ? " Correct!" : " Error!" + " (" + sum + "/" + crc32) + "), spend time: " + spend + "ms");
        return isCorrect;
    }
