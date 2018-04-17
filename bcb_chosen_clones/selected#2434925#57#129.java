    public void run() {
        if (datamodel != null && source != null) {
            logger.fine("Start Importer");
            final StartGui startgui = StartGui.getSingelton();
            startgui.setIndeterminate(false);
            startgui.setBarValue(0);
            final List<AgentData> selected = datamodel.getSelected();
            final int barstep = 100 / (selected.size() + 1);
            int barvalue = 0;
            stopflag = false;
            for (final AgentData agt : selected) {
                logger.fine("Start import of " + agt.getArchDesc());
                if (stopflag) {
                    logger.fine("stopflag was true");
                    break;
                }
                barvalue += barstep;
                startgui.setBarValue(barvalue);
                final String candlehome = System.getenv("CANDLEHOME");
                if (candlehome == null) {
                    JOptionPane.showMessageDialog(StartGui.getSingelton(), "Environment CANDLEHOME not set!!", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                }
                boolean noprereq = startgui.getProperty("NoPrereq", "false").equalsIgnoreCase("true");
                final List<String> cmd = new ArrayList<String>();
                cmd.add(candlehome + System.getProperty("file.separator") + "bin" + System.getProperty("file.separator") + "tacmd");
                cmd.add("addBundles");
                cmd.add("-i");
                cmd.add(agt.getDirectory());
                cmd.add("-p");
                cmd.add(agt.getArch());
                cmd.add("-t");
                cmd.add(agt.getProductCode());
                cmd.add("-v");
                cmd.add(agt.getFullVersion());
                if (noprereq) {
                    cmd.add("-n");
                }
                cmd.add("-f");
                String msg = "Import: " + agt.getDesc() + " ( " + agt.getFormatedVersion() + " " + agt.getArchDesc() + " )";
                logger.fine(msg);
                startgui.setStatusLine(msg);
                startgui.console(msg);
                System.out.println("Execute: " + cmd);
                try {
                    final ProcessBuilder pb = new ProcessBuilder(cmd);
                    process = pb.start();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        startgui.console(line);
                        System.out.println(line);
                    }
                    bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    while ((line = bufferedReader.readLine()) != null) {
                        startgui.console(line);
                        System.err.println(line);
                    }
                    process.waitFor();
                } catch (final IOException e) {
                    System.err.println("\nCan't exec: " + cmd);
                } catch (final InterruptedException e) {
                    System.err.println("Interrupted");
                }
            }
            startgui.setBarValue(100);
            startgui.startScan();
            startgui.console("import finish!");
            System.out.println("import finish!");
        } else {
            System.err.println("No datamodel/source given");
        }
    }
