    protected void actionExportMaps(@SuppressWarnings("unused") ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        if (mapsPrevDir == null) {
            mapsPrevDir = new File(".").getAbsoluteFile();
        } else {
            fileChooser.setCurrentDirectory(mapsPrevDir);
        }
        fileChooser.setSelectedFile(new File(mapsPrevDir, "tsp_maps.zip"));
        fileChooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                if (f.getAbsolutePath().toLowerCase().endsWith(".zip")) return true;
                return false;
            }

            @Override
            public String getDescription() {
                return "*.zip";
            }
        });
        if (fileChooser.showSaveDialog(parent.gui) == JFileChooser.APPROVE_OPTION) {
            try {
                mapsPrevDir = fileChooser.getCurrentDirectory();
                File f = fileChooser.getSelectedFile();
                String fileName = f.getName();
                if (!fileName.toLowerCase().endsWith(".zip")) {
                    String ext = "zip";
                    if (!fileName.endsWith(".")) {
                        ext = "." + ext;
                    }
                    fileName += ext;
                    File parentDir = f.getParentFile();
                    f = new File(parentDir, fileName);
                }
                if (f.exists()) {
                    if (JOptionPane.showConfirmDialog(parent.gui, "Should be existing file overwritten ?", "Question", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                        return;
                    }
                }
                ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(f));
                zip.setLevel(9);
                for (String mapName : TSP.mapFiles) {
                    if (mapName != null) {
                        InputStream i = new Object().getClass().getResourceAsStream("/org/saiko/ai/genetics/tsp/etc/" + mapName + ".csv");
                        if (i != null) {
                            ZipEntry zipEntry = new ZipEntry(mapName + ".csv");
                            zip.putNextEntry(zipEntry);
                            byte b[] = new byte[1024];
                            int size;
                            while ((size = i.read(b)) > 0) {
                                zip.write(b, 0, size);
                            }
                            zip.closeEntry();
                        }
                        i.close();
                    }
                }
                zip.close();
                JOptionPane.showMessageDialog(parent.gui, "OK, maps exported to the file: \n" + f, "Info", JOptionPane.INFORMATION_MESSAGE);
            } catch (Throwable ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(parent.gui, "Can not export maps.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
