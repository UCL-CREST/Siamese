    public static void zipFiles(String sourcepath, String target) {
        try {
            String source;
            String fileName;
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(target));
            File f = new File(sourcepath);
            String[] children;
            if (f.isDirectory()) children = f.list();
            FilenameFilter filter = new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    return name.endsWith(".faa");
                }
            };
            children = f.list(filter);
            for (int i = 0; i < children.length; i++) {
                fileName = children[i];
                System.out.println("src :" + fileName);
                source = sourcepath + fileName;
                System.out.println("source :" + source);
                zos.putNextEntry(new ZipEntry(fileName));
                FileInputStream fis = new FileInputStream(source);
                int size = 0;
                byte[] buffer = new byte[1024];
                while ((size = fis.read(buffer, 0, buffer.length)) > 0) {
                    zos.write(buffer, 0, size);
                }
                fis.close();
            }
            zos.closeEntry();
            zos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
