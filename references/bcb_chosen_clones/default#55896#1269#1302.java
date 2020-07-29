    public boolean ErrorCheck(String fileName) {
        String line;
        StringTokenizer st;
        String testToken;
        boolean error = false;
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            while ((line = in.readLine()) != null) {
                if (line.equals(":-( /dev/dvd: media is not recognized as recordable DVD: 9")) {
                    MessageBox("Non-recoverable error occurred." + "\nClass Name: " + new Exception().getStackTrace()[1].getClassName() + "\nMethod Name: " + new Exception().getStackTrace()[1].getMethodName() + "\nError was: " + line, 0);
                    return true;
                }
                st = new StringTokenizer(line, "*,;:'-~\t ");
                while (st.hasMoreTokens()) {
                    testToken = st.nextToken();
                    if (testToken.equalsIgnoreCase("Error")) {
                        MessageBox("Non-recoverable error occurred." + "\nClass Name: " + new Exception().getStackTrace()[1].getClassName() + "\nMethod Name: " + new Exception().getStackTrace()[1].getMethodName() + "\nError was: " + line, 0);
                        error = true;
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            SaveStackTrace.printTrace(strOutputDir, ex);
            MessageBox("Can not find " + fileName + "\n" + ex.toString(), 0);
            ex.printStackTrace();
            return true;
        } catch (IOException ex) {
            SaveStackTrace.printTrace(strOutputDir, ex);
            MessageBox("IO Error\n" + ex.toString(), 0);
            ex.printStackTrace();
            return true;
        }
        return error;
    }
