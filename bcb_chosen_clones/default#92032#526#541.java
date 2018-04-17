    public void open() {
        String in_list = (String) list.getSelectedValue();
        if (in_list.equals("[more files...]")) {
            JFileChooser projectloader = new JFileChooser();
            if (projectloader.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File selected = projectloader.getSelectedFile();
                Workbench.loadProject(selected.getPath());
                this.setVisible(false);
                dispose();
            }
        } else {
            Workbench.loadProject(picdev_projectdir + "/" + (String) list.getSelectedValue() + ".pds");
            this.setVisible(false);
            dispose();
        }
    }
