    private void OpenSchedule() {
        if (!ClearSchedule()) {
            Exit();
            return;
        }
        JFileChooser chooser = new JFileChooser(parentG);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        cxExtFileFilter filter = new cxExtFileFilter("sch", "SCHEDULE representation files (*.sch)");
        chooser.setFileFilter(filter);
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            Exit();
            return;
        }
        chooser.setVisible(false);
        repaint();
        setTitle("gxTool Schedule - [" + chooser.getSelectedFile().getPath() + "]");
        waitFrame.showWait();
        InitGraph(chooser.getSelectedFile().getPath());
        waitFrame.hideWait();
        undoManager.discardAllEdits();
        miSave.setEnabled(true);
        miSaveAs.setEnabled(true);
        miViewSource.setEnabled(true);
        miZoomOut.setEnabled(true);
        miZoomIn.setEnabled(true);
        miZoomAct.setEnabled(true);
        miLayout.setEnabled(true);
        bLayout.setEnabled(true);
        bEdges.setEnabled(true);
        bSave.setEnabled(true);
        bViewSource.setEnabled(true);
        bZoomOut.setEnabled(true);
        bZoomAct.setEnabled(true);
        bZoomIn.setEnabled(true);
    }
