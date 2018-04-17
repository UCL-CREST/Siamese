    public List<AddressDisplayBean> getAddresses() {
        List<AddressDisplayBean> displayBeans = new ArrayList<AddressDisplayBean>();
        for (AddressType type : addressTypes) {
            Class displayBeanClass = getAddressDisplayBeanClass();
            Address address = partner.getAddresses().get(type);
            if (address == null) {
                address = getAddressInstance(type);
                partner.getAddresses().put(type, address);
            }
            try {
                Constructor constructor = displayBeanClass.getConstructor(getAddressDisplayBeanConstructorSignature());
                AddressDisplayBean displayBean = (AddressDisplayBean) constructor.newInstance(address, type, messageSource, locale, contactChannelTypes);
                displayBeans.add(displayBean);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return displayBeans;
    }
