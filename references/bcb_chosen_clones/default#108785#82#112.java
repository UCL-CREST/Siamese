    private void run() throws Exception {
        runTime = Runtime.getRuntime();
        String perlCommand = "perl ".concat(command);
        dlhttpd.logger.info("Script " + path + " run with command: " + perlCommand);
        File workDir = new File(ws.documentRoot.concat(ws.cgiLocation));
        process = runTime.exec(perlCommand, cgiLoader.getEnvArray(env), workDir);
        commandsOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
        commandsErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        (new CGIError(this, commandsErr)).start();
        String line = null;
        boolean isStatus = false;
        do {
            if ((line = commandsOut.readLine()) == null || "".equals(line)) break;
            if (line.indexOf(":") >= 0) {
                String field = line.substring(0, line.indexOf(":")).trim();
                String value = line.substring(line.indexOf(":") + 1).trim();
                if (field.compareToIgnoreCase("Content-type") == 0) if (type == null) type = value; else throw new Exception("Script " + path + " ERROR: double header Content-type"); else if (field.compareToIgnoreCase("Location") == 0) if (location == null) location = value; else throw new Exception("Script " + path + " ERROR: double header Location"); else if (field.compareToIgnoreCase("Status") == 0) {
                    if (!isStatus) try {
                        status = Integer.parseInt(value);
                        isStatus = true;
                    } catch (NumberFormatException e) {
                        throw new Exception("Script " + path + " ERROR: parse header Status: " + e.getMessage());
                    } else throw new Exception("Script " + path + " ERROR: double header Status");
                }
            }
        } while (true);
        if (type == null || "".equals(type)) if (location == null || "".equals(location)) throw new Exception("Script " + path + "  ERROR: bad headers Content-type and Location"); else {
            isClose = true;
            close();
        }
    }
