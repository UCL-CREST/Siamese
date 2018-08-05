                public void actionPerformed(java.awt.event.ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser(relativePath);
                    fileChooser.showSaveDialog(null);
                    File chosenFile = fileChooser.getSelectedFile();
                    try {
                        byte b[] = new byte[512];
                        ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(chosenFile));
                        HashSet<String> written = new HashSet<String>();
                        for (int i = 0; i < objects.size(); i++) {
                            OBJE obje = objects.elementAt(i);
                            String fpath = obje.getFilepath();
                            int hasDrive = fpath.indexOf(":\\");
                            if (hasDrive == -1) hasDrive = fpath.indexOf(":/");
                            String firstChar = fpath.substring(0, 1);
                            if ((hasDrive == -1) && (!firstChar.equals("/"))) fpath = relativePath + fpath;
                            if (!written.contains(fpath)) {
                                written.add(fpath);
                                System.out.println("Added file to zip " + fpath);
                                InputStream in = new FileInputStream(fpath);
                                ZipEntry ze = new ZipEntry("media/" + obje.getFilename());
                                zout.putNextEntry(ze);
                                int len = 0;
                                while ((len = in.read(b)) != -1) {
                                    zout.write(b, 0, len);
                                }
                                zout.closeEntry();
                                in.close();
                            }
                        }
                        zout.close();
                    } catch (IOException ie) {
                        System.out.println(ie.getMessage());
                    }
                }
