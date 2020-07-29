    public LookupCmd(ClientHandler cur_client, String Issued_Command) {
        StringTokenizer ST = new StringTokenizer(Issued_Command);
        ST.nextToken();
        if (!(ST.hasMoreTokens())) {
            cur_client.sendFromBot("The lookup command list\n\nUsage:\n\n!lookup <switch> <input>\n\nAvailable Switches:\n\nCommand		Input	  Information\n\nnslookup		<dns>	- Shows DNS to IP information in client\nping		<nick>	- Shows ping statistics in client\nrevdns		<nick>	- Shows hostname of user\ntraceroute	<nick>	- Shows route to user taken by server\nwhois 		<nick>	- Shows ripe information on user");
            return;
        }
        String carrier = ST.nextToken();
        if (carrier.equalsIgnoreCase("revdns")) {
            if (!(ST.hasMoreTokens())) {
                cur_client.sendFromBot("Error: couldn't retrive reverse dns information");
                return;
            }
            String input = ST.nextToken();
            ClientNod user = null;
            for (ClientNod x : SimpleHandler.getUsers()) {
                if (x.cur_client.NI.equalsIgnoreCase(input)) user = x;
            }
            if (user == null) {
                cur_client.sendFromBot("Error: No user with that nick");
                return;
            }
            try {
                InetAddress addr = InetAddress.getByName(user.cur_client.RealIP);
                cur_client.sendFromBot("Fetching Reverse DNS Information... (please be patient)");
                cur_client.sendFromBot(addr.getHostName());
            } catch (UnknownHostException uhe) {
                cur_client.sendFromBot("Error: Couldn't resolve to host");
                return;
            }
        } else if (carrier.equalsIgnoreCase("whois")) {
            if (!(ST.hasMoreTokens())) {
                cur_client.sendFromBot("Error: couldn't retrive whois information");
                return;
            }
            String input = ST.nextToken();
            ClientNod user = null;
            for (ClientNod x : SimpleHandler.getUsers()) {
                if (x.cur_client.NI.equalsIgnoreCase(input)) user = x;
            }
            if (user == null) {
                cur_client.sendFromBot("Error: No user with that nick");
                return;
            }
            cur_client.sendFromBot("Fetching Whois Information... (please be patient)");
            Socket theSocket;
            DataInputStream theWhoisStream;
            PrintStream ps;
            try {
                theSocket = new Socket("whois.ripe.net", 43);
                ps = new PrintStream(theSocket.getOutputStream());
                ps.print(user.cur_client.RealIP + "\r\n");
                theWhoisStream = new DataInputStream(theSocket.getInputStream());
                String s;
                while ((s = theWhoisStream.readLine()) != null) {
                    cur_client.sendFromBot(s);
                }
            } catch (Exception e) {
                System.err.println(e);
            }
        } else if (carrier.equalsIgnoreCase("traceroute")) {
            if (!(ST.hasMoreTokens())) {
                cur_client.sendFromBot("Error: couldn't retrive traceroute infomation");
                return;
            }
            String input = ST.nextToken();
            ClientNod user = null;
            for (ClientNod x : SimpleHandler.getUsers()) {
                if (x.cur_client.NI.equalsIgnoreCase(input)) user = x;
            }
            if (user == null) {
                cur_client.sendFromBot("Error: No user with that nick");
                return;
            }
            cur_client.sendFromBot("Fetching Traceroute Information... (please be patient)");
            cur_client.sendFromBot(TraceRoute.Trace(user.cur_client.RealIP));
        } else if (carrier.equalsIgnoreCase("nslookup")) {
            if (!(ST.hasMoreTokens())) {
                cur_client.sendFromBot("Error: couldn't retrive nslookup information");
                return;
            }
            String input = ST.nextToken();
            try {
                InetAddress inetHost = InetAddress.getByName(input);
                String hostName = inetHost.getHostName();
                cur_client.sendFromBot("Fetching NSLookup Information... (please be patient)");
                cur_client.sendFromBot("The host name was: " + hostName);
                cur_client.sendFromBot("The hosts IP address is: " + inetHost.getHostAddress());
            } catch (UnknownHostException ex) {
                cur_client.sendFromBot("Unrecognized host");
            }
        } else if (carrier.equalsIgnoreCase("ping")) {
            if (!(ST.hasMoreTokens())) {
                cur_client.sendFromBot("Error: couldn't retrive ping information");
                return;
            }
            String input = ST.nextToken();
            ClientNod user = null;
            for (ClientNod x : SimpleHandler.getUsers()) {
                if (x.cur_client.NI.equalsIgnoreCase(input)) user = x;
            }
            if (user == null) {
                cur_client.sendFromBot("Error: No user with that nick");
                return;
            }
            {
                cur_client.sendFromBot("Fetching Ping Information... (please be patient)");
                int i = 0;
                String ip;
                ip = user.cur_client.RealIP;
                String pingResult = "";
                String pingCmd = "ping " + ip;
                try {
                    Runtime r = Runtime.getRuntime();
                    Process p = r.exec(pingCmd);
                    BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null && i < 5) {
                        cur_client.sendFromBot(inputLine);
                        i++;
                    }
                    cur_client.sendFromBot("Ping is done..");
                    in.close();
                    return;
                } catch (IOException e) {
                    System.out.println(e);
                } catch (Exception ee) {
                    System.out.println(ee);
                }
            }
        } else {
            cur_client.sendFromBot("Error: Unknown Switch use !lookup for list of commands");
            return;
        }
    }
