    private void build() {
        if (mpasm_path == null) {
            Object options[] = { "   Yes   ", "    No    ", "  Help  " };
            int result = JOptionPane.showOptionDialog(Workbench.mainframe, "PIC Development Stuido does not have an inbuilt compiler so\nyou have to download one (i.e. MPASMWIN.EXE). Click on help for more info.\nDo you want to set the path to the compiler now?", "Path to compiler executable is not specified", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (result == 0) {
                JFileChooser lstchooser = new JFileChooser();
                lstchooser.setDialogTitle("Set path to compilator executable");
                if (lstchooser.showOpenDialog(Workbench.mainframe) == JFileChooser.APPROVE_OPTION) {
                    File file = lstchooser.getSelectedFile();
                    mpasm_path = file.getPath();
                    try {
                        RandomAccessFile piccfg = new RandomAccessFile("pic.cfg", "rw");
                        piccfg.writeBytes(mpasm_path);
                        piccfg.close();
                    } catch (java.io.IOException ioe) {
                    }
                }
            }
        }
        if (mpasm_path != null) {
            progwindow.save_source(source_filename);
            try {
                Thread.sleep(100);
                Runtime rt = Runtime.getRuntime();
                String runparams[] = { mpasm_path, "-p16F84", "\"" + source_filename + "\"" };
                try {
                    rt.exec(runparams);
                    Thread.sleep(100);
                } catch (java.io.IOException ioe) {
                    System.out.println("Problem with compilation");
                }
            } catch (java.lang.InterruptedException ie) {
            }
            reset();
        }
    }
