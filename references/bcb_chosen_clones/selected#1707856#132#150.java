    void copyDirectoryToZip(File basedir, ZipOutputStream zos) throws Exception {
        List list = getFilenames(basedir.getAbsolutePath(), true);
        ZipEntry entry = new ZipEntry("_nanoInstaller_/filelist");
        zos.putNextEntry(entry);
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            String filename = (String) it.next();
            byte[] buffer = (filename + "\n").getBytes();
            zos.write(buffer, 0, buffer.length);
        }
        zos.closeEntry();
        System.out.println("  Packaging " + list.size() + " files...");
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            String filename = (String) it.next();
            if (verbose) {
                System.out.println("  Adding " + filename + " ...");
            }
            copyFileToZip(filename, basedir.getAbsolutePath() + "/" + filename, zos);
        }
    }
