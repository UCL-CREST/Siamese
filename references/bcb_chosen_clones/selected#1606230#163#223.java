        public void run() {
            root.setCursor(Cursor.getPredefinedCursor(3));
            try {
                tempArchiveFile = File.createTempFile("JZip", ".zip");
                if (table.getRowCount() != delRows.length) {
                    stream = new FileOutputStream(tempArchiveFile);
                    out = new ZipOutputStream(stream);
                    boolean aflag[] = new boolean[table.getRowCount()];
                    for (int i = 0; i < table.getRowCount(); i++) {
                        aflag[i] = false;
                    }
                    for (int j = 0; j < delRows.length; j++) {
                        aflag[delRows[j]] = true;
                    }
                    ZipFile zipfile = new ZipFile(archiveFile);
                    for (int k = 0; k < table.getRowCount(); k++) {
                        if (!aflag[k]) {
                            Object obj = ziptm.getRealValueAt(k, 6);
                            String s1 = ziptm.getRealValueAt(k, 0).toString();
                            String s2 = s1;
                            String s3 = "";
                            if (obj != null) {
                                String s4 = obj.toString();
                                s2 = s4 + s1;
                            }
                            ZipEntry zipentry = zipfile.getEntry(s2.replace(File.separatorChar, '/'));
                            if (zipentry == null) {
                                System.out.println("The entry with name<" + s2 + "> is null");
                            } else {
                                InputStream inputstream = zipfile.getInputStream(zipentry);
                                out.putNextEntry(zipentry);
                                do {
                                    int l = inputstream.read(buffer, 0, buffer.length);
                                    if (l <= 0) {
                                        break;
                                    }
                                    out.write(buffer, 0, l);
                                } while (true);
                                inputstream.close();
                                out.closeEntry();
                            }
                        }
                    }
                    zipfile.close();
                    out.close();
                    stream.close();
                }
                String s = archiveFile.getAbsolutePath();
                archiveFile.delete();
                tempArchiveFile.renameTo(new File(s));
                tempArchiveFile.getCanonicalPath();
                openArchive(new File(s));
                table.clearSelection();
                setStatus("Totally " + delRows.length + " file(s) deleted from this archive!");
            } catch (Exception exception) {
                exception.printStackTrace();
                setStatus("Error: " + exception.getMessage());
            }
            root.setCursor(Cursor.getPredefinedCursor(0));
            notifyAll();
        }
