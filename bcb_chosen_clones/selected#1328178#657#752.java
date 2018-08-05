        private final int addFilesToZip(ArrayList<File> files) throws IOException {
            File old_file = null;
            try {
                byte[] buf = new byte[BLOCK_SIZE];
                ZipOutputStream out;
                if (newZip) {
                    out = new ZipOutputStream(new FileOutputStream(zipFile));
                } else {
                    ZipFile zf = new ZipFile(zipFile);
                    int num_entries = zf.size();
                    long total_size = zipFile.length(), bytes_saved = 0;
                    old_file = new File(zipFile.getAbsolutePath() + "_tmp_" + (new Date()).getSeconds() + ".zip");
                    if (!zipFile.renameTo(old_file)) throw new RuntimeException("could not rename the file " + zipFile.getAbsolutePath() + " to " + old_file.getAbsolutePath());
                    ZipInputStream zin = new ZipInputStream(new FileInputStream(old_file));
                    out = new ZipOutputStream(new FileOutputStream(zipFile));
                    int e_i = 0, pp;
                    ZipEntry entry = zin.getNextEntry();
                    while (entry != null) {
                        if (isStopReq()) break;
                        pp = e_i++ * 100 / num_entries;
                        sendProgress(prep, pp, 0);
                        String name = entry.getName();
                        boolean notInFiles = true;
                        for (File f : files) {
                            if (isStopReq()) break;
                            String f_path = f.getAbsolutePath();
                            if (f_path.regionMatches(true, basePathLen, name, 0, name.length())) {
                                notInFiles = false;
                                break;
                            }
                        }
                        if (notInFiles) {
                            out.putNextEntry(new ZipEntry(name));
                            int len;
                            while ((len = zin.read(buf)) > 0) {
                                if (isStopReq()) break;
                                out.write(buf, 0, len);
                                bytes_saved += len;
                                sendProgress(prep, pp, (int) (bytes_saved * 100 / total_size));
                            }
                        }
                        entry = zin.getNextEntry();
                    }
                    zin.close();
                    if (isStopReq()) {
                        out.close();
                        zipFile.delete();
                        old_file.renameTo(zipFile);
                        return 0;
                    }
                }
                double conv = 100. / (double) totalSize;
                long byte_count = 0;
                int i;
                for (i = 0; i < files.size(); i++) {
                    if (isStopReq()) break;
                    File f = files.get(i);
                    String fn = f.getAbsolutePath();
                    String rfn = destPath + fn.substring(basePathLen);
                    if (f.isDirectory()) {
                        out.putNextEntry(new ZipEntry(rfn + SLS));
                    } else {
                        out.putNextEntry(new ZipEntry(rfn));
                        String pack_s = ctx.getString(R.string.packing, fn);
                        InputStream in = new FileInputStream(f);
                        int len;
                        int so_far = (int) (byte_count * conv);
                        while ((len = in.read(buf)) > 0) {
                            if (isStopReq()) break;
                            out.write(buf, 0, len);
                            byte_count += len;
                            sendProgress(pack_s, so_far, (int) (byte_count * conv));
                        }
                        in.close();
                    }
                    out.closeEntry();
                    if (move) f.delete();
                }
                out.close();
                if (isStopReq()) {
                    zipFile.delete();
                    if (!newZip) old_file.renameTo(zipFile);
                    return 0;
                }
                if (!newZip) old_file.delete();
                return i;
            } catch (Exception e) {
                error(e.getMessage());
                e.printStackTrace();
                if (!newZip) {
                    zipFile.delete();
                    if (!newZip && old_file != null) old_file.renameTo(zipFile);
                }
                return 0;
            }
        }
