    void IconmenuItem6_actionPerformed(ActionEvent e) {
        JFileChooser jFileChooser1 = new JFileChooser();
        String separator = "";
        if (JFileChooser.APPROVE_OPTION == jFileChooser1.showOpenDialog(this.getFatherFrame())) {
            setDefaultPath(jFileChooser1.getSelectedFile().getPath());
            separator = jFileChooser1.getSelectedFile().separator;
            File dirImg = new File("." + separator + "images");
            if (!dirImg.exists()) {
                dirImg.mkdir();
            }
            int index = getDefaultPath().lastIndexOf(separator);
            String imgName = getDefaultPath().substring(index);
            String newPath = dirImg + imgName;
            try {
                File inputFile = new File(getDefaultPath());
                File outputFile = new File(newPath);
                FileInputStream in = new FileInputStream(inputFile);
                FileOutputStream out = new FileOutputStream(outputFile);
                int c;
                while ((c = in.read()) != -1) out.write(c);
                in.close();
                out.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                LogHandler.log(ex.getMessage(), Level.INFO, "LOG_MSG", isLoggingEnabled());
                JOptionPane.showMessageDialog(null, ex.getMessage().substring(0, Math.min(ex.getMessage().length(), getFatherPanel().MAX_DIALOG_MSG_SZ)) + "-" + getClass(), "", JOptionPane.ERROR_MESSAGE);
            }
            setDefaultPath(newPath);
            createDefaultImage();
        }
    }
