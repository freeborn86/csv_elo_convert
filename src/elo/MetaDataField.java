package elo;

public class MetaDataField {
	String name;
	String text;
	String key;
	String acl;
	
	MetaDataField(String name, String text, Integer key, String acl){
		this.name = name;
		this.text = text;
		this.key = "[KEY" + key + "]";
		this.acl = acl;
	}
	
	MetaDataField(Integer key){
		this ("","",key,"");
	}
}
