    public static LogLineProcessor getLogLineProcessor(String type) throws Exception {
        return (LogLineProcessor) Class.forName(type).getConstructor().newInstance();
    }
