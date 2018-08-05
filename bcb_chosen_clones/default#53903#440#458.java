        public void actionPerformed(ActionEvent e) {
            Frame frame = getFrame();
            JFileChooser chooser = new JFileChooser(new File("."));
            JCadFileFilter filter = new JCadFileFilter(new String("DXF"), "DXF Files");
            chooser.addChoosableFileFilter(filter);
            int returnVal = chooser.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String file = chooser.getSelectedFile().getName();
                if (file == null) {
                    return;
                }
                File f = chooser.getSelectedFile();
                if (f.exists()) {
                    frame.setTitle(file);
                    cadpanel.loadFile(f);
                }
                repaint();
            }
        }
