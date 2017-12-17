public class KeepingGoods {
    /**
     * collect goods that collect in bill
     */

    String in;
    String keepId;
    String keepWeight;

    public KeepingGoods(String in, String keepId, String keepWeight) {
        this.in = in;
        this.keepId = keepId;
        this.keepWeight = keepWeight;
    }

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

    public String getKeepId() {
        return keepId;
    }

    public void setKeepId(String keepId) {
        this.keepId = keepId;
    }

    public String getKeepWeight() {
        return keepWeight;
    }

    public void setKeepWeight(String keepWeight) {
        this.keepWeight = keepWeight;
    }
}
