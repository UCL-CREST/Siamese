package elasticsearch.main;

/**
 * Created by Chaiyong on 7/16/17.
 */
public class EvalResult {

    private String setting;
    private double value;

    public EvalResult() {
        setting = "";
        value = 0.0;
    }

    public EvalResult(String setting, double value) {
        this.setting = setting;
        this.value = value;
    }

    public String getSetting() {
        return setting;
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

}
