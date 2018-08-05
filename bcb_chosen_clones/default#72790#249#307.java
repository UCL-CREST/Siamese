    public void ProcessAndOutputFile(File path, String httpMethod, String httpURIVars, InetAddress remoteIP) {
        File fileToRead = path;
        DataInputStream fileIn = null;
        String actionHandler = Misc.getActionHandler(fileToRead.getPath());
        try {
            if (actionHandler != null) {
                Misc.putSysMessage(0, "Preprocessing...");
                String command = "";
                if (actionHandler.indexOf(' ') != -1) command += "\"" + actionHandler + "\""; else command += actionHandler;
                command += " " + fileToRead.getPath().replace('\\', '/');
                Misc.putSysMessage(0, "Executing: " + command);
                Process Handler = Runtime.getRuntime().exec(command);
                if (Handler != null) fileIn = new DataInputStream(Handler.getInputStream());
                this.outputStatusLine(200);
                this.outputStdHeaders();
                this.outputConnectionHeader();
                this.outputFlush();
            } else {
                fileIn = new DataInputStream(new BufferedInputStream(new FileInputStream(fileToRead)));
                this.outputStatusLine(200);
                this.outputStdHeaders();
                SimpleDateFormat temp = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
                temp.setTimeZone(new SimpleTimeZone(0, "GMT"));
                String dateStr = temp.format(new Date(fileToRead.lastModified()));
                this.outputHeader("Last-Modified: " + dateStr);
                this.outputHeader("Content-type: " + Misc.getContentType(fileToRead.getPath()));
                this.outputHeader("Content-length: " + fileToRead.length());
                this.outputConnectionHeader();
                this.outputHeader("");
                this.outputFlush();
            }
            if (this.sendBody == true) {
                if (actionHandler == null) {
                    byte[] buffer = new byte[(int) fileToRead.length()];
                    fileIn.readFully(buffer);
                    this.out.write(buffer);
                    this.outputFlush();
                } else {
                    byte[] buffer = new byte[2];
                    int status = 0;
                    while (status != -1) {
                        status = fileIn.read(buffer);
                        this.out.write(buffer);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            Misc.putSysMessage(1, "FileRead: " + e);
        } catch (IOException e) {
            Misc.putSysMessage(1, "FileRead: " + e);
        } finally {
            try {
                if (fileIn != null) fileIn.close();
            } catch (IOException e) {
                Misc.putSysMessage(1, "FileRead: " + e);
            }
            this.outputFlush();
        }
    }
