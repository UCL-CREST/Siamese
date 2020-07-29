            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                fc.setCurrentDirectory(new File("."));
                int res = fc.showOpenDialog(null);
                if (res == JFileChooser.APPROVE_OPTION) {
                    loadedFile = fc.getSelectedFile();
                    System.out.println(loadedFile);
                    MLNFile = loadedFile.toString();
                    System.out.println(MLNFile);
                    if (MLNFile != "") {
                        inferButton.setEnabled(true);
                    }
                }
                try {
                    BufferedReader in = new BufferedReader(new FileReader(MLNFile));
                    String str = "";
                    while (in.ready()) {
                        str += in.readLine() + "\n";
                    }
                    jTA_MLN.setText(str);
                    in.close();
                } catch (IOException ex) {
                }
                try {
                    BufferedReader in = new BufferedReader(new FileReader(MLNFile));
                    String str = "";
                    String line = "";
                    String comment = "//";
                    while (in.ready()) {
                        line = in.readLine() + "\n";
                        if (line.length() == 1) ; else if (line.substring(0, 2).equals(comment)) ; else {
                            str += line;
                            System.out.println("str: " + str);
                        }
                        System.out.println("line: " + line);
                    }
                    jTA_Tree.setText(str);
                    in.close();
                } catch (IOException ex) {
                }
            }
