    public static void main(String[] a) {
        port = (new Integer(a[0])).intValue();
        System.out.println("Port " + port);
        try {
            skt = new Socket("localhost", port);
            out = new PrintWriter(skt.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
            stdIn = new BufferedReader(new InputStreamReader(System.in));
            out.println("app __GUI__");
            String line = in.readLine().trim();
            System.out.println("start line=" + line);
            StringTokenizer st = new StringTokenizer(line);
            temp_dir = st.nextToken();
            take_screenshots = st.nextToken().equals("True");
            createAndShowGUI();
            while (true) {
                System.out.println("waiting for input from client");
                line = in.readLine().trim();
                System.out.println("line=" + line);
                st = new StringTokenizer(line);
                if (st.countTokens() == 0) {
                    continue;
                }
                String cmd = st.nextToken().toLowerCase();
                Vector args = new Vector();
                while (st.hasMoreTokens()) {
                    args.addElement(st.nextToken());
                }
                if (cmd.equals("app_start")) {
                    handleAppStart(args);
                } else if (cmd.equals("format")) {
                    handleFormat(args);
                } else if (cmd.equals("app_stop")) {
                    handleAppStop(args);
                } else if (cmd.equals("warn_user")) {
                    handleWarnUser(args);
                } else if (cmd.equals("report_bug_on_shortcut")) {
                    reportBug();
                } else if (cmd.equals("report_not_bug_on_shortcut")) {
                    reportNotBug();
                } else if (cmd.equals("report_not_bug_on_quit?")) {
                    handleReportNotBugOnQuit(args);
                } else if (cmd.equals("nearest_neighbors")) {
                    handleNearestNeighbors(args);
                } else {
                    System.out.println("Unrecognized command " + cmd + ".");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
