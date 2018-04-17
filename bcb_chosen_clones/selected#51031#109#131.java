    protected void copyFile(File src, File dest) throws IOException {
        if (src.exists()) {
            if (src.isDirectory()) {
                dest.mkdir();
                File children[] = src.listFiles();
                for (int i = 0; i < children.length; i++) {
                    File srcChild = children[i];
                    File destChild = new File(dest, srcChild.getName());
                    copyFile(srcChild, destChild);
                }
            } else {
                dest.createNewFile();
                OutputStream out = new FileOutputStream(dest);
                InputStream in = new FileInputStream(src);
                int c;
                while ((c = in.read()) >= 0) {
                    out.write(c);
                }
                in.close();
                out.close();
            }
        }
    }
