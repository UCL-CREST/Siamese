    private String convert(InputStream input, String encoding) throws Exception {
        Process p = Runtime.getRuntime().exec("tidy -q -f /dev/null -wrap 0 --output-xml yes --doctype omit --force-output true --new-empty-tags  " + emptyTags + " --quote-nbsp no -utf8");
        Thread t = new CopyThread(input, p.getOutputStream());
        t.start();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        IOUtils.copy(p.getInputStream(), output);
        p.waitFor();
        t.join();
        return output.toString();
    }
