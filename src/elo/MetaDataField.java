package elo;

public class MetaDataField {
	String name;
	String text;
	String keynum;
	String keykey;
	String acl;
	
	MetaDataField(String name, String text, Integer keyn, String keyk, String acl){
		this.name = name;
		this.text = text;
		this.keynum = "[KEY" + keyn + "]";
		this.keykey = keyk;
		this.acl = acl;
	}
	
	MetaDataField(Integer key){
		this ("","",key,"","");
	}
	
	public String toString(){
		return this.name + " " + this.text + " " +  this.keynum + "" +  this.keykey + " " + this.acl;
	}
}
