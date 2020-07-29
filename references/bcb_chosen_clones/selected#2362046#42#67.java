    public void addDir(ZipOutputStream out, String dirname) throws EditorException {
        byte[] buf = new byte[1024];
        File dir = new File(dirname);
        String dName = dir.getName();
        if (dir.exists()) {
            try {
                File file = new File(dirname);
                File[] children = file.listFiles();
                for (int i = 0; i < children.length; i++) {
                    if ((children[i].isFile()) && (!children[i].getName().endsWith("fop.zip"))) {
                        FileInputStream fis = new FileInputStream(children[i]);
                        out.putNextEntry(new ZipEntry(dName + "/" + children[i].getName()));
                        int len;
                        while ((len = fis.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                        out.closeEntry();
                        fis.close();
                    }
                }
            } catch (IOException e) {
                String msg = "Unable to add a directory to the ZIP output.";
                throw new EditorException(msg, e);
            }
        }
    }
