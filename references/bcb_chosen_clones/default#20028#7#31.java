    public static void main(String[] args) {
        try {
            Runtime r = Runtime.getRuntime();
            String s = "Hello World";
            String[] a = { "echo", s };
            Process p = r.exec(a);
            InputStream is = p.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String result = br.readLine();
            if (!s.equals(result)) {
                System.out.println("bad 1");
                return;
            }
            result = br.readLine();
            if (result != null) {
                System.out.println("bad 2");
                return;
            }
            int c = p.waitFor();
            System.out.println(c == 0 ? "ok" : "bad 3");
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
