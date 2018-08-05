    public static void backupGardenia(String gardeniaHome, String backupFile) throws Exception {
        gardeniaHome = '\\' == File.separatorChar ? gardeniaHome.replace('\\', '/') : gardeniaHome;
        gardeniaHome = gardeniaHome.endsWith("/") ? gardeniaHome : gardeniaHome + "/";
        List allToZip = getFilesRecursive(new File(gardeniaHome + "data"));
        byte[] buffer = new byte[4096];
        FileOutputStream out = new FileOutputStream(backupFile);
        ZipOutputStream zout = new ZipOutputStream(out);
        zout.setLevel(9);
        int baseNameSize = gardeniaHome.length();
        for (Iterator it = allToZip.iterator(); it.hasNext(); ) {
            File f = (File) it.next();
            if (f.getName().equals("gardenia01.lck")) continue;
            ZipEntry zipEntry = new ZipEntry(f.getCanonicalPath().substring(baseNameSize));
            zipEntry.setTime(f.lastModified());
            zout.putNextEntry(zipEntry);
            FileInputStream fis = new FileInputStream(f);
            int bytes = 0;
            while ((bytes = fis.read(buffer, 0, buffer.length)) > 0) {
                zout.write(buffer, 0, bytes);
            }
            fis.close();
        }
        zout.close();
        out.close();
    }
