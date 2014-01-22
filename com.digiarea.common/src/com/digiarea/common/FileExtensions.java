package com.digiarea.common;

public enum FileExtensions {

	JAVA("java"), JS("js"), CPP("cpp"), C("c"), XML("xml"), HTML(
			"html"), FXML("fxml");

	public final String EXT;
	public final String END;
	public final String ALL;

	private FileExtensions(String ext) {
		EXT = ext;
		END = "." + EXT;
		ALL = "*" + END;
	}
}
