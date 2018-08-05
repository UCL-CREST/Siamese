    private void importSources() {
        InputOutput io = IOProvider.getDefault().getIO("Import Sources", false);
        io.select();
        PrintWriter pw = new PrintWriter(io.getOut());
        pw.println("Beginning transaction....");
        pw.println("Processing selected files:");
        String[][] selectedFiles = getSelectedFiles(pw);
        if (selectedFiles.length == 0) {
            pw.println("There are no files to process.");
        } else {
            pw.println(new StringBuilder("Importing ").append(selectedFiles.length).append(" files to ").append(group.getDisplayName()).append(" within project ").append(ProjectUtils.getInformation(project).getDisplayName()).toString());
            FileObject destFO = group.getRootFolder();
            try {
                String destRootDir = new File(destFO.getURL().toURI()).getAbsolutePath();
                if (destFO.canWrite()) {
                    for (String[] s : selectedFiles) {
                        try {
                            File parentDir = new File(new StringBuilder(destRootDir).append(File.separator).append(s[0]).toString());
                            if (!parentDir.exists()) {
                                parentDir.mkdirs();
                            }
                            File f = new File(new StringBuilder(destRootDir).append(s[0]).append(File.separator).append(s[1]).toString());
                            if (!f.exists()) {
                                f.createNewFile();
                            }
                            FileInputStream fin = null;
                            FileOutputStream fout = null;
                            byte[] b = new byte[1024];
                            int read = -1;
                            try {
                                File inputFile = new File(new StringBuilder(rootDir).append(s[0]).append(File.separator).append(s[1]).toString());
                                pw.print(new StringBuilder("\tImporting file:").append(inputFile.getAbsolutePath()).toString());
                                fin = new FileInputStream(inputFile);
                                fout = new FileOutputStream(f);
                                while ((read = fin.read(b)) != -1) {
                                    fout.write(b, 0, read);
                                }
                                pw.println(" ... done");
                                fin.close();
                                fout.close();
                            } catch (FileNotFoundException ex) {
                                DialogDisplayer.getDefault().notify(new NotifyDescriptor.Exception(ex, "Error while importing sources!"));
                            } catch (IOException ex) {
                                DialogDisplayer.getDefault().notify(new NotifyDescriptor.Exception(ex, "Error while importing sources!"));
                            } finally {
                                if (fin != null) {
                                    try {
                                        fin.close();
                                    } catch (IOException ex) {
                                        DialogDisplayer.getDefault().notify(new NotifyDescriptor.Exception(ex, "Error while importing sources!"));
                                    }
                                }
                                if (fout != null) {
                                    try {
                                        fout.close();
                                    } catch (IOException ex) {
                                    }
                                }
                            }
                        } catch (IOException ex) {
                            DialogDisplayer.getDefault().notify(new NotifyDescriptor.Exception(ex, "Error while importing sources!"));
                        }
                    }
                    pw.println("Import sources completed successfully.");
                } else {
                    pw.println("Cannot write to the destination directory." + " Please check the priviledges and try again.");
                    return;
                }
            } catch (FileStateInvalidException ex) {
                DialogDisplayer.getDefault().notify(new NotifyDescriptor.Exception(ex, "Error while importing sources!"));
                pw.println("Import failed!!");
            } catch (URISyntaxException ex) {
                DialogDisplayer.getDefault().notify(new NotifyDescriptor.Exception(ex, "Error while importing sources!"));
                pw.println("Import failed!!");
            }
        }
    }
