    public void run(String args[]) {
        w = new DDS.WaitSet();
        dpQos = new DDS.DomainParticipantQosHolder();
        tQos = new DDS.TopicQosHolder();
        sQos = new DDS.SubscriberQosHolder();
        drQos = new DDS.DataReaderQosHolder();
        dpf = DDS.DomainParticipantFactory.get_instance();
        dpf.get_default_participant_qos(dpQos);
        dp = dpf.create_participant(myDomain, dpQos.value, null, DDS.ANY_STATUS.value);
        if (dp == null) {
            System.out.println("Receive: ERROR - Splice Daemon not running");
            return;
        }
        dpQos = null;
        dp.get_default_subscriber_qos(sQos);
        sQos.value.partition.name = new String[1];
        sQos.value.partition.name[0] = read_partition;
        s = dp.create_subscriber(sQos.value, null, DDS.ANY_STATUS.value);
        sQos = null;
        s.get_default_datareader_qos(drQos);
        dp.get_default_topic_qos(tQos);
        PP_target_dt = new messaging.PP_target_msgTypeSupport();
        if (dp == null) {
            System.out.println("Receive: ERROR - Splice Daemon not running2");
            return;
        }
        PP_target_dt.register_type(dp, "messaging::PP_bridgefromudp_msg");
        PP_target_topic = dp.create_topic("PP_bridgefromudp_topic", "messaging::PP_bridgefromudp_msg", tQos.value, null, DDS.ANY_STATUS.value);
        PP_target_reader = messaging.PP_target_msgDataReaderHelper.narrow(s.create_datareader(PP_target_topic, drQos.value, null, DDS.ANY_STATUS.value));
        PP_target_sc = PP_target_reader.get_statuscondition();
        PP_target_sc.set_enabled_statuses(DDS.DATA_AVAILABLE_STATUS.value);
        result = w.attach_condition(PP_target_sc);
        assert (result == RETCODE_OK.value);
        DDS.Duration_t wait_timeout = new DDS.Duration_t(DDS.DURATION_INFINITE_SEC.value, DDS.DURATION_INFINITE_NSEC.value);
        System.out.println("Receive: waiting for 'Send'");
        result = w._wait(conditionList, wait_timeout);
        System.out.println("Receive: got it");
        if (result == RETCODE_ALREADY_DELETED.value) {
        } else if (conditionList.value != null) {
            if (conditionList.value[0] == PP_target_sc) {
                result = PP_target_reader.take(PP_target_dataList, infoList, 1, DDS.ANY_SAMPLE_STATE.value, DDS.ANY_VIEW_STATE.value, DDS.ANY_INSTANCE_STATE.value);
                String msg = PP_target_dataList.value[0].a_string;
                try {
                    DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    InputSource is = new InputSource();
                    is.setCharacterStream(new StringReader(msg));
                    Document doc = db.parse(is);
                    NodeList nodes = doc.getElementsByTagName("msg");
                    Element element = (Element) nodes.item(0);
                    Node node = element.getFirstChild();
                    msg = node.getNodeValue();
                } catch (Exception e) {
                    System.out.println(e);
                }
                if (msg != null) {
                    System.out.println("Receiver : I got a message ==> " + msg + "\n");
                    result = PP_target_reader.return_loan(PP_target_dataList, infoList);
                } else {
                    System.out.println("Receive: unknown condition triggered: " + conditionList.value[0]);
                }
            } else {
                System.out.println("Receive: unknown condition triggered");
            }
        }
        result = s.delete_datareader(PP_target_reader);
        result = dp.delete_subscriber(s);
        result = dp.delete_topic(PP_target_topic);
        result = dpf.delete_participant(dp);
        w = null;
        PP_target_dt = null;
        dpQos = null;
        tQos = null;
        drQos = null;
        return;
    }
