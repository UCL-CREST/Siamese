    public static void zipFolder(String folderSrc, String archiveDest) throws FileNotFoundException, IOException, Exception {
        byte data[] = new byte[BUFFER];
        FileOutputStream dest = new FileOutputStream(archiveDest);
        BufferedOutputStream buff = new BufferedOutputStream(dest);
        ZipOutputStream out = new ZipOutputStream(buff);
        out.setMethod(ZipOutputStream.DEFLATED);
        out.setLevel(9);
        File f = new File(folderSrc);
        if (f.isDirectory()) {
            String files[] = f.list();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    FileInputStream fi;
                    if (api.utils.getOs.isWindows()) {
                        fi = new FileInputStream(folderSrc + "\\" + files[i]);
                    } else {
                        fi = new FileInputStream(folderSrc + "/" + files[i]);
                    }
                    BufferedInputStream buffi = new BufferedInputStream(fi, BUFFER);
                    if (api.utils.getOs.isWindows()) {
                        ZipEntry entry = new ZipEntry(folderSrc.substring(folderSrc.lastIndexOf("\\") + 1) + "\\" + files[i]);
                        out.putNextEntry(entry);
                    } else {
                        ZipEntry entry = new ZipEntry(folderSrc.substring(folderSrc.lastIndexOf("/") + 1) + "/" + files[i]);
                        out.putNextEntry(entry);
                    }
                    int count;
                    while ((count = buffi.read(data, 0, BUFFER)) != -1) {
                        out.write(data, 0, count);
                    }
                    out.closeEntry();
                    buffi.close();
                }
            }
        }
        out.flush();
        out.close();
    }
