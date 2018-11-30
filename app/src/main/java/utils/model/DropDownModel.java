package utils.model;

public  class DropDownModel {

    private String Name;
    private String Id;
    private String Colour= "";

    public DropDownModel(String name, String id) {
        Name = name;
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getColour() {
        return Colour;
    }

    public DropDownModel setColour(String colour) {
        if(Colour.trim().equals(""))
            Colour = "#FFFFFFFF";
        Colour = colour;
        return this;
    }
}
