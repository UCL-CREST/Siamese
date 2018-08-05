    static void runCmd(String cmdLine) throws java.lang.Exception, java.io.IOException {
        String[] cmd = new String[2];
        if (argXEcl.nt) {
            cmd[0] = "cmd";
            cmd[1] = "/c";
        } else {
            cmd[0] = "/bin/sh";
            cmd[1] = "-c";
        }
        String[] command = { cmd[0], cmd[1], cmdLine };
        Runtime rt = Runtime.getRuntime();
        Process p = rt.exec(command);
        PrintWriter outf;
        outf = new PrintWriter(System.out);
        InputStream procStdout = p.getInputStream();
        InputStream procStderr = p.getErrorStream();
        int exit = 0, n1, n2;
        boolean processEnded = false;
        if (flg_newCompilation) {
            pnlOut_txtArea.setText("");
            flg_newCompilation = false;
        }
        if (flg_OutputPrompt) System.out.println(cmdLine);
        if (!mnuHelp_flgEclHelp && !mnuHelp_flgEclVersion && !mnuHelp_flgEsHelp && !mnuHelp_flgEsVersion) pnlOut_txtArea.append(cmdLine + "\n");
        while (!processEnded) {
            xecl.f.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            pnlEclCmd_txtArg.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            pnlOut_txtArea.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            try {
                exit = p.exitValue();
                processEnded = true;
                xecl.f.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                pnlEclCmd_txtArg.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                pnlOut_txtArea.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            } catch (IllegalThreadStateException e) {
            }
            n1 = procStdout.available();
            if (n1 > 0) {
                for (int i = 0; i < n1; i++) {
                    int b = procStdout.read();
                    if (b < 0) break;
                    outf.write(b);
                    if (mnuHelp_flgEclVersion) {
                        if (b == 10 || b == 13) EclCmpVersion += "\n"; else EclCmpVersion += (char) b;
                    } else if (mnuHelp_flgEsVersion) {
                        EsCmpVersion += (char) b;
                    }
                    if (!mnuHelp_flgEclVersion && !mnuHelp_flgEsHelp && !mnuHelp_flgEsVersion && !mnuHelp_flgEclHelp) {
                        pnlOut_txtArea.setText(pnlOut_txtArea.getText() + "" + (char) b);
                        if (flg_OutputPrompt) {
                            System.err.print((char) b);
                            System.err.flush();
                        }
                    }
                }
            }
            n2 = procStderr.available();
            if (n2 > 0) {
                byte[] pbytes = new byte[n2];
                procStderr.read(pbytes);
                if (mnuHelp_flgEclHelp) EclHelp += new String(pbytes); else if (mnuHelp_flgEsVersion) EsCmpVersion += new String(pbytes); else if (mnuHelp_flgEsHelp) EsHelp += new String(pbytes);
                if (!mnuHelp_flgEclVersion && !mnuHelp_flgEsHelp && !mnuHelp_flgEsVersion && !mnuHelp_flgEclHelp) {
                    pnlOut_txtArea.append(new String(pbytes));
                    if (flg_OutputPrompt) {
                        System.err.print(new String(pbytes));
                        System.err.flush();
                    }
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        }
        do {
            n1 = procStdout.available();
            if (n1 > 0) {
                for (int i = 0; i < n1; i++) {
                    int b = procStdout.read();
                    if (b < 0) break;
                    outf.write(b);
                    pnlOut_txtArea.append("" + (char) b);
                    if (flg_OutputPrompt) {
                        System.err.print((char) b);
                        System.err.flush();
                    }
                }
            }
            n2 = procStderr.available();
            if (n2 > 0) {
                byte[] pbytes = new byte[n2];
                procStderr.read(pbytes);
                pnlOut_txtArea.append(new String(pbytes));
                if (flg_OutputPrompt) {
                    System.err.print(new String(pbytes));
                    System.err.flush();
                }
            }
        } while (n1 > 0 || n2 > 0);
        if (exit != 0) {
            if (!mnuHelp_flgEclVersion) {
                flg_errCompilation = true;
                pnlOut_txtArea.append("Error " + exit + " during subprocess execution ");
                if (flg_OutputPrompt) System.err.println("Error " + exit + " during subprocess execution");
            }
        }
    }
