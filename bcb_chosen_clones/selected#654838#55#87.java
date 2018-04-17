    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Open File")) {
            Desktop desktop = null;
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
                try {
                    File destfile = null;
                    destfile = Globals.getTransferModel().getDestFile(index);
                    desktop.open(destfile);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else if (command.equals("Open Directory")) {
            Desktop desktop = null;
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
                try {
                    File destfile = null;
                    destfile = Globals.getTransferModel().getDestFile(index).getParentFile();
                    desktop.open(destfile);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else if (command.equals("Remove transfer")) {
            Globals.getTransferModel().removeTransfer(index);
        } else if (command.equals("Remove all inactive transfers")) {
            Globals.getTransferModel().removeAllEndedTransfers();
        }
    }
