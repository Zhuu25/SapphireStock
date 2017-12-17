public class GoodsForReturn {

    String idd;
    Double weightt;
    String st_date;
    String end_date;
    String status;

    public GoodsForReturn(String idd, Double weightt, String st_date, String end_date, String status) {
        this.idd = idd;
        this.weightt = weightt;
        this.st_date = st_date;
        this.end_date = end_date;
        this.status = status;
    }

    public String getIdd() {
        return idd;
    }

    public void setIdd(String idd) {
        this.idd = idd;
    }

    public Double getWeightt() {
        return weightt;
    }

    public void setWeightt(Double weightt) {
        this.weightt = weightt;
    }

    public String getSt_date() {
        return st_date;
    }

    public void setSt_date(String st_date) {
        this.st_date = st_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
