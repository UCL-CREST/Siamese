        public void actionPerformed(ActionEvent event) {
            int wynik = wybor.showOpenDialog(ProjectGUI.this);
            if (wynik == JFileChooser.APPROVE_OPTION) {
                try {
                    area.setText("");
                    String nazwa = wybor.getSelectedFile().getPath();
                    FileReader in = new FileReader(new File(nazwa));
                    BufferedReader buf = new BufferedReader(in);
                    String end = "\n";
                    String line;
                    while ((line = buf.readLine()) != null) {
                        area.append(line.concat(end));
                    }
                    buf.close();
                    in.close();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Blad podczas odczytu pliku", "Blad", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
