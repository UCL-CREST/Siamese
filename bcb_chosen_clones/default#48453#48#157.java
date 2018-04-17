    public void run() {
        try {
            charsExpected = 10000 * (testData[0].length() + testData[1].length());
            String fileName = "/tmp/out" + myNumber;
            final Process tac = Runtime.getRuntime().exec(new String[] { PROGRAM, fileName }, null, new File("/tmp"));
            Thread writer = new Thread() {

                public void run() {
                    DataOutputStream stdin = new DataOutputStream(tac.getOutputStream());
                    try {
                        for (int x = 0; x < 10000; x++) {
                            for (int i = 0; i < testData.length; i++) {
                                charsWritten += testData[i].length();
                                stdin.writeUTF(testData[i]);
                            }
                        }
                        stdin.flush();
                        stdin.close();
                    } catch (IOException e) {
                        throw new Error("TestRuntimeExec FAILED");
                    }
                }
            };
            Thread reader = new Thread() {

                public void run() {
                    DataInputStream stdout = new DataInputStream(tac.getInputStream());
                    try {
                        for (int x = 0; x < 10000; x++) {
                            for (int i = 0; i < testData.length; i++) {
                                String in = stdout.readUTF();
                                charsRead += in.length();
                                if (!in.equals(testData[i])) throw new Error("TestRuntimeExec FAILED: bad input " + in);
                            }
                        }
                        int exitCode = tac.waitFor();
                        if (exitCode == 0 && charsRead == charsExpected && charsWritten == charsExpected) System.err.println("TestRuntimeExec SUCCESS"); else System.err.println("TestRuntimeExec FAILED");
                    } catch (Throwable e) {
                        e.printStackTrace();
                        throw new Error("TestRuntimeExec FAILED");
                    }
                }
            };
            writer.start();
            reader.start();
            final Thread waiter = new Thread() {

                public void run() {
                    try {
                        int exitCode = tac.waitFor();
                        System.out.println("waitFor(): Process exited with code " + exitCode);
                    } catch (InterruptedException e) {
                        if (!interruptWait) {
                            System.out.println("Waiting thread uninterrupted unexpectedly!!!");
                            System.out.println("TestRuntimeExec FAILED");
                            System.exit(1);
                        }
                        System.out.println("Waiting thread interrupted! (THIS IS GOOD)");
                        e.printStackTrace();
                    }
                }
            };
            waiter.start();
            if (interruptWait) {
                new Thread() {

                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (Exception e) {
                        }
                        waiter.interrupt();
                    }
                }.start();
            }
            Thread poller = new Thread() {

                public void run() {
                    int exitCode = -99;
                    boolean exited = false;
                    do {
                        try {
                            exitCode = tac.exitValue();
                            exited = true;
                        } catch (IllegalThreadStateException e) {
                            System.out.println("still alive!");
                            try {
                                Thread.sleep(1000);
                            } catch (Exception ee) {
                            }
                        }
                    } while (!exited);
                    System.out.println("exitValue(): Process exited with code " + exitCode);
                }
            };
            poller.start();
            try {
                reader.join();
                writer.join();
                waiter.join();
                poller.join();
            } catch (InterruptedException eee) {
                eee.printStackTrace();
            }
        } catch (Throwable e) {
            System.err.println("TestRuntimeExec FAILED with");
            e.printStackTrace();
            System.exit(1);
        }
    }
