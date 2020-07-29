    public static void main(String args[]) {
        String s = null;
        try {
            String[] cmd = new String[9];
            cmd[0] = "java";
            cmd[1] = "-Dant.home=C:\\julien\\prgm\\dev\\ant\\apache-ant-1.7.0";
            cmd[2] = "-cp";
            cmd[3] = "\"C:\\julien\\creation\\work\\eclipseWorkspace\\InterfaceA_Sematech_GUI\\war\\WEB-INF\\lib\\ant.jar\"";
            cmd[4] = "org.apache.tools.ant.Main";
            cmd[5] = "-buildfile";
            cmd[6] = "C:\\julien\\creation\\work\\eclipseWorkspace\\InterfaceA_Sematech_SOAP\\build.xml";
            cmd[7] = "clean";
            cmd[8] = "run.client";
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
        } catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }
    }
