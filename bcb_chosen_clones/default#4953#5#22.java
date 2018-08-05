    public static void main(String argv[]) {
        try {
            String line;
            Process p = Runtime.getRuntime().exec("./cmdpy.py");
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                String[] tokens = line.split("[ ]+");
                for (int i = 0; i < tokens.length; ++i) {
                    System.out.print(tokens[i]);
                    System.out.print(" ");
                }
                System.out.println("");
            }
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
