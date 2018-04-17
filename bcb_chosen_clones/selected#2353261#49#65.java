    public static void copyFile(String source, String destination) throws IOException {
        File srcDir = new File(source);
        File[] files = srcDir.listFiles();
        FileChannel in = null;
        FileChannel out = null;
        for (File file : files) {
            try {
                in = new FileInputStream(file).getChannel();
                File outFile = new File(destination, file.getName());
                out = new FileOutputStream(outFile).getChannel();
                in.transferTo(0, in.size(), out);
            } finally {
                if (in != null) in.close();
                if (out != null) out.close();
            }
        }
    }
