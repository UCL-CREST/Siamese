    public void run() {
        try {
            if (this.finished == false) {
                Runtime runner = Runtime.getRuntime();
                process = runner.exec(command);
                new ProcessInputReaderThread(process.getInputStream(), out);
                new ProcessInputReaderThread(process.getErrorStream(), err);
                exitCode = new Integer(process.waitFor());
            }
        } catch (Exception e) {
            err.append("Error running command.\n");
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            PrintWriter errData = new PrintWriter(ba);
            e.printStackTrace(errData);
            errData.flush();
            err.append(ba.toString());
            e.printStackTrace();
        }
        this.finished = true;
    }
