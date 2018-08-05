    private int execute(String cmd, String args, String workdir, boolean sync_flag, Shell shell) {
        System.out.println("Execute: " + cmd + " " + args + "  in wrk dir: " + workdir);
        if ((cmd == null || cmd.equals("")) && (args != null && args != "")) {
            int i = args.lastIndexOf(".");
            if (i == -1) {
                String s_msg = Translator.getString("MESSAGE_CANT_DEFINE_FILE_TYPE") + args;
                if (shell != null) {
                    MessageDialog.openError(shell, Translator.getString("MESSAGEBOX_TITLE_ERROR"), s_msg);
                }
                System.err.println(s_msg);
                return -1;
            }
            String extension = args.substring(i);
            if (extension.endsWith("\"")) {
                extension = extension.substring(0, extension.length() - 1);
            }
            Program p = Program.findProgram(extension);
            if (p == null) {
                String s_msg = Translator.getString("MESSAGE_CANT_DEFINE_FILE_TYPE") + "! " + args;
                if (shell != null) {
                    MessageDialog.openError(shell, Translator.getString("MESSAGEBOX_TITLE_ERROR"), s_msg);
                }
                System.err.println(s_msg);
                return -1;
            }
            p.execute(args);
            return 0;
        }
        if ((cmd != null && !cmd.equals("")) && (args == null || args.equals(""))) {
            File file = new File(cmd);
            if (!file.isAbsolute()) {
                Program.launch(cmd);
                return 0;
            }
        }
        if (cmd != null && !cmd.equals("")) {
            File file = new File(Strings.replace(cmd, "\"", ""));
            if (!file.isAbsolute()) {
                String s = findProgram(cmd);
                if (s == null) {
                    String s_err = Translator.getString("MESSAGE_CANT_FIND_PROGRAM") + "! " + cmd;
                    if (shell != null) {
                        MessageDialog.openError(shell, Translator.getString("MESSAGEBOX_TITLE_ERROR"), s_err);
                    }
                    System.err.println(s_err);
                    return -1;
                } else {
                    cmd = s;
                }
            }
        }
        File wrk_dir = null;
        if (workdir != null && !workdir.equals("")) {
            wrk_dir = new File(workdir);
            if (!wrk_dir.exists()) {
                String s_err = Translator.getString("MESSAGE_FOLDER_NOT_EXIST") + "! " + workdir;
                if (shell != null) {
                    MessageDialog.openError(shell, Translator.getString("MESSAGEBOX_TITLE_ERROR"), s_err);
                }
                System.err.println(s_err);
                wrk_dir = null;
            }
        }
        if (args != null) cmd = cmd + " " + args;
        Process proc = null;
        int ret = -111;
        Runtime rt = Runtime.getRuntime();
        try {
            proc = rt.exec(cmd, null, wrk_dir);
            if (errorGobbler == null) {
                errorGobbler = new StreamOutRedirect(proc.getErrorStream(), StreamOutRedirect.ERR_STREAM, "cp866");
                errorGobbler.start();
            } else {
                if (!errorGobbler.isRunning()) {
                    errorGobbler.interrupt();
                    errorGobbler = new StreamOutRedirect(proc.getErrorStream(), StreamOutRedirect.ERR_STREAM, "cp866");
                    errorGobbler.start();
                }
            }
            if (outGobbler == null) {
                outGobbler = new StreamOutRedirect(proc.getInputStream(), StreamOutRedirect.OUT_STREAM, "cp866");
                outGobbler.start();
            } else {
                if (!outGobbler.isRunning()) {
                    outGobbler.interrupt();
                    outGobbler = new StreamOutRedirect(proc.getInputStream(), StreamOutRedirect.OUT_STREAM, "cp866");
                    outGobbler.start();
                }
            }
            if (sync_flag) {
                ret = proc.waitFor();
            }
            return ret;
        } catch (IOException e) {
            String s_err = e.getMessage();
            if (shell != null) {
                MessageDialog.openError(shell, Translator.getString("MESSAGEBOX_TITLE_ERROR"), s_err);
            }
            System.err.println(s_err);
            e.printStackTrace();
            return -1;
        } catch (InterruptedException e) {
            String s_err = e.getMessage();
            if (shell != null) {
                MessageDialog.openError(shell, Translator.getString("MESSAGEBOX_TITLE_ERROR"), s_err);
            }
            System.err.println(s_err);
            e.printStackTrace();
            return -1;
        } catch (Exception e) {
            String s_err = e.getMessage();
            if (shell != null) {
                MessageDialog.openError(shell, Translator.getString("MESSAGEBOX_TITLE_ERROR"), s_err);
            }
            System.err.println(s_err);
            e.printStackTrace();
            return -1;
        }
    }
