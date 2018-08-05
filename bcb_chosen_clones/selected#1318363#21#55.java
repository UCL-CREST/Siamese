    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
            out.println("# Welcome to Imperium Space");
            out.println("# first dev version :)");
            gc = new GameCnx(client);
            while (true) {
                String command = in.readLine();
                if (command.toLowerCase().equals("quit")) {
                    client.close();
                    return;
                }
                try {
                    Class cmd = Class.forName("server." + command);
                    java.lang.reflect.Constructor ctr = cmd.getConstructor(new Class[] { GameCnx.class });
                    Object o = ctr.newInstance(new Object[] { gc });
                    Method qrm = cmd.getMethod("query", new Class[] { InputStreamReader.class, PrintWriter.class });
                    qrm.invoke(o, new Object[] { new InputStreamReader(client.getInputStream()), out });
                } catch (Exception e) {
                    out.println("ERR:Command not found : " + command);
                    System.out.println("Impossible to load command : " + command);
                    System.out.println("the exception : " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("IOException : " + e.getClass().getName());
            System.out.println("Message : " + e.getMessage());
            e.printStackTrace();
            return;
        }
    }
