    public void createZipCopy(IUIContext ui, final String zipFileName, final File[] filesToZip, final FilenameFilter fileFilter, Timestamp timestamp) {
        TestCase.assertNotNull(ui);
        TestCase.assertNotNull(zipFileName);
        TestCase.assertFalse(zipFileName.trim().length() == 0);
        TestCase.assertNotNull(filesToZip);
        TestCase.assertNotNull(timestamp);
        String nameCopy = zipFileName;
        if (nameCopy.endsWith(".zip")) {
            nameCopy = nameCopy.substring(0, zipFileName.length() - 4);
        }
        nameCopy = nameCopy + "_" + timestamp.toString() + ".zip";
        final String finalZip = nameCopy;
        IWorkspaceRunnable noResourceChangedEventsRunner = new IWorkspaceRunnable() {

            public void run(IProgressMonitor runnerMonitor) throws CoreException {
                try {
                    Map<String, File> projectFiles = new HashMap<String, File>();
                    IPath basePath = new Path("/");
                    for (File nextLocation : filesToZip) {
                        projectFiles.putAll(getFilesToZip(nextLocation, basePath, fileFilter));
                    }
                    if (projectFiles.isEmpty()) {
                        PlatformActivator.logDebug("Zip file (" + zipFileName + ") not created because there were no files to zip");
                        return;
                    }
                    IPath resultsPath = PlatformActivator.getDefault().getResultsPath();
                    File copyRoot = resultsPath.toFile();
                    copyRoot.mkdirs();
                    IPath zipFilePath = resultsPath.append(new Path(finalZip));
                    String zipFileName = zipFilePath.toPortableString();
                    ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
                    try {
                        out.setLevel(Deflater.DEFAULT_COMPRESSION);
                        for (String filePath : projectFiles.keySet()) {
                            File nextFile = projectFiles.get(filePath);
                            FileInputStream fin = new FileInputStream(nextFile);
                            try {
                                out.putNextEntry(new ZipEntry(filePath));
                                try {
                                    byte[] bin = new byte[4096];
                                    int bread = fin.read(bin, 0, 4096);
                                    while (bread != -1) {
                                        out.write(bin, 0, bread);
                                        bread = fin.read(bin, 0, 4096);
                                    }
                                } finally {
                                    out.closeEntry();
                                }
                            } finally {
                                fin.close();
                            }
                        }
                    } finally {
                        out.close();
                    }
                } catch (FileNotFoundException e) {
                    Status error = new Status(Status.ERROR, PlatformActivator.PLUGIN_ID, Status.ERROR, e.getLocalizedMessage(), e);
                    throw new CoreException(error);
                } catch (IOException e) {
                    Status error = new Status(Status.ERROR, PlatformActivator.PLUGIN_ID, Status.ERROR, e.getLocalizedMessage(), e);
                    throw new CoreException(error);
                }
            }
        };
        try {
            IWorkspace workspace = ResourcesPlugin.getWorkspace();
            workspace.run(noResourceChangedEventsRunner, workspace.getRoot(), IWorkspace.AVOID_UPDATE, new NullProgressMonitor());
        } catch (CoreException ce) {
            PlatformActivator.logException(ce);
        }
    }
