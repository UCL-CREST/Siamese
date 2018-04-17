    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == pnlExit_btnOk) {
            ExitOk();
        }
        if (event.getSource() == pnlExit_btnCancel) {
            ExitCancel();
        }
        if (event.getSource() == pnlBtn_btnAdd) {
            if (mode == Const.INCLUDE) {
                addInclude();
            }
            if (mode == Const.MACRO || mode == Const.TYPELIB) {
                addElement();
            }
        }
        if (event.getSource() == pnlBtn_btnRemove) {
            int nb = 0;
            for (int i = 0; i < nbIncl; i++) if (pnlList_lst.isSelectedIndex(i)) nb++;
            if (nb > 0) {
                removeSelected(pnlList_lst.getSelectedIndices(), nb);
                pnlBtn_btnRemove.setEnabled(false);
                pnlOpt_txt1.requestFocus();
            }
        }
        if (event.getSource() == pnlBtn_btnRemoveAll) {
            if (mode == Const.INCLUDE) {
                include.removeAllElements();
                pnlList_lst.setListData(include);
                nbIncl = 0;
                pnlBtn_btnRemove.setEnabled(false);
            }
            if (mode == Const.MACRO || mode == Const.TYPELIB) {
                if (mode == Const.MACRO) {
                    macro.removeAllElements();
                    value.removeAllElements();
                }
                if (mode == Const.TYPELIB) {
                    lib.removeAllElements();
                    type.removeAllElements();
                }
                nbIncl = 0;
                pnlList_lst.setListData(macro);
                pnlBtn_btnRemove.setEnabled(false);
            }
            pnlBtn_btnRemoveAll.setEnabled(false);
            pnlOpt_txt1.requestFocus();
        }
        if (event.getSource() == pnlOpt_btnI) {
            xecl.chooser.setDialogTitle("Select the directory to include");
            xecl.chooser.setCurrentDirectory(new File("."));
            xecl.chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            xecl.chooser.setFileFilter(xecl.hFilter);
            int ret = xecl.chooser.showOpenDialog(xecl.f);
            if (xecl.chooser.getSelectedFile() != null && ret == 0) {
                pnlOpt_txt1.setText(xecl.chooser.getSelectedFile().toString());
                if (mode == Const.INCLUDE) {
                    addInclude();
                }
                if (mode == Const.TYPELIB) {
                    pnlOpt_txtValue.requestFocus();
                }
            }
        }
    }
