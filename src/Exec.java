import java.util.HashSet;
import java.util.Scanner;

import sample.CommandSample;
import sample.DynamicParamsSample;
import sample.DynamicSettingsSample;
import sample.GenericSample;
import sample.IExec;
import sample.ImmutableSample;
import sample.LineSample;
import sample.MethodSample;
import sample.MultiThreadSample;
import sample.SortSample;
import sample.TestEnumSample;
import sample.TestEnumSample.TestEnum;


public class Exec {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
//		IExec iexec = new ImmutableSample();
		
//		IExec iexec = new SortSample();
		
//		IExec iexec = new DynamicParamsSample();

//		IExec iexec = new TestEnumSample();
		
//		IExec iexec = new LineSample();
		
//		IExec iexec = new MethodSample();
		
//		IExec iexec = new GenericSample();
		
//		IExec iexec = new DynamicSettingsSample();
		
//		IExec iexec = new MultiThreadSample();
		
		IExec iexec = new CommandSample();
		
		iexec.Exec();
		
//		System.out.print("Input : ");
//		String str = new Scanner(System.in).nextLine();
//		System.out.println("you type in : " + str + ", is instance of whitespace ? " + str.trim().isEmpty());
//		System.out.println((" " + "123" + " ").trim() + 123456);
//		
//		System.out.println(Class.forName("[B").getName());
//		
//		
//		System.out.println(str.split(";").length + str.split(";")[0]);
//		str = "";
//		System.out.println(str.split(";").length + str.split(";")[0]);
//		
//		boolean b = false;
//		str = b + "";
//		
//		System.out.println(Boolean.valueOf(str));
		
//		Scanner scanner = new Scanner(System.in);
//		System.out.println(scanner.nextInt());
//		
//		String strTest = "123;";
//		strTest = strTest.substring(0,  strTest.length() - 1);
//		System.out.println(strTest);
//		String[] strs = strTest.split(";");
//		for(String str : strs)
//		{
//			System.out.println("str is " + str);
//		}
		
//		HashSet<String> set = new HashSet<String>();
//		set.add("123");
//		set.add("456");
		
		
	}

}
