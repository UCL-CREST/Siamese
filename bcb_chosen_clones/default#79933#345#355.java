    private void buttonSaveAsActionPerformed(java.awt.event.ActionEvent evt) {
        final JFileChooser fc = new JFileChooser(m_NomFichierActif);
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            m_Bino.save(file.toString());
            m_NomFichierActif = file.toString();
            m_DefaultFolder = m_NomFichierActif;
        }
        setTitre();
    }
