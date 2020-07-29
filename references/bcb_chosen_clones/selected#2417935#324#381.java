    private static void ftpTest() {
        FTPClient f = new FTPClient();
        try {
            f.connect("oscomak.net");
            System.out.print(f.getReplyString());
            f.setFileType(FTPClient.BINARY_FILE_TYPE);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String password = JOptionPane.showInputDialog("Enter password");
        if (password == null || password.equals("")) {
            System.out.println("No password");
            return;
        }
        try {
            f.login("oscomak_pointrel", password);
            System.out.print(f.getReplyString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String workingDirectory = f.printWorkingDirectory();
            System.out.println("Working directory: " + workingDirectory);
            System.out.print(f.getReplyString());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            f.enterLocalPassiveMode();
            System.out.print(f.getReplyString());
            System.out.println("Trying to list files");
            String[] fileNames = f.listNames();
            System.out.print(f.getReplyString());
            System.out.println("Got file list fileNames: " + fileNames.length);
            for (String fileName : fileNames) {
                System.out.println("File: " + fileName);
            }
            System.out.println();
            System.out.println("done reading stream");
            System.out.println("trying alterative way to read stream");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            f.retrieveFile(fileNames[0], outputStream);
            System.out.println("size: " + outputStream.size());
            System.out.println(outputStream.toString());
            System.out.println("done with alternative");
            System.out.println("Trying to store file back");
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            boolean storeResult = f.storeFile("test.txt", inputStream);
            System.out.println("Done storing " + storeResult);
            f.disconnect();
            System.out.print(f.getReplyString());
            System.out.println("disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
