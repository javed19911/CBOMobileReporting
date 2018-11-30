package utils.adapterutils;

public class MailModel {
	public String name="";
	public String id="";
	public boolean selected=false;
	
	public MailModel(String name,String id){
		this.name=name;
		this.id=id;
		selected=false;
		
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name=name;
	}
	public String getId(){
		return id;
	}
	public void setid(String id){
		this.id=id;
	}
	public boolean isSelected(){
		return selected;
	}
	public void setSelected(boolean selected){
		this.selected=selected;
	}

}
