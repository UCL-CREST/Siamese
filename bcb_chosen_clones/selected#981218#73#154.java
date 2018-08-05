    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonPut) {
            int returnVal = fileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                Thread thread = new Thread(new Runnable() {

                    public void run() {
                        LoadingBar loadingBar = LoadingBar.getInstance(frame);
                        loadingBar.setConfiguration(true, 100);
                        loadingBar.setValue(1, "Doing Put...");
                        loadingBar.begin();
                        File fichero = fileChooser.getSelectedFile();
                        FileResource fileResource = new FileResource(fichero);
                        try {
                            getDHashNode().put(fileResource);
                        } catch (StorageException e1) {
                            loadingBar.end();
                            JOptionPane.showMessageDialog(frame, e1.getMessage());
                        }
                        loadingBar.end();
                    }
                });
                thread.start();
            }
        } else {
            if (e.getSource() == buttonGet) {
                final String a = JOptionPane.showInputDialog(null, "Please write the name of the file that you wish retrieve", "Insert a file name", JOptionPane.INFORMATION_MESSAGE);
                if (a == null) return;
                Thread thread = new Thread(new Runnable() {

                    public void run() {
                        LoadingBar loadingBar = LoadingBar.getInstance(frame);
                        loadingBar.setConfiguration(true, 100);
                        loadingBar.setValue(1, "Doing Get...");
                        loadingBar.begin();
                        try {
                            Resource resource = getDHashNode().get(a);
                            if (resource instanceof FileResource) {
                                FileResource fileResource = (FileResource) resource;
                                Map<String, Object> params = new HashMap<String, Object>();
                                params.put(ResourceParams.MANAGER_NAME.name(), getDHashNode().getName() + "/gets/");
                                params.put(ResourceParams.PERSIST_TYPE.name(), "get");
                                fileResource.persist(params);
                            }
                        } catch (StorageException e1) {
                            loadingBar.end();
                            JOptionPane.showMessageDialog(frame, e1.getMessage() + "\nPlease try again later", "ERROR", JOptionPane.INFORMATION_MESSAGE);
                        }
                        loadingBar.end();
                    }
                });
                thread.start();
            } else {
                if (e.getSource() == buttonOpen) {
                    File file = new File("dhash/" + EscapeChars.forHTML(getDHashNode().getName(), true));
                    Desktop desktop = null;
                    if (Desktop.isDesktopSupported()) {
                        desktop = Desktop.getDesktop();
                    }
                    try {
                        desktop.open(file);
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(frame, "The save path is: " + file.getAbsolutePath() + File.separator, "ERROR", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    Thread thread = new Thread(new Runnable() {

                        public void run() {
                            LoadingBar loadingBar = LoadingBar.getInstance(frame);
                            loadingBar.setConfiguration(true, 100);
                            loadingBar.setValue(1, "Leaving...");
                            loadingBar.begin();
                            exit();
                            loadingBar.end();
                        }
                    });
                    thread.start();
                }
            }
        }
    }
