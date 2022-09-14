package io.spring.initializr.generator.spring.docker;

import java.util.List;

/**
 * The {@link Command} is executable commands
 * 
 * @author Sayeed AP
 */
public class Command {

	private CommandType type = CommandType.ENTRYPOINT;

	private String command;

	private List<String> parameter;

	public Command(CommandType type, String command, List<String> parameter) {
		super();
		this.type = type;
		this.command = command;
		this.parameter = parameter;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public List<String> getParameter() {
		return parameter;
	}

	public void setParameter(List<String> parameter) {
		this.parameter = parameter;
	}

	public CommandType getType() {
		return type;
	}

	public void setType(CommandType type) {
		this.type = type;
	}

	/**
	 * @author Sayeed AP
	 */
	enum CommandType {

		CMD,

		ENTRYPOINT,

		RUN

	}

}