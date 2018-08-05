    private static String executeBulkLoadWithTimeout(String command, String logLines, long timeout) {
        Process loadProcess = null;
        boolean programExitedWithoutErrors = false;
        boolean programExitedWithErrors = false;
        String ls = System.getProperty("line.separator");
        LinkedList output = new LinkedList();
        LinkedList error = new LinkedList();
        int numLines = new Integer(logLines).intValue();
        InputStream is = null;
        BufferedReader bf = null;
        InputStream is2 = null;
        BufferedReader br = null;
        long l1 = System.currentTimeMillis();
        long millis = 0;
        long secs = 0;
        long min = 0;
        long hours = 0;
        Runtime rt = Runtime.getRuntime();
        try {
            loadProcess = rt.exec(command);
        } catch (IOException e) {
            return "IOException thrown while executing the program " + command + ls + " Message = " + e.getMessage() + ls;
        }
        while (timeout > 1000 && loadProcess != null) {
            try {
                Thread.sleep(1000);
                long t1 = System.currentTimeMillis();
                int ret = Integer.MIN_VALUE;
                try {
                    ret = loadProcess.exitValue();
                    if (ret == 0) {
                        programExitedWithoutErrors = true;
                    } else programExitedWithErrors = true;
                    break;
                } catch (IllegalThreadStateException e) {
                    is = loadProcess.getInputStream();
                    bf = new BufferedReader(new InputStreamReader(is));
                    is2 = loadProcess.getErrorStream();
                    br = new BufferedReader(new InputStreamReader(is2));
                    try {
                        output = getOutput(bf, numLines);
                        error = getError(br, numLines);
                    } catch (IOException e1) {
                        return "Caught IOException. Message = " + ls + e1.getMessage() + ls;
                    }
                    timeout -= 1000 + (System.currentTimeMillis() - t1);
                    continue;
                }
            } catch (InterruptedException e) {
            }
        }
        if (programExitedWithoutErrors || programExitedWithErrors) {
            if (output.isEmpty()) {
                is = loadProcess.getInputStream();
                bf = new BufferedReader(new InputStreamReader(is));
                is2 = loadProcess.getErrorStream();
                br = new BufferedReader(new InputStreamReader(is2));
                try {
                    output = getOutput(bf, numLines);
                    System.out.println("output = " + output.toString());
                    error = getError(br, numLines);
                } catch (IOException e1) {
                    return "Caught IOException. Message = " + ls + e1.getMessage() + ls;
                }
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
                if (is != null) {
                    is.close();
                }
                if (is2 != null) {
                    is2.close();
                }
                if (bf != null) {
                    bf.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
            }
            long l2 = System.currentTimeMillis();
            millis = l2 - l1;
            System.out.println("millis = " + millis);
            String time = new org.xaware.shared.util.ElapsedTimeConverter().convertElapsedTime(millis);
            message.append(ls + "TIME TO EXECUTE: " + time + " (HH:MM:SS:MS) " + ls);
            return message.toString();
        }
        return "Timed out... Program " + command + " may still be running";
    }
