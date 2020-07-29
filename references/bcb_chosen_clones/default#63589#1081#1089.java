    void copyFile(String src, String dest) throws IOException {
        int amount;
        byte[] buffer = new byte[4096];
        FileInputStream in = new FileInputStream(src);
        FileOutputStream out = new FileOutputStream(dest);
        while ((amount = in.read(buffer)) != -1) out.write(buffer, 0, amount);
        in.close();
        out.close();
    }
