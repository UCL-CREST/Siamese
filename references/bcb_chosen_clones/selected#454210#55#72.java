    public void myClickHandler(View view) {
        switch(view.getId()) {
            case R.id.ReadWebPage:
                try {
                    textView.setText("");
                    URL url = new URL(urlText.getText().toString());
                    BufferedReader rd = new BufferedReader(new InputStreamReader(url.openStream()));
                    String line = "";
                    while ((line = rd.readLine()) != null) {
                        textView.append(line);
                    }
                } catch (Exception e) {
                    System.out.println("Nay, did not work");
                    textView.setText(e.getMessage());
                }
                break;
        }
    }
