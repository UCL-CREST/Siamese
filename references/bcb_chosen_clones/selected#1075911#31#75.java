    protected Void doInBackground() throws Exception {
        try {
            java.util.List<String> cmd = buildCommands();
            StringBuilder cmdStr = new StringBuilder("Executing:\n   ");
            for (String str : cmd) {
                cmdStr.append(" ").append(str);
            }
            cmdStr.append("\n\n");
            gui.console.setText(cmdStr.toString());
            gui.cancel.setEnabled(true);
            gui.deploy.setEnabled(false);
            gui.console.setCaretPosition(gui.console.getDocument().getLength());
            ProcessBuilder builder = new ProcessBuilder(cmd).redirectErrorStream(true);
            builder.environment().putAll(System.getenv());
            deploy = builder.start();
            reader = new Thread() {

                @Override
                public void run() {
                    try {
                        BufferedReader in = new BufferedReader(new InputStreamReader(deploy.getInputStream()));
                        try {
                            String line;
                            while ((line = in.readLine()) != null && reader != null && deploy != null && !isCancelled()) {
                                gui.console.getDocument().insertString(gui.console.getDocument().getLength(), line + "\n", null);
                                gui.console.setCaretPosition(gui.console.getDocument().getLength());
                            }
                        } finally {
                            in.close();
                        }
                    } catch (Exception ignored) {
                    }
                }
            };
            reader.start();
            deploy.waitFor();
        } catch (InterruptedException ignored) {
        } catch (Exception e) {
            gui.console.setText(ExceptionUtils.asText(e));
        } finally {
            gui.deploy.setEnabled(true);
            gui.cancel.setEnabled(false);
        }
        return null;
    }
