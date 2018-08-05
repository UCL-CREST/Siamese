    public static void main(String argv[]) {
        try {
            String line;
            Process p = Runtime.getRuntime().exec("sar");
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                String[] tokens = line.split("[ ]+");
                if (tokens.length > 3) System.out.println(tokens[3]);
            }
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
