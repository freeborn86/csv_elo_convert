package elo;

public class MetaDataField {
	String name;
	String text;
	String keynum;
	String keykey;
	String acl;

	MetaDataField(String name, String text, Integer keyn, String keyk, String acl) {
		this.name = name;
		this.text = text;
		this.keynum = "[KEY" + keyn + "]";
		this.keykey = keyk;
		this.acl = "Acl=\""+ acl + "\"";
	}

	MetaDataField(Integer key) {
		this("", "", key, "", "");
	}

	//TODO fix this calss, separate what belongs to constructor and toString
	public String toString(String currentText) {

		String ret = "";
		if (this.keynum != null && this.keynum != "")
			ret += this.keynum + "\n";
		if (this.name != null && this.name != "")
			ret += this.name + "\n";
		if (currentText != null && currentText != "")
			ret ="KEYTEXT=\"" + currentText + "\n";
		if (this.keykey != null && this.keykey != "")
			ret +="KEYKEY=\"" + keykey + "\n";
		if (this.acl != null && this.acl != "")
			ret += this.acl+ "\n";
		
		return ret;
	}
	
	public String toString(String currentText, int keynum) {

		String ret = "";
		if (keynum !=-1 )
			ret += keynum + "\n";
		if (this.name != null && this.name != "")
			ret += this.name + "\n";
		if (currentText != null && currentText != "")
			ret ="KEYTEXT=\"" + currentText + "\n";
		if (this.keykey != null && this.keykey != "")
			ret +="KEYKEY=\"" + keykey + "\n";
		if (this.acl != null && this.acl != "")
			ret += this.acl+ "\n";
		
		return ret;
	}

	public String toString() {
		return toString(this.text);
	}
}
