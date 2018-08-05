    public LocalizationSolver(String name, String serverIP, int portNum, String workDir) {
        this.info = new HashMap<String, Object>();
        this.workDir = workDir;
        try {
            Socket solverSocket = new Socket(serverIP, portNum);
            this.fromServer = new Scanner(solverSocket.getInputStream());
            this.toServer = new PrintWriter(solverSocket.getOutputStream(), true);
            this.toServer.println("login client abc");
            this.toServer.println("solver " + name);
            System.out.println(this.fromServer.nextLine());
        } catch (IOException e) {
            System.err.println(e);
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Localization Solver started with name: " + name);
    }
