            public void actionPerformed(ActionEvent e) {
                JFileChooser d = new JFileChooser("Keywarden XML file");
                d.setCurrentDirectory(new File("."));
                d.setFileFilter(new FileFilter() {

                    @Override
                    public boolean accept(File f) {
                        return f.isDirectory() || f.getName().toLowerCase().endsWith(".xml");
                    }

                    @Override
                    public String getDescription() {
                        return "*.xml";
                    }
                });
                if (d.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    tfKeywarden.setText(d.getSelectedFile().getPath());
                }
            }
