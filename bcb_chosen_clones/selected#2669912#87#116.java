    private static void serveHTML() throws Exception {
        Bus bus = BusFactory.getDefaultBus();
        DestinationFactoryManager dfm = bus.getExtension(DestinationFactoryManager.class);
        DestinationFactory df = dfm.getDestinationFactory("http://cxf.apache.org/transports/http/configuration");
        EndpointInfo ei = new EndpointInfo();
        ei.setAddress("http://localhost:8080/test.html");
        Destination d = df.getDestination(ei);
        d.setMessageObserver(new MessageObserver() {

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
        });
    }
