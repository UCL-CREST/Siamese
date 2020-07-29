    public static void decompressFile(File f) throws IOException {
        File target = new File(f.toString().substring(0, f.toString().length() - 3));
        System.out.print("Decompressing: " + f.getName() + ".. ");
        long initialSize = f.length();
        GZIPInputStream in = new GZIPInputStream(new FileInputStream(f));
        FileOutputStream fos = new FileOutputStream(target);
        byte[] buf = new byte[1024];
        int read;
        while ((read = in.read(buf)) != -1) {
            fos.write(buf, 0, read);
        }
        System.out.println("Done.");
        fos.close();
        in.close();
        long endSize = target.length();
        System.out.println("Initial size: " + initialSize + "; Decompressed size: " + endSize);
    }
