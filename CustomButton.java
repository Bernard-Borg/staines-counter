import javax.swing.JButton;

public class CustomButton extends JButton {
	private static final long serialVersionUID = 1L;
	
	private String message;
	private int counter;
	
	public CustomButton(String name, String message) {
		super(name);
		this.message = message;
		this.counter = 0;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
	
	public void inc() {
		counter++;
	}
	
	public void dec() {
		counter--;
	}
}
