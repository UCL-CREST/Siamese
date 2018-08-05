    public void createJAR(String fileString, String ext) {
        try {
            File file = new File(fileString);
            int i = fileString.lastIndexOf(java.io.File.separator);
            String dir = fileString.substring(0, i + 1);
            if (ext.matches("jar")) {
                jarFile = new File(getClass().getClassLoader().getResource("jsdviewer.jar").toURI());
                java.io.FileOutputStream fstrm = new java.io.FileOutputStream(file);
                FileChannel in = (new java.io.FileInputStream(jarFile)).getChannel();
                FileChannel out = fstrm.getChannel();
                in.transferTo(0, jarFile.length(), out);
                in.close();
                out.close();
            } else {
                file.mkdir();
            }
            File.umount(file);
            File temp = new File(dir + "document.jsd");
            FileOutputStream fstrm2 = new FileOutputStream(temp.getCanonicalPath());
            ostrm = new ObjectOutputStream(fstrm2);
            ostrm.writeObject(doc);
            ostrm.flush();
            ostrm.close();
            File.umount();
            File docFile = new File(file.getCanonicalPath() + java.io.File.separator + "document.jsd");
            File.cp_p(temp, docFile);
            File.umount();
            temp.delete();
            File.umount(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
