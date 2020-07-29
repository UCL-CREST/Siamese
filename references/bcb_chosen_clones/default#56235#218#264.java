    public void run() {
        if (System.getProperty("os.name").startsWith("Mac")) {
            Globals g = new Globals();
            Preferences prefs_static = Preferences.userNodeForPackage(g.getClass());
            Globals.quaquaActive = prefs_static.get("QUAQUA", "true").equals("true");
            Globals.weAreOnAMac = true;
            System.setProperty("com.apple.macos.useScreenMenuBar", "true");
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "FidoCadJ");
            try {
                if (Globals.quaquaActive) {
                    UIManager.setLookAndFeel("ch.randelshofer.quaqua.QuaquaLookAndFeel");
                    System.out.println("Quaqua look and feel active");
                }
            } catch (Exception e) {
                System.out.println("The Quaqua look and feel is not available");
                System.out.println("I will continue with the basic Apple l&f");
            }
        } else if (System.getProperty("os.name").startsWith("Win")) {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (Exception E) {
            }
            Globals.quaquaActive = false;
        } else {
            Globals.quaquaActive = false;
        }
        if (Globals.weAreOnAMac) {
            try {
                Class a = Class.forName("AppleSpecific");
                Object b = a.newInstance();
                Method m = a.getMethod("answerFinder", null);
                m.invoke(b, null);
            } catch (Exception exc) {
                Globals.weAreOnAMac = false;
                System.out.println("It seems that this software has been " + "compiled on a system different from MacOSX. Some nice " + "integrations with MacOSX will therefore be absent. If " + "you have compiled on MacOSX, make sure you used the " + "'compile' or 'rebuild' script along with the 'mac' " + "option.");
            }
        }
        FidoFrame popFrame = new FidoFrame(true);
        if (!libDirectory.equals("")) {
            popFrame.libDirectory = libDirectory;
        }
        popFrame.init();
        popFrame.loadLibraries();
        if (!loadFile.equals("")) popFrame.load(loadFile);
        popFrame.setVisible(true);
    }
