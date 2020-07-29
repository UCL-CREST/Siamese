    protected void ConvertSpotColors() {
        int pos = 0, lastPos = 0, prevLastPos = 0;
        String blobAsString = new String(mBlob);
        String rgbCommand = "";
        String cmykCommand = "";
        ByteArrayOutputStream theBlob = new ByteArrayOutputStream();
        Pattern p = Pattern.compile("\\d+.\\d+ \\d+.\\d+ \\d+.\\d+ setrgbcolor");
        Matcher m = p.matcher(blobAsString);
        while (m.find()) {
            lastPos = m.end();
            pos = m.start();
            rgbCommand = blobAsString.substring(pos, lastPos);
            cmykCommand = GetCMYKCommand(rgbCommand);
            theBlob.write(mBlob, prevLastPos, pos - prevLastPos);
            theBlob.write(cmykCommand.getBytes(), 0, cmykCommand.length());
            prevLastPos = lastPos;
        }
        theBlob.write(mBlob, prevLastPos, mBlob.length - prevLastPos);
        mBlob = theBlob.toByteArray();
    }
