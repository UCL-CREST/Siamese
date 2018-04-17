    public void runCommand(String s) {
        input.setLength(0);
        error.setLength(0);
        try {
            command = s;
            Runtime a = Runtime.getRuntime();
            java.lang.Process p = a.exec(command);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((command = stdInput.readLine()) != null) {
                System.out.println(command);
                input.append(command + "\n");
            }
            while ((command = stdError.readLine()) != null) {
                System.out.println(command);
                error.append(command + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
