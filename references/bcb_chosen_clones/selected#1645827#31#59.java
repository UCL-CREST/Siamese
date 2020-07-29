    private static void addDirectory(ZipOutputStream zout, File fileSource, String innerFolder) throws IOException {
        if (fileSource.isHidden()) {
            System.out.println("Skiping hidden folder " + fileSource.getName());
            return;
        }
        File[] files = fileSource.listFiles();
        System.out.println("Adding directory " + fileSource.getName());
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                addDirectory(zout, files[i], innerFolder + files[i].getName() + "/");
                continue;
            }
            try {
                System.out.println("Adding file " + files[i].getName());
                byte[] buffer = new byte[1024];
                FileInputStream fin = new FileInputStream(files[i]);
                zout.putNextEntry(new ZipEntry(innerFolder + files[i].getName()));
                int length;
                while ((length = fin.read(buffer)) > 0) {
                    zout.write(buffer, 0, length);
                }
                zout.closeEntry();
                fin.close();
            } catch (IOException ioe) {
                System.out.println("IOException :" + ioe);
                throw ioe;
            }
        }
    }
