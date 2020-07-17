package pers.chemyoo.core.utils;

import java.util.ArrayList;
import java.util.List;

public class CheckResults
{
	private boolean hasError = false;
	
	private List<String> messages = new ArrayList<>();

	public List<String> getMessages()
	{
		return messages;
	}

	public void addMessages(String message)
	{
		if(!isBlank(message)) {
			this.messages.add(message);
			if(!hasError) {
				this.hasError = true;
			}
		}
	}
	
	public static boolean isBlank(String str) {
		return str == null || str.trim().length() == 0;
	}

	public boolean isHasError()
	{
		return hasError;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("CheckResults [hasError=");
		builder.append(hasError);
		builder.append(", messages=");
		builder.append(messages);
		builder.append("]");
		return builder.toString();
	}
	
}
