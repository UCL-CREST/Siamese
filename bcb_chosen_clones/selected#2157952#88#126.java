        @Override
        public void run() {
            long timeout = 10 * 1000L;
            long start = (new Date()).getTime();
            try {
                InputStream is = socket.getInputStream();
                boolean available = false;
                while (!available && !socket.isClosed()) {
                    try {
                        if (is.available() != 0) {
                            available = true;
                        } else {
                            Thread.sleep(100);
                        }
                    } catch (Exception e) {
                        LOG.error("Error checking socket", e);
                    }
                    long curr = (new Date()).getTime();
                    if ((curr - start) >= timeout) {
                        break;
                    }
                }
                if (socket.isClosed()) {
                } else {
                    tmpFile = File.createTempFile("ftp", "dat", new File("./tmp"));
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tmpFile));
                    IOUtils.copy(is, bos);
                    bos.flush();
                    bos.close();
                }
                String msg = FtpResponse.ReadComplete.asString() + ClientCommand.SP + "Read Complete" + ClientCommand.CRLF;
                List<String> list = new ArrayList<String>();
                list.add(msg);
                ClientResponse response = new ClientResponse(list);
                ftpClient.notifyListeners(response);
            } catch (Exception e) {
                LOG.error("Error reading server response", e);
            }
        }
