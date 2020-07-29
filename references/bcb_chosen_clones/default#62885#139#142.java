        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) m_viewer.setGMNFile(chooser.getSelectedFile());
        }
