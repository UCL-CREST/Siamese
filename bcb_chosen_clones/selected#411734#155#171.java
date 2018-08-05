    boolean execute_this(String command, PrintWriter out) {
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec(command);
            process.waitFor();
            DataInputStream in = new DataInputStream(process.getInputStream());
            String line = null;
            while ((line = in.readLine()) != null) {
                out.print(line + "||\n");
            }
        } catch (Exception e) {
            out.println("Problem with command");
            return false;
        }
        return true;
    }
