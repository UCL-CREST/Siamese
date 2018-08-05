    @SuppressWarnings("unchecked")
    public Controller(View view, Runtime runtime, Messages messages) {
        this.view = view;
        this.runtime = runtime;
        this.messages = messages;
        try {
            Class<? extends PcscEidSpi> pcscEidClass = (Class<? extends PcscEidSpi>) Class.forName("be.fedict.eid.applet.sc.PcscEid");
            Constructor<? extends PcscEidSpi> pcscEidConstructor = pcscEidClass.getConstructor(View.class, Messages.class);
            this.pcscEidSpi = pcscEidConstructor.newInstance(this.view, this.messages);
        } catch (Exception e) {
            String msg = "error loading PC/SC eID component: " + e.getMessage();
            this.view.addDetailMessage(msg);
            throw new RuntimeException(msg);
        }
        this.pcscEidSpi.addObserver(new PcscEidObserver());
        ProtocolContext protocolContext = new LocalAppletProtocolContext(this.view);
        this.protocolStateMachine = new ProtocolStateMachine(protocolContext);
    }
