        public void run() {
            try {
                File outDir = new File(outDirTextField.getText());
                if (!outDir.exists()) {
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            JOptionPane.showMessageDialog(UnpackWizard.this, "The chosen directory does not exist!", "Directory Not Found Error", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                    return;
                }
                if (!outDir.isDirectory()) {
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            JOptionPane.showMessageDialog(UnpackWizard.this, "The chosen file is not a directory!", "Not a Directory Error", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                    return;
                }
                if (!outDir.canWrite()) {
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            JOptionPane.showMessageDialog(UnpackWizard.this, "Cannot write to the chosen directory!", "Directory Not Writeable Error", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                    return;
                }
                File archiveDir = new File("foo.bar").getAbsoluteFile().getParentFile();
                URL baseUrl = UnpackWizard.class.getClassLoader().getResource(UnpackWizard.class.getName().replaceAll("\\.", "/") + ".class");
                if (baseUrl.getProtocol().equals("jar")) {
                    String jarPath = baseUrl.getPath();
                    jarPath = jarPath.substring(0, jarPath.indexOf('!'));
                    if (jarPath.startsWith("file:")) {
                        try {
                            archiveDir = new File(new URI(jarPath)).getAbsoluteFile().getParentFile();
                        } catch (URISyntaxException e1) {
                            e1.printStackTrace(System.err);
                        }
                    }
                }
                SortedMap<Integer, String> inputFileNames = new TreeMap<Integer, String>();
                for (Entry<Object, Object> anEntry : indexProperties.entrySet()) {
                    String key = anEntry.getKey().toString();
                    if (key.startsWith("archive file ")) {
                        inputFileNames.put(Integer.parseInt(key.substring("archive file ".length())), anEntry.getValue().toString());
                    }
                }
                byte[] buff = new byte[64 * 1024];
                try {
                    long bytesToWrite = 0;
                    long bytesReported = 0;
                    long bytesWritten = 0;
                    for (String aFileName : inputFileNames.values()) {
                        File aFile = new File(archiveDir, aFileName);
                        if (aFile.exists()) {
                            if (aFile.isFile()) {
                                bytesToWrite += aFile.length();
                            } else {
                                final File wrongFile = aFile;
                                SwingUtilities.invokeLater(new Runnable() {

                                    public void run() {
                                        JOptionPane.showMessageDialog(UnpackWizard.this, "File \"" + wrongFile.getAbsolutePath() + "\" is not a standard file!", "Non Standard File Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                });
                                return;
                            }
                        } else {
                            final File wrongFile = aFile;
                            SwingUtilities.invokeLater(new Runnable() {

                                public void run() {
                                    JOptionPane.showMessageDialog(UnpackWizard.this, "File \"" + wrongFile.getAbsolutePath() + "\" does not exist!", "File Not Found Error", JOptionPane.ERROR_MESSAGE);
                                }
                            });
                            return;
                        }
                    }
                    MultiFileInputStream mfis = new MultiFileInputStream(archiveDir, inputFileNames.values().toArray(new String[inputFileNames.size()]));
                    TarArchiveInputStream tis = new TarArchiveInputStream(new BufferedInputStream(mfis));
                    TarArchiveEntry tarEntry = tis.getNextTarEntry();
                    while (tarEntry != null) {
                        File outFile = new File(outDir.getAbsolutePath() + "/" + tarEntry.getName());
                        if (outFile.exists()) {
                            final File wrongFile = outFile;
                            SwingUtilities.invokeLater(new Runnable() {

                                public void run() {
                                    JOptionPane.showMessageDialog(UnpackWizard.this, "Was about to write out file \"" + wrongFile.getAbsolutePath() + "\" but it already " + "exists.\nPlease [re]move existing files out of the way " + "and try again.", "File Not Found Error", JOptionPane.ERROR_MESSAGE);
                                }
                            });
                            return;
                        }
                        if (tarEntry.isDirectory()) {
                            outFile.getAbsoluteFile().mkdirs();
                        } else {
                            outFile.getAbsoluteFile().getParentFile().mkdirs();
                            OutputStream os = new BufferedOutputStream(new FileOutputStream(outFile));
                            int len = tis.read(buff, 0, buff.length);
                            while (len != -1) {
                                os.write(buff, 0, len);
                                bytesWritten += len;
                                if (bytesWritten - bytesReported > (10 * 1024 * 1024)) {
                                    bytesReported = bytesWritten;
                                    final int progress = (int) (bytesReported * 100 / bytesToWrite);
                                    SwingUtilities.invokeLater(new Runnable() {

                                        @Override
                                        public void run() {
                                            progressBar.setValue(progress);
                                        }
                                    });
                                }
                                len = tis.read(buff, 0, buff.length);
                            }
                            os.close();
                        }
                        tarEntry = tis.getNextTarEntry();
                    }
                    long expectedCrc = 0;
                    try {
                        expectedCrc = Long.parseLong(indexProperties.getProperty("CRC32", "0"));
                    } catch (NumberFormatException e) {
                        System.err.println("Error while obtaining the expected CRC");
                        e.printStackTrace(System.err);
                    }
                    if (mfis.getCRC() == expectedCrc) {
                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                progressBar.setValue(0);
                                JOptionPane.showMessageDialog(UnpackWizard.this, "Extraction completed successfully!", "Done!", JOptionPane.INFORMATION_MESSAGE);
                            }
                        });
                        return;
                    } else {
                        System.err.println("CRC Error: was expecting " + expectedCrc + " but got " + mfis.getCRC());
                        SwingUtilities.invokeLater(new Runnable() {

                            public void run() {
                                progressBar.setValue(0);
                                JOptionPane.showMessageDialog(UnpackWizard.this, "CRC Error: the data extracted does not have the expected CRC!\n" + "You should probably delete the extracted files, as they are " + "likely to be invalid.", "CRC Error", JOptionPane.ERROR_MESSAGE);
                            }
                        });
                        return;
                    }
                } catch (final IOException e) {
                    e.printStackTrace(System.err);
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            progressBar.setValue(0);
                            JOptionPane.showMessageDialog(UnpackWizard.this, "Input/Output Error: " + e.getLocalizedMessage(), "Input/Output Error", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                    return;
                }
            } finally {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        progressBar.setValue(0);
                        setEnabled(true);
                    }
                });
            }
        }
