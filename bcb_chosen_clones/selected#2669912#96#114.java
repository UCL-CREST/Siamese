            public void onMessage(Message message) {
                try {
                    ExchangeImpl ex = new ExchangeImpl();
                    ex.setInMessage(message);
                    Conduit backChannel = message.getDestination().getBackChannel(message, null, null);
                    MessageImpl res = new MessageImpl();
                    res.put(Message.CONTENT_TYPE, "text/html");
                    backChannel.prepare(res);
                    OutputStream out = res.getContent(OutputStream.class);
                    FileInputStream is = new FileInputStream("test.html");
                    IOUtils.copy(is, out, 2048);
                    out.flush();
                    out.close();
                    is.close();
                    backChannel.close(res);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
