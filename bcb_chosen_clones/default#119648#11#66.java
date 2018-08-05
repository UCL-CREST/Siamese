    public static void main(String args[]) {
        DatagramSocket socket = null;
        InetAddress addr = null;
        DatagramPacket in = null;
        String queryList;
        if (args.length == 2) {
            DatagramPacket out;
            byte[] data = null;
            try {
                addr = InetAddress.getByName(args[0]);
                queryList = args[1];
                String line;
                StringTokenizer token = null;
                ReceiveThread recvThread = null;
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                DataOutputStream outputStream = new DataOutputStream(buffer);
                FileInputStream inputFile = new FileInputStream(queryList);
                BufferedReader input = new BufferedReader(new InputStreamReader(inputFile));
                long startTime = System.currentTimeMillis();
                long endTime = System.currentTimeMillis();
                while ((line = input.readLine()) != null && !line.equals("")) {
                    socket = new DatagramSocket();
                    token = new StringTokenizer(line, "_");
                    outputStream.writeInt((int) Math.round(Math.random() * 42949.67295 * 100000));
                    int opt = (new Integer((token.nextToken()).substring(2))).intValue();
                    outputStream.writeByte(opt);
                    if (opt != 17) {
                        int key = (new Integer(token.nextToken())).intValue();
                        outputStream.writeInt(key);
                    }
                    if ((opt == 1) || (opt == 3) || (opt == 6) || (opt == 8)) {
                        data = (token.nextToken()).getBytes();
                        outputStream.writeInt(data.length);
                        outputStream.write(data, 0, data.length);
                    }
                    byte[] query = buffer.toByteArray();
                    out = new DatagramPacket(query, query.length, addr, port);
                    socket.send(out);
                    recvThread = new ReceiveThread(socket, addr);
                    recvThread.start();
                    recvThread.join();
                    buffer.reset();
                    endTime = System.currentTimeMillis();
                    System.out.println(endTime - startTime);
                }
                System.out.println("\n" + (endTime - startTime));
                outputStream.close();
            } catch (IOException e) {
                System.err.println("\nCouldn't get the connection.");
                System.exit(1);
            } catch (InterruptedException e) {
                System.err.println("\nThread error.");
                System.exit(1);
            }
        }
    }
