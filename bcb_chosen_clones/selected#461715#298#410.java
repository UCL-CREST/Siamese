    private void updateArchive(File f) {
        if (Exclude.matches(f)) return;
        try {
            checkSecurity(f);
        } catch (UpdateSecurityException e) {
            log.log(e.getMessage());
            return;
        }
        if (!f.exists()) {
            updateSimple(f);
            return;
        }
        log.log("Updating archive " + f.getAbsolutePath());
        File ftmp = new File(f.getAbsolutePath() + ".tmp");
        if (ftmp.exists()) {
            ftmp.delete();
        }
        boolean renamed = f.renameTo(ftmp);
        if (!renamed) throw new UpdateIOException("Aborting update. Unable to modify archive " + f.getAbsolutePath());
        TreeSet<String> changed = new TreeSet<String>();
        ZipInputStream in = new NullEofZipInputStream(jarInputStream);
        ZipOutputStream out;
        try {
            out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(f)));
            while (true) {
                ZipEntry e = in.getNextEntry();
                if (e == null) {
                    break;
                }
                changed.add(e.getName());
                log.log("    replacing " + e.getName());
                bentry.reset();
                int bb;
                while (true) {
                    bb = in.read();
                    if (bb < 0) {
                        break;
                    } else {
                        bentry.write(bb);
                    }
                }
                bentry.close();
                e.setSize(bentry.size());
                out.putNextEntry(e);
                bentry.writeTo(out);
                out.closeEntry();
            }
            log.log("Putting the rest of contents");
            TreeSet<String> enew = new TreeSet<String>();
            enew.addAll(changed);
            Iterator<String> iter = deleteThem.iterator();
            String item = f.getAbsolutePath();
            String this_archive = item.substring(applicationDirectoryName.length());
            TreeSet<String> deleted = new TreeSet<String>();
            while (iter.hasNext()) {
                item = iter.next();
                int p = item.indexOf('!');
                if (p > 0) {
                    String archive = item.substring(0, p);
                    if (archive.equals(this_archive)) {
                        String path = item.substring(p + 1);
                        deleted.add(path);
                        iter.remove();
                    }
                }
            }
            in = new NullEofZipInputStream(new BufferedInputStream(new FileInputStream(ftmp)));
            ZipEntry be = new ZipEntry(f.getAbsolutePath().substring(applicationDirectoryName.length()));
            ZipOutputStream bos = null;
            while (true) {
                ZipEntry e = in.getNextEntry();
                if (e == null) {
                    break;
                }
                String ename = e.getName();
                enew.remove(ename);
                if (!changed.contains(ename) && !deleted.contains(ename)) {
                    store(in, out, e);
                } else {
                    if (!deleted.contains(ename)) {
                        log.log("    discarding replaced " + ename);
                    } else {
                        log.log("    deleting " + ename);
                    }
                    if (createUninstaller) {
                        if (bos == null) {
                            bos = new ZipOutputStream(uninstallStream);
                            uninstallStream.putNextEntry(be);
                        }
                        store(in, bos, e);
                    }
                }
            }
            in.close();
            out.flush();
            out.close();
            if (bos != null) {
                assert createUninstaller;
                bos.flush();
                bos.close();
                uninstallStream.closeEntry();
            }
            log.log("Deleting " + ftmp.getAbsolutePath());
            ftmp.deleteOnExit();
            iter = enew.iterator();
            while (iter.hasNext()) {
                item = this_archive + "!" + iter.next();
                newEntries.add(item);
            }
        } catch (IOException e1) {
            throw new UpdateIOException("Error updating archive " + f.getAbsolutePath(), e1);
        }
    }
