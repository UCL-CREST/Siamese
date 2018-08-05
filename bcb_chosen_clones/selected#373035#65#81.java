    private String loadLogFile() {
        StringBuffer result = new StringBuffer();
        try {
            String line;
            Process p = Runtime.getRuntime().exec("eventviewer.exe");
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                result.append(line + "\n");
                System.out.println(line);
            }
            input.close();
            return result.toString();
        } catch (Exception err) {
            err.printStackTrace();
            return "";
        }
    }
