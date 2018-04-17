                    }
                case 'l':
                    {
                        for (int i = 0; i < arraySize; i++) {
                            dataArray[i] = (float) dis.readLong();
                        }
                        break;
                    }
                case 'f':
                    {
                        for (int i = 0; i < arraySize; i++) {
                            dataArray[i] = dis.readFloat();
                        }
                        break;
                    }
                case 'd':
                    {
                        for (int i = 0; i < arraySize; i++) {
                            dataArray[i] = (float) dis.readDouble();
