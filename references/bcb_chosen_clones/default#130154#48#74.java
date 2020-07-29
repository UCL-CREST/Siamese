    private void addDirectory(ZipOutputStream zout, File kaynakDizin) {
        File[] files = kaynakDizin.listFiles();
        if (Sunucu.DEBUG) System.out.println("Adding directory " + kaynakDizin.getName());
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().equals(zipIsmi)) continue;
            if (files[i].isDirectory()) {
                if (files[i].getName() == IgnoreDirectory) continue;
                addDirectory(zout, files[i]);
                continue;
            }
            try {
                if (Sunucu.DEBUG) System.out.println("Adding file " + files[i].getName());
                byte[] buffer = new byte[1024];
                FileInputStream fin = new FileInputStream(files[i]);
                String icerdekiYol = files[i].getAbsolutePath().substring(dizinIsmi.length() + 1);
                zout.putNextEntry(new ZipEntry(icerdekiYol));
                int length;
                while ((length = fin.read(buffer)) > 0) {
                    zout.write(buffer, 0, length);
                }
                zout.closeEntry();
                fin.close();
            } catch (IOException ioe) {
                System.out.println("IOException :" + ioe);
            }
        }
    }
