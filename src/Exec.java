import java.util.HashSet;

import sample.DynamicParamsSample;
import sample.IExec;
import sample.ImmutableSample;
import sample.SortSample;
import sample.TestEnumSample;
import sample.TestEnumSample.TestEnum;


public class Exec {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		IExec iexec = new ImmutableSample();
		
//		IExec iexec = new SortSample();
		
//		IExec iexec = new DynamicParamsSample();
//		
		IExec iexec = new TestEnumSample();
		iexec.Exec();
		
//		HashSet<String> set = new HashSet<String>();
//		set.add("123");
//		set.add("456");
		
		
	}

}
