package Model;

public class Products {

    private String cakeName, description, price, cakeimage, category, pid, date, time,Kg;


    public Products(){

    }



    public Products(String cakeName, String description, String price, String cakeimage, String category, String pid, String date, String time,String Kg) {

        this.cakeName = cakeName;
        this.description = description;
        this.price = price;
        this.cakeimage =cakeimage;
        this.category = category;
        this.pid = pid;
        this.date = date;
        this.time = time;
        this.Kg=Kg;
    }

    public String getKg() {
        return Kg;
    }

    public void setKg(String kg) {
        Kg = kg;
    }

    public String getCakename() {
        return cakeName;
    }

    public void setCakename(String cakename) {
        this.cakeName = cakename;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCakeimage() {
        return cakeimage;
    }

    public void setCakeimage(String cakeimage) {
        this.cakeimage = cakeimage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
