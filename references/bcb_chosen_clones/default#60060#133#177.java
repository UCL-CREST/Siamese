        public void run() {
            if (cmdline == null) return;
            BufferedReader inp = null;
            String text = new String(), line;
            cmdfield.setEnabled(false);
            list.setEnabled(false);
            try {
                proc = Runtime.getRuntime().exec(cmdline);
                inp = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                synchronized (this) {
                    started = true;
                    notifyAll();
                }
                line = inp.readLine();
                while (line != null) {
                    text += line + "\n";
                    setText(text);
                    setCaretPosition(text.length());
                    line = inp.readLine();
                }
            } catch (IOException ioe) {
                text += ioe.getMessage() + "\n";
                synchronized (this) {
                    started = true;
                    notifyAll();
                }
                proc = null;
            }
            if (proc != null) {
                try {
                    int code = proc.waitFor();
                    if (killed) status.setText(status.getText() + " (code " + code + ")"); else status.setText("Exited with code: " + code);
                } catch (InterruptedException ie) {
                }
                sm.fetchProcessStatus(hProcess, ps);
                times.setText(ps.getTimes());
                memory.setText(ps.getMemory());
                proc = null;
            }
            setText(text);
            setCaretPosition(text.length());
            exe = null;
            cmdfield.setEnabled(true);
            list.setEnabled(true);
        }
