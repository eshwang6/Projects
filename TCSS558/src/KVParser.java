import java.util.HashMap;

public class KVParser {
	
	
	//use HashMap to store key-values
	private HashMap<String, String> hashMap = new HashMap<String, String>();

	/*
	 * Parses from both TCP/UDP client and check if the input is formatted correctly.
	 * It performs three operations : PUT, GET, and DELETE.
	 * The input command should be in the form of "operator(key,value)"
	 * the GET and DELETE operator only has one parameter, while PUT has two parameters.
	 * The operator is NOT case-sensitive.
	 * The key and value is case-sensitive and space-sensitive.
	 */
	public String KVParse(String inputKV) throws Exception{

		//split the command by parentheses and comma
		String[] opKeyVal = inputKV.split("[(,)]");
		
		//if the input command is in a well-formatted fashion, perform operations
		
		//for PUT operation with two parameters
		if(opKeyVal[0].equalsIgnoreCase("Put") && opKeyVal.length==3){
			
			//if the key does not exist, store the key-value
			if(!hashMap.containsKey(opKeyVal[1])){

				hashMap.put(opKeyVal[1], opKeyVal[2]);
				return "Put Key :" + opKeyVal[1] + " Value : " + opKeyVal[2];

			}
			else{
				
				//if the key already exists, update the value.
				hashMap.put(opKeyVal[1], opKeyVal[2]);
				return "Put Key :" + opKeyVal[1] + " Updated Value : " + opKeyVal[2];

			}

		}
		
		//for GET operation with one parameter
		else if(opKeyVal[0].equalsIgnoreCase("Get") && opKeyVal.length == 2){

			//if the key exists, return the value.
			if(hashMap.containsKey(opKeyVal[1])){

				return "Value Got :" + hashMap.get(opKeyVal[1]);

			}
			else{
				
				//if the key does not exist, state that the key does not exist.
				return "The Key "+opKeyVal[1]+" does not exist.";
				
			}

		}
		
		//for DELETE operation with one parameter
		else if(opKeyVal[0].equalsIgnoreCase("Delete") && opKeyVal.length == 2){

			//if the key exists, delete the key and return the deletion.
			if(hashMap.containsKey(opKeyVal[1])){

				hashMap.remove(opKeyVal[1]);

				return "The Key "+opKeyVal[1]+" has been deleted.";

			}else{
				
				//if the key does not exist, return that the key does not exist.
				return "The Key "+opKeyVal[1]+" does not exist.";

			}

		}

		//If the input command is not in a well-formatted fashion, 
		//it throws Exception that it is an incorrect input.
		throw new Exception("Incorrect Input");

	}

}
