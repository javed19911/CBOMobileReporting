package utils.adapterutils;

public class GiftModel {

    private String name = "";
    private String id = "";
    private boolean selected = false;
    private String score = "";
    private String rate = "";
    private String sample = "";
    private int Stock = 0;
    private int Balance = 0;
    private boolean highlight=false;


    public GiftModel(String name, String id, String rate) {
        this.name = name;
        this.id = id;
        this.rate = rate;
        selected = false;
        this.highlight=false;
        score = "";
    }

    public GiftModel(String name, String id, String rate,int Stock,int Balance) {
        this.name = name;
        this.id = id;
        this.rate = rate;
        selected = false;
        this.highlight=false;
        score = "";
        this.Stock = Stock;
        this.Balance = Balance;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isHighlighted() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public String getSample() {
        return sample;
    }

    public int getStock() {
        return Stock;
    }

    public void setStock(int stock) {
        Stock = stock;
    }

    public int getBalance() {
        return Balance;
    }

    public void setBalance(int balance) {
        Balance = balance;
    }
}

