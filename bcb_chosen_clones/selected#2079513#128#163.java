                @Override
                public void run() {
                    byte[] data = soundMap.get(effect);
                    AudioFormat af = soundFormat.get(effect);
                    final CyclicBarrier barrier = new CyclicBarrier(2);
                    try {
                        Clip c = AudioSystem.getClip();
                        c.open(af, data, 0, data.length);
                        FloatControl fc = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
                        if (fc != null) {
                            fc.setValue(AudioThread.computeGain(fc, vol));
                        }
                        LineListener ll = new LineListener() {

                            @Override
                            public void update(LineEvent event) {
                                if (event.getType() == Type.STOP || event.getType() == Type.CLOSE) {
                                    try {
                                        barrier.await();
                                    } catch (InterruptedException ex) {
                                    } catch (BrokenBarrierException ex) {
                                    }
                                }
                            }
                        };
                        c.addLineListener(ll);
                        c.start();
                        barrier.await();
                        c.removeLineListener(ll);
                        c.close();
                    } catch (LineUnavailableException ex) {
                        ex.printStackTrace();
                    } catch (InterruptedException ex) {
                    } catch (BrokenBarrierException ex) {
                    }
                }
