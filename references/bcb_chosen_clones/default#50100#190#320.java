    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == pnl1_btnCellname) {
            pnl1_txtCellname.setForeground(Color.black);
            xecl.chooser.setDialogTitle("Select the Cell's name");
            String path = "";
            if (!pnl1_txtWorkspace.getText().equals("")) {
                path += pnl1_txtWorkspace.getText();
                if (!pnl1_txtLibname.getText().equals("")) path += xecl.dirsep + pnl1_txtLibname.getText();
            }
            if (path.equals("")) xecl.chooser.setCurrentDirectory(new File("C:\\MyWorkspace")); else xecl.chooser.setCurrentDirectory(new File(path));
            xecl.chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int ret = xecl.chooser.showOpenDialog(xecl.f);
            if (xecl.chooser.getSelectedFile() != null && ret == 0) {
                File Cell, Lib, Workspace;
                Cell = xecl.chooser.getSelectedFile();
                Lib = xecl.chooser.getCurrentDirectory();
                Workspace = xecl.chooser.getCurrentDirectory();
                pnl1_txtCellname.setText(Cell.getName());
                pnl1_txtLibname.setText(Lib.getName());
                pnl1_txtWorkspace.setText(Workspace.getParent());
                xecl.tabOptionEcl[Const.VCCCELL] = true;
                xecl.tabOptionEcl[Const.VCCLIB] = true;
                xecl.tabOptionEcl[Const.WORKSPACE] = true;
            }
        }
        if (event.getSource() == pnl1_btnLibname) {
            pnl1_txtLibname.setForeground(Color.black);
            String path = "";
            if (!pnl1_txtWorkspace.getText().equals("")) path += pnl1_txtWorkspace.getText();
            if (path.equals("")) xecl.chooser.setCurrentDirectory(new File("C:\\MyWorkspace")); else xecl.chooser.setCurrentDirectory(new File(path));
            xecl.chooser.setDialogTitle("Select the Lib's name");
            xecl.chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int ret = xecl.chooser.showOpenDialog(xecl.f);
            if (xecl.chooser.getSelectedFile() != null && ret == 0) {
                File Lib, Workspace;
                Lib = xecl.chooser.getSelectedFile();
                Workspace = xecl.chooser.getCurrentDirectory();
                pnl1_txtLibname.setText(Lib.getName());
                pnl1_txtWorkspace.setText(Workspace.getPath());
                xecl.tabOptionEcl[Const.VCCLIB] = true;
                xecl.tabOptionEcl[Const.WORKSPACE] = true;
            }
        }
        if (event.getSource() == pnl1_btnWorkspace) {
            pnl1_txtWorkspace.setForeground(Color.black);
            if (xecl.Vcc_strWorkspace == null) xecl.chooser.setCurrentDirectory(new File("C:\\MyWorkspace")); else xecl.chooser.setCurrentDirectory(new File(xecl.Vcc_strWorkspace));
            xecl.chooser.setDialogTitle("Select the workspace's name");
            xecl.chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int ret = xecl.chooser.showOpenDialog(xecl.f);
            if (xecl.chooser.getSelectedFile() != null && ret == 0) {
                File Workspace;
                Workspace = xecl.chooser.getSelectedFile();
                pnl1_txtWorkspace.setText(Workspace.toString());
                xecl.tabOptionEcl[Const.WORKSPACE] = true;
            }
        }
        if (event.getSource() == pnl3_btnMainmodule) {
            pnl3_txtMainmodule.setForeground(Color.black);
            String path = "";
            if (!pnl1_txtWorkspace.getText().equals("")) {
                path += pnl1_txtWorkspace.getText();
                if (!pnl1_txtLibname.getText().equals("")) {
                    path += xecl.dirsep + pnl1_txtLibname.getText();
                    if (!pnl1_txtCellname.getText().equals("")) path += xecl.dirsep + pnl1_txtCellname.getText();
                }
            }
            if (path == null) xecl.chooser.setCurrentDirectory(new File("C:\\MyWorkspace")); else xecl.chooser.setCurrentDirectory(new File(path));
            xecl.chooser.setDialogTitle("Select the Mainmodule's name");
            xecl.chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int ret = xecl.chooser.showOpenDialog(xecl.f);
            if (xecl.chooser.getSelectedFile() != null && ret == 0) {
                pnl3_txtMainmodule.setText(xecl.chooser.getSelectedFile().getName());
                xecl.Vcc_strMainmodule = pnl3_txtMainmodule.getText();
                xecl.tabOptionEcl[Const.MAINMODULE] = true;
            }
        }
        if (event.getSource() == pnl1_btnDfttypelib) {
            pnl1_txtDfttypelib.setForeground(Color.black);
            String path = "";
            if (!pnl1_txtWorkspace.getText().equals("")) {
                path += pnl1_txtWorkspace.getText();
                if (!pnl1_txtLibname.getText().equals("")) {
                    path += xecl.dirsep + pnl1_txtLibname.getText();
                    if (!pnl1_txtCellname.getText().equals("")) path += xecl.dirsep + pnl1_txtCellname.getText();
                }
            }
            if (path == null) xecl.chooser.setCurrentDirectory(new File("C:\\MyWorkspace")); else xecl.chooser.setCurrentDirectory(new File(path));
            xecl.chooser.setDialogTitle("Select the user-defineds for all types");
            xecl.chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int ret = xecl.chooser.showOpenDialog(xecl.f);
            if (xecl.chooser.getSelectedFile() != null && ret == 0) {
                pnl1_txtDfttypelib.setText(xecl.chooser.getSelectedFile().getName());
                xecl.Vcc_strDfttypelib = pnl1_txtDfttypelib.getText();
                xecl.tabOptionEcl[Const.DEFAULTTYPELIB] = true;
            }
        }
        if (event.getSource() == pnl1_btnTypelib) {
            this.setModal(false);
            xecl_I_D_OptDlg dlgTypelib = new xecl_I_D_OptDlg(xecl.f, Const.TYPELIB);
            dlgTypelib.setModal(true);
            dlgTypelib.setVisible(true);
            this.setModal(true);
        }
        if (event.getSource() == pnl1_btnTypelibfile) {
            pnl1_txtTypelibfile.setForeground(Color.black);
            pnl1_txtTypelibfile.setForeground(Color.black);
            String path = "";
            if (!pnl1_txtWorkspace.getText().equals("")) {
                path += pnl1_txtWorkspace.getText();
                if (!pnl1_txtLibname.getText().equals("")) {
                    path += xecl.dirsep + pnl1_txtLibname.getText();
                    if (!pnl1_txtCellname.getText().equals("")) path += xecl.dirsep + pnl1_txtCellname.getText();
                }
            }
            if (path == null) xecl.chooser.setCurrentDirectory(new File("C:\\MyWorkspace")); else xecl.chooser.setCurrentDirectory(new File(path));
            xecl.chooser.setDialogTitle("Select the user-defined type");
            xecl.chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int ret = xecl.chooser.showOpenDialog(xecl.f);
            if (xecl.chooser.getSelectedFile() != null && ret == 0) {
                pnl1_txtTypelibfile.setText(xecl.chooser.getSelectedFile().getName());
                xecl.tabOptionEcl[Const.TYPELIBFILE] = true;
                xecl.Vcc_strTypelibfile = pnl1_txtTypelibfile.getText();
            }
        }
        if (event.getSource() == pnl4_btnCANCEL) {
            ExitCancel();
        }
        if (event.getSource() == pnl4_btnOK) {
            ExitOk();
        }
    }
