    private void startProcess() {
        try {
            System.err.println("Starting process:  " + processCommand);
            process = Runtime.getRuntime().exec(processCommand);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        in = new PrintWriter(process.getOutputStream(), true);
        out = new BufferedReader(new InputStreamReader(process.getInputStream()));
        err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
    }
