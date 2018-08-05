    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("camel/exec-context.xml");
        CamelContext context = appContext.getBean(CamelContext.class);
        Exchange exchange = new DefaultExchange(context);
        List<String> arg = new ArrayList<String>();
        arg.add("/home/sumit/derby.log");
        arg.add("helios:cameltesting/");
        exchange.getIn().setHeader(ExecBinding.EXEC_COMMAND_ARGS, arg);
        Exchange res = context.createProducerTemplate().send("direct:input", exchange);
        ExecResult result = (ExecResult) res.getIn().getBody();
        System.out.println(result.getExitValue());
        System.out.println(result.getCommand());
        if (result.getStderr() != null) {
            IOUtils.copy(result.getStderr(), new FileOutputStream(new File("/home/sumit/error.log")));
        }
        if (result.getStdout() != null) {
            IOUtils.copy(result.getStdout(), new FileOutputStream(new File("/home/sumit/out.log")));
        }
        appContext.close();
    }
