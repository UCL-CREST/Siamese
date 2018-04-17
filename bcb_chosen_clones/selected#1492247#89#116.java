    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            Installer installer = new Installer();
            String panelsStr = System.getProperty("panels");
            List<InstallPanel> panels = new ArrayList<InstallPanel>();
            installer.panels = panels;
            if (panelsStr != null) {
                StringTokenizer tk = new StringTokenizer(panelsStr, ", \t\n\r");
                while (tk.hasMoreTokens()) {
                    String clazzStr = tk.nextToken();
                    Class clazz = Class.forName(clazzStr);
                    Constructor constr = clazz.getConstructor(Installer.class);
                    InstallPanel p = (InstallPanel) constr.newInstance(installer);
                    panels.add(p);
                }
            } else {
                System.err.println("No panels loaded");
                System.exit(0);
            }
            InstallerFrame frame = new InstallerFrame(installer, panels);
            frame.setSize(1000, 700);
            frame.setLocation(10, 10);
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
