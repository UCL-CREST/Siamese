    public void run() throws Exception {
        if (destDir == null || srcDir == null) {
            throw new Exception("Source and destination directories can not be null.");
        }
        System.out.println("Looking for java files ...");
        String fileNames = getFiles();
        StringBuffer command = new StringBuffer("javadoc -d ");
        command.append(destDir);
        command.append(options);
        command.append(fileNames);
        System.out.println("Command : \n" + command.toString());
        Process p = Runtime.getRuntime().exec(command.toString());
        java.io.BufferedInputStream bis = new java.io.BufferedInputStream(p.getInputStream());
        System.out.println("Process Output : ");
        byte b = 0;
        while ((b = (byte) bis.read()) != -1) {
            System.out.print((char) b);
        }
        int output = p.waitFor();
        System.out.println("Stopped with code : " + output);
    }
