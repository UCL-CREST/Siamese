    private static File buildJarFile(File clazz, String QName) throws Exception {
        ZipOutputStream out = null;
        try {
            if (clazz.getName().indexOf(".class") == -1) throw new Exception("WorflowFactory buildJarFile: only .class files accepted");
            String temp = QName.replace(".", "/");
            String name = temp.substring(temp.lastIndexOf("/") + 1, temp.length());
            String javaPath = temp.substring(0, temp.lastIndexOf("/"));
            File outFile = new File(clazz.getParent() + "/" + name + ".jar");
            out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outFile)));
            byte[] data = new byte[1000];
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(clazz), 1000);
            out.putNextEntry(new ZipEntry(javaPath + "/" + clazz.getName()));
            int count;
            while ((count = in.read(data, 0, 1000)) != -1) {
                out.write(data, 0, count);
            }
            out.closeEntry();
            out.flush();
            out.close();
            return outFile;
        } catch (Exception e) {
            String err = "WorflowFactory buildJarFile: error creating jar file for " + clazz.getName();
            log.debug(err, e);
            throw new Exception(err, e);
        }
    }
