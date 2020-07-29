    public String readReferenceText(final String ident) throws NoContentException {
        try {
            String name = getFilename(ident);
            URL url = new URL(FreqAnalysisPlugin.getDefault().getBundle().getEntry("/"), name);
            InputStream in = url.openStream();
            InputStreamReader isr = new InputStreamReader(in, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();
            String line = br.readLine();
            while (null != line) {
                buffer.append(line + "\n");
                line = br.readLine();
            }
            return buffer.toString();
        } catch (MalformedURLException muEx) {
            logError(muEx);
        } catch (UnsupportedEncodingException ueEx) {
            logError(ueEx);
        } catch (IOException ioEx) {
            logError(ioEx);
        }
        throw new NoContentException("Unable to find or read reference text.");
    }
