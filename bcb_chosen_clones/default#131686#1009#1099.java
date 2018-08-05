    public static String executeProgram(String programName) {
        Runtime rt = Runtime.getRuntime();
        long l1 = System.currentTimeMillis();
        Process p = null;
        int retStatus = 0;
        long millis = 0;
        long secs = 0;
        long min = 0;
        long hours = 0;
        LinkedList output = new LinkedList();
        LinkedList error = new LinkedList();
        String ls = System.getProperty("line.separator");
        try {
            p = rt.exec(programName);
        } catch (IOException e) {
            return "Caught IOException while running " + programName + ls + " Message = " + e.getMessage() + ls;
        }
        InputStream is = p.getInputStream();
        BufferedReader bf = new BufferedReader(new InputStreamReader(is));
        String line = null;
        try {
            while ((line = bf.readLine()) != null) {
                output.addLast(line);
            }
        } catch (IOException e) {
            return "Caught IOException. " + ls + "Message = " + e.getMessage() + ls;
        }
        InputStream es = p.getErrorStream();
        BufferedReader ebf = new BufferedReader(new InputStreamReader(es));
        try {
            while ((line = ebf.readLine()) != null) {
                error.addLast(line);
            }
        } catch (IOException e) {
            return "Caught IOException. " + ls + "Message = " + e.getMessage() + ls;
        }
        try {
            retStatus = p.waitFor();
        } catch (InterruptedException e) {
        }
        StringBuffer message = new StringBuffer();
        if (output.size() != 0) {
            message.append(ls + "OUTPUT:" + ls);
            for (Iterator iter = output.iterator(); iter.hasNext(); ) {
                message.append((String) iter.next() + ls);
            }
        }
        if (error.size() != 0) {
            message.append("ERRORS:" + ls);
            for (Iterator iter = error.iterator(); iter.hasNext(); ) {
                message.append((String) iter.next() + ls);
            }
        }
        try {
            is.close();
            bf.close();
            es.close();
            ebf.close();
        } catch (IOException e) {
            return "Caught IOException while closing resources...." + ls + "Message: " + e.getMessage() + ls;
        }
        long l2 = System.currentTimeMillis();
        millis = l2 - l1;
        hours = millis / 3600000;
        millis = millis % 3600000;
        min = millis / 60000;
        millis = millis % 60000;
        secs = millis / 1000;
        millis = millis % 1000;
        message.append(ls + "TIME TO EXECUTE: ");
        if (hours < 10) {
            message.append("0");
        }
        message.append(hours + ":");
        if (min < 10) {
            message.append("0");
        }
        message.append(min + ":");
        if (secs < 10) {
            message.append("0");
        }
        message.append(secs + ":");
        if (millis < 10) {
            message.append("0");
        }
        if (millis < 100) {
            message.append("0");
        }
        message.append(millis + "(HH:MM:SS:MS)" + ls);
        return message.toString();
    }
