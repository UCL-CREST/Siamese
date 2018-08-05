    private static void copyFile(String src, String dest) {
        try {
            File inputFile = new File(src);
            File outputFile = new File(dest);
            FileInputStream in = new FileInputStream(inputFile);
            FileOutputStream out = new FileOutputStream(outputFile);
            FileChannel inc = in.getChannel();
            FileChannel outc = out.getChannel();
            inc.transferTo(0, inc.size(), outc);
            inc.close();
            outc.close();
            in.close();
            out.close();
        } catch (Exception e) {
        }
    }
