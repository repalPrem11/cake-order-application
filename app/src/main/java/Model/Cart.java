package Model;

public class Cart {

    private String cakename, cakeprice, pid, discount, quantity,cakeimage;

    public Cart() {

    }

    public Cart(String cakename, String cakeprice, String pid, String discount, String quantity,String cakeimage) {
        this.cakename = cakename;
        this.cakeprice = cakeprice;
        this.pid = pid;
        this.discount = discount;
        this.quantity = quantity;
        this.cakeimage=cakeimage;
    }

    public String getCakename() {
        return cakename;
    }

    public void setCakename(String cakename) {
        this.cakename = cakename;
    }

    public String getCakeprice() {
        return cakeprice;
    }

    public void setCakeprice(String cakeprice) {
        this.cakeprice = cakeprice;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCakeimage() {
        return cakeimage;
    }

    public void setCakeimage(String cakeimage) {
        this.cakeimage = cakeimage;
    }
}