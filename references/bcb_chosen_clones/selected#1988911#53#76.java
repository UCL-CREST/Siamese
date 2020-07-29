    public void testQueryForBinary() throws InvalidNodeTypeDefException, ParseException, Exception {
        JCRNodeSource source = (JCRNodeSource) resolveSource(BASE_URL + "images/photo.png");
        assertNotNull(source);
        assertEquals(false, source.exists());
        OutputStream os = source.getOutputStream();
        assertNotNull(os);
        String content = "foo is a bar";
        os.write(content.getBytes());
        os.flush();
        os.close();
        QueryResultSource qResult = (QueryResultSource) resolveSource(BASE_URL + "images?/*[contains(local-name(), 'photo.png')]");
        assertNotNull(qResult);
        Collection results = qResult.getChildren();
        assertEquals(1, results.size());
        Iterator it = results.iterator();
        JCRNodeSource rSrc = (JCRNodeSource) it.next();
        InputStream rSrcIn = rSrc.getInputStream();
        ByteArrayOutputStream actualOut = new ByteArrayOutputStream();
        IOUtils.copy(rSrcIn, actualOut);
        rSrcIn.close();
        assertEquals(content, actualOut.toString());
        actualOut.close();
        rSrc.delete();
    }
