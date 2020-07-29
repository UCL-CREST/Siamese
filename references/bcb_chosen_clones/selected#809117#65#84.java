        public SequenceIterator call(SequenceIterator[] arguments, XPathContext context) throws XPathException {
            try {
                String encodedString = ((StringValue) arguments[0].next()).getStringValue();
                byte[] decodedBytes = Base64.decode(encodedString);
                if (arguments.length > 1 && ((BooleanValue) arguments[1].next()).getBooleanValue()) {
                    ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);
                    GZIPInputStream zis = new GZIPInputStream(bis);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    IOUtils.copy(zis, baos);
                    decodedBytes = baos.toByteArray();
                }
                Document doc = XmlUtils.stringToDocument(new String(decodedBytes, "UTF-8"));
                Source source = new DOMSource(doc.getDocumentElement());
                XPathEvaluator evaluator = new XPathEvaluator(context.getConfiguration());
                NodeInfo[] infos = new NodeInfo[] { evaluator.setSource(source) };
                return new ArrayIterator(infos);
            } catch (Exception e) {
                throw new XPathException("Could not base64 decode string", e);
            }
        }
