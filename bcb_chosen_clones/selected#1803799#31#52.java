    public String fetch(final String address) throws EncoderException {
        final String escapedAddress = new URLCodec().encode(address);
        final String requestUrl = GeoCodeFetch.urlXmlPath + "&" + "address=" + escapedAddress;
        this.log.debug("requestUrl: {}", requestUrl);
        try {
            final StringBuffer sb = new StringBuffer();
            final URL url = new URL(requestUrl);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                this.log.debug("line: {}", line);
                sb.append(line);
            }
            reader.close();
            return (sb.toString());
        } catch (final MalformedURLException ex) {
            this.log.error(ExceptionUtils.getStackTrace(ex));
        } catch (final IOException ex) {
            this.log.error(ExceptionUtils.getStackTrace(ex));
        }
        return ("");
    }
