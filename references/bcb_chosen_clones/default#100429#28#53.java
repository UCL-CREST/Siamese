    private static int executeCommand(final String commandName, final List<String> arguments) {
        System.out.println("executing [" + commandName.toString() + "] with arguments: " + Arrays.toString(arguments.toArray()));
        try {
            final List<String> pbArgs = new LinkedList<String>(arguments);
            pbArgs.add(0, commandName);
            final ProcessBuilder pb = new ProcessBuilder(pbArgs);
            System.out.println("starting ...");
            final Process process = pb.start();
            InputStream inputStream = process.getInputStream();
            InputStream errorStream = process.getErrorStream();
            ThreadedStreamHandler outputStreamHandler = ThreadedStreamHandler.newSimple("OUT", inputStream);
            ThreadedStreamHandler errorStreamHandler = ThreadedStreamHandler.newSimple("ERR", errorStream);
            outputStreamHandler.start();
            errorStreamHandler.start();
            System.out.println("command running");
            final int exitValue = process.waitFor();
            outputStreamHandler.interrupt();
            errorStreamHandler.interrupt();
            outputStreamHandler.join();
            errorStreamHandler.join();
            System.out.println("finished. exitValue: " + exitValue);
            return exitValue;
        } catch (final Exception e) {
            throw new RuntimeException("executing command failed", e);
        }
    }
