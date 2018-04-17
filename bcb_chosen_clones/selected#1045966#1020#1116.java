    public void getDirectory(String dir_name, java.util.zip.ZipOutputStream out, com.fujitsu.arcon.njs.interfaces.IncarnatedUser user, UspaceManager.Uspace us) throws NJSException, IOException, TSIUnavailableException {
        TSIConnection c = null;
        boolean still_ok = true;
        try {
            c = getTSIConnectionFactory().getTSIConnection(user);
            String command = makeTSIIdentityLine(us) + "#TSI_GETDIRECTORY\n" + "#TSI_DIRECTORY " + dir_name + "\n";
            String reply;
            try {
                reply = c.send(command);
            } catch (IOException ioex) {
                throw new IOException("Getting directory from TSI <" + getName() + ">: " + ioex);
            }
            if (!reply.equals("TSI_OK\n")) {
                c.dead();
                throw new IOException("TSI <" + getName() + "> responded incorrectly while getting directory: " + reply);
            }
            byte[] buffer = new byte[BUFFER_SIZE];
            String root_dir = c.getLine();
            int root_length = root_dir.length() + 1;
            String file_name = c.getLine();
            while (!file_name.equals("TSI_OK")) {
                if (file_name.startsWith("TSI_FAILED")) {
                    c.getLine();
                    throw new IOException("TSI <" + getName() + "> errors getting directory: " + file_name);
                }
                String size = c.getLine();
                String mode = "6";
                if (size.indexOf(" ") > 0) {
                    mode = size.substring(size.indexOf(" ")).trim();
                    size = size.substring(0, size.indexOf(" ")).trim();
                }
                long num_bytes = Long.valueOf(size).longValue();
                byte[] m_mode = new byte[1];
                m_mode[0] = Byte.parseByte(mode);
                file_name = file_name.substring(root_length);
                if (logger.CHAT) logger.chat(getName(), "getDirectory expecting file <" + file_name + "> size <" + num_bytes + "> <" + m_mode[0] + ">");
                if (num_bytes == -1) {
                    try {
                        ZipEntry next_file = new ZipEntry(file_name);
                        next_file.setExtra(m_mode);
                        if (still_ok) out.putNextEntry(next_file);
                        int this_block = Integer.valueOf(c.getLine()).intValue();
                        do {
                            c.getData(buffer, 0, this_block);
                            try {
                                if (still_ok) out.write(buffer, 0, this_block);
                            } catch (IOException ioex) {
                                if (logger.CHAT) logger.chat(getName(), "getDirectory, downstream IO error - devnulling fifo");
                                still_ok = false;
                            }
                            this_block = Integer.valueOf(c.getLine()).intValue();
                        } while (this_block > 0);
                        if (still_ok) out.closeEntry();
                        file_name = c.getLine();
                    } catch (IOException ioex) {
                        c.dead();
                        throw new IOException("TSI <" + getName() + ">  getDirectory IO Error during fifo <" + file_name + ">");
                    } catch (NumberFormatException ex) {
                        c.dead();
                        throw new IOException("TSI <" + getName() + "> synchronisation problem expecting a fifo <" + file_name + "> size: " + size);
                    }
                } else {
                    try {
                        ZipEntry next_file = new ZipEntry(file_name);
                        next_file.setSize(num_bytes);
                        next_file.setExtra(m_mode);
                        if (still_ok) out.putNextEntry(next_file);
                        do {
                            int to_get = (int) (num_bytes > BUFFER_SIZE ? BUFFER_SIZE : num_bytes);
                            c.getData(buffer, 0, to_get);
                            num_bytes -= to_get;
                            try {
                                if (still_ok) out.write(buffer, 0, to_get);
                            } catch (IOException ioex) {
                                if (logger.CHAT) logger.chat(getName(), "getDirectory, downstream IO error - devnulling");
                                still_ok = false;
                            }
                        } while (num_bytes > 0);
                        if (still_ok) out.closeEntry();
                        file_name = c.getLine();
                    } catch (IOException ioex) {
                        c.dead();
                        throw new IOException("TSI <" + getName() + ">  getDirectory IO Error during file <" + file_name + ">");
                    } catch (NumberFormatException ex) {
                        c.dead();
                        throw new IOException("TSI <" + getName() + "> synchronisation problem expecting a file <" + file_name + "> size: " + size);
                    }
                }
            }
            c.getLine();
        } finally {
            c.done();
        }
        if (!still_ok) {
            throw new IOException("TSI <" + getName() + "> lost downstream connection during getDirectory");
        }
    }
