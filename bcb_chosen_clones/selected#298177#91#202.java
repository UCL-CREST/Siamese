    public synchronized boolean dump(String project, String snapshotDirName, String snapshotId, boolean ignoreDerivedResources) {
        String errorMsg = "Problems encountered while trying to create local snapshot.";
        boolean rc = false;
        boolean oldSpyIgnoreDerivedResources = spy.isIgnoreDerivedResources();
        if ((project != null) && (snapshotDirName != null) && (snapshotId != null)) {
            try {
                IProject proj = null;
                ArrayList<String> fileList = null;
                workspace.save(true, null);
                if (spy.exists(project)) {
                    proj = root.getProject(project);
                    if (!proj.isOpen()) proj.open(null);
                    spy.setIgnoreDerivedResources(ignoreDerivedResources);
                    fileList = spy.listProjectMemberFiles(proj);
                    if (XPLog.isDebugEnabled()) {
                        XPLog.printDebug(LogConstants.LOG_PREFIX_LOCALSNAPSHOT + "project members: " + fileList);
                    }
                } else {
                    if (XPLog.isDebugEnabled()) {
                        XPLog.printDebug(LogConstants.LOG_PREFIX_LOCALSNAPSHOT + "project \"" + project + "\" does not exist in the workspace \"" + root.getName() + "\"!");
                    }
                    return false;
                }
                try {
                    BufferedInputStream origin = null;
                    IPath destArchivePath = getSnapshotPath(proj.getName(), ILocalSnapshotConstants.LOCAL_SNAPSHOT_DIR, snapshotId);
                    File destDir = new File((destArchivePath.removeLastSegments(1)).toOSString());
                    if (!(destDir.exists())) {
                        destDir.mkdirs();
                    }
                    try {
                        renameSnapshot(destArchivePath, ILocalSnapshotConstants.SNAPSHOT_SEPARATOR);
                    } catch (NullPointerException npe) {
                        logException(0, errorMsg, npe);
                    } catch (SecurityException se) {
                        logException(0, errorMsg, se);
                    }
                    FileOutputStream dest = new FileOutputStream(destArchivePath.toOSString());
                    CheckedOutputStream checksum = new CheckedOutputStream(dest, new Adler32());
                    ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(checksum));
                    byte data[] = new byte[ILocalSnapshotConstants.BUFFER];
                    String files[] = new String[fileList.toArray().length];
                    fileList.toArray(files);
                    try {
                        IPath rootDir = proj.getLocation();
                        try {
                            for (int i = 0; i < files.length; i++) {
                                IPath aPath = rootDir.append(files[i]);
                                String entryPathName = (new Path(files[i])).toString();
                                File file = aPath.toFile();
                                if (file.exists()) {
                                    if (XPLog.isDebugEnabled()) {
                                        XPLog.printDebug(LogConstants.LOG_PREFIX_LOCALSNAPSHOT + "adding:" + aPath.toOSString());
                                    }
                                    if (file.isFile()) {
                                        try {
                                            ZipEntry entry = new ZipEntry(entryPathName);
                                            out.putNextEntry(entry);
                                            FileInputStream fi = new FileInputStream(aPath.toOSString());
                                            origin = new BufferedInputStream(fi, ILocalSnapshotConstants.BUFFER);
                                            int count;
                                            while ((count = origin.read(data, 0, ILocalSnapshotConstants.BUFFER)) != -1) {
                                                out.write(data, 0, count);
                                            }
                                        } finally {
                                            origin.close();
                                        }
                                    } else if (file.isDirectory()) {
                                        ZipEntry entry = null;
                                        if (entryPathName.endsWith(ILocalSnapshotConstants.FILE_SEPARATOR)) {
                                            entry = new ZipEntry(entryPathName);
                                        } else {
                                            entry = new ZipEntry(entryPathName + ILocalSnapshotConstants.FILE_SEPARATOR);
                                        }
                                        if (entry != null) {
                                            out.putNextEntry(entry);
                                        }
                                    }
                                } else {
                                    if (XPLog.isDebugEnabled()) {
                                        XPLog.printDebug(LogConstants.LOG_PREFIX_LOCALSNAPSHOT + "file does not exist:" + aPath.toOSString());
                                    }
                                }
                            }
                            out.flush();
                        } finally {
                            out.close();
                        }
                        rc = true;
                        if (XPLog.isDebugEnabled()) {
                            XPLog.printDebug(LogConstants.LOG_PREFIX_LOCALSNAPSHOT + "checksum: " + checksum.getChecksum().getValue());
                        }
                    } catch (ZipException e) {
                        logException(0, errorMsg, e);
                    } catch (FileNotFoundException e) {
                        logException(0, errorMsg, e);
                    } catch (IOException e) {
                        logException(0, errorMsg, e);
                    }
                } catch (FileNotFoundException e) {
                    logException(0, errorMsg, e);
                }
            } catch (CoreException e) {
                logException(0, errorMsg, e);
            } catch (Exception e) {
                logException(0, errorMsg, e);
            } finally {
                spy.setIgnoreDerivedResources(oldSpyIgnoreDerivedResources);
            }
        }
        return rc;
    }
