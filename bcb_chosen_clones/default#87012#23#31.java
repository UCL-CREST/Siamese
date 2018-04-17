    public synchronized void writeFile(String filename, String target) throws IOException {
        int amount;
        byte buffer[] = new byte[4096];
        FileInputStream in = new FileInputStream(filename);
        putNextEntry(new ZipEntry(target));
        while ((amount = in.read(buffer)) != -1) write(buffer, 0, amount);
        closeEntry();
        in.close();
    }
