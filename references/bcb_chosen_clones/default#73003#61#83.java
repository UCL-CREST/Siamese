    public List<String> execute() {
        try {
            Process process = new ProcessBuilder().command(args).redirectErrorStream(true).start();
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            List<String> outputLines = new ArrayList<String>();
            String outputLine;
            while ((outputLine = in.readLine()) != null) {
                outputLines.add(outputLine);
            }
            if (process.waitFor() != 0 && !permitNonZeroExitStatus) {
                StringBuilder message = new StringBuilder();
                for (String line : outputLines) {
                    message.append("\n").append(line);
                }
                throw new RuntimeException("Process failed: " + args + message);
            }
            return outputLines;
        } catch (IOException e) {
            throw new RuntimeException("Process failed: " + args, e);
        } catch (InterruptedException e) {
            throw new RuntimeException("Process failed: " + args, e);
        }
    }
