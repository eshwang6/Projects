package Project;

public class MyStack<Type>{
	
	private MyNode<Type> top;
	
	private int sizeCount;
	
	public MyStack(){
		
		top = null;
		
		sizeCount = 0;
		
	}
	
	public boolean isEmpty(){
		
		return top == null;
		
	}
	
	public void push(Type item){
		
		MyNode<Type> temp = new MyNode<Type>(item);
		temp.next = top;
		top = temp;
		
		sizeCount++;
		
	}
	
	public Type pop(){
		if (isEmpty()) return null;
		MyNode<Type> temp;
		temp = top;
		top = top.next;
		sizeCount--;
		
		return temp.node;
		
	}
	
	public Type peek(){
		
		if(isEmpty()){
			return null;		
		}else{
			return top.node;
		}
		
	}
	
	public int size(){
		
		return sizeCount;
		
	}
	
	public String toString(){
		
		String display = "";
		MyNode <Type> current = top;
		
		while (current != null){
			
			display += current.node.toString();
			if (current.next != null) display += ", ";
			current = current.next;
			
		}
		
		return display;
		
	}
	
}

class MyNode<Type>{
	
	public Type node;
	
	public MyNode<Type> next;
	
	public MyNode (Type input){
		
		node = input;
		next = null;
		
	}
	
}
	



