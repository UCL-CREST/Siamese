    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == close) {
            if (mode == STARTUP) System.exit(0); else {
                this.setVisible(false);
                dispose();
            }
        }
        if (evt.getSource() == back) {
            switch(step) {
                case 2:
                    step = 0;
                    Step1();
                    break;
                case 3:
                    step = 1;
                    Step2(project_name);
                    break;
            }
        }
        if (evt.getSource() == next) {
            switch(step) {
                case 1:
                    String name = new String(text.getText());
                    if (!name.equals("")) Step2(name);
                    break;
                case 2:
                    if (listModel.size() < 1) break;
                    comps = new String[listModel.size()];
                    for (int i = 0; i < listModel.size(); i++) comps[i] = (String) listModel.get(i);
                    Step3();
                    break;
            }
        }
        if (evt.getSource() == cancel) {
            if (mode == NEW || mode == MODIFY) {
                setVisible(false);
                dispose();
            } else {
                step = 0;
                Step0();
            }
        }
        if (evt.getSource() == add) {
            switch(step) {
                case 2:
                    if (combo.getSelectedItem().equals("PIC 16F84 Microcontroller")) {
                        String name = (String) combo.getSelectedItem();
                        if (listModel.contains(name)) {
                            listModel.addElement(name + " ");
                        } else listModel.addElement(name);
                        Comp temp = (Comp) foundcomps.get(combo.getSelectedIndex());
                        listModel_nr.addElement(temp.getName() + " " + project_name + "/" + project_name + ".asm");
                    } else {
                        String name = (String) combo.getSelectedItem();
                        if (listModel.contains(name)) {
                            listModel.addElement(name + " ");
                        } else listModel.addElement(name);
                        Comp temp = (Comp) foundcomps.get(combo.getSelectedIndex());
                        listModel_nr.addElement(temp.getName());
                    }
                    break;
                case 3:
                    String con = output.getSelectedItem() + " : " + output_pin.getSelectedItem() + " -> " + input.getSelectedItem() + " : " + input_pin.getSelectedItem();
                    String con_nr = output.getSelectedIndex() + ":" + output_pin.getSelectedIndex() + "," + input.getSelectedIndex() + ":" + input_pin.getSelectedIndex();
                    connects.addElement(con);
                    connects_nr.addElement(con_nr);
                    break;
            }
        }
        if (evt.getSource() == remove) {
            int index = list.getSelectedIndex();
            if (index != -1) {
                switch(step) {
                    case 2:
                        listModel.remove(index);
                        listModel_nr.remove(index);
                        connects = new DefaultListModel();
                        connects_nr = new DefaultListModel();
                        break;
                    case 3:
                        connects.remove(index);
                        connects_nr.remove(index);
                        break;
                }
            }
        }
        if (evt.getSource() == output) {
            output_pin.removeAllItems();
            for (int i = 0; i < foundcomps.size(); i++) {
                String sel_comp = ((String) output.getSelectedItem()).trim();
                Comp temp = (Comp) foundcomps.get(i);
                if (sel_comp.equals(temp.getDispName())) {
                    String[] pinnames = temp.getPins();
                    int j = 0;
                    while (j < pinnames.length) output_pin.addItem(pinnames[j++]);
                }
            }
        }
        if (evt.getSource() == input) {
            input_pin.removeAllItems();
            for (int i = 0; i < foundcomps.size(); i++) {
                String sel_comp = ((String) input.getSelectedItem()).trim();
                Comp temp = (Comp) foundcomps.get(i);
                if (sel_comp.equals(temp.getDispName())) {
                    String[] pinnames = temp.getPins();
                    int j = 0;
                    while (j < pinnames.length) input_pin.addItem(pinnames[j++]);
                }
            }
        }
        if (evt.getSource() == finish) {
            JFileChooser projectsaver = new JFileChooser(picdev_projectdir);
            projectsaver.setDialogTitle("Save Project as");
            ExampleFileFilter filter = new ExampleFileFilter();
            filter.addExtension("pds");
            filter.setDescription("PIC Development Studio project");
            projectsaver.setFileFilter(filter);
            File project_filename = new File(picdev_projectdir + "/" + project_name + ".pds");
            projectsaver.setSelectedFile(project_filename);
            if (projectsaver.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    project_filename = projectsaver.getSelectedFile();
                    RandomAccessFile project_file = new RandomAccessFile(project_filename, "rw");
                    project_file.writeBytes("# Do NOT modify the project file manually!!\n");
                    project_file.writeBytes("# Version 1.0\n");
                    project_file.writeBytes("# Components:\n");
                    for (int i = 0; i < listModel.size(); i++) project_file.writeBytes((String) listModel_nr.get(i) + "\n");
                    project_file.writeBytes("# Connections:\n");
                    for (int i = 0; i < connects_nr.size(); i++) project_file.writeBytes((String) connects_nr.get(i) + "\n");
                    project_file.writeBytes("# Connection info:\n");
                    for (int i = 0; i < connects.size(); i++) project_file.writeBytes((String) connects.get(i) + "\n");
                    project_file.writeBytes("# End");
                    project_file.close();
                    String project_path = project_filename.getPath();
                    project_path = project_path.substring(0, Math.max(project_path.lastIndexOf('/'), project_path.lastIndexOf('\\'))) + "/" + project_name;
                    File project_dir = new File(project_path);
                    project_dir.mkdir();
                    for (int i = 0; i < listModel.size(); i++) if (((String) listModel.get(i)).equals("PIC 16F84 Microcontroller")) {
                        File template = new File(System.getProperty("user.dir") + "/pic16f84 template.asm");
                        File dest = new File(project_path + "/" + project_name + ".asm");
                        CopyFile(template, dest);
                    }
                } catch (IOException e) {
                    System.out.println("Error writing project file");
                }
                this.setVisible(false);
                dispose();
                Workbench.loadProject(project_filename.getPath());
            }
        }
        if (evt.getSource() == open) {
            if (list.getSelectedIndex() != -1) {
                open();
            }
        }
        if (evt.getSource() == newproject) {
            ReadInClasses();
            Step1();
        }
    }
