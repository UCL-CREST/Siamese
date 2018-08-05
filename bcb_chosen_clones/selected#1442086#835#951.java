        private final int copyFiles(File[] list, String dest, boolean dest_is_full_name) throws InterruptedException {
            Context c = ctx;
            File file = null;
            for (int i = 0; i < list.length; i++) {
                boolean existed = false;
                FileChannel in = null;
                FileChannel out = null;
                File outFile = null;
                file = list[i];
                if (file == null) {
                    error(c.getString(R.string.unkn_err));
                    break;
                }
                String uri = file.getAbsolutePath();
                try {
                    if (isStopReq()) {
                        error(c.getString(R.string.canceled));
                        break;
                    }
                    long last_modified = file.lastModified();
                    String fn = file.getName();
                    outFile = dest_is_full_name ? new File(dest) : new File(dest, fn);
                    if (file.isDirectory()) {
                        if (depth++ > 40) {
                            error(ctx.getString(R.string.too_deep_hierarchy));
                            break;
                        } else if (outFile.exists() || outFile.mkdir()) {
                            copyFiles(file.listFiles(), outFile.getAbsolutePath(), false);
                            if (errMsg != null) break;
                        } else error(c.getString(R.string.cant_md, outFile.getAbsolutePath()));
                        depth--;
                    } else {
                        if (existed = outFile.exists()) {
                            int res = askOnFileExist(c.getString(R.string.file_exist, outFile.getAbsolutePath()), commander);
                            if (res == Commander.SKIP) continue;
                            if (res == Commander.REPLACE) {
                                if (outFile.equals(file)) continue; else outFile.delete();
                            }
                            if (res == Commander.ABORT) break;
                        }
                        if (move) {
                            long len = file.length();
                            if (file.renameTo(outFile)) {
                                counter++;
                                totalBytes += len;
                                int so_far = (int) (totalBytes * conv);
                                sendProgress(outFile.getName() + " " + c.getString(R.string.moved), so_far, 0);
                                continue;
                            }
                        }
                        in = new FileInputStream(file).getChannel();
                        out = new FileOutputStream(outFile).getChannel();
                        long size = in.size();
                        final long max_chunk = 524288;
                        long pos = 0;
                        long chunk = size > max_chunk ? max_chunk : size;
                        long t_chunk = 0;
                        long start_time = 0;
                        int speed = 0;
                        int so_far = (int) (totalBytes * conv);
                        String sz_s = Utils.getHumanSize(size);
                        String rep_s = c.getString(R.string.copying, fn);
                        for (pos = 0; pos < size; ) {
                            if (t_chunk == 0) start_time = System.currentTimeMillis();
                            sendProgress(rep_s + sizeOfsize(pos, sz_s), so_far, (int) (totalBytes * conv), speed);
                            long transferred = in.transferTo(pos, chunk, out);
                            pos += transferred;
                            t_chunk += transferred;
                            totalBytes += transferred;
                            if (isStopReq()) {
                                Log.d(TAG, "Interrupted!");
                                error(c.getString(R.string.canceled));
                                return counter;
                            }
                            long time_delta = System.currentTimeMillis() - start_time;
                            if (time_delta > 0) {
                                speed = (int) (1000 * t_chunk / time_delta);
                                t_chunk = 0;
                            }
                        }
                        in.close();
                        out.close();
                        in = null;
                        out = null;
                        if (i >= list.length - 1) sendProgress(c.getString(R.string.copied_f, fn) + sizeOfsize(pos, sz_s), (int) (totalBytes * conv));
                        counter++;
                    }
                    if (move) file.delete();
                    outFile.setLastModified(last_modified);
                    final int GINGERBREAD = 9;
                    if (android.os.Build.VERSION.SDK_INT >= GINGERBREAD) ForwardCompat.setFullPermissions(outFile);
                } catch (SecurityException e) {
                    error(c.getString(R.string.sec_err, e.getMessage()));
                } catch (FileNotFoundException e) {
                    error(c.getString(R.string.not_accs, e.getMessage()));
                } catch (ClosedByInterruptException e) {
                    error(c.getString(R.string.canceled));
                } catch (IOException e) {
                    String msg = e.getMessage();
                    error(c.getString(R.string.acc_err, uri, msg != null ? msg : ""));
                } catch (RuntimeException e) {
                    error(c.getString(R.string.rtexcept, uri, e.getMessage()));
                } finally {
                    try {
                        if (in != null) in.close();
                        if (out != null) out.close();
                        if (!move && errMsg != null && outFile != null && !existed) {
                            Log.i(TAG, "Deleting failed output file");
                            outFile.delete();
                        }
                    } catch (IOException e) {
                        error(c.getString(R.string.acc_err, uri, e.getMessage()));
                    }
                }
            }
            return counter;
        }
