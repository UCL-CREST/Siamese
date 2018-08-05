    Megahal(String halPath) {
        halParam[0] = new String(halPath);
        halParam[1] = new String("--no-prompt");
        halParam[2] = new String("--no-wrap");
        halParam[3] = new String("--no-banner");
        System.out.print("Launching MEGAHAL... ");
        try {
            this.halProc = Runtime.getRuntime().exec(halParam);
            this.out = new PrintWriter(this.halProc.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(this.halProc.getInputStream()));
        } catch (IOException ex) {
            System.out.println("failed (Reason: " + ex + ").");
            System.exit(0);
        }
        this.readLine();
        System.out.println("done.");
    }
