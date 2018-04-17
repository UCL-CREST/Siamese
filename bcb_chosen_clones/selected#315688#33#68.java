    public void run() {
        ServerSocket sock = null;
        OutputStream os = null;
        try {
            sock = new ServerSocket();
            sock.bind(new InetSocketAddress(port));
            sock.setSoTimeout(500);
            sock.setPerformancePreferences(1, 1, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (RemoteServer.isRunning()) {
            Socket client = null;
            try {
                client = sock.accept();
                os = client.getOutputStream();
            } catch (SocketTimeoutException se) {
                continue;
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                while (RemoteServer.isRunning()) {
                    BufferedImage image = robot.createScreenCapture(size);
                    ImageIO.write(image, "jpeg", os);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
