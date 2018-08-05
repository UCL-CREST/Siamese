    public static void main(String[] args) {
        if (args.length < 2) {
            BeschreibungAnzeigen();
            return;
        }
        Properties arguments = ArgParser.ArgsToProperties(args);
        if (arguments == null) {
            System.out.println("Error in the command line arguments!");
            BeschreibungAnzeigen();
            return;
        }
        final String[] commands = ArgParser.getUnnamedArguments(arguments);
        new ForLoop(0, commands.length - 1, 1, commands.length) {

            @Override
            public void Loop(int i) {
                char[] puffer = new char[1000];
                try {
                    Process prozess = Runtime.getRuntime().exec(commands[i]);
                    BufferedReader ausgabe = new BufferedReader(new InputStreamReader(prozess.getInputStream()));
                    BufferedReader error = new BufferedReader(new InputStreamReader(prozess.getErrorStream()));
                    while (true) {
                        while (ausgabe.ready()) {
                            int anzahl = ausgabe.read(puffer, 0, 1000);
                            for (int x = 0; x < anzahl; x++) System.out.print(puffer[x]);
                        }
                        while (error.ready()) {
                            int anzahl = error.read(puffer, 0, 1000);
                            for (int x = 0; x < anzahl; x++) System.out.print(puffer[x]);
                        }
                        try {
                            int wert = prozess.exitValue();
                            break;
                        } catch (IllegalThreadStateException ex) {
                        }
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException ex) {
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        };
    }
