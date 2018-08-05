    synchronized void loadCart() {
        JFileChooser fd;
        cart.saveBattery(PgbSettings.savepath, curfile);
        fd = new JFileChooser(curpath);
        fd.setDialogTitle("Load ROM ...");
        fd.setFileFilter(cart);
        if (fd.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) return;
        curfile = fd.getSelectedFile().getName();
        curpath = fd.getCurrentDirectory().getAbsolutePath();
        if (cart.load(curpath, curfile)) {
            setSystem(PgbSettings.desiredsystem);
            PgbSettings.gamestring = cart.getName();
            net.sendInfo();
            reset();
            unpause();
        }
    }
