package elo;

public class MetaDataField {
	String name;
	String text;
	String keynum;
	String keykey;
	String acl;

	MetaDataField(String name, String text, Integer keyn, String keyk, String acl) {
		this.name = name;
		this.text = "KEYNAME=" + text;
		this.keynum = "[KEY" + keyn + "]";
		this.keykey = keyk;
		this.acl = "Acl=\"" + acl + "\"";
	}

	MetaDataField(Integer key) {
		this("", "", key, "", "");
	}

	public void setText(String text) {
		this.text = text;
	}

	// TODO fix this calss, separate what belongs to constructor and toString
	
	//this toString is only used when client data is not availible and reading from the ini file
	public String toString(String currentText) {

		String ret = "";
		if (this.keynum != null && this.keynum != "")
			ret += this.keynum + "\n";
		if (this.name != null && this.name != "")
			ret += "KEYNAME=\"" + this.name + "\"\n";
		// if (currentText != null && currentText != "")
		ret += "KEYTEXT=\"" + currentText + "\"\n";
		if (this.keykey != null && this.keykey != "")
			ret += "KEYKEY=\"" + keykey + "\"\n";
		if (this.acl != null && this.acl != "")
			ret += this.acl + "\n";

		return ret;
	}

	public String toString(String currentText, int keynum) {

		String ret = "";
		if (keynum != -1)
			ret += "[KEY" + keynum + "]\n";
		if (this.name != null && this.name != "")
			ret += "KEYNAME=\"" + this.name + "\"\n";
		// if (currentText != null && currentText != "")
		ret += "KEYTEXT=\"" + currentText + "\"\n";
		if (this.keykey != null && this.keykey != "")
			ret += "KEYKEY=\"" + keykey + "\"\n";
		if (this.acl != null && this.acl != "")
			ret += this.acl + "\n";

		return ret;
	}

	public String toString(int keynum) {
		String ret = "";
		if (keynum != -1)
			ret +=  "[KEY" + keynum + "]\n";
		if (this.name != null && this.name != "")
			ret += "KEYNAME=\"" + this.name + "\"\n";
		// if (currentText != null && currentText != "")
		ret += "KEYTEXT=\"" + this.text + "\"\n";
		if (this.keykey != null && this.keykey != "")
			ret += "KEYKEY=\"" + keykey + "\"\n";
		if (this.acl != null && this.acl != "")
			ret += this.acl + "\n";

		return ret;
	}

	public String toString() {
		return toString(this.text);
	}
}
