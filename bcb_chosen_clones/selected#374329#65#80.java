                public void run() {
                    ShareFolder part = (ShareFolder) ObjectClone.clone(readers[j]);
                    ShareFileReader reader = new ShareFileReader(readers[j], files[0]);
                    ShareFileWriter writer = new ShareFileWriter(part, new File("Downloads/" + readers[j].getName()));
                    long tot = 0;
                    byte[] b = new byte[(int) (Math.random() * 10000)];
                    while (tot < readers[j].getSize()) {
                        reader.read(b);
                        byte[] bwrite = new byte[(int) (Math.random() * 10000) + b.length];
                        System.arraycopy(b, 0, bwrite, 0, b.length);
                        writer.write(bwrite, b.length);
                        tot += b.length;
                    }
                    done++;
                    System.out.println((int) (done * 100.0 / PARTS) + "% Complete");
                }
