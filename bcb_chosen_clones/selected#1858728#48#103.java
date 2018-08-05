    public void run() {
        String command;
        while (true) {
            synchronized (cmd) {
                if (cmd.isEmpty()) {
                    try {
                        cmd.wait();
                    } catch (InterruptedException e) {
                    }
                }
                command = (String) cmd.firstElement();
                cmd.removeElementAt(0);
            }
            if (command.equals("cftd")) {
                wlPanel panel = (wlPanel) cmd.firstElement();
                cmd.removeElementAt(0);
                try {
                    Transfer transfer = (Transfer) cmd.firstElement();
                    cmd.removeElementAt(0);
                    MyFile file = transfer.getSource();
                    MyFile to = transfer.getDest();
                    if (file.isDirectory()) {
                        new File(to.getAbsolutePath()).mkdir();
                        frame.getQueueList().removeFirst();
                        frame.getQueueList().updateView();
                        MyFile[] files = file.list();
                        for (int i = files.length - 1; i >= 0; i--) {
                            MyFile tmp = new MyFile(files[i].getName());
                            tmp.setFtpMode(false);
                            tmp.setAbsolutePath(to.getAbsolutePath() + File.separator + files[i].getName());
                            frame.getQueueList().addAtBegin(new Transfer(files[i], tmp, transfer.modeFrom, transfer.modeTo, transfer.from_to, null, null));
                        }
                    } else {
                        copyFileToDir(file, to, panel);
                        frame.getQueueList().removeFirst();
                        frame.getQueueList().updateView();
                    }
                } catch (IOException e) {
                    System.out.println(e.toString());
                }
                while (!wlFxp.getTm().waiting) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                    }
                }
                synchronized (wlFxp.getTm().done) {
                    wlFxp.getTm().done.notify();
                }
            }
            try {
                sleep(50);
            } catch (InterruptedException e) {
            }
        }
    }
