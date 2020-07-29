    public static boolean dumpToStream(ReplicatedProjectInfo projectInfo, ArrayList<String> memberList, ArrayList<String> dumpList, String base, OutputStream output, IProgressRelay progress) {
        String prefix = base;
        if ((dumpList == null || dumpList.isEmpty()) && memberList == null && projectInfo == null) {
            return true;
        }
        if (output == null) {
            return false;
        }
        if (dumpList == null) {
            dumpList = new ArrayList<String>();
        }
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_SNAPSHOTUTILS + "Dumping snapshot of " + dumpList.size() + " files to output stream: " + output.getClass().getSimpleName());
        }
        if (progress != null) {
            progress.beginTask("Dumping " + dumpList.size() + " files.", dumpList.size());
        }
        if (prefix == null) {
            prefix = SEPARATOR;
        } else if (!prefix.endsWith(SEPARATOR)) {
            prefix = prefix.concat(SEPARATOR);
        }
        CheckedOutputStream checked = new CheckedOutputStream(output, new Adler32());
        BufferedOutputStream buffered = new BufferedOutputStream(checked, BUFFER);
        ZipOutputStream stream = new ZipOutputStream(buffered);
        BufferedInputStream origin = null;
        byte[] buffer = new byte[BUFFER];
        String fullPath = null;
        File currentFile = null;
        try {
            if (projectInfo != null) {
                ZipEntry entry = new ZipEntry(PROJECT_INFO_NAME);
                stream.putNextEntry(entry);
                ObjectOutputStream oos = new ObjectOutputStream(stream);
                oos.writeObject(projectInfo);
                oos.flush();
                stream.closeEntry();
            }
            if (memberList != null) {
                ZipEntry entry = new ZipEntry(PROJECT_MEMBER_LIST_NAME);
                stream.putNextEntry(entry);
                ObjectOutputStream oos = new ObjectOutputStream(stream);
                oos.writeObject(memberList);
                oos.flush();
                stream.closeEntry();
            }
            for (String fileName : dumpList) {
                if (progress != null) {
                    progress.subTask("Dumping: " + fileName);
                }
                fullPath = prefix.concat(fileName);
                currentFile = new File(fullPath);
                if (currentFile.exists()) {
                    if (currentFile.isFile()) {
                        if (XPLog.isDebugEnabled()) {
                            XPLog.printDebug(LogConstants.LOG_PREFIX_SNAPSHOTUTILS + "adding file: " + fullPath);
                        }
                        try {
                            ZipEntry entry = new ZipEntry(fileName);
                            stream.putNextEntry(entry);
                            FileInputStream fi = new FileInputStream(currentFile);
                            origin = new BufferedInputStream(fi, BUFFER);
                            int count;
                            while ((count = origin.read(buffer, 0, BUFFER)) != -1) {
                                stream.write(buffer, 0, count);
                            }
                            stream.closeEntry();
                        } finally {
                            origin.close();
                        }
                    } else if (currentFile.isDirectory()) {
                        if (XPLog.isDebugEnabled()) {
                            XPLog.printDebug(LogConstants.LOG_PREFIX_SNAPSHOTUTILS + "adding directory: " + fullPath);
                        }
                        ZipEntry entry = new ZipEntry(fileName.endsWith(SEPARATOR) ? fileName : fileName.concat(SEPARATOR));
                        if (entry != null) {
                            stream.putNextEntry(entry);
                            stream.closeEntry();
                        }
                    }
                } else {
                    if (XPLog.isDebugEnabled()) {
                        XPLog.printDebug(LogConstants.LOG_PREFIX_SNAPSHOTUTILS + "File: " + fullPath + " doesn't exist.");
                    }
                    return false;
                }
                if (progress != null) {
                    progress.worked(1);
                }
            }
            stream.flush();
            stream.finish();
            buffered.flush();
            checked.flush();
            output.flush();
            if (progress != null) {
                progress.done();
            }
        } catch (IOException i) {
            if (XPLog.isDebugEnabled()) {
                XPLog.printDebug(LogConstants.LOG_PREFIX_SNAPSHOTUTILS + "An error occurred during dump: " + i.getMessage());
            }
            return false;
        }
        return true;
    }
