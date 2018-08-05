        public void run() {
            ArrayList<Thread> ftpThreadList = new ArrayList<Thread>();
            class ftpLinkElem extends Thread {

                TreeNode node;

                FTPClient ftp;

                protected ftpLinkElem(TreeNode node, FTPClient ftpC) {
                    super();
                    this.node = node;
                    this.ftp = ftpC;
                }

                public void run() {
                    try {
                        String fileLoc = getFileLocation(node.getURL());
                        FTPFile[] fTest = ftp.listFiles(fileLoc);
                        if (fTest[0].isFile()) {
                            int rsp = ftp.getReplyCode();
                            String msg = ftp.getReplyString();
                            if (!FTPReply.isPositiveCompletion(rsp)) {
                                ftp.disconnect();
                                System.out.print("[FAILED] " + msg);
                                failedList.add(node);
                                failCnt++;
                            }
                            System.out.println("FTP Response: " + msg);
                        } else {
                            System.out.print("[FAILED]");
                            failedList.add(node);
                            failCnt++;
                        }
                    } catch (IOException ie) {
                        ie.printStackTrace();
                    }
                }
            }
            updateCurStatus("Checking ftp links", "");
            for (TreeNode testFtp : ftpURLs) {
                try {
                    FTPClient ftp = new FTPClient();
                    ftp.connect(FTPSERVER);
                    ftp.login(FTPUSER, FTPPW);
                    updateCurStatus(testFtp.getURL(), "");
                    ftpLinkElem fe = new ftpLinkElem(testFtp, ftp);
                    fe.start();
                    ftpThreadList.add(fe);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for (Thread t : ftpThreadList) {
                try {
                    if (t.isAlive()) {
                        t.join();
                    }
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
