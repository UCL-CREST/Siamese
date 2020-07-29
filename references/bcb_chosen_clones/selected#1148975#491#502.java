    private void copy(File in, File out) {
        log.info("Copying yam file from: " + in.getName() + " to: " + out.getName());
        try {
            FileChannel ic = new FileInputStream(in).getChannel();
            FileChannel oc = new FileOutputStream(out).getChannel();
            ic.transferTo(0, ic.size(), oc);
            ic.close();
            oc.close();
        } catch (IOException ioe) {
            fail("Failed testing while copying modified file: " + ioe.getMessage());
        }
    }
