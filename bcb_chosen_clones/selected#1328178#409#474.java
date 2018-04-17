        @Override
        public void run() {
            Init(null);
            File old_file = new File(zipFile.getAbsolutePath() + "_tmp_" + (new Date()).getSeconds() + ".zip");
            try {
                ZipFile zf = new ZipFile(zipFile);
                int removed = 0, processed = 0, num_entries = zf.size();
                long total_size = zipFile.length(), bytes_saved = 0;
                final String del = ctx.getString(R.string.deleting_a);
                if (!zipFile.renameTo(old_file)) {
                    error("could not rename the file " + zipFile.getAbsolutePath() + " to " + old_file.getAbsolutePath());
                } else {
                    ZipInputStream zin = new ZipInputStream(new FileInputStream(old_file));
                    ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
                    byte[] buf = new byte[BLOCK_SIZE];
                    ZipEntry entry = zin.getNextEntry();
                    while (entry != null) {
                        if (isStopReq()) break;
                        String name = entry.getName();
                        boolean spare_this = true;
                        for (ZipEntry z : mList) {
                            if (isStopReq()) break;
                            String name_to_delete = z.getName();
                            if (name.startsWith(name_to_delete)) {
                                spare_this = false;
                                removed++;
                                break;
                            }
                        }
                        if (spare_this) {
                            int pp = ++processed * 100 / num_entries;
                            out.putNextEntry(new ZipEntry(name));
                            int len;
                            while ((len = zin.read(buf)) > 0) {
                                if (isStopReq()) break;
                                out.write(buf, 0, len);
                                bytes_saved += len;
                                sendProgress(del, pp, (int) (bytes_saved * 100 / total_size));
                            }
                        }
                        entry = zin.getNextEntry();
                    }
                    zin.close();
                    try {
                        out.close();
                    } catch (Exception e) {
                        Log.e(TAG, "DelEngine.run()->out.close()", e);
                    }
                    if (isStopReq()) {
                        zipFile.delete();
                        old_file.renameTo(zipFile);
                        processed = 0;
                        error(s(R.string.interrupted));
                    } else {
                        old_file.delete();
                        zip = null;
                        sendResult(Utils.getOpReport(ctx, removed, R.string.deleted));
                        return;
                    }
                }
            } catch (Exception e) {
                error(e.getMessage());
            }
            sendResult(Utils.getOpReport(ctx, 0, R.string.deleted));
            super.run();
        }
