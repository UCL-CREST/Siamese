    public static void compressFile(File f) throws IOException {
        File target = new File(f.toString() + ".gz");
        System.out.print("Compressing: " + f.getName() + ".. ");
        long initialSize = f.length();
        FileInputStream fis = new FileInputStream(f);
        GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(target));
        byte[] buf = new byte[1024];
        int read;
        while ((read = fis.read(buf)) != -1) {
            out.write(buf, 0, read);
        }
        System.out.println("Done.");
        fis.close();
        out.close();
        long endSize = target.length();
        System.out.println("Initial size: " + initialSize + "; Compressed size: " + endSize);
    }
