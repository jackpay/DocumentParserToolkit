package ac.uk.susx.tag.input;

public class CommandLineOption {
	
	private String cmd;
	private String description;

	public CommandLineOption(String cmd, String description) {
		this.cmd = cmd;
		this.description = description;
	}
	
	public String getCommand() {
		return cmd;
	}
	
	public String getDescription() {
		return description;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CommandLineOption) {
			CommandLineOption cml = (CommandLineOption) obj;
			if(cml.getCommand().equals(this.getCommand()) && this.getDescription().equals(this.getDescription())){
				return true;
			}
		}
		return false;
	}
	
	

}
