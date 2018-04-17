        public void run() {
            root.setCursor(Cursor.getPredefinedCursor(3));
            try {
                Thread.sleep(30L);
                tempArchiveFile = File.createTempFile("JZip", ".zip");
                stream = new FileOutputStream(tempArchiveFile);
                out = new ZipOutputStream(stream);
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory() && retrieveSubCheckBox.isSelected()) {
                        addAllDir(files[i]);
                    } else {
                        addOneFile(files[i]);
                    }
                }
                if (archiveFile.length() != 0L) {
                    ZipFile zipfile = new ZipFile(archiveFile);
                    Enumeration enumeration = zipfile.entries();
                    while (enumeration.hasMoreElements()) {
                        ZipEntry zipentry = (ZipEntry) enumeration.nextElement();
                        ZipEntry zipentry1 = new ZipEntry(zipentry.getName());
                        zipentry1.setTime(zipentry.getTime());
                        zipentry1.setComment(zipentry.getComment());
                        zipentry1.setExtra(zipentry.getExtra());
                        try {
                            out.putNextEntry(zipentry1);
                        } catch (Exception _ex) {
                            continue;
                        }
                        InputStream inputstream = zipfile.getInputStream(zipentry);
                        do {
                            int j = inputstream.read(buffer, 0, buffer.length);
                            if (j <= 0) {
                                break;
                            }
                            out.write(buffer, 0, j);
                        } while (true);
                        inputstream.close();
                    }
                    zipfile.close();
                }
                out.close();
                stream.close();
                String s = archiveFile.getAbsolutePath();
                archiveFile.delete();
                tempArchiveFile.renameTo(new File(s));
                tempArchiveFile.getCanonicalPath();
                openArchive(currentArchive);
                setStatus("Totally " + filesAdded + " file(s) added into archive " + archiveFile);
            } catch (Exception exception) {
                exception.printStackTrace();
                setStatus("Error: " + exception.getMessage());
            }
            root.setCursor(Cursor.getPredefinedCursor(0));
        }
