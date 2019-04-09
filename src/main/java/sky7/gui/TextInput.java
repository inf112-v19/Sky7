package sky7.gui;

import com.badlogic.gdx.Input.TextInputListener;

// Get user input
public class TextInput implements TextInputListener {
	String input;
	
	@Override
	public void input (String text) {
		input = text;
		System.out.println("Entered IP: " + input);
	}

	@Override
	public void canceled () {
	}

	public String getinput() {
		return input;
	}
}
