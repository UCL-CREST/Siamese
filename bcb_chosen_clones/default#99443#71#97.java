    public void run() {
        if ($selectedTest != null) {
            if ($selectedTest.returnsCommand()) {
                Debug.log("Ping test", "Test requires command to be executed.");
                String cmd = $selectedTest.getCommand();
                Debug.log("Ping test", "Executing '" + cmd + "'");
                try {
                    Process p = Runtime.getRuntime().exec(cmd);
                    BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line = "";
                    $output = new ArrayList();
                    while ((line = in.readLine()) != null) {
                        Debug.log("Ping test", "    " + line);
                        $output.add(line);
                    }
                    Debug.log("Ping test", "Test done executing");
                } catch (Exception e) {
                    Debug.log("Ping test", "Execution failed");
                }
            } else {
                Debug.log("Ping test", "Test can run on its own");
                $selectedTest.run();
            }
        } else {
            Debug.log("Ping test", "No test selected, can't run");
        }
    }
