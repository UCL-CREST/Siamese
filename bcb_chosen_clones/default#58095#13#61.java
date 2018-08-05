    public void execute() {
        if (app.get_gui().applet != null) {
            InfoDialog d = new InfoDialog(app.get_gui().get_frame(), "I/O Error", AppText.no_applet_io);
            d.show();
            return;
        }
        FileDialog fd = new FileDialog(app.get_gui().get_frame(), "Load Tree", FileDialog.LOAD);
        fd.show();
        if (fd.getFile() == null) {
            return;
        }
        String file_name = fd.getDirectory() + fd.getFile();
        app.get_gui().get_frame().setCursor(Frame.WAIT_CURSOR);
        try {
            DataInputStream di;
            try {
                di = new DataInputStream(new BufferedInputStream(new FileInputStream(file_name)));
            } catch (java.io.IOException e) {
                app.get_gui().get_frame().setCursor(Frame.DEFAULT_CURSOR);
                InfoDialog d = new InfoDialog(app.get_gui().get_frame(), "I/O Error", "The file " + file_name + "\n" + "does not exist or can't be opened due to security restrictions.");
                d.show();
                return;
            }
            app.make_empty_tree();
            String line;
            while ((line = di.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line);
                if (st.hasMoreTokens()) {
                    char key = st.nextToken().toUpperCase().charAt(0);
                    if (key >= 'A' && key <= 'Z') {
                        String info = "";
                        if (st.hasMoreTokens()) {
                            info = String.valueOf(st.nextToken());
                        }
                        try {
                            app.example_root = app.insert(app.example_root, key, info);
                        } catch (AlreadyThere a) {
                        }
                    }
                }
            }
            di.close();
            app.get_gui().get_frame().setCursor(Frame.DEFAULT_CURSOR);
        } catch (java.io.IOException e) {
            System.out.println("i/o error");
            app.get_gui().get_frame().setCursor(Frame.DEFAULT_CURSOR);
        }
        app.get_gui().main_window.repaint();
    }
