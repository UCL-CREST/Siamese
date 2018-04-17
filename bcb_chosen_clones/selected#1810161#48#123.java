    public static void exportProject(IProject project) {
        Shell shell = Display.getCurrent().getActiveShell();
        IFolder outputFolder = BaseProjectHelper.getOutputFolder(project);
        if (outputFolder != null) {
            IPath binLocation = outputFolder.getLocation();
            String fileName = project.getName() + AndroidConstants.DOT_ANDROID_PACKAGE;
            File file = new File(binLocation.toOSString() + File.separator + fileName);
            if (file.exists() == false || file.isFile() == false) {
                MessageDialog.openError(Display.getCurrent().getActiveShell(), "Android IDE Plug-in", String.format("Failed to export %1$s: %2$s doesn't exist!", project.getName(), file.getPath()));
                return;
            }
            FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
            fileDialog.setText("Export Project");
            fileDialog.setFileName(fileName);
            String saveLocation = fileDialog.open();
            if (saveLocation != null) {
                ZipInputStream zis = null;
                ZipOutputStream zos = null;
                FileInputStream input = null;
                FileOutputStream output = null;
                try {
                    input = new FileInputStream(file);
                    zis = new ZipInputStream(input);
                    File saveFile = new File(saveLocation);
                    output = new FileOutputStream(saveFile);
                    zos = new ZipOutputStream(output);
                } catch (FileNotFoundException e) {
                    if (zis != null) {
                        try {
                            zis.close();
                        } catch (IOException e1) {
                        }
                    }
                    MessageDialog.openError(shell, "Android IDE Plug-in", String.format("Failed to export %1$s: %2$s doesn't exist!", project.getName(), file.getPath()));
                    return;
                }
                try {
                    ZipEntry entry;
                    byte[] buffer = new byte[4096];
                    while ((entry = zis.getNextEntry()) != null) {
                        String name = entry.getName();
                        if (entry.isDirectory() || name.startsWith("META-INF/")) {
                            continue;
                        }
                        ZipEntry newEntry;
                        if (entry.getMethod() == JarEntry.STORED) {
                            newEntry = new JarEntry(entry);
                        } else {
                            newEntry = new JarEntry(name);
                        }
                        zos.putNextEntry(newEntry);
                        int count;
                        while ((count = zis.read(buffer)) != -1) {
                            zos.write(buffer, 0, count);
                        }
                        zos.closeEntry();
                        zis.closeEntry();
                    }
                } catch (IOException e) {
                    MessageDialog.openError(shell, "Android IDE Plug-in", String.format("Failed to export %1$s: %2$s", project.getName(), e.getMessage()));
                } finally {
                    try {
                        zos.close();
                    } catch (IOException e) {
                    }
                    try {
                        zis.close();
                    } catch (IOException e) {
                    }
                }
                MessageDialog.openWarning(shell, "Android IDE Plug-in", String.format("An unsigned package of the application was saved at\n%1$s\n\n" + "Before publishing the application you will need to:\n" + "- Sign the application with your release key,\n" + "- run zipalign on the signed package. ZipAlign is located in <SDK>/tools/\n\n" + "Aligning applications allows Android to use application resources\n" + "more efficiently.", saveLocation));
            }
        } else {
            MessageDialog.openError(shell, "Android IDE Plug-in", String.format("Failed to export %1$s: Could not get project output location", project.getName()));
        }
    }
