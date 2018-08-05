    private void buttonLoadActionPerformed(java.awt.event.ActionEvent evt) {
        final JFileChooser fc = new JFileChooser(m_DefaultFolder);
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            m_Bino.load(file.toString());
            m_NomFichierActif = file.toString();
            m_DefaultFolder = m_NomFichierActif;
            initValues();
        }
        setTitre();
        repaint();
    }
