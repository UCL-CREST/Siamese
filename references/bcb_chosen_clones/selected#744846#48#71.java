    public void addDir(ZipOutputStream out, String dirname) {
        byte[] buf = new byte[1024];
        File dir = new File(projHandler.getProjectPath() + File.separator + dirname);
        if (dir.exists()) {
            try {
                File file = new File(projHandler.getProjectPath() + File.separator + dirname);
                File[] children = file.listFiles();
                for (int i = 0; i < children.length; i++) {
                    if (children[i].isFile()) {
                        FileInputStream fis = new FileInputStream(children[i]);
                        out.putNextEntry(new ZipEntry(dirname.replace('\\', '/') + '/' + children[i].getName()));
                        int len;
                        while ((len = fis.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                        out.closeEntry();
                        fis.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
