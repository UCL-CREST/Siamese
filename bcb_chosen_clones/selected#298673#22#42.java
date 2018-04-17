    public MonsenClientID() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            NetworkInterface ni = NetworkInterface.getByInetAddress(address);
            byte[] unreadableMacAddress = ni.getHardwareAddress();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < unreadableMacAddress.length; i++) {
                buffer.append(Integer.toHexString(unreadableMacAddress[i] & 0xFF));
            }
            uniqeClientID = (long) (buffer.toString().hashCode() & 0xFFFFFFFFL);
        } catch (SocketException e) {
            log.error("An error occured while generating uniqe id " + e.getMessage());
            uniqeClientID = (long) UUID.randomUUID().hashCode();
        } catch (UnknownHostException e) {
            log.error("An error occured while generating uniqe id " + e.getMessage());
            uniqeClientID = (long) UUID.randomUUID().hashCode();
        } catch (Exception e) {
            log.error("An error occured while generating uniqe id " + e.getMessage());
            uniqeClientID = (long) UUID.randomUUID().hashCode();
        }
    }
