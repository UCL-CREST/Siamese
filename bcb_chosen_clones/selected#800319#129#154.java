    public static void zipFiles(String outputFileName, Vector<String> fns) {
        byte[] buf = new byte[100000];
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outputFileName));
            for (int i = 0; i < fns.size(); i++) {
                println("Zipping file: " + fns.get(i));
                FileInputStream in = new FileInputStream(fns.get(i));
                out.putNextEntry(new ZipEntry(fns.get(i)));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        } catch (IOException e) {
            System.err.println("Error in zipping...");
        }
        for (int i = 0; i < fns.size(); i++) {
            File f = new File(fns.get(i));
            boolean del = f.delete();
            if (del) println(fns.get(i) + " deleted..."); else System.err.println("Error: failed to delete " + fns.get(i));
        }
        fns.clear();
    }
