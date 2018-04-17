    public InstanceMonitor(String awsAccessId, String awsSecretKey, String bucketName, boolean first) throws IOException {
        this.awsAccessId = awsAccessId;
        this.awsSecretKey = awsSecretKey;
        props = new Properties();
        while (true) {
            try {
                s3 = new RestS3Service(new AWSCredentials(awsAccessId, awsSecretKey));
                bucket = new S3Bucket(bucketName);
                S3Object obj = s3.getObject(bucket, EW_PROPERTIES);
                props.load(obj.getDataInputStream());
                break;
            } catch (S3ServiceException ex) {
                logger.error("problem fetching props from bucket, retrying", ex);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException iex) {
                }
            }
        }
        URL url = new URL("http://169.254.169.254/latest/meta-data/hostname");
        hostname = new BufferedReader(new InputStreamReader(url.openStream())).readLine();
        url = new URL("http://169.254.169.254/latest/meta-data/instance-id");
        instanceId = new BufferedReader(new InputStreamReader(url.openStream())).readLine();
        url = new URL("http://169.254.169.254/latest/meta-data/public-ipv4");
        externalIP = new BufferedReader(new InputStreamReader(url.openStream())).readLine();
        this.dns = new NetticaAPI(props.getProperty(NETTICA_USER), props.getProperty(NETTICA_PASS));
        this.userData = awsAccessId + " " + awsSecretKey + " " + bucketName;
        this.first = first;
        logger.info("InstanceMonitor initialized, first=" + first);
    }
