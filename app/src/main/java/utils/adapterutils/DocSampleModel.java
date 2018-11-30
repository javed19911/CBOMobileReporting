package utils.adapterutils;

public class DocSampleModel {
	public String name="";
	public String id="";
	public String rowid="";
	public String file_ext="";
	private Boolean checked=false,highlight=false;

	public DocSampleModel(String name,String id,String rowid,Boolean highlight){
		this.name=name;
		this.id=id;
		this.rowid=rowid;
		this.highlight=highlight;
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


	public boolean isHighlighted() {
		return highlight;
	}
	public void setHighlight(boolean highlight) {
		this.highlight = highlight;
	}


	public String getRowId(){
		return rowid;
	}
	public void setRowId(String rowid){
		this.rowid=rowid;
		
	}
	public String get_file_ext(){
		return file_ext;
	}
	public void set_file_ext(String file_ext){
		this.file_ext=file_ext;

	}
	public Boolean get_Checked(){
		return checked;
	}
	public void set_Checked(Boolean checked){
		this.checked=checked;

	}
}
