        public void actionPerformed(ActionEvent event) {
            int wynik = wyborPliku.showOpenDialog(Pasinterpreter.this);
            if (wynik == JFileChooser.APPROVE_OPTION) {
                try {
                    tekst.setText("");
                    String nazwa = wyborPliku.getSelectedFile().getPath();
                    FileReader in = new FileReader(new File(nazwa));
                    BufferedReader buf = new BufferedReader(in);
                    String enter = "\n";
                    String linia;
                    while ((linia = buf.readLine()) != null) {
                        tekst.append(linia.concat(enter));
                    }
                    ;
                    buf.close();
                    in.close();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Blad otwarcia", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
