        public Void doInBackground() {
            setProgress(0);
            for (int i = 0; i < uploadFiles.size(); i++) {
                String filePath = uploadFiles.elementAt(i).getFilePath();
                String fileName = uploadFiles.elementAt(i).getFileName();
                String fileMsg = "Uploading file " + (i + 1) + "/" + uploadFiles.size() + "\n";
                this.publish(fileMsg);
                try {
                    File inFile = new File(filePath);
                    FileInputStream in = new FileInputStream(inFile);
                    byte[] inBytes = new byte[(int) chunkSize];
                    int count = 1;
                    int maxCount = (int) (inFile.length() / chunkSize);
                    if (inFile.length() % chunkSize > 0) {
                        maxCount++;
                    }
                    int readCount = 0;
                    readCount = in.read(inBytes);
                    while (readCount > 0) {
                        File splitFile = File.createTempFile("upl", null, null);
                        String splitName = splitFile.getPath();
                        FileOutputStream out = new FileOutputStream(splitFile);
                        out.write(inBytes, 0, readCount);
                        out.close();
                        boolean chunkFinal = (count == maxCount);
                        fileMsg = " - Sending chunk " + count + "/" + maxCount + ": ";
                        this.publish(fileMsg);
                        boolean uploadSuccess = false;
                        int uploadTries = 0;
                        while (!uploadSuccess && uploadTries <= 5) {
                            uploadTries++;
                            boolean uploadStatus = upload(splitName, fileName, count, chunkFinal);
                            if (uploadStatus) {
                                fileMsg = "OK\n";
                                this.publish(fileMsg);
                                uploadSuccess = true;
                            } else {
                                fileMsg = "ERROR\n";
                                this.publish(fileMsg);
                                uploadSuccess = false;
                            }
                        }
                        if (!uploadSuccess) {
                            fileMsg = "There was an error uploading your files. Please let the pipeline administrator know about this problem. Cut and paste the messages in this box, and supply them.\n";
                            this.publish(fileMsg);
                            errorFlag = true;
                            return null;
                        }
                        float thisProgress = (count * 100) / (maxCount);
                        float completeProgress = (i * (100 / uploadFiles.size()));
                        float totalProgress = completeProgress + (thisProgress / uploadFiles.size());
                        setProgress((int) totalProgress);
                        splitFile.delete();
                        readCount = in.read(inBytes);
                        count++;
                    }
                } catch (Exception e) {
                    this.publish(e.toString());
                }
            }
            return null;
        }
