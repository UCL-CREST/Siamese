        public void zip() {
            byte[] buf = new byte[1024];
            try {
                String outFilename = getOutName();
                ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
                for (int i = min; i <= max; i++) {
                    String fname = name + "_" + i + ".html";
                    FileInputStream in = new FileInputStream(path + fname);
                    out.putNextEntry(new ZipEntry(fname));
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    out.closeEntry();
                    in.close();
                }
                out.close();
            } catch (IOException ex) {
                System.out.println("Exception: " + ex);
            }
        }
