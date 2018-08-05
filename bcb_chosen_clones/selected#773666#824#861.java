        private void zip(String base, Set<DataFile> dss, ZipOutputStream zout) throws IOException, Exception {
            for (DataFile ds : dss) {
                if (ds.isDirectory()) {
                    ZipEntry entry = new ZipEntry(base + ds.getName() + "/");
                    entry.setMethod(ZipEntry.DEFLATED);
                    try {
                        zout.putNextEntry(entry);
                        zout.closeEntry();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                        Core.Logger.log(ioe.getMessage(), Level.WARNING);
                    }
                    zip(base + ds.getName() + "/", ds.children(), zout);
                } else {
                    int read;
                    byte[] buff = new byte[0x800];
                    ZipEntry entry = new ZipEntry(base + ds.getName());
                    entry.setMethod(ZipEntry.DEFLATED);
                    try {
                        InputStream in = ds.getInputStream();
                        zout.putNextEntry(entry);
                        progress_bytes_max = ds.size();
                        progress_bytes_current = 0;
                        while ((read = in.read(buff)) != -1) {
                            zout.write(buff, 0, read);
                            progress_bytes_current += read;
                            if (stopped) throw new Exception("Thread stopped by user input.");
                        }
                        zout.closeEntry();
                        progress_file_current++;
                        in.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                        Core.Logger.log(ioe.getMessage(), Level.WARNING);
                    }
                }
            }
        }
