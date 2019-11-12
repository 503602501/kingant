package key;

import net.sourceforge.htmlunit.corejs.javascript.tools.debugger.Main;

public class FF {

		{
	        System.out.println("NEW 对象，只会执行一次，相当于static我是父类-----大括号中的代码");
	    }
		
		static{
			System.out.println("我是父类---  ----static代码块");
		}
	    
	    
	    public FF(){
	        System.out.println("我是父类A----------的构造方法");
	    }
	    
	    public static void main(String[] args) {
	    	new FF();
	    	StringBuffer s= new StringBuffer();
	    	
		}
}
