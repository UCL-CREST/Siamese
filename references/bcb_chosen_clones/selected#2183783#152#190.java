    public void actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser("");
        fc.setMultiSelectionEnabled(false);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int option = 0;
        boolean save = m_data != null;
        if (save) option = fc.showSaveDialog(this); else option = fc.showOpenDialog(this);
        if (option != JFileChooser.APPROVE_OPTION) return;
        File file = fc.getSelectedFile();
        if (file == null) return;
        log.info(file.toString());
        try {
            if (save) {
                FileOutputStream os = new FileOutputStream(file);
                byte[] buffer = (byte[]) m_data;
                os.write(buffer);
                os.flush();
                os.close();
                log.config("Save to " + file + " #" + buffer.length);
            } else {
                FileInputStream is = new FileInputStream(file);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024 * 8];
                int length = -1;
                while ((length = is.read(buffer)) != -1) os.write(buffer, 0, length);
                is.close();
                byte[] data = os.toByteArray();
                m_data = data;
                log.config("Load from " + file + " #" + data.length);
                os.close();
            }
        } catch (Exception ex) {
            log.log(Level.WARNING, "Save=" + save, ex);
        }
        try {
            fireVetoableChange(m_columnName, null, m_data);
        } catch (PropertyVetoException pve) {
        }
    }
