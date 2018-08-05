    public String exec(String path, boolean wait) {
        String out = "";
        try {
            String line;
            Process p = Runtime.getRuntime().exec(path);
            if (wait) {
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                while ((line = input.readLine()) != null) {
                    if (out != "") out += "\r\n";
                    out += line;
                    System.out.println(line);
                    line = err.readLine();
                    if (line != null) System.err.println(line);
                }
                input.close();
            }
        } catch (Exception err) {
            System.err.println("ERROR");
            err.printStackTrace();
        }
        return out;
    }
