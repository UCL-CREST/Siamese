    public void run() {
        try {
            Socket socket = getSocket();
            System.out.println("opening socket to " + address + " on " + port);
            InputStream in = socket.getInputStream();
            for (; ; ) {
                FileTransferHeader header = FileTransferHeader.readHeader(in);
                if (header == null) break;
                System.out.println("header: " + header);
                String[] parts = header.getFilename().getSegments();
                String filename;
                if (parts.length > 0) filename = "dl-" + parts[parts.length - 1]; else filename = "dl-" + session.getScreenname();
                System.out.println("writing to file " + filename);
                long sum = 0;
                if (new File(filename).exists()) {
                    FileInputStream fis = new FileInputStream(filename);
                    byte[] block = new byte[10];
                    for (int i = 0; i < block.length; ) {
                        int count = fis.read(block);
                        if (count == -1) break;
                        i += count;
                    }
                    FileTransferChecksum summer = new FileTransferChecksum();
                    summer.update(block, 0, 10);
                    sum = summer.getValue();
                }
                FileChannel fileChannel = new FileOutputStream(filename).getChannel();
                FileTransferHeader outHeader = new FileTransferHeader(header);
                outHeader.setHeaderType(FileTransferHeader.HEADERTYPE_ACK);
                outHeader.setIcbmMessageId(cookie);
                outHeader.setBytesReceived(0);
                outHeader.setReceivedChecksum(sum);
                OutputStream socketOut = socket.getOutputStream();
                System.out.println("sending header: " + outHeader);
                outHeader.write(socketOut);
                for (int i = 0; i < header.getFileSize(); ) {
                    long transferred = fileChannel.transferFrom(Channels.newChannel(in), 0, header.getFileSize() - i);
                    System.out.println("transferred " + transferred);
                    if (transferred == -1) return;
                    i += transferred;
                }
                System.out.println("finished transfer!");
                fileChannel.close();
                FileTransferHeader doneHeader = new FileTransferHeader(header);
                doneHeader.setHeaderType(FileTransferHeader.HEADERTYPE_RECEIVED);
                doneHeader.setFlags(doneHeader.getFlags() | FileTransferHeader.FLAG_DONE);
                doneHeader.setBytesReceived(doneHeader.getBytesReceived() + 1);
                doneHeader.setIcbmMessageId(cookie);
                doneHeader.setFilesLeft(doneHeader.getFilesLeft() - 1);
                doneHeader.write(socketOut);
                if (doneHeader.getFilesLeft() - 1 <= 0) {
                    socket.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
