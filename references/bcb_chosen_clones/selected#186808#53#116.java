    public void notifyTerminated(Writer r) {
        all_writers.remove(r);
        if (all_writers.isEmpty()) {
            all_terminated = true;
            Iterator iterator = open_files.iterator();
            while (iterator.hasNext()) {
                FileWriter.FileChunk fc = (FileWriter.FileChunk) iterator.next();
                do {
                    try {
                        fc.stream.flush();
                        fc.stream.close();
                    } catch (IOException e) {
                    }
                    fc = fc.next;
                } while (fc != null);
            }
            iterator = open_files.iterator();
            boolean all_ok = true;
            while (iterator.hasNext()) {
                FileWriter.FileChunk fc = (FileWriter.FileChunk) iterator.next();
                logger.logComment("File chunk <" + fc.name + "> " + fc.start_byte + " " + fc.position + " " + fc.actual_file);
                boolean ok = true;
                while (fc.next != null) {
                    ok = ok && (fc.start_byte + fc.actual_file.length()) == fc.next.start_byte;
                    fc = fc.next;
                }
                if (ok) {
                    logger.logComment("Received file <" + fc.name + "> is contiguous (and hopefully complete)");
                } else {
                    logger.logError("Received file <" + fc.name + "> is NOT contiguous");
                    all_ok = false;
                }
            }
            if (all_ok) {
                byte[] buffer = new byte[16384];
                iterator = open_files.iterator();
                while (iterator.hasNext()) {
                    FileWriter.FileChunk fc = (FileWriter.FileChunk) iterator.next();
                    try {
                        if (fc.next != null) {
                            FileOutputStream fos = new FileOutputStream(fc.actual_file, true);
                            fc = fc.next;
                            while (fc != null) {
                                FileInputStream fis = new FileInputStream(fc.actual_file);
                                int actually_read = fis.read(buffer);
                                while (actually_read != -1) {
                                    fos.write(buffer, 0, actually_read);
                                    actually_read = fis.read(buffer);
                                }
                                fc.actual_file.delete();
                                fc = fc.next;
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            fte.allWritersTerminated();
            fte = null;
        }
    }
