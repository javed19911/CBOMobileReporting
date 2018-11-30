package utils.adapterutils;


public class Apprisal_Spinner_Model {

    String name;
    String id;

    public Apprisal_Spinner_Model(String name,String id){


        this.name = name;
        this.id = id;

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
}
