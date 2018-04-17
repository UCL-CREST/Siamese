        public void run() {
            Socket s = null;
            OutputStream out = null;
            DataInputStream in = null;
            try {
                int count = 0;
                while (count < 3) {
                    count++;
                    int portAsInt = 0;
                    try {
                        portAsInt = Integer.parseInt(port);
                    } catch (NumberFormatException ee) {
                        throw new Exception("NscaSend.send_nsca(): port was malformed: " + port);
                    }
                    s = new Socket();
                    s.setKeepAlive(true);
                    s.setSoTimeout(timeout);
                    s.setTcpNoDelay(false);
                    java.net.InetSocketAddress socketAddress = new InetSocketAddress(host, portAsInt);
                    s.connect(socketAddress);
                    if (s.isBound()) break; else {
                        try {
                            s.close();
                        } catch (Exception e) {
                        }
                        s = null;
                    }
                }
                out = s.getOutputStream();
                in = new DataInputStream(s.getInputStream());
                byte[] received_iv = new byte[128];
                in.readFully(received_iv, 0, 128);
                int time = in.readInt();
                String temp;
                byte[] host_name = new byte[64];
                temp = (null == reportingHost) ? "UNKNOWN" : reportingHost;
                System.arraycopy(temp.getBytes(), 0, host_name, 0, temp.getBytes().length);
                byte[] service_name = new byte[128];
                temp = (null == reportingService) ? "UNKNOWN" : reportingService;
                System.arraycopy(temp.getBytes(), 0, service_name, 0, temp.getBytes().length);
                byte[] plugin_output = new byte[512];
                message.replaceAll("\n", "<linefeed>");
                if ((null != message) && (message.getBytes().length <= 512)) {
                    System.arraycopy(message.getBytes(), 0, plugin_output, 0, message.getBytes().length);
                } else if (null != message) {
                    System.arraycopy(message.getBytes(), 0, plugin_output, 0, plugin_output.length);
                } else {
                    System.arraycopy("<null>".getBytes(), 0, plugin_output, 0, plugin_output.length);
                }
                int alert_size = 4 + 4 + 4 + 4 + host_name.length + service_name.length + plugin_output.length;
                byte[] alert = new byte[alert_size];
                alert[0] = (byte) ((nsca_version >> 8) & 0xff);
                alert[1] = (byte) (nsca_version & 0xff);
                alert[4] = (byte) ((0 >> 24) & 0xff);
                alert[5] = (byte) ((0 >> 16) & 0xff);
                alert[6] = (byte) ((0 >> 8) & 0xff);
                alert[7] = (byte) (0 & 0xff);
                alert[8] = (byte) ((time >> 24) & 0xff);
                alert[9] = (byte) ((time >> 16) & 0xff);
                alert[10] = (byte) ((time >> 8) & 0xff);
                alert[11] = (byte) (time & 0xff);
                alert[12] = (byte) ((return_code >> 8) & 0xff);
                alert[13] = (byte) (return_code & 0xff);
                int offset = 14;
                System.arraycopy(host_name, 0, alert, offset, host_name.length);
                offset += host_name.length;
                System.arraycopy(service_name, 0, alert, offset, service_name.length);
                offset += service_name.length;
                System.arraycopy(plugin_output, 0, alert, offset, plugin_output.length);
                offset += plugin_output.length;
                CRC32 crc = new CRC32();
                crc.update(alert);
                long crc_value = crc.getValue();
                alert[4] = (byte) ((crc_value >> 24) & 0xff);
                alert[5] = (byte) ((crc_value >> 16) & 0xff);
                alert[6] = (byte) ((crc_value >> 8) & 0xff);
                alert[7] = (byte) (crc_value & 0xff);
                encrypt_buffer(encryption_method, alert, received_iv);
                out.write(alert, 0, alert.length);
                out.flush();
                out.close();
                out = null;
                in.close();
                in = null;
                s.close();
                s = null;
                finished = true;
            } catch (Exception e) {
            } finally {
                if (null != out) {
                    try {
                        out.close();
                    } catch (Exception ee) {
                    }
                }
                if (null != out) {
                    try {
                        in.close();
                    } catch (Exception ee) {
                    }
                }
                if (null != s) {
                    try {
                        s.close();
                    } catch (Exception ee) {
                    }
                }
                synchronized (this) {
                    notifyAll();
                }
            }
        }
