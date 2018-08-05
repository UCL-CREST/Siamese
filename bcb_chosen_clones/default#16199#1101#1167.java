    public static String executeBulkLoad(String command, String logLines, String timeout) {
        String ls = System.getProperty("line.separator");
        long ptimeout = 0;
        int numLines = 0;
        try {
            ptimeout = new Long(timeout).longValue();
            numLines = new Integer(logLines).intValue();
        } catch (NumberFormatException e) {
            return "The second and third parameters( number of lines and timeout ) need to be numbers. " + ls + e.getMessage();
        }
        if (ptimeout > 0) return executeBulkLoadWithTimeout(command, logLines, ptimeout);
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
        try {
            p = rt.exec(command);
        } catch (IOException e) {
            return "Caught IOException while executing the command. Message = " + ls + e.getMessage() + ls;
        }
        InputStream is = p.getInputStream();
        BufferedReader bf = new BufferedReader(new InputStreamReader(is));
        InputStream is2 = p.getErrorStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is2));
        try {
            output = getOutput(bf, numLines);
            error = getError(br, numLines);
        } catch (IOException e) {
            return "Caught IOException. Message = " + ls + e.getMessage() + ls;
        }
        StringBuffer message = new StringBuffer();
        if (output.size() > 0) {
            message.append(ls + "OUTPUT:" + ls);
            for (Iterator iter = output.iterator(); iter.hasNext(); ) {
                message.append((String) iter.next() + ls);
            }
        }
        if (error.size() > 0) {
            message.append("ERRORS:" + ls);
            for (Iterator iter = error.iterator(); iter.hasNext(); ) {
                message.append((String) iter.next() + ls);
            }
        }
        try {
            retStatus = p.waitFor();
        } catch (InterruptedException e) {
        }
        try {
            is.close();
            is2.close();
            bf.close();
            br.close();
        } catch (IOException e) {
        }
        long l2 = System.currentTimeMillis();
        millis = l2 - l1;
        System.out.println("millis = " + millis);
        String time = new org.xaware.shared.util.ElapsedTimeConverter().convertElapsedTime(millis);
        message.append(ls + "TIME TO EXECUTE: " + time + " (HH:MM:SS:MS) " + ls);
        return message.toString();
    }
