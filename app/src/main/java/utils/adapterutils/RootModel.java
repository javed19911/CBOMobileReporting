package utils.adapterutils;

public class RootModel {
	
	public String rootName="";
	public String rootId="";
	public boolean isSelected= false;
	
	public RootModel(String rootName,String rootId){
		this.rootName=rootName;
		this.rootId=rootId;
	}
	
	public String getRootName(){
		return rootName;
	}
	public void setRootName(String rootName){
		this.rootName=rootName;
	}
	
	public String getRootId(){
		return rootId;
	}
	public void setRootId(String rootId){
		this.rootId=rootId;
	}
	public boolean isSelected(){
		return isSelected;
	}
	public void setSelected(boolean isSelected){
		this.isSelected=isSelected;
	}

}
