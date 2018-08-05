    public Thread transmitThread(final Socket s, final CountDownLatch abortLatch) {
        final Thread theThread = new Thread() {

            @Override
            public void run() {
                final Random r = new Random();
                final Counter counter = new Counter("TRANSMIT", plotter);
                counter.start();
                try {
                    final DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
                    while (!Thread.interrupted()) {
                        if (!running.get()) {
                            Thread.sleep(1000);
                            continue;
                        }
                        final int packetSize = r.nextInt(8192) + 2048;
                        final byte[] packet = new byte[packetSize];
                        r.nextBytes(packet);
                        final CRC32 crc = new CRC32();
                        crc.update(packet);
                        dos.writeInt(packetSize);
                        counter.addBytes(INTEGER_BYTES);
                        dos.write(packet);
                        counter.addBytes(packetSize);
                        dos.writeLong(crc.getValue());
                        counter.addBytes(CRC_BYTES);
                        dos.flush();
                    }
                } catch (InterruptedException e) {
                    System.out.println("Transmitter thread interrupted!");
                } catch (Exception e) {
                    System.err.println("Unexpected error in transmitter thread: " + e.getMessage());
                } finally {
                    abortLatch.countDown();
                    counter.interrupt();
                }
            }
        };
        return theThread;
    }
