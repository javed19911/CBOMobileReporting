package utils.model;

import java.io.Serializable;

public  class DropDownModel implements Serializable{

    private String Name;
    private String Id;
    private int DESIG_ID = 1;
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
        if ( Colour.trim().isEmpty()){
            setColour("");
        }
        return Colour;
    }

    public int getDESIG_ID() {
        return DESIG_ID;
    }

    public DropDownModel setDESIG_ID(int DESIG_ID) {
        this.DESIG_ID = DESIG_ID;
        return this;
    }

    public DropDownModel setColour(String colour) {
        if(Colour.trim().equals(""))
            Colour = "#FF066199";
        Colour = colour;
        return this;
    }
}
