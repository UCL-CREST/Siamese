    void IconmenuItem5_actionPerformed(ActionEvent e) {
        JFileChooser jFileChooser1 = new JFileChooser();
        String separator = "";
        if (getPath() != null && !getPath().equals("")) {
            jFileChooser1.setCurrentDirectory(new File(getPath()));
            jFileChooser1.setSelectedFile(new File(getPath()));
        }
        if (JFileChooser.APPROVE_OPTION == jFileChooser1.showOpenDialog(this.getFatherFrame())) {
            setPath(jFileChooser1.getSelectedFile().getPath());
            separator = jFileChooser1.getSelectedFile().separator;
            File dirImg = new File("." + separator + "images");
            if (!dirImg.exists()) {
                dirImg.mkdir();
            }
            int index = getPath().lastIndexOf(separator);
            String imgName = getPath().substring(index);
            String newPath = dirImg + imgName;
            try {
                File inputFile = new File(getPath());
                File outputFile = new File(newPath);
                if (!inputFile.getCanonicalPath().equals(outputFile.getCanonicalPath())) {
                    FileInputStream in = new FileInputStream(inputFile);
                    FileOutputStream out = new FileOutputStream(outputFile);
                    int c;
                    while ((c = in.read()) != -1) out.write(c);
                    in.close();
                    out.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                LogHandler.log(ex.getMessage(), Level.INFO, "LOG_MSG", isLoggingEnabled());
                JOptionPane.showMessageDialog(null, ex.getMessage().substring(0, Math.min(ex.getMessage().length(), getFatherPanel().MAX_DIALOG_MSG_SZ)) + "-" + getClass(), "", JOptionPane.ERROR_MESSAGE);
            }
            setPath(newPath);
            if (getDefaultPath() == null || getDefaultPath().equals("")) {
                String msgString = "E' stata selezionata un'immagine da associare all'IconShape, ma non e' " + "stata selezionata ancora nessun'immagine di default. Imposto quella scelta anche come " + "immagine di default?";
                if (JOptionPane.showConfirmDialog(null, msgString.substring(0, Math.min(msgString.length(), getFatherPanel().MAX_DIALOG_MSG_SZ)), "choose one", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    setDefaultPath(newPath);
                    createDefaultImage();
                }
            }
            createImage();
        }
    }
