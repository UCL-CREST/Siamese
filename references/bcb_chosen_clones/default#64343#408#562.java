    private void listeners() {
        inferButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String args = "-marginal -i samples/smoke/prog.mln -e samples/smoke/evidence.db " + "-queryFile samples/smoke/query.db -r out.txt";
                String[] argsArray;
                argsArray = args.split(" ");
                for (final String arg : argsArray) {
                    System.out.println(arg);
                }
                System.out.println("\nresultado da combobox: \n" + jCB.getSelectedItem());
                System.out.println("*** Welcome to mine " + Config.product_name + "!");
                CommandOptions options = new CommandOptions();
                Config.db_url = "jdbc:postgresql://localhost:5432/tuffydb";
                Config.db_username = "tuffer";
                Config.db_password = "strongPasswoRd";
                String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
                String user = System.getProperty("user.name").toLowerCase().replaceAll("\\W", "_");
                String machine = null;
                try {
                    machine = java.net.InetAddress.getLocalHost().getHostName().toLowerCase().replaceAll("\\W", "_");
                } catch (UnknownHostException e2) {
                    e2.printStackTrace();
                }
                System.out.println(machine);
                String prod = Config.product_line;
                Config.db_schema += prod + "_" + machine + "_" + user + "_" + pid;
                if (jCB.getSelectedItem().equals("Marginal")) {
                    options.marginal = true;
                }
                if (jCB.getSelectedItem().equals("Dual")) {
                    options.dual = true;
                }
                options.fprog = MLNFile;
                options.fevid = arqEvd.getText();
                options.fquery = arqQry.getText();
                options.fout = "out.txt";
                if (!options.isDLearningMode) {
                    System.out.println("disablePartition" + options.disablePartition);
                    if (!options.disablePartition) {
                        result += (String) new NewPartInfer().run(options);
                    } else {
                        new NonPartInfer().run(options);
                    }
                } else {
                    DNLearner l = new DNLearner();
                    try {
                        l.run(options);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
                jTAInfer.setText(result);
            }
        });
        MLNLoadButton.addActionListener(new ActionListener() {

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
        });
        MLNSaveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    BufferedWriter out = new BufferedWriter(new FileWriter(MLNFile));
                    out.write(jTA_MLN.getText());
                    out.close();
                } catch (IOException ex) {
                }
            }
        });
        paramApplyButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println("aplicou");
            }
        });
        paramSaveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    BufferedWriter out = new BufferedWriter(new FileWriter(confFile));
                    Set<Map.Entry<String, Parameter>> sParam = parameterMap.entrySet();
                    Iterator itParam = sParam.iterator();
                    System.out.println(sParam.getClass());
                    Set<Map.Entry<String, JComponent>> sGui = parameterGuiMap.entrySet();
                    Iterator itGui = sGui.iterator();
                    System.out.println(sGui.getClass());
                    while (itParam.hasNext()) {
                        Map.Entry mParam = (Map.Entry) itParam.next();
                        System.out.println("mParam -> " + mParam.getKey());
                        while (itGui.hasNext()) {
                            Map.Entry mGui = (Map.Entry) itGui.next();
                            System.out.println("paramKey " + mParam.getKey() + ", GuiKey " + mGui.getKey());
                            if (mParam.getKey().equals(mGui.getKey())) {
                                System.out.println("entrou");
                                Parameter param = ((Entry<String, Parameter>) mParam).getValue();
                                if (mGui.getValue().getClass().equals(textField.getClass())) {
                                    JTextField value = ((Entry<String, JTextField>) mGui).getValue();
                                    out.write(param.getAttribute() + " = " + value.getText() + "\n");
                                } else if (mGui.getValue().getClass().equals(checkBox.getClass())) {
                                    JCheckBox value = ((Entry<String, JCheckBox>) mGui).getValue();
                                    out.write(param.getAttribute() + " = " + value.isSelected() + "\n");
                                } else System.out.println(mGui.getValue().getClass());
                                System.out.println("passou");
                            }
                        }
                        itGui = sGui.iterator();
                    }
                    out.close();
                } catch (IOException ex) {
                }
            }
        });
    }
