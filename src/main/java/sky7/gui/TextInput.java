package sky7.gui;

import com.badlogic.gdx.Input.TextInputListener;

// Get user input
public class TextInput implements TextInputListener {
	String input;
	GUI gui;
	
	public TextInput(GUI gui) {
	    this.gui = gui;
	}
	
	@Override
	public void input (String text) {
		input = text;
		System.out.println("Entered IP: " + input);
		gui.connectClient(input);
	}

	@Override
	public void canceled () {
	}

	public String getinput() {
		return input;
	}
}
