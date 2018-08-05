    public String execute(String command, String path) throws RemoteException, Exception {
        String[] cmd = command.split(" ");
        if (cmd.length == 0) throw new RemoteException("No command given");
        String[] env = new String[] {};
        File workDir = null;
        if (path == null || new File(path).exists() == false) workDir = new File(AgentApi.getAPI().getDirectory()); else workDir = new File(path);
        Process p = Runtime.getRuntime().exec(cmd, env, workDir);
        StreamGobbler stdout = new StreamGobbler(p.getInputStream());
        StreamGobbler stderr = new StreamGobbler(p.getErrorStream());
        stdout.start();
        stderr.start();
        int exitVal = p.waitFor();
        stdout.join(2000);
        stderr.join(2000);
        String result = stdout.sb.toString();
        if (exitVal != 0) {
            result += "\n\nEXITCODE: " + exitVal + "\n";
        }
        if (stderr.sb.length() > 0) {
            result += "\n\nSTDERR:\n" + stderr.sb.toString();
        }
        return result.toString();
    }
