    public static void main(String[] args) {
        if (args.length < 2) {
            usage();
            System.exit(0);
        }
        VFSHook hook = new VFSHook();
        String cmd = args[0].toLowerCase();
        if (cmd.equals("-m")) {
            hook.monitor(args[1]);
        } else if (cmd.equals("-p")) {
            JFileChooser chooser = new JFileChooser();
            int returnVal = chooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                f.renameTo(new File(args[1] + "/" + f.getName()));
            }
            System.exit(0);
        } else {
            usage();
        }
    }
