    public void compressWorkflow(Vector filenames, String output, String prefix) throws IOException {
        byte[] buf = new byte[1024];
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(output));
        for (int re = 0; re < filenames.size(); re++) {
            String s = (String) filenames.get(re);
            File f = new File((String) filenames.get(re));
            if (!f.isDirectory()) {
                System.out.println("FILE:**********" + s);
                FileInputStream in = new FileInputStream(s);
                out.putNextEntry(new ZipEntry(((String) filenames.get(re)).substring(prefix.length())));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            } else {
                System.out.println("DIR:**********" + s);
            }
        }
        out.close();
    }
