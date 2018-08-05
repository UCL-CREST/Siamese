    public void runCommand() {
        input = null;
        error = null;
        try {
            Runtime a = Runtime.getRuntime();
            java.lang.Process p = a.exec(command);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            System.out.println("Here is the standard output of the command:\n");
            while ((command = stdInput.readLine()) != null) {
                input.append(command);
            }
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((command = stdError.readLine()) != null) {
                error.append(command);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
