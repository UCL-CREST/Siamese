    public void actionPerformed(ActionEvent aev) {
        Object evsrc = aev.getSource();
        JFileChooser fc;
        if (evsrc == itemExit) {
            System.exit(0);
        } else if (evsrc == itemSplit) {
            fc = new JFileChooser();
            fc.setMultiSelectionEnabled(false);
            if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                JDialog dialog = new SplitDialog(this, (fc.getSelectedFile()).getPath());
                dialog.setVisible(true);
                dialog.dispose();
            }
        } else if (evsrc == itemJoin) {
            fc = new JFileChooser();
            fc.setMultiSelectionEnabled(true);
            if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                JDialog dialog = new JoinDialog(this, fc.getSelectedFiles());
                dialog.setVisible(true);
                dialog.dispose();
            }
        } else if ((evsrc == itemMD5) || (evsrc == itemSHA1)) {
            int k, l;
            fc = new JFileChooser();
            fc.setMultiSelectionEnabled(false);
            if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                ComputeHash task;
                showProgress("Computing hash signature...");
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                task = new ComputeHash(fc.getSelectedFile(), (evsrc == itemMD5) ? ("MD5") : ("SHA"));
                task.addPropertyChangeListener(this);
                task.execute();
            }
        }
    }
