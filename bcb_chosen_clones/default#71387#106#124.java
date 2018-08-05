            public void actionPerformed(ActionEvent arg0) {
                JFileChooser d = new JFileChooser("Converted XML file");
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
                    tfConverted.setText(d.getSelectedFile().getPath());
                }
            }
