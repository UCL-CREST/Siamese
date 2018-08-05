    @Override
    public void _commit(final boolean finishChosen) throws CommitStepException {
        if (finishChosen) {
            final Runnable runnableProcess = new Runnable() {

                public void run() {
                    final ProgressIndicator progressIndicator = ProgressManager.getInstance().getProgressIndicator();
                    final String zipFileName = zipFileNameField.getText();
                    final List<List<String>> rawFilesToZip = QuickZipHelper.getFilesStructureToZip(myProject, mySelectedFragments);
                    int totalFilesAndFolders = 0;
                    for (final List<String> loopData : rawFilesToZip) {
                        totalFilesAndFolders += loopData.size();
                    }
                    try {
                        final ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(zipFileName));
                        outputStream.setMethod(ZipOutputStream.DEFLATED);
                        outputStream.setComment("Zip file created by QuickZip");
                        int filesZipped = 0;
                        for (final List<String> loopData : rawFilesToZip) {
                            boolean firstEntryProcessed = false;
                            int prefixLengthToSkip = 0;
                            for (final String loopFileName : loopData) {
                                progressIndicator.setFraction((1.0 * filesZipped) / totalFilesAndFolders);
                                progressIndicator.setText2(QuickZipBundle.message("title.quick-zip.zip-message", loopFileName));
                                if (!firstEntryProcessed) {
                                    prefixLengthToSkip = loopFileName.lastIndexOf("/") + 1;
                                    firstEntryProcessed = true;
                                } else {
                                    final File loopFile = new File(loopFileName);
                                    if (loopFile.isFile()) {
                                        final String zipEntryPath = loopFileName.substring(prefixLengthToSkip);
                                        final byte[] buf = new byte[1024];
                                        outputStream.putNextEntry(new ZipEntry(zipEntryPath));
                                        final FileInputStream loopInputStream = new FileInputStream(loopFile);
                                        int len;
                                        while ((len = loopInputStream.read(buf)) > 0) {
                                            outputStream.write(buf, 0, len);
                                        }
                                        outputStream.closeEntry();
                                        loopInputStream.close();
                                    }
                                }
                                filesZipped++;
                            }
                        }
                        outputStream.close();
                    } catch (Exception ex) {
                        Messages.showErrorDialog(myProject, ex.getMessage(), "Error");
                    }
                }
            };
            final String progressTitle = QuickZipBundle.message("title.quick-zip.zip-dialog");
            ProgressManager.getInstance().runProcessWithProgressSynchronously(runnableProcess, progressTitle, false, myProject);
        }
    }
