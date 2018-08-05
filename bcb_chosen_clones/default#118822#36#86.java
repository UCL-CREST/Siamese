    public void run() {
        BufferedReader keyb = new BufferedReader(new InputStreamReader(System.in));
        boolean running = true;
        StringTokenizer st = new StringTokenizer("");
        String cmd;
        while (running) {
            try {
                st = new StringTokenizer((String) keyb.readLine(), " ");
            } catch (Exception e) {
            }
            while (st.hasMoreTokens()) {
                cmd = st.nextToken();
                if (cmd.equals("exit")) {
                    running = false;
                    continue;
                } else if (cmd.equals("help")) {
                    printHelp();
                    continue;
                } else if (cmd.equals("players")) {
                    printPlayers();
                    continue;
                } else if (cmd.equals("tracks")) {
                    printTracks();
                    continue;
                } else if (cmd.equals("kick")) {
                    if (st.hasMoreTokens()) {
                        readParams(cmd, st);
                    } else {
                        System.out.println("\"kick\" who?\n");
                    }
                    continue;
                } else if (cmd.equals("update")) {
                    s.updateTrackList();
                } else if (cmd.equals(" ")) {
                    continue;
                } else if (cmd.equals(" ")) {
                    continue;
                } else if (cmd.equals("set")) {
                    if (st.hasMoreTokens()) {
                        readParams(cmd, st);
                    } else {
                        System.out.println("\"set\" what?");
                    }
                    continue;
                } else {
                    System.out.println("Invalid command. Type help for list of commands.\n");
                    continue;
                }
            }
        }
    }
