    private void buttonSaveActionPerformed(java.awt.event.ActionEvent evt) {
        if (m_NomFichierActif == "") {
            final JFileChooser fc = new JFileChooser();
            int returnVal = fc.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                m_NomFichierActif = file.toString();
                m_DefaultFolder = m_NomFichierActif;
            }
        }
        if (m_NomFichierActif != "") m_Bino.save(m_NomFichierActif);
        setTitre();
    }
