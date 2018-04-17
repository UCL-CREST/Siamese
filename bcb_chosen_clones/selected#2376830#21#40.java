    public static void main(String argv[]) {
        try {
            if (argv.length != 1 && argv.length != 3) {
                usage();
                System.exit(1);
            }
            URL url = new URL(argv[0]);
            URLConnection conn;
            conn = url.openConnection();
            if (conn.getHeaderField("WWW-Authenticate") != null) {
                auth(conn, argv[1], argv[2]);
            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) System.out.println(line);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
