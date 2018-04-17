    public static void zip(String path, String source, String target, ZipOutputContainer outc, LongNum lm, long ml, StringBuffer msg) throws IOException {
        File f = new File(source);
        String[] flist = f.list();
        String zfn = null;
        if (flist == null || flist.length < 1) return;
        File ft = new File(target);
        ZipEntry ze = null;
        String p = null;
        File nf = null;
        InputStream b = null;
        ZipOutputStream out = null;
        try {
            for (int i = 0; i < flist.length; i++) {
                p = f.getAbsolutePath();
                if (!p.endsWith("" + File.separatorChar)) p = p + File.separatorChar;
                nf = new File(p + flist[i]);
                if (nf.isDirectory()) {
                    out = outc.getOut();
                    zfn = nf.toString();
                    zfn = zfn.substring(path.length(), zfn.length());
                    if (zfn.startsWith("/") || zfn.startsWith("\\")) zfn = zfn.substring(1, zfn.length());
                    if (!zfn.endsWith("/")) zfn = zfn + "/";
                    ze = new ZipEntry(zfn);
                    ze.setTime(nf.lastModified());
                    out.putNextEntry(ze);
                    out.closeEntry();
                    zip(path, p + flist[i], target, outc, lm, ml, msg);
                } else {
                    if (flist[i].equals("core") && nf.length() > 1000000) {
                        nf.delete();
                        tools.util.LogMgr.debug("******* Deleted existing core from context dir: " + nf);
                        msg.append("******* Deleted existing core from context dir: " + nf + "\r\n");
                    } else {
                        if (!ft.getParent().equals(nf.getParent())) {
                            zfn = nf.toString();
                            zfn = zfn.substring(path.length(), zfn.length());
                            if (zfn.startsWith("/") || zfn.startsWith("\\")) zfn = zfn.substring(1, zfn.length());
                            ze = new ZipEntry(zfn.replace('\\', '/'));
                            ze.setTime(nf.lastModified());
                            try {
                                try {
                                    b = new FileInputStream(nf);
                                } catch (FileNotFoundException fe) {
                                    tools.util.LogMgr.err(nf + " FileUtil.zip " + fe.toString());
                                    b = null;
                                    msg.append("Unable to Archive the Following File " + nf + "\r\n");
                                }
                                out = outc.getOut();
                                if (ml > 0 && lm.value > ml) {
                                    out.close();
                                    File ffd = new File(ft.getParent());
                                    int ct = ffd.list().length + 1;
                                    target = target.substring(0, target.length() - 4);
                                    target = target + ct + ".zip";
                                    out = new ZipOutputStream(new FileOutputStream(new File(target)));
                                    outc.setOut(out);
                                    lm.value = 0;
                                }
                                if (b != null) {
                                    lm.increment(nf.length());
                                    out.putNextEntry(ze);
                                    StreamUtil.write(b, out);
                                }
                            } finally {
                                if (b != null) b.close();
                            }
                            out.closeEntry();
                        }
                    }
                }
            }
        } catch (Throwable e) {
            if (out != null) try {
                out.close();
            } catch (Exception io) {
            }
        }
    }
