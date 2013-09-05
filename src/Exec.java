import sample.IExec;
import sample.ImmutableSample;
import sample.SortSample;
import sample.TestEnumSample.TestEnum;


public class Exec {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		IExec iexec = new ImmutableSample();
//		iexec.Exec();
		
		IExec iexec = new SortSample();
		iexec.Exec();
	}

}
