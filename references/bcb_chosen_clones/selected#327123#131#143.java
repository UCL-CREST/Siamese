    private Vector executer(String cmd) {
        Vector result = new Vector();
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) result.addElement(line);
            input.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
