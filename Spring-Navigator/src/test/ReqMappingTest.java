package test;

public class ReqMappingTest {

	
	public static void main(String[] args) {
		
		String reqMap = "{asd}/cihazlar/siralama/enpopuler";//cihazlar/{category}
		///cihazlar/{category}/list/autocomplete.json
		///cihazlar/{category}/siralama/{sortingType}/tumu
		
		String url="${customerType}/cihazlar/siralama/enpopuler";
		
		System.out.println(validateUrl(reqMap, url));
		
	}
	
	public static boolean validateUrl(String reqMappingUrl, String searchText){
		reqMappingUrl = truncateUrl(reqMappingUrl);
			
		String[] reqArr = reqMappingUrl.split("/");
		String[] searchArr = searchText.split("/");
		boolean notSame=false;
		if(reqArr.length == searchArr.length){
			for (int i=0;i<reqArr.length;i++) {
				if(reqArr[i].startsWith("{") && reqArr[i].endsWith("}") || 
						searchArr[i].startsWith("${") && searchArr[i].endsWith("}"))
					continue;
				if(!reqArr[i].equals(searchArr[i])){
					notSame=true;
					return false;
				}
			}
		}else{
			notSame = true;
		}
		return !notSame;
	}
	
public static String truncateUrl(String str){
		
		if(str.startsWith("/")){
			str = str.substring(1);
		}
		
		if(str.endsWith("/")){
			str = str.substring(0, str.length()-1);
		}
		
		return str;
	}
}
