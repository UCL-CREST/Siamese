    private void _start(String command) throws ProtocolError {
        try {
            process = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            throw new ProtocolError("Impossible to run the program: " + command + ". " + e.getMessage());
        }
        if (console == null) {
            console = new StandardIOConsole();
        }
        in_reader = new ProcessReader(console.getIn(), process.getOutputStream(), true);
        err_reader = new ProcessReader(process.getErrorStream(), console.getErr(), false);
        out_reader = new ProcessReader(process.getInputStream(), console.getOut(), false);
    }
