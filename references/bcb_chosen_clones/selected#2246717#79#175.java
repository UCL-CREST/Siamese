    public void run() {
        long starttime = (new Date()).getTime();
        Matcher m = Pattern.compile("(\\S+);(\\d+)").matcher(Destination);
        boolean completed = false;
        if (OutFile.length() > IncommingProcessor.MaxPayload) {
            logger.warn("Payload is too large!");
            close();
        } else {
            if (m.find()) {
                Runnable cl = new Runnable() {

                    public void run() {
                        WaitToClose();
                    }
                };
                Thread t = new Thread(cl);
                t.start();
                S = null;
                try {
                    String ip = m.group(1);
                    int port = Integer.valueOf(m.group(2));
                    SerpentEngine eng = new SerpentEngine();
                    byte[] keybytes = new byte[eng.getBlockSize()];
                    byte[] ivbytes = new byte[eng.getBlockSize()];
                    Random.nextBytes(keybytes);
                    Random.nextBytes(ivbytes);
                    KeyParameter keyparm = new KeyParameter(keybytes);
                    ParametersWithIV keyivparm = new ParametersWithIV(keyparm, ivbytes);
                    byte[] parmbytes = BCUtils.writeParametersWithIV(keyivparm);
                    OAEPEncoding enc = new OAEPEncoding(new ElGamalEngine(), new RIPEMD128Digest());
                    enc.init(true, PublicKey);
                    byte[] encbytes = enc.encodeBlock(parmbytes, 0, parmbytes.length);
                    PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new SerpentEngine()));
                    cipher.init(true, keyivparm);
                    byte[] inbuffer = new byte[128];
                    byte[] outbuffer = new byte[256];
                    int readlen = 0;
                    int cryptlen = 0;
                    FileInputStream fis = new FileInputStream(OutFile);
                    FileOutputStream fos = new FileOutputStream(TmpFile);
                    readlen = fis.read(inbuffer);
                    while (readlen >= 0) {
                        if (readlen > 0) {
                            cryptlen = cipher.processBytes(inbuffer, 0, readlen, outbuffer, 0);
                            fos.write(outbuffer, 0, cryptlen);
                        }
                        readlen = fis.read(inbuffer);
                    }
                    cryptlen = cipher.doFinal(outbuffer, 0);
                    if (cryptlen > 0) {
                        fos.write(outbuffer, 0, cryptlen);
                    }
                    fos.close();
                    fis.close();
                    S = new Socket(ip, port);
                    DataOutputStream dos = new DataOutputStream(S.getOutputStream());
                    dos.writeInt(encbytes.length);
                    dos.write(encbytes);
                    dos.writeLong(TmpFile.length());
                    fis = new FileInputStream(TmpFile);
                    readlen = fis.read(inbuffer);
                    while (readlen >= 0) {
                        dos.write(inbuffer, 0, readlen);
                        readlen = fis.read(inbuffer);
                    }
                    DataInputStream dis = new DataInputStream(S.getInputStream());
                    byte[] encipbytes = StreamUtils.readBytes(dis);
                    cipher.init(false, keyivparm);
                    byte[] decipbytes = new byte[encipbytes.length];
                    int len = cipher.processBytes(encipbytes, 0, encipbytes.length, decipbytes, 0);
                    len += cipher.doFinal(decipbytes, len);
                    byte[] realbytes = new byte[len];
                    System.arraycopy(decipbytes, 0, realbytes, 0, len);
                    String ipstr = new String(realbytes, "ISO-8859-1");
                    Callback.Success(ipstr);
                    completed = true;
                    dos.write(0);
                    dos.flush();
                    close();
                } catch (Exception e) {
                    close();
                    if (!completed) {
                        e.printStackTrace();
                        Callback.Fail(e.getMessage());
                    }
                }
            } else {
                close();
                logger.warn("Improper destination string. " + Destination);
                Callback.Fail("Improper destination string. " + Destination);
            }
        }
        CloseWait();
        long newtime = (new Date()).getTime();
        long timediff = newtime - starttime;
        logger.debug("Outgoing processor took: " + timediff);
    }
