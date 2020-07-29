    public static void main(String args[]) {
        try {
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec("Disk.exe t 6291529");
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String h = in.readLine();
            while (h != null) {
                System.out.println(h);
                h = in.readLine();
            }
            OutputStream out = p.getOutputStream();
            InputStream err = p.getErrorStream();
            p.destroy();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
