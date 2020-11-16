import java.util.Scanner;

/*Singh Karanbir
The program converts any infix expression in a postfix expression
3 Classes with Instances: Node,Stack,Queue
2 classes with static methods: main and postFix
 */
//Using template T for storing characters and ints during the conversion and evaluation
class Node<T> {
    private T value;
    private Node next;
    //contructor1
    Node(T n, Node<T> ntx){
        value = n;
        next = ntx;
    }
    //constructor 2
    Node(T n){
        value = n;
        next = null;
    }

    public T getValue(){
        return value;
    }

    public void setValue(T val){ value = val; }

    public Node getNext(){
        return next;
    }
    public void setNext(Node ntx) {
        next = ntx;
    }
}

class Stack<T>{
    private Node top;
    public void push(T val){
        if(top == null){
            top = new Node(val);
        }
        else{
            Node tmp = new Node(val);
            Node tmp2 = top;
            top = tmp;
            top.setNext(tmp2);
        }
    }
    public void pop(){
        top = top.getNext();
    }
    public T peek(){
        return (T) top.getValue();
    }
    public boolean isEmpty(){
        if (top == null)
            return true;
        else
            return false;
    }
}

class Queue{
    private Node front,rear;
    public void enqueue(char val){
        if (front == null && rear == null){
            front = new Node(val);
            rear = front;
        }
        else{
            rear.setNext(new Node(val));
            rear = rear.getNext();
        }
    }
    public char dequeue(){
        char val = (char) front.getValue();
        front = front.getNext();
        return val;
    }
    public void getQueue(){
        Node tmp = front;
        while(tmp!= null){
            System.out.print(tmp.getValue());
            tmp = tmp.getNext();
        }
    }
    public void getRear(){System.out.println(rear.getValue()); }
    public char peek(){return (char) front.getValue();}
    public boolean isEmpty(){
        if (front == null)
            return true;
        return false;
    }
}
class postFix {
    private static Queue que = new Queue();
    public static String infixToPostFix(String expression){
        Stack<Character> stk = new Stack(); // Declare stack as a stack of characters
        String postFixExpression = new String(); // use the string to store the expression in the string and use it in future
        for(int i = 0; i < expression.length();i++){
            if(Character.isDigit(expression.charAt(i)) == true){
                postFixExpression+=expression.charAt((i));//if the character is digit, just store it
                que.enqueue(expression.charAt(i));
            }
            else if(expression.charAt(i) == '(') //open parenthesis will always be pushed in the stack
                stk.push(expression.charAt(i));
            else if (expression.charAt(i) == ')'){//pop everything out until the open parenthesis is not found
                while(stk.peek() != '(' && stk.isEmpty() == false){
                    postFixExpression+=stk.peek();
                    que.enqueue(stk.peek());
                    stk.pop();
                }
                if(stk.peek() == '(')
                    stk.pop();
            }
            else{
                if(stk.isEmpty() == true)
                    stk.push(expression.charAt(i));
                else if(precedence(expression.charAt(i)) > precedence(stk.peek()))//check precedence, if the current operator is greater it will always be pushed in the stack
                    stk.push(expression.charAt(i));
                else{//if precedence is lower than pop everything out
                    while(stk.isEmpty() == false || (precedence(expression.charAt(i)) > precedence(stk.peek()))){//only stop when stack is empty or when the top has higher precedence than the current operator
                        postFixExpression+=stk.peek();
                        que.enqueue(stk.peek());
                        stk.pop();
                    }
                    stk.push(expression.charAt(i));
                }
            }
        }
        if(stk.isEmpty() == false)
            while(stk.isEmpty() == false){
                postFixExpression+=stk.peek();
                que.enqueue(stk.peek());
                stk.pop();
            }
        return postFixExpression;
    }

    public static int precedence(char operator){
        if(operator == '=')
            return 1;
        if(operator == '(')
            return 1;
        if(operator == '+' || operator == '-')
            return 2;
        if(operator == '*' || operator == '/')
            return 3;
        return 0;
    }
    public static void evaluatePostFix(){
        char operator;
        int leftOperand;
        int rightOperand;
        //Queue que = new Queue();
        Stack<Integer> stk = new Stack();//This time the stack will be a stack of integers
        while(que.isEmpty() == false){
            if(Character.isDigit(que.peek())){// if the character is digit then parse it as an int an push into the stack
                stk.push(Integer.parseInt(String.valueOf(Character.getNumericValue(que.peek()))));
                que.dequeue();
            }
            else{// if character is operator than pop two elements from the stack, first one as rightOp and second as left, calculate the result and push onto stack
                operator = que.peek();
                que.dequeue();
                rightOperand = stk.peek();
                stk.pop();
                leftOperand = stk.peek();
                stk.pop();
                stk.push(performOperation(operator,leftOperand,rightOperand));
            }
        }
        //Last item in the stack will always be the result
        System.out.println("Result: "+stk.peek());
    }
    public static int performOperation(char op, int l, int r){
        if(op == '+')
            return l + r;
        if(op == '-')
            return l-r;
        if(op == '/')
            return l/r;
        if(op == '*')
            return l*r;
        return 0;
    }
}
public class Driver {
    public static void main(String args[]){
        Scanner read = new Scanner(System.in);
        System.out.println("Please insert an infix expression:");
        String infixExpression = read.nextLine();
        String postFixExpression = new String();
        postFixExpression = postFix.infixToPostFix(infixExpression);
        System.out.println("The conversion to Post Fix expression is: "+postFixExpression);
        postFix.evaluatePostFix();
    }
}
