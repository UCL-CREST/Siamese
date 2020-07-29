    private void uploadFileToWebSite(String siteDir, String channelAsciiName, Map synFileList) throws Exception {
        if (siteDir == null) {
            siteDir = "";
        }
        log.debug("uploadFileToWebSite begin! siteDir:= " + siteDir + "  currDate:= " + new Date().toString());
        siteDir = new File(siteDir).getPath() + File.separator;
        FTPClient client = new FTPClient();
        try {
            for (int i = 0; i < 3; i++) {
                try {
                    client.connect(ftpServerIp, ftpPort);
                    break;
                } catch (IOException ex2) {
                    if (i == 2) {
                        log.error("ftp����������ʧ��,�Ѿ�����3��!", ex2);
                        throw new IOException("ftp����������ʧ��,�Ѿ�����3��!" + ex2.toString());
                    }
                }
            }
            for (int i = 0; i < 3; i++) {
                try {
                    client.login(ftpLoginUser, ftpPassword);
                    break;
                } catch (IOException ex3) {
                    if (i == 2) {
                        log.error("��¼ftp������ʧ��,�Ѿ�����3��!", ex3);
                        throw new IOException("��¼ftp������ʧ��,�Ѿ�����3��!" + ex3.toString());
                    }
                }
            }
            log.debug("Ftp login is over !");
            client.syst();
            String ftpWD = client.printWorkingDirectory();
            log.debug("client.initiateListParsing() is over !");
            client.setFileType(FTPClient.BINARY_FILE_TYPE);
            client.enterLocalPassiveMode();
            Iterator iterator = synFileList.keySet().iterator();
            ArrayList alKey = new ArrayList();
            while (iterator.hasNext()) {
                alKey.add((String) iterator.next());
            }
            log.debug("FTP Files size:= " + alKey.size());
            String basePath = ftpRootPath + (channelAsciiName == null || channelAsciiName.trim().equals("") ? "" : File.separator + channelAsciiName);
            log.debug("localRootPath:= " + localRootPath + " basePath:= " + basePath);
            String path;
            boolean isSuc;
            String sFileSep = File.separator;
            String sRep = "";
            if (basePath.startsWith("/")) {
                sFileSep = "/";
                sRep = "\\";
            } else if (basePath.startsWith("\\")) {
                sFileSep = "\\";
                sRep = "/";
            }
            if (!"".equals(sRep)) {
                basePath = StringUtil.replaceAll(basePath, sRep, sFileSep);
                while (basePath.startsWith(sFileSep)) basePath = basePath.substring(1);
            }
            for (int j = 0; j < alKey.size(); j++) {
                String key = (String) alKey.get(j);
                File file = new File(siteDir + key);
                String filePath = file.getParent();
                String fileName = file.getName();
                if (fileName == null || filePath == null || !file.exists() || filePath.length() < localRootPath.length()) {
                    continue;
                }
                filePath = filePath.substring(localRootPath.length());
                FileInputStream fis = null;
                String temp1;
                ArrayList alTemp;
                int iInd;
                try {
                    path = basePath + (filePath == null || filePath.trim().equals("") || filePath.equals(File.separator) ? "" : File.separator + filePath);
                    if (!"".equals(sRep)) {
                        path = StringUtil.replaceAll(path, sRep, sFileSep);
                    }
                    if (!client.changeWorkingDirectory(path)) {
                        isSuc = client.makeDirectory(path);
                        if (isSuc) {
                            log.debug(" **** makeDirectory1(" + path + "): " + isSuc);
                        } else {
                            temp1 = path;
                            alTemp = new ArrayList();
                            iInd = temp1.lastIndexOf(sFileSep);
                            alTemp.add(temp1.substring(iInd));
                            temp1 = temp1.substring(0, iInd);
                            isSuc = client.makeDirectory(temp1);
                            if (isSuc) {
                                log.debug(" **** makeDirectory2(" + temp1 + "): " + isSuc);
                            }
                            while (!"".equals(temp1) && !isSuc) {
                                iInd = temp1.lastIndexOf(sFileSep);
                                alTemp.add(temp1.substring(iInd));
                                temp1 = temp1.substring(0, iInd);
                                isSuc = client.makeDirectory(temp1);
                                if (isSuc) {
                                    log.debug(" **** makeDirectory3(" + temp1 + "): " + isSuc);
                                }
                            }
                            for (int i = alTemp.size(); i > 0; i--) {
                                temp1 += alTemp.get(i - 1);
                                isSuc = client.makeDirectory(temp1);
                                log.debug(" **** makeDirectory4(" + temp1 + "): " + isSuc);
                            }
                        }
                        client.changeWorkingDirectory(path);
                    }
                    fis = new FileInputStream(file);
                    client.storeFile(fileName, fis);
                    client.changeWorkingDirectory(ftpWD);
                } catch (Throwable ex1) {
                    log.error("ͬ���ļ�����:������ļ�Ϊ:" + file.getPath());
                    ex1.printStackTrace();
                } finally {
                    try {
                        fis.close();
                    } catch (RuntimeException e1) {
                        log.error("close()����!");
                        e1.printStackTrace();
                    }
                    file = null;
                }
            }
        } catch (Throwable ex) {
            log.error("ͬ��ʧ��--1202!", ex);
            ex.printStackTrace();
        } finally {
            if (client != null && client.isConnected()) {
                client.disconnect();
            }
        }
    }
