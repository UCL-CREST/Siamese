    public static void main(String args[]) throws Exception {
        java.io.File lib = new java.io.File("lib");
        File[] fs = lib.listFiles();
        for (int k = 0; k < fs.length; k++) {
            if (!fs[k].getName().toLowerCase().equals("ingeniaseditor.jar")) addFile(fs[k]); else {
                System.err.println(fs[k].getName());
            }
        }
        if (args.length > 0) {
            if (args[0].toLowerCase().equals("-t")) {
                File tfiles = new File("tutorial");
                File[] tutorials = tfiles.listFiles();
                File selected = (File) javax.swing.JOptionPane.showInputDialog(null, "Select one tutorial", "tutorials", javax.swing.JOptionPane.QUESTION_MESSAGE, null, tutorials, tutorials[0]);
                if (selected != null) {
                    Player player = new Player(selected.getPath());
                    player.play();
                } else {
                    System.exit(0);
                }
            } else {
                Class c = Class.forName("ingenias.editor.IDE");
                Method m = c.getMethod("main", new Class[] { String[].class });
                Object[] argl = new Object[args.length];
                System.arraycopy(args, 0, argl, 0, args.length);
                m.invoke(c, new Object[] { new String[0] });
            }
        } else {
            Class c = Class.forName("ingenias.editor.IDE");
            Method m = c.getMethod("main", new Class[] { String[].class });
            Object[] argl = new Object[args.length];
            System.arraycopy(args, 0, argl, 0, args.length);
            m.invoke(c, new Object[] { new String[0] });
        }
    }
