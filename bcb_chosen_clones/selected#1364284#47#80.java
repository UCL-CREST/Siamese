    private final void doProcess(final List<String> aspellCommandLine, final Processor<T> processor) {
        workThread = new Thread() {

            public void run() {
                String line;
                ProcessBuilder pb = new ProcessBuilder(aspellCommandLine);
                pb.redirectErrorStream(true);
                try {
                    Log.log(Log.DEBUG, this, "Starting listing with aspell=" + aspellCommandLine.get(0));
                    process = pb.start();
                    InputStream is = process.getInputStream();
                    BufferedReader input = new BufferedReader(new InputStreamReader(is));
                    while ((line = input.readLine()) != null) {
                        processor.accumulate(line);
                    }
                } catch (IOException e) {
                    Log.log(Log.ERROR, FutureListDicts.class, "Exception while listing dicts");
                    Log.log(Log.ERROR, FutureListDicts.class, e.getMessage());
                    saveException(new SpellException(e.getMessage()));
                } catch (SpellException spe) {
                    saveException(spe);
                } catch (Abort a) {
                    Log.log(Log.ERROR, FutureListDicts.class, "Dictionnaries listing was aborted");
                } finally {
                    if (process != null) process.destroy();
                    synchronized (lock) {
                        done = true;
                        result = processor.done();
                    }
                }
            }
        };
        workThread.start();
    }
