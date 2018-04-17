    public static void writeZip(File targetFile, File baseDir, List files, StreamAndEntryProvider pr, ProcessIndicator pi) throws IOException {
        ZipOutputStream jos = pr.createStream(new FileOutputStream(targetFile), baseDir, files);
        String baseDirPath = baseDir.getCanonicalPath();
        for (Iterator it = files.iterator(); it.hasNext(); ) {
            File classFile = (File) it.next();
            if (!classFile.exists()) {
                throw new FileNotFoundException(classFile.getAbsolutePath());
            }
            String fullFilePath = classFile.getCanonicalPath();
            if (!fullFilePath.startsWith(baseDirPath)) {
                throw new IllegalArgumentException("Expected file '" + fullFilePath + "' to be " + "located under " + baseDir.getAbsolutePath());
            }
            String tmpFileName = fullFilePath.substring(baseDirPath.length() + 1);
            String fileName = tmpFileName.replace(File.separatorChar, '/');
            if (classFile.isFile()) {
                ZipEntry entry = pr.createEntry(fileName, classFile);
                entry.setTime(classFile.lastModified());
                jos.putNextEntry(entry);
                pi.startFile(targetFile, entry, classFile);
                int notificationGranularity = pi.getNotificationGranularity();
                if (notificationGranularity < 1 || notificationGranularity > MAX_BUFFER_SIZE) notificationGranularity = DEFAULT_BUFFER_SIZE;
                FileInputStream fis = new FileInputStream(classFile);
                byte[] buffer = new byte[notificationGranularity];
                int i = 0;
                while (true) {
                    int len = fis.read(buffer);
                    if (len < 1) break;
                    jos.write(buffer, 0, len);
                    pi.progress(targetFile, entry, classFile, len);
                }
                fis.close();
                jos.closeEntry();
                pi.endFile(targetFile, entry, classFile);
            } else {
                if (!classFile.isDirectory()) {
                    throw new IOException("Ups. File-Object that is neither file nor directory? " + classFile.getAbsolutePath());
                }
                ZipEntry entry = pr.createEntry(fileName + "/", classFile);
                jos.putNextEntry(entry);
                pi.startDirectory(targetFile, entry, classFile);
                pi.endDirectory(targetFile, entry, classFile);
                jos.closeEntry();
            }
        }
        pi.endZipFile(targetFile, baseDir, files);
        jos.close();
    }
