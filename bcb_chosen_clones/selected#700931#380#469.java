    protected void attachAndRunDemo(final File selectedFile, final File autoStartFile) {
        if (selectedFile.getName().toLowerCase().endsWith(".pdf")) {
            try {
                final File pdfFile = selectedFile;
                if (pdfFile.exists()) {
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(pdfFile);
                    } else {
                        System.out.println("Awt Desktop is not supported!");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            config.sidplay2().setLastDirectory(config.sidplay2().getDemos());
            if (diskfileFilter.accept(selectedFile)) {
                getUiEvents().fireEvent(IInsertMedia.class, new IInsertMedia() {

                    @Override
                    public MediaType getMediaType() {
                        return MediaType.DISK;
                    }

                    @Override
                    public File getSelectedMedia() {
                        return selectedFile;
                    }

                    @Override
                    public File getAutostartFile() {
                        return autoStartFile;
                    }

                    @Override
                    public Component getComponent() {
                        return DiskCollection.this;
                    }
                });
            } else {
                getUiEvents().fireEvent(IInsertMedia.class, new IInsertMedia() {

                    @Override
                    public MediaType getMediaType() {
                        return MediaType.TAPE;
                    }

                    @Override
                    public File getSelectedMedia() {
                        return selectedFile;
                    }

                    @Override
                    public File getAutostartFile() {
                        return autoStartFile;
                    }

                    @Override
                    public Component getComponent() {
                        return DiskCollection.this;
                    }
                });
            }
            if (autoStartFile == null) {
                final String command;
                if (diskfileFilter.accept(selectedFile)) {
                    command = "LOAD\"*\",8,1\rRUN\r";
                } else {
                    command = "LOAD\rRUN\r";
                }
                getUiEvents().fireEvent(Reset.class, new Reset() {

                    @Override
                    public boolean switchToVideoTab() {
                        return true;
                    }

                    @Override
                    public String getCommand() {
                        return command;
                    }

                    @Override
                    public Component getComponent() {
                        return DiskCollection.this;
                    }
                });
            }
        }
    }
