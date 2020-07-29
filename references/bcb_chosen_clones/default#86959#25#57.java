            @Override
            public void run() {
                final Counter counter = new Counter("TRANSMIT", plotter);
                counter.start();
                try {
                    final DataInputStream dis = new DataInputStream(s.getInputStream());
                    while (!Thread.interrupted()) {
                        if (!running.get()) {
                            Thread.sleep(1000);
                            continue;
                        }
                        final int nextPacketSize = dis.readInt();
                        counter.addBytes(INTEGER_BYTES);
                        final byte[] packet = new byte[nextPacketSize];
                        dis.readFully(packet);
                        counter.addBytes(nextPacketSize);
                        final long otherSideCrc = dis.readLong();
                        counter.addBytes(CRC_BYTES);
                        final CRC32 crc = new CRC32();
                        crc.update(packet);
                        if (otherSideCrc != crc.getValue()) {
                            throw new RuntimeException("CRC values don't match: this: " + crc.getValue() + ", other: " + otherSideCrc);
                        }
                    }
                } catch (InterruptedException e) {
                    System.out.println("Receiver thread interrupted!");
                } catch (Exception e) {
                    System.err.println("Unexpected error in receiver thread: " + e.getMessage());
                } finally {
                    abortLatch.countDown();
                    counter.interrupt();
                }
            }
