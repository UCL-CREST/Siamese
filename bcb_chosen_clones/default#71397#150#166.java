    public synchronized void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        try {
            if (cmd.equals("load")) {
                int returnVal = dialog.showOpenDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    loadData(dialog.getSelectedFile().getAbsolutePath());
                }
            } else if (cmd.equals("reset")) {
                loadData(null);
            }
        } catch (VisADException exc) {
            exc.printStackTrace();
        } catch (RemoteException exc) {
            exc.printStackTrace();
        }
    }
