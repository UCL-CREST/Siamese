    public G8RResponse(InputStream source) throws G8RException, EOFException {
        G8RMessageReader reader = new G8RMessageReader(source);
        String buffer;
        Scanner firstLine = reader.readLine();
        if (firstLine == null) {
            throw new G8RException(G8RErrors.EMPTY);
        }
        G8RProtocolUtility.checkVersion(firstLine);
        buffer = firstLine.next();
        if (buffer.equals(OK)) {
            status = buffer;
        } else if (buffer.equals(ERROR)) {
            status = buffer;
        } else {
            throw new G8RException(G8RErrors.STATUS);
        }
        setFunction(firstLine.next());
        G8RProtocolUtility.validateToken(getFunction());
        try {
            firstLine.skip(" ");
        } catch (NoSuchElementException e) {
            throw new G8RException(G8RErrors.SPACE);
        }
        firstLine.useDelimiter("\r\n");
        if (firstLine.hasNext("\\p{Print}+")) {
            message = firstLine.next();
        } else {
            message = "";
        }
        firstLine.close();
        setCookies(new CookieList(reader));
    }
