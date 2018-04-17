    public static File zipFile(File f, boolean absolutePaths, boolean deleteOriginal, boolean forceDelete, int level) throws IOException {
        if (!f.exists()) {
            throw new FileNotFoundException("file " + f + " does not exist");
        }
        File destination = new File(f.getAbsolutePath() + ".zip");
        if (destination.exists()) {
            throw new IOException("zipped file " + destination + " exists");
        }
        ZipOutputStream zout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destination)));
        zout.setLevel(level);
        NSArray<File> files = f.isDirectory() ? arrayByAddingFilesInDirectory(f, true) : new NSArray<File>(f);
        try {
            BufferedInputStream origin = null;
            byte data[] = new byte[2048];
            for (int i = 0; i < files.count(); i++) {
                File currentFile = files.objectAtIndex(i);
                FileInputStream fi = new FileInputStream(currentFile);
                origin = new BufferedInputStream(fi, 2048);
                String entryName = currentFile.getAbsolutePath();
                if (!absolutePaths) {
                    if (f.isDirectory()) {
                        entryName = entryName.substring(f.getAbsolutePath().length() + 1, entryName.length());
                    } else {
                        entryName = entryName.substring(f.getParentFile().getAbsolutePath().length() + 1, entryName.length());
                    }
                }
                ZipEntry entry = new ZipEntry(entryName);
                zout.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, 2048)) != -1) {
                    zout.write(data, 0, count);
                }
                origin.close();
            }
            zout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (deleteOriginal) {
            if (f.canWrite() || forceDelete) {
                if (!deleteDirectory(f)) {
                    deleteDirectory(f);
                }
            }
        }
        return destination;
    }
