    private boolean exec(String[] command, boolean displayOut, boolean displayErr) throws Exception {
        Process p = Runtime.getRuntime().exec(command);
        p.waitFor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedReader reader2 = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        String err = "";
        String s = null;
        while ((s = reader.readLine()) != null) {
            if (displayOut) {
                info("\t\t" + s);
            }
        }
        while ((s = reader2.readLine()) != null) {
            err += s;
            if (displayErr) {
                info("\t\t ERROR: " + s);
            }
        }
        reader.close();
        reader2.close();
        return err.length() == 0;
    }
