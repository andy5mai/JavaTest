package sample;

public class LineSample implements IExec
{

	@Override
	public void Exec()
	{
		char cPatchChar = ' ';
		
		ALineBuilder lineBuilder = new LineBuilder("key", cPatchChar, 25);
		lineBuilder.addField("type", cPatchChar, 4);
		
		lineBuilder.printTitle();
		lineBuilder.printSeparate();
		
		lineBuilder.fillContent("AVATAR");
		lineBuilder.fillContent("hash");
		lineBuilder.printContent();
		
		lineBuilder.fillContent("MTUMAIL");
		lineBuilder.fillContent("set");
		lineBuilder.printContent();
		
		lineBuilder.fillContent("INBOX");
		lineBuilder.fillContent("hash");
		lineBuilder.printContent();
		
		lineBuilder.fillContent("TEST");
		lineBuilder.fillContent("123456789");
		lineBuilder.printContent();
	}
	
}
