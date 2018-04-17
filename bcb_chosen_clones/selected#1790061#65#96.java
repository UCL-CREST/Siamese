    public String run(MimeMessage msg) throws UserMenuEntryInvocationException {
        File f = null;
        try {
            StringBuffer output = new StringBuffer();
            BufferedReader in = null;
            Process p;
            f = writeTemporaryMessageFile(msg);
            try {
                String lineSep = System.getProperty("line.separator", "\n");
                String line;
                p = Runtime.getRuntime().exec(new String[] { getCommand(), f.getAbsolutePath() });
                p.waitFor();
                in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((line = in.readLine()) != null) {
                    output.append(line);
                    output.append(lineSep);
                }
                return output.toString();
            } catch (Exception e) {
                throw new UserMenuEntryInvocationException(e.getMessage());
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                    }
                }
            }
        } finally {
            if (f != null) f.delete();
        }
    }
