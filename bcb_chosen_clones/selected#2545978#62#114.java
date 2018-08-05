    public String read(String sourceUri) throws IOException {
        LOGGER.finer("Reading metadata of source URI: \"" + sourceUri + "\" through proxy: " + this);
        try {
            HarvestMetadataRequest harvestMetadataRequest = new HarvestMetadataRequest();
            harvestMetadataRequest.setService(info.newService());
            harvestMetadataRequest.setCredentials(info.newCredentials());
            harvestMetadataRequest.executeHarvest(sourceUri);
            String md = harvestMetadataRequest.getMetadata();
            if (md.length() == 0) {
                LOGGER.finer("Reading metadata of source URI: \"" + sourceUri + "\" through proxy: " + this + "; Received no METADATA response.");
                return "";
            }
            Pattern pattern = Pattern.compile("\"[^\"]+\"");
            Matcher matcher = pattern.matcher(md);
            int start = 0;
            ArrayList<int[]> sections = new ArrayList<int[]>();
            while (matcher.find(start)) {
                int[] section = new int[] { matcher.start() + 1, matcher.end() - 1 };
                sections.add(section);
                start = matcher.end() + 1;
            }
            for (int i = sections.size() - 1; i >= 0; i--) {
                int[] section = sections.get(i);
                String text = md.substring(section[0], section[1]);
                text = Val.escapeXml(text);
                md = md.substring(0, section[0]) + text + md.substring(section[1], md.length());
            }
            Document doc = DomUtil.makeDomFromString(md, false);
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();
            String url = (String) xPath.evaluate("/METADATA/METADATA_DATASET/@url", doc, XPathConstants.STRING);
            LOGGER.finer("Reading metadata of id: \"" + sourceUri + "\" through proxy: " + this + "; Received no metadata URL within METADATA response.");
            if (url.length() == 0) return "";
            url = Val.chkStr(url).replaceAll("\\{", "%7B").replaceAll("\\}", "%7D");
            HttpClientRequest cr = new HttpClientRequest();
            cr.setUrl(url);
            StringHandler sh = new StringHandler();
            cr.setContentHandler(sh);
            cr.execute();
            String mdText = sh.getContent();
            LOGGER.finer("Received metadata of id: \"" + sourceUri + "\" through proxy: " + this);
            LOGGER.finest(mdText);
            return mdText;
        } catch (XPathExpressionException ex) {
            throw new IOException("Error accessing metadata. Cause: " + ex.getMessage());
        } catch (SAXException ex) {
            throw new IOException("Error accessing metadata. Cause: " + ex.getMessage());
        } catch (ParserConfigurationException ex) {
            throw new IOException("Error accessing metadata. Cause: " + ex.getMessage());
        } catch (ImsServiceException ex) {
            throw new IOException("Error accessing metadata. Cause: " + ex.getMessage());
        }
    }
