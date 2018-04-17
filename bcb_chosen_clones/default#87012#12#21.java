    public synchronized void writeFile(String filename) throws IOException {
        int amount;
        byte buffer[] = new byte[4096];
        File f = new File(filename);
        FileInputStream in = new FileInputStream(f);
        putNextEntry(new ZipEntry(f.getName()));
        while ((amount = in.read(buffer)) != -1) write(buffer, 0, amount);
        closeEntry();
        in.close();
    }
