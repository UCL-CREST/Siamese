    public void run() {
        try {
            {
                ThreadUpdateProgressableGUI threadUpdateProgressableGUI = new ThreadUpdateProgressableGUI(progressableGUI);
                threadUpdateProgressableGUI.maximumProgress = inFile.length();
                threadUpdateProgressableGUI.start();
                this.checksums = new ArrayList<Long>();
                BufferedOutputStream bufferOut = new BufferedOutputStream(new CheckedOutputStreamSplited(inFile, outputFolder, segmentSize, checksums), IOutils.BUFFER_SIZE);
                ZipOutputStream out;
                if (password != null) out = new ZipOutputStream(new CryptoOutputStream(bufferOut, password)); else out = new ZipOutputStream(bufferOut);
                out.putNextEntry(new ZipEntry(inFile.getName()));
                InputStream in = new BufferedInputStream(new FileInputStream(inFile), IOutils.BUFFER_SIZE);
                byte[] buf = new byte[IOutils.BUFFER_SIZE];
                int len;
                while ((len = in.read(buf)) > 0 && keepAlive) {
                    out.write(buf, 0, len);
                    threadUpdateProgressableGUI.currentProgress += len;
                }
                in.close();
                threadUpdateProgressableGUI.keepAlive = false;
                out.finish();
                out.close();
                MainSystray.guiFactory.getMessageDisplayer().displayInfoMessage(Messages.message.getString("splitMerge.alert.splitCompleted"));
            }
            Properties properties = new Properties();
            properties.setProperty("segment.number", String.valueOf(checksums.size()));
            properties.setProperty("version", "3");
            for (int i = 0; i < checksums.size(); ) {
                Long adler32 = (Long) checksums.get(i++);
                properties.setProperty("segment." + i + ".adler32", adler32.toString());
            }
            if (password == null) properties.storeToXML(new FileOutputStream(new File(outputFolder, inFile.getName() + ".open")), "OpenP2M Splited File Descriptor (compatible with 7-zip decompressor)", "UTF-8"); else properties.storeToXML(new FileOutputStream(new File(outputFolder, inFile.getName() + ".open")), "OpenP2M Splited File Descriptor (NOT compatible with 7-zip decompressor, different encryptation)", "UTF-8");
        } catch (IOException ex) {
            MainSystray.guiFactory.getMessageDisplayer().showException(ex);
        } catch (Exception ex) {
            MainSystray.guiFactory.getMessageDisplayer().showException(ex);
        }
        progressableGUI.setButtonsStatus(true);
    }
