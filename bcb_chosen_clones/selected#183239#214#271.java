    @SuppressWarnings("unchecked")
    private void writeRawData(IMessageBeanManager messageBeanManager, IMessageBeanDescriptor messageBeanDescriptor) {
        IDerivativeConverter<DataOutputStream> derivativeConverter = DerivativeFactory.createDerivativeConverter(DataOutputStream.class);
        ZipEntry ze = new ZipEntry(messageBeanDescriptor.getCanonicalName() + "_" + DATA);
        ze.setComment((null == messageBeanDescriptor.getDescription()) ? Namespace.NULL_TOKEN : messageBeanDescriptor.getDescription());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            Class messageBeanClass = Class.forName(messageBeanDescriptor.getCanonicalName());
            PropertyDescriptor[] propertyDescriptors = BeanUtil.propertyDescriptors(messageBeanClass);
            if (null != propertyDescriptors) {
                Map<String, Method> methodMap = new HashMap<String, Method>();
                for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                    methodMap.put(propertyDescriptor.getName(), propertyDescriptor.getReadMethod());
                }
                for (Object o : messageBeanManager.createIterator(messageBeanClass)) {
                    for (IMessageBeanProperty beanProperty : messageBeanDescriptor.properties()) {
                        logger.fine("Deriving value of property " + beanProperty.getId());
                        if (beanProperty.getDataType() != DataType.IdentityType && beanProperty.getDataType() != DataType.TransientType) {
                            Object value = null;
                            try {
                                Method readMethod = methodMap.get(beanProperty.getId());
                                if (null != readMethod) {
                                    value = readMethod.invoke(o);
                                } else {
                                    throw new IllegalStateException("Incorrectly mapped property '" + beanProperty.getId() + "': please fix the property name in the IMessageBeanDescriptor registration");
                                }
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                            derivativeConverter.convert(dos, beanProperty.getDataType(), value, IPackagingVisitor.DIRECTION_OUT);
                        }
                    }
                }
                try {
                    dos.flush();
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    byte[] data = baos.toByteArray();
                    ze.setSize(data.length);
                    zipStream.putNextEntry(ze);
                    zipStream.write(data, 0, data.length);
                    zipStream.closeEntry();
                    zipStream.flush();
                    logger.fine("Wrote data zip entry " + ze.getName());
                } catch (Exception e) {
                    logger.warning("Unable to serialize message bean descriptor " + messageBeanDescriptor.toString() + ": " + e.getMessage());
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
