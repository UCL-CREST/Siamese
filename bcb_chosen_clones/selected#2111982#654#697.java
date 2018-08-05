    private void addToArchive(ZipOutputStream stream, File file, FileFilter filter, String entryName, int recurseDepth) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] ls = file.listFiles(filter);
                if (ls.length == 0) {
                    if (archiveEntryList_.contains(entryName + "/")) {
                        System.err.println("  warning: duplicate entry: " + entryName + "/" + ". Skipping.");
                    } else {
                        System.out.println("  " + file);
                        ZipEntry newEntry = new ZipEntry(entryName + "/");
                        stream.putNextEntry(newEntry);
                        stream.closeEntry();
                        archiveEntryList_.add(entryName + "/");
                    }
                }
                for (int i = 0; i < ls.length; i++) {
                    String ename = ls[i].getAbsolutePath();
                    ename = entryName + ename.substring(file.getAbsolutePath().length());
                    ename = convertToGenericPath(ename);
                    if (ename.startsWith("/")) ename = ename.substring(1);
                    if (recurseDepth > 0) addToArchive(stream, ls[i], filter, ename, recurseDepth - 1);
                }
            } else if (archiveEntryList_.contains(entryName)) {
                System.err.println("  warning: duplicate entry: " + entryName + ". Skipping.");
            } else {
                System.out.println("  " + file);
                archivecount_++;
                ZipEntry newEntry = new ZipEntry(entryName);
                stream.putNextEntry(newEntry);
                FileInputStream in = new FileInputStream(file);
                byte[] buf = new byte[2048];
                int read = in.read(buf, 0, buf.length);
                while (read > 0) {
                    stream.write(buf, 0, read);
                    read = in.read(buf, 0, buf.length);
                }
                in.close();
                stream.closeEntry();
                archiveEntryList_.add(entryName);
            }
        } else {
            System.err.println("  error: file " + file + " does not exist. Skipping.");
        }
    }
