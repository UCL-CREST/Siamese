    private static PrintWriter getOut(int first, int last, String dir, String lang) throws IOException {
        if (dir == null) return null;
        File file = new File(dir, lang + ".wikipedia.zip");
        if (!file.exists()) file.createNewFile();
        if (!file.canWrite()) throw new IllegalArgumentException("can't write " + file);
        log(true, "Writing Wikipedia events into " + file.getAbsolutePath());
        System.setProperty("line.separator", "\n");
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(file));
        out.putNextEntry(new ZipEntry(lang + ".wikipedia"));
        return new PrintWriter(new OutputStreamWriter(out, UTF8));
    }
