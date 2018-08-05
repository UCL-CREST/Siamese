    public void createShellScript(String filename, int fileType, java.lang.String workDir, String temporaryDirectory) {
        String theCommand = " ";
        if (fileType == 0) {
            theCommand = "unrar e -y " + filename;
            System.out.println(theCommand);
        }
        PrintWriter shellScript = null;
        try {
            shellScript = new PrintWriter(new FileOutputStream(System.getProperty("user.home") + File.separatorChar + tempDirName + File.separatorChar + "tempscript.sh"));
            shellScript.println(theCommand);
            shellScript.close();
        } catch (FileNotFoundException exception) {
            System.err.println("Problem opening the shell script");
        }
        Runtime theRuntime = Runtime.getRuntime();
        Process p;
        try {
            p = theRuntime.exec("sh tempscript.sh", null, new File(System.getProperty("user.home") + File.separatorChar + tempDirName));
            InputStream stdin = p.getInputStream();
            InputStreamReader isr = new InputStreamReader(stdin);
            BufferedReader br = new BufferedReader(isr);
            String lineIn = null;
            while ((lineIn = br.readLine()) != null) {
                System.out.println(lineIn);
            }
            p.waitFor();
        } catch (IOException exception) {
            System.err.println("problem executing script");
        } catch (InterruptedException exception) {
            System.err.println("problem executing shell script");
        }
        try {
            p = theRuntime.exec("rm tempscript.sh", null, new File(System.getProperty("user.home") + File.separatorChar + tempDirName));
            p.waitFor();
        } catch (IOException exception) {
            System.err.println("problem removing script");
        } catch (InterruptedException exception) {
            System.err.println("problem removing the shell script");
        }
        theApp.fileStuff.setTempDirFlag(true);
    }
