    protected ArraySet getHostsFrom(Host host) {
        ArraySet incomingHosts = new ArraySet();
        String response = null;
        try {
            Socket clientSocket = new Socket(host.getName(), host.getPort());
            BufferedReader inStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            DataOutputStream outStream = new DataOutputStream(clientSocket.getOutputStream());
            outStream.writeBytes("get hosts\n");
            response = inStream.readLine();
            if (response.equals("Host List")) {
                response = inStream.readLine();
            } else {
                incomingHosts = null;
            }
            clientSocket.close();
        } catch (IOException e) {
            incomingHosts = null;
        }
        if (response != null) {
            response = response.substring(1, (response.length() - 1));
            StringTokenizer tokenizer = new StringTokenizer(response, ", ");
            Host incomingHost;
            while (tokenizer.hasMoreTokens()) {
                incomingHost = new Host(tokenizer.nextToken());
                incomingHosts.add(incomingHost);
            }
        } else {
            incomingHosts = null;
        }
        return incomingHosts;
    }
