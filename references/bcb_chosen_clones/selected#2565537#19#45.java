    public static void copy(String fileFrom, String fileTo) throws IOException {
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputStream = new FileInputStream(fileFrom);
            outputStream = new FileOutputStream(fileTo);
            inputChannel = inputStream.getChannel();
            outputChannel = outputStream.getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
        } finally {
            try {
                inputChannel.close();
            } finally {
                try {
                    outputChannel.close();
                } finally {
                    try {
                        inputStream.close();
                    } finally {
                        outputStream.close();
                    }
                }
            }
        }
    }
