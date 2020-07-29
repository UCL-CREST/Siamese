    public void save() {
        final JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileFilter() {

            public String getDescription() {
                return "PDF File";
            }

            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".pdf");
            }
        });
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File targetFile = fc.getSelectedFile();
        if (!targetFile.getName().toLowerCase().endsWith(".pdf")) {
            targetFile = new File(targetFile.getParentFile(), targetFile.getName() + ".pdf");
        }
        if (targetFile.exists()) {
            if (JOptionPane.showConfirmDialog(this, "Do you want to overwrite the file?") != JOptionPane.YES_OPTION) {
                return;
            }
        }
        try {
            final InputStream is = new FileInputStream(filename);
            try {
                final OutputStream os = new FileOutputStream(targetFile);
                try {
                    final byte[] buffer = new byte[32768];
                    for (int read; (read = is.read(buffer)) != -1; ) {
                        os.write(buffer, 0, read);
                    }
                } finally {
                    os.close();
                }
            } finally {
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
