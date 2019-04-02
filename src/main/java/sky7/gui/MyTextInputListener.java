package sky7.gui;

import com.badlogic.gdx.Input.TextInputListener;

// Get user input
public class MyTextInputListener implements TextInputListener {
	String input;
	
	@Override
	public void input (String text) {
		input = text;
		System.out.println(input);
	}

	@Override
	public void canceled () {
	}

	public String getinput() {
		return input;
	}
}
