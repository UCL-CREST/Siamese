    public static void main(String args[]) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec("./cog-shell");
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        OutputStream out = process.getOutputStream();
        String line;
        out.write("list d\n".getBytes());
        out.flush();
        out.close();
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }
