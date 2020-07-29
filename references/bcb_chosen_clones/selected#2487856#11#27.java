    public static void main(String[] args) throws IOException {
        PrintStream filesTxt = new PrintStream(new BufferedOutputStream(new FileOutputStream("temp/index/files.txt")));
        String[] files = new File(Constants.INDEX_PATH).list();
        for (int i = 0; i < files.length; i++) {
            String f = files[i];
            if (f.equals("deletable") || f.startsWith(".")) continue;
            FileInputStream in = new FileInputStream(new File(Constants.INDEX_PATH, f));
            FileOutputStream out = new FileOutputStream(new File(targetDir, f + ".t"));
            byte[] buf = new byte[16384];
            int n;
            while ((n = in.read(buf)) > 0) out.write(buf, 0, n);
            in.close();
            out.close();
            filesTxt.println(f);
        }
        filesTxt.close();
    }
