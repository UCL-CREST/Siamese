            public void actionPerformed(ActionEvent event) {
                try {
                    final JFileChooser fc = new JFileChooser();
                    fc.showOpenDialog(reliabilityFrame);
                    try {
                        File file = fc.getSelectedFile();
                        if (file != null) {
                            FileReader reader = new FileReader(file);
                            BufferedReader breader = new BufferedReader(reader);
                            String line = breader.readLine();
                            if (line != null) {
                                line = line.replaceAll("X", "*");
                                line = line.replaceAll(" ", "");
                                sysAlgebra.setText(line);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
