    private void addFilesetsToArchive(ZipOutputStream stream) throws java.io.IOException {
        for (int i = 0; i < filesets.size(); i++) {
            FileSet fs = (FileSet) filesets.elementAt(i);
            DirectoryScanner ds = fs.getDirectoryScanner(project);
            String workDirAsString = fs.getDir(project).toString();
            String[] srcFiles = ds.getIncludedFiles();
            String[] srcDirs = ds.getIncludedDirectories();
            for (int j = 0; j < srcFiles.length; j++) {
                System.out.println("file=" + srcFiles[j]);
                ZipEntry newEntry = new ZipEntry(srcFiles[j]);
                stream.putNextEntry(newEntry);
                FileInputStream in = new FileInputStream(workDirAsString + File.separator + srcFiles[j]);
                byte[] buf = new byte[2048];
                int read = in.read(buf, 0, buf.length);
                while (read > 0) {
                    stream.write(buf, 0, read);
                    read = in.read(buf, 0, buf.length);
                }
                in.close();
                stream.closeEntry();
                archivecount_++;
                archiveEntryList_.add(srcFiles[j]);
            }
        }
    }
