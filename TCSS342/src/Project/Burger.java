package Project;

public class Burger {

	private String patty;

	private MyStack<String>botHalf;
	
	private MyStack<String>topHalf;
	
	
	public Burger(boolean theWorks){
		
		patty = "Beef";
		
		if(theWorks){
			
			makeBotHalf(true);
			makeTopHalf(true);
			
			
		}else{
			
			makeBotHalf(false);
			makeTopHalf(false);
			
		}
		
	}
	public void changePatties(String pattyType){
	
		MyStack<String> tempStack = new MyStack<String>();
		while(!topHalf.isEmpty()){
			
			String ingredient = topHalf.pop();
//			tempStack.push(ingredient.equals(patty) ? pattyType : ingredient);
			if(ingredient.equals(patty)){

				tempStack.push(pattyType);
				
			}
			else {
				tempStack.push(ingredient);
			}
			
		}
		while(!tempStack.isEmpty()){
			
			topHalf.push(tempStack.pop());
			
		}
		while(!botHalf.isEmpty()){
			
			String ingredient = botHalf.pop();
			if(ingredient.equals(patty)){

				tempStack.push(pattyType);
				
			}
			else {
				
				tempStack.push(ingredient);
			
			}
			
		}
		while(!tempStack.isEmpty()){
			
			botHalf.push(tempStack.pop());
			
		}
	
		patty = pattyType;
	
	}
	public void addPatty(){
		
		topHalf.push(patty);
		
	}
	public void removePatty(){
		
		MyStack<String> tempStack = new MyStack<String>();
		
		while(!topHalf.isEmpty()){
			
			tempStack.push(topHalf.pop());
			
			if(tempStack.peek().equals(patty)){
				
				tempStack.pop();
					
			}
			
		}
		while(!tempStack.isEmpty()){
			
			topHalf.push(tempStack.pop());
			
		}
		
	}
	public void addCategory(String type){
		
		if(type.equals("Cheese")){
			
			MyStack<String> tempStack = new MyStack<String>();
			
			while(!botHalf.isEmpty()){
				
				tempStack.push(botHalf.pop());
			
			}
			
			tempStack.push("Cheddar");
			tempStack.push("Mozzarella");
			tempStack.push("Pepperjack");
			
			while(!tempStack.isEmpty()){
				
				botHalf.push(tempStack.pop());
				
			}
				
		}
		
		if(type.equals("Veggies")){
			
			MyStack<String> tempStack = new MyStack<String>();

			while(!topHalf.isEmpty()){

				tempStack.push(topHalf.pop());

				if(tempStack.peek().equals(patty)){
					
					tempStack.push(topHalf.pop());
					
					if(topHalf.peek().equals(patty)){
						
						tempStack.push(topHalf.pop());
						
						tempStack.push("Onions");
						tempStack.push("Tomato");
						tempStack.push("Lettuce");
						break;
					}else{
						
						tempStack.push("Onions");
						tempStack.push("Tomato");
						tempStack.push("Lettuce");
						break;
					}
					
				}else{
					
					tempStack.push("Onions");
					tempStack.push("Tomato");
					tempStack.push("Lettuce");
					break;
				}
				
			}
			while(!topHalf.isEmpty()){

				tempStack.push(topHalf.pop());

			}
			
			tempStack.push("Pickle");
			
			while(!tempStack.isEmpty()){
				
				topHalf.push(tempStack.pop());
				
			}
			
			
			while(!botHalf.isEmpty()){
				
				tempStack.push(botHalf.pop());
				
				if(botHalf.peek().equals(patty)){
					
					tempStack.push("Mushrooms");
					
					break;
				}
				
			}
			
			while(!tempStack.isEmpty()){
				
				botHalf.push(tempStack.pop());
				
			}
			
		}
		
		if(type.equals("Sauce")){
			
			MyStack<String> tempStack = new MyStack<String>();
			
			while(!topHalf.isEmpty()){
				
				tempStack.push(topHalf.pop());
				
				if(botHalf.peek().equals("Bun")){
					
					tempStack.push("Baron-Sauce");
					tempStack.push("Mayonnaise");
					
					break;
					
				}
				
			}
			while(!tempStack.isEmpty()){
				
				topHalf.push(tempStack.pop());
				
			}

			while(!botHalf.isEmpty()){
				
				tempStack.push(botHalf.pop());
				
				if(tempStack.peek().equals("Bun")){
					
					tempStack.push("Ketchup");
					tempStack.push("Mustard");
					
					break;
				}
				
			}
			
			while(!tempStack.isEmpty()){
				
				botHalf.push(tempStack.pop());
				
			}
			
		}
	
	}
	public void removeCategory(String type){
		
		if(type.equals("Cheese")){
			
			MyStack<String> tempStack = new MyStack<String>();
			
			while(!botHalf.isEmpty()){
				
				tempStack.push(botHalf.pop());
				if(tempStack.peek().equals("Pepperjack") || tempStack.peek().equals("Mozzarella") || tempStack.peek().equals("Cheddar")){
					
					tempStack.pop();
					
				}else{
					
					
					
				}
				
			}
			while(!tempStack.isEmpty()){
				
				botHalf.push(tempStack.pop());
				
			}
			
		}
		
		if(type.equals("Veggies")){
			
			MyStack<String> tempStack1 = new MyStack<String>();
			
			while(!topHalf.isEmpty()){
				
				tempStack1.push(topHalf.pop());
				
				if(tempStack1.peek().equals("Pickle") || tempStack1.peek().equals("Lettuce") || tempStack1.peek().equals("Tomato")
						|| tempStack1.peek().equals("Onions") ){
					
					tempStack1.pop();
					
				}
				
			}
			while(!tempStack1.isEmpty()){
				
				topHalf.push(tempStack1.pop());
				
			}

			MyStack<String> tempStack2 = new MyStack<String>();
			
			while(!botHalf.isEmpty()){
				
				tempStack2.push(botHalf.pop());
				
				if(tempStack2.peek().equals("Mushrooms")){
					
					tempStack2.pop();
					
				}
				
			}
			
			while(!tempStack2.isEmpty()){
				
				botHalf.push(tempStack2.pop());
				
			}
			
		}
		
		if(type.equals("Sauce")){
			
			MyStack<String> tempStack = new MyStack<String>();
			
			while(!topHalf.isEmpty()){
				
				tempStack.push(topHalf.pop());
				
				if(tempStack.peek().equals("Baron-Sauce") || tempStack.peek().equals("Mayonnaise")){
					
					tempStack.pop();
					
				}
				
			}
			while(!tempStack.isEmpty()){
				
				topHalf.push(tempStack.pop());
				
			}

			while(!botHalf.isEmpty()){
				
				tempStack.push(botHalf.pop());
				
				if(tempStack.peek().equals("Ketchup") || tempStack.peek().equals("Mustard")){
					
					tempStack.pop();
					
				}
				
			}
			
			while(!tempStack.isEmpty()){
				
				botHalf.push(tempStack.pop());
				
			}
			
		}
		
	}
	public void addIngredient(String type){
		
		if(type.equals("Cheddar")){
			
			MyStack<String> tempStack = new MyStack<String>();
			while(!botHalf.isEmpty()){
				
				tempStack.push(botHalf.pop());
				
				if(tempStack.peek().equals(patty)){
					
					tempStack.push("Cheddar");
					
					break;
					
				}
				
			}
			while(!tempStack.isEmpty()){
				
				botHalf.push(tempStack.pop());
				
			}
			
		}
		
		if(type.equals("Mozzarella")){
			
			MyStack<String> tempStack = new MyStack<String>();
			while(!botHalf.isEmpty()){
				
				tempStack.push(botHalf.pop());
				
				if(tempStack.peek().equals(patty)){

					if(botHalf.isEmpty()){
						
						tempStack.push("Mozzarella");
					
					}else{
						
						tempStack.push(botHalf.pop());
						if(tempStack.peek().equals("Cheddar")){
							
							tempStack.push("Mozzarella");
						
						}else{
							
							botHalf.push(tempStack.pop());
							tempStack.push("Mozzarella");
							
							break;
							
						}
						
					}
					
				}
				
			}
			while(!tempStack.isEmpty()){
				
				botHalf.push(tempStack.pop());
				
			}
			
		}
		
		if(type.equals("Pepperjack")){
			
			MyStack<String> tempStack = new MyStack<String>();
			while(!botHalf.isEmpty()){
				
				tempStack.push(botHalf.pop());
				
				if(botHalf.isEmpty()){

					tempStack.push("Pepperjack");
					
				}
				
			}
			while(!tempStack.isEmpty()){
				
				botHalf.push(tempStack.pop());
				
			}
			
		}
		
		if(type.equals("Lettuce")){
			
			MyStack<String> tempStack = new MyStack<String>();
			while(!topHalf.isEmpty()){
				
				tempStack.push(topHalf.pop());
				
				if(tempStack.peek().equals("Baron-Sauce") || tempStack.peek().equals("Mayonnaise") || tempStack.peek().equals("Bun")){
					
					topHalf.push(tempStack.pop());;
					tempStack.push("Lettuce");
					break;
					
				}
			
			}
			while(!tempStack.isEmpty()){
				
				topHalf.push(tempStack.pop());
				
			}
			
		}
		
		if(type.equals("Tomato")){
			
			MyStack<String> tempStack = new MyStack<String>();
			while(!topHalf.isEmpty()){
				
				tempStack.push(topHalf.pop());
				
				if(tempStack.peek().equals("Lettuce") || tempStack.peek().equals("Baron-Sauce")
						|| tempStack.peek().equals("Mayonnaise") || tempStack.peek().equals("Bun")){
					
					topHalf.push(tempStack.pop());;
					tempStack.push("Tomato");
					break;
					
				}
				
			}
			while(!tempStack.isEmpty()){
				
				topHalf.push(tempStack.pop());
				
			}
			
		}
		
		if(type.equals("Onions")){

			MyStack<String> tempStack = new MyStack<String>();
			
			if(topHalf.peek().equals(patty)){

				tempStack.push(topHalf.pop());

				if(topHalf.peek().equals(patty)){

					tempStack.push(topHalf.pop());
					tempStack.push("Onions");
					
				}else{

					tempStack.push("Onions");
				}

			}else{

				tempStack.push("Onions");

			}
			
			while(!tempStack.isEmpty()){
				
				topHalf.push(tempStack.pop());
				
			}
			
		}
		
		if(type.equals("Pickle")){
			
			MyStack<String> tempStack = new MyStack<String>();
			while(!topHalf.isEmpty()){
				
				tempStack.push(topHalf.pop());
				
				if(topHalf.isEmpty()){
					
					tempStack.push("Pickle");
					
				}
				
			
			}
			while(!tempStack.isEmpty()){
				
				topHalf.push(tempStack.pop());
				
			}
			
		}
		
		if(type.equals("Mushrooms")){
			
			MyStack<String> tempStack = new MyStack<String>();
			while(!botHalf.isEmpty()){
				
				tempStack.push(botHalf.pop());
				
				if(botHalf.peek().equals(patty)){
					
					tempStack.push("Mushrooms");
					break;
				}
				
			
			}
			while(!tempStack.isEmpty()){
				
				botHalf.push(tempStack.pop());
				
			}
			
		}
		
		if(type.equals("Ketchup")){
			
			MyStack<String> tempStack = new MyStack<String>();
			while(!botHalf.isEmpty()){
				
				tempStack.push(botHalf.pop());
				
				if(tempStack.peek().equals("Bun")){
					
					tempStack.push("Ketchup");
					break;
				}
				
			}
			while(!tempStack.isEmpty()){
				
				botHalf.push(tempStack.pop());
				
			}
			
		}
		
		if(type.equals("Mustard")){
			
			MyStack<String> tempStack = new MyStack<String>();
			while(!botHalf.isEmpty()){
				
				tempStack.push(botHalf.pop());
				
				if(tempStack.peek().equals("Bun")){
					
					if(botHalf.peek().equals("Ketchup")){
						
						tempStack.push(botHalf.pop());
						tempStack.push("Mustard");
						break;
						
					}else{
						
						tempStack.push("Mustard");
						break;
						
					}
			
				}
				
			}
			while(!tempStack.isEmpty()){
				
				botHalf.push(tempStack.pop());
				
			}
		
		}
		
		if(type.equals("Mayonnaise")){
			
			MyStack<String> tempStack = new MyStack<String>();
			while(!topHalf.isEmpty()){
				
				tempStack.push(topHalf.pop());
				
				if(tempStack.peek().equals("Bun")){
					
					topHalf.push(tempStack.pop());
					tempStack.push("Mayonnaise");
					break;
				}
				
			}
			while(!tempStack.isEmpty()){
				
				topHalf.push(tempStack.pop());
				
			}
			
			
		}
		
		if(type.equals("Baron-Sauce")){
			
			MyStack<String> tempStack = new MyStack<String>();
			while(!topHalf.isEmpty()){
				
				tempStack.push(topHalf.pop());
				
				if(tempStack.peek().equals("Mayonnaise")){
					
					topHalf.push(tempStack.pop());
					tempStack.push("Baron-Sauce");
					break;
			
				}else if(tempStack.peek().equals("Bun")){
					
					topHalf.push(tempStack.pop());
					tempStack.push("Baron-Sauce");
					break;
					
				}
				
			}
			while(!tempStack.isEmpty()){
				
				topHalf.push(tempStack.pop());
				
			}
			
		}
		
	}
	public void removeIngredient(String type){

		if(type.equals("Cheddar")){
			
			MyStack<String> tempStack = new MyStack<String>();
			while(!botHalf.isEmpty()){
				
				tempStack.push(botHalf.pop());
				
				if(tempStack.peek().equals("Cheddar")){
					
					tempStack.pop();
					
					break;
					
				}
				
			}
			while(!tempStack.isEmpty()){
				
				botHalf.push(tempStack.pop());
				
			}
			
		}
		
		if(type.equals("Mozzarella")){
			
			MyStack<String> tempStack = new MyStack<String>();
			while(!botHalf.isEmpty()){
				
				tempStack.push(botHalf.pop());
				
				if(tempStack.peek().equals("Mozzarella")){
					
					tempStack.pop();
					
					break;
					
				}
				
			}
			while(!tempStack.isEmpty()){
				
				botHalf.push(tempStack.pop());
				
			}
			
		}
		
		if(type.equals("Pepperjack")){
			
			MyStack<String> tempStack = new MyStack<String>();
			while(!botHalf.isEmpty()){
				
				tempStack.push(botHalf.pop());
				
				if(tempStack.peek().equals("Pepperjack")){
					
					tempStack.pop();
					
					break;
					
				}
				
			}
			while(!tempStack.isEmpty()){
				
				botHalf.push(tempStack.pop());
				
			}
			
		}
		
		if(type.equals("Lettuce")){
			
			MyStack<String> tempStack = new MyStack<String>();
			while(!topHalf.isEmpty()){
				
				tempStack.push(topHalf.pop());
				
				if(tempStack.peek().equals("Lettuce")){
					
					tempStack.pop();
					
					break;
					
				}
				
			}
			while(!tempStack.isEmpty()){
				
				topHalf.push(tempStack.pop());
				
			}
			
		}
		
		if(type.equals("Tomato")){
			
			MyStack<String> tempStack = new MyStack<String>();
			while(!topHalf.isEmpty()){
				
				tempStack.push(topHalf.pop());
				
				if(tempStack.peek().equals("Tomato")){
					
					tempStack.pop();
					
					break;
					
				}
				
			}
			while(!tempStack.isEmpty()){
				
				topHalf.push(tempStack.pop());
				
			}
			
		}
		
		if(type.equals("Onions")){
			
			MyStack<String> tempStack = new MyStack<String>();
			while(!topHalf.isEmpty()){
				
				tempStack.push(topHalf.pop());
				
				if(tempStack.peek().equals("Onions")){
					
					tempStack.pop();
					
					break;
					
				}
				
			}
			while(!tempStack.isEmpty()){
				
				topHalf.push(tempStack.pop());
				
			}
			
		}
		
		if(type.equals("Pickle")){
			
			MyStack<String> tempStack = new MyStack<String>();
			while(!topHalf.isEmpty()){
				
				tempStack.push(topHalf.pop());
				
				if(tempStack.peek().equals("Pickle")){
					
					tempStack.pop();
					
					break;
					
				}
				
			}
			while(!tempStack.isEmpty()){
				
				topHalf.push(tempStack.pop());
				
			}
			
		}
		
		if(type.equals("Mushrooms")){
			
			MyStack<String> tempStack = new MyStack<String>();
			while(!botHalf.isEmpty()){
				
				tempStack.push(botHalf.pop());
				
				if(tempStack.peek().equals("Mushrooms")){
					
					tempStack.pop();
					
					break;
					
				}
				
			}
			while(!tempStack.isEmpty()){
				
				botHalf.push(tempStack.pop());
				
			}
			
		}
		
		if(type.equals("Ketchup")){
			
			MyStack<String> tempStack = new MyStack<String>();
			while(!botHalf.isEmpty()){
				
				tempStack.push(botHalf.pop());
				
				if(tempStack.peek().equals("Ketchup")){
					
					tempStack.pop();
					
					break;
					
				}
				
			}
			while(!tempStack.isEmpty()){
				
				botHalf.push(tempStack.pop());
				
			}
			
		}
		
		if(type.equals("Mustard")){
			
			MyStack<String> tempStack = new MyStack<String>();
			while(!botHalf.isEmpty()){
				
				tempStack.push(botHalf.pop());
				
				if(tempStack.peek().equals("Mustard")){
					
					tempStack.pop();
					
					break;
					
				}
				
			}
			while(!tempStack.isEmpty()){
				
				botHalf.push(tempStack.pop());
				
			}
			
		}
		
		if(type.equals("Mayonnaise")){
			
			MyStack<String> tempStack = new MyStack<String>();
			while(!topHalf.isEmpty()){
				
				tempStack.push(topHalf.pop());
				
				if(tempStack.peek().equals("Mayonnaise")){
					
					tempStack.pop();
					
					break;
					
				}
				
			}
			while(!tempStack.isEmpty()){
				
				topHalf.push(tempStack.pop());
				
			}
			
		}
		
		if(type.equals("Baron-Sauce")){
			
			MyStack<String> tempStack = new MyStack<String>();
			while(!topHalf.isEmpty()){
				
				tempStack.push(topHalf.pop());
				
				if(tempStack.peek().equals("Baron-Sauce")){
					
					tempStack.pop();
					
					break;
					
				}
				
			}
			while(!tempStack.isEmpty()){
				
				topHalf.push(tempStack.pop());
				
			}
			
		}
			
	}
	public String toString(){
		
		MyStack<String> stack = new MyStack<String>();
		
		while(!botHalf.isEmpty()){
			
			stack.push(botHalf.pop());
			
		}
		
		while(!topHalf.isEmpty()){
			
			stack.push(topHalf.pop());
			
		}
		
		return "["+stack.toString()+"]";
		
	}
	
	private void makeBotHalf(boolean baron){
		botHalf = new MyStack<String>();
		
		if(baron){
		
			botHalf.push("Pepperjack");
			botHalf.push("Mozzarella");
			botHalf.push("Cheddar");
			botHalf.push(patty);
			botHalf.push("Mushrooms");
			botHalf.push("Mustard");
			botHalf.push("Ketchup");
			botHalf.push("Bun");

		}else{
			botHalf.push(patty);
			botHalf.push("Bun");
		}
		
	}
	
	private void makeTopHalf(boolean baron){
		
		topHalf = new MyStack<String>();
		if(baron){

			topHalf.push("Pickle");
			topHalf.push("Bun");
			topHalf.push("Mayonnaise");
			topHalf.push("Baron-Sauce");
			topHalf.push("Lettuce");
			topHalf.push("Tomato");
			topHalf.push("Onions");
	
		}else{
			
			topHalf.push("Bun");
			
		}
	
	}
	
}
