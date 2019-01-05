package utils.adapterutils;

public class Dcr_Workwith_Model 
	{
	
		public String name="";
		public String id="";
		public String ResigYN="0";
		public String LeaveYN="1";
		public boolean selected=false,independent_list=false;
		
		
		public Dcr_Workwith_Model(String name,String id)
		{
			this.name=name;
			this.id=id;
			selected=false;
			independent_list=false;
			ResigYN="0";
			LeaveYN="1";
		}

		public Dcr_Workwith_Model(String name,String id,String ResigYN,String LeaveYN,String WorkWithYN)
		{
			this.name=name;
			this.id=id;
			selected=false;
			independent_list=false;
			this.ResigYN=ResigYN;
			this.LeaveYN = LeaveYN;
			selected = WorkWithYN.equals("1");
		}

		public String getName()
		{
			return name;
		}
		
		public void setName(String name)
		{
			this.name=name;
		}
		
		public String getId()
		{
			return id;
		}
		
		public void setId(String id)
		{
			this.id=id;
		}

		public String getResigYN()
		{
			return ResigYN;
		}

		public void setResigYN(String ResigYN)
		{
			this.ResigYN=ResigYN;
		}
		
		public boolean isSelected()
		{
			return selected;
		}
		
		public void setSelected(boolean selected)
		{
			this.selected=selected;
		}

		public boolean isindependentSelected()
		{
			return independent_list;
		}

		public void setindependentSelected(boolean independent_list)
		{
			this.independent_list=independent_list;
		}

		public String getLeaveYN() {
			return LeaveYN;
		}

		public void setLeaveYN(String leaveYN) {
			LeaveYN = leaveYN;
		}


	}
