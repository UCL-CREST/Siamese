    protected static final void copyFile(String from, String to) throws SeleniumException {
        try {
            java.io.File fileFrom = new File(from);
            java.io.File fileTo = new File(to);
            FileReader in = new FileReader(fileFrom);
            FileWriter out = new FileWriter(fileTo);
            int c;
            while ((c = in.read()) != -1) out.write(c);
            in.close();
            out.close();
        } catch (Exception e) {
            throw new SeleniumException("Failed to copy new file : " + from + " to : " + to, e);
        }
    }
