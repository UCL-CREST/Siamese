        private MemoryUsage measure() {
            String[] commands = GET_DIRTY_PAGES;
            if (className != null) {
                List<String> commandList = new ArrayList<String>(GET_DIRTY_PAGES.length + 1);
                commandList.addAll(Arrays.asList(commands));
                commandList.add(className);
                commands = commandList.toArray(new String[commandList.size()]);
            }
            try {
                final Process process = Runtime.getRuntime().exec(commands);
                final InputStream err = process.getErrorStream();
                Thread errThread = new Thread() {

                    @Override
                    public void run() {
                        copy(err, System.err);
                    }
                };
                errThread.setDaemon(true);
                errThread.start();
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = in.readLine();
                if (line == null || !line.startsWith("DECAFBAD,")) {
                    System.err.println("Got bad response for " + className + ": " + line + "; command was " + Arrays.toString(commands));
                    errorCount += 1;
                    return NOT_AVAILABLE;
                }
                in.close();
                err.close();
                process.destroy();
                return new MemoryUsage(line);
            } catch (IOException e) {
                System.err.println("Error getting stats for " + className + ".");
                e.printStackTrace();
                return NOT_AVAILABLE;
            }
        }
