package oripa.doc;

import oripa.resource.Constants;

public class DocHolder {
	private static DocHolder instance = null;
	
	public static DocHolder getInstance(){
		if(instance == null){
			instance = new DocHolder(Constants.DEFAULT_PAPER_SIZE);
		}
		
		return instance;
	}

//	public static DocHolder getInstance(double size){
//		if(instance == null){
//			instance = new DocHolder(size);
//		}
//		
//		return instance;
//	}
//-----------------------------------------------------
	
	private Doc doc;
	
	private DocHolder(double size){
		doc = new Doc(size);
	}
	
	public void setDoc(Doc doc) {
		this.doc = doc;
	}

	
	
	public Doc getDoc(){
		return doc;
	}
	
	
}
