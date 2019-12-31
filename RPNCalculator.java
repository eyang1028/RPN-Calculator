import java.util.Scanner; //will use scanner later

class Stack { //this is my previously written stack class
	double Stack[];
	int StackSize;
	int StackPosition;
	
	Stack(int size) { //superclass constructor here
		if(size > 0) {
			Stack = new double[size];
			StackSize = size;
			StackPosition = -1; //stack is empty
		} else System.out.println("Stack has to be positive.");
	}
	
	void push(double num) {
		if(StackPosition >= StackSize - 1 || StackPosition < -1) { //if stack is full, leave message
			System.out.println("Stack is full or corrupted.");
			return;
		} else Stack[++StackPosition] = num; //if none of the previous conditions are true, just put num on top
	}
	
	boolean isStackEmpty() { //a method of checking if stack is empty
		if(StackPosition <= -1) //check if stack is empty
			return true;
		else
			return false;
	}
	
	boolean isStackFull() { //this is another method of checking if stack is full
		if(StackPosition >= StackSize - 1) //check if stack is full
			return true;
		else
			return false;
	}

	double pop() {
		if(StackPosition <= -1) { //check if stack is empty again
			System.out.println("Stack is empty.");
			return -1; //return -1 b/c this needs to return an integer
		} else { //otherwise, take out number at top of stack
			double ret;
			ret = Stack[StackPosition--];
			return ret;
		}
	}
	
	void PrintStack() {
		for(int i = StackPosition; i >= 0; i--) //print out the integers in stack
			System.out.println("Stack[" + i + "] = " + Stack[i]);
	}
}

class rpnCalculator extends Stack { //I extended the Stack

	rpnCalculator(int size) { //subclass constructor here
		super(size); //inherit from the superclass
	}

	void Calculate() //define method called Calculate
	throws java.io.IOException { //I will be reading in a string
		char a;
		double val = 0;
		boolean dec = false;
		double factor = 10;
		boolean inNumProc = false; //if there's a number in process; before the number encounters a space
		
		System.out.println("Please enter an RPN operation or type q to quit."); //instructions here
		
		while(true) {
			a = (char) System.in.read(); //read in RPN operation
			if(a >= '0' && a <= '9' || a == '.' || a == ' ') {
				if(a == '.') { //if character is a decimal
					dec = true;
					factor = 0.1; //multiply by 0.1 if the number is after a decimal point
				} else if(a == ' ') {
					if (inNumProc) {
						push(val); 
						inNumProc = false; //after you push, you won't have the number in process anymore
						dec = false; //reset decimal
					}
				} else { 
					if(!inNumProc) { //initiate the first number entered
						inNumProc = true; //set it true
						val = a - 48; //48 is 0 so you have to subtract the character by it to get the correct number
					} else { //if you've already entered digits
						if(!dec) { //if not a decimal
							val = val * 10 + (a - 48); //moving onto the next digit
						} else {
							val += factor * (a - 48); //if it's a decimal
							factor *= 0.1; //move onto next decimal place
						}
					}
				}
			} else if(a == '+') { //addition
				if(inNumProc) {
					val += pop();
					inNumProc = false;
					dec = false;
				} else val = pop() + pop();
				push(val);
			} else if(a == '-') { //subtraction
				if(inNumProc) {
					val = pop() - val;
					inNumProc = false;
					dec = false;
				} else {
					val = pop();
					val = pop() - val;
				}
				push(val);
			} else if(a == '*') { //multiplication
				if(inNumProc) {
					val *= pop();
					inNumProc = false;
					dec = false;
				} else {
					val = pop();
					val *= pop();
				}
				push(val);
			} else if(a == '/') { //division
				if(inNumProc) {
					val = pop() / val;
					inNumProc = false;
					dec = false;
				} else {
					val = pop();
					val = pop() / val;
				}
				push(val);
			} else if(a == 13) { //once user enters their RPN operation
				val = pop();
				if(isStackEmpty()) System.out.println("The result is: " + val); //stack has to be empty after entering RPN operation
				else {
					PrintStack();
					System.out.println("Invalid RPN."); //invalid RPN operation if stack is not empty
				}
				a = (char) System.in.read(); //throw away the enter and rescan for another RPN operation
			} else if(a == 'q') { //q for quit
				System.out.println("Exiting the program.");
				break;
			} else { //simple error handling
				System.out.println("Wrong input.");
				break;
			}
		}
	}
}

class ExecuteCalculator { //class for executing the calculator
	public static void main(String args[])
	throws java.io.IOException {
		rpnCalculator myCalculator = new rpnCalculator(25);
		
		myCalculator.Calculate(); //call the Calculate method
	}
}
