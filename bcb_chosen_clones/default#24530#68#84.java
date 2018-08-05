    public static void decompressFile(File orig) throws IOException {
        File file = new File(INPUT + orig.toString());
        File target = new File(OUTPUT + orig.toString().replaceAll(".xml.gz", ".xml"));
        System.out.println("    Decompressing \"" + file.getName() + "\" into \"" + target + "\"");
        long l = file.length();
        GZIPInputStream gzipinputstream = new GZIPInputStream(new FileInputStream(file));
        FileOutputStream fileoutputstream = new FileOutputStream(target);
        byte abyte0[] = new byte[1024];
        int i;
        while ((i = gzipinputstream.read(abyte0)) != -1) fileoutputstream.write(abyte0, 0, i);
        fileoutputstream.close();
        gzipinputstream.close();
        long l1 = target.length();
        System.out.println("    Initial size: " + l + "; Decompressed size: " + l1 + ".");
        System.out.println("    Done.");
        System.out.println();
    }
