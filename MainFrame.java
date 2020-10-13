import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;
import javax.swing.border.LineBorder;
import java.awt.Font;
import javax.swing.JTextArea;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import javax.swing.border.CompoundBorder;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

public class MainFrame extends JFrame implements KeyListener, ActionListener{

	private static final long serialVersionUID = 1L;
	private static final String version = "v0.3 [BETA]";
	private JPanel contentPane;
	private JPanel topPanel;
	private JPanel middlePanel;
	private JPanel bottomPanel;
	private JPanel bottomLeftPanel;
	private JPanel bottomRightPanel;
	
	private CustomButton ummmButton;
	private CustomButton understandButton;
	private CustomButton okButton;
	private CustomButton noButton;
	private CustomButton huhButton;
	private CustomButton staresButton;
	private CustomButton questionsButton;
	private CustomButton clearButton;
	private CustomButton keyButton;
	private JButton reportButton;
	private JButton copyButton;
	private JButton saveButton;
	private JButton settingsButton;
	private List<CustomButton> buttons;
	
	private final Color COLOR_FOREGROUND = Color.WHITE;
	private final Color COLOR_BACKGROUND = new Color(128, 0, 0);
	
	private JTextArea textArea;
	
	private boolean isSaved = true;

	public MainFrame() {
		setIconImage(Toolkit.getDefaultToolkit().getImage("img/logo.png"));
		setBackground(Color.WHITE);
		setTitle("Staines Counter " + version);
		addKeyListener(this);
		setFocusable(true);
		requestFocus();
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(750, 500);
		setLocationRelativeTo(null);
		
		addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent windowEvent) {
		        if(isSaved == false) {
		        	int choice = JOptionPane.showConfirmDialog(null, "Do you want to save?", "Save", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
		        	if (choice == JOptionPane.YES_OPTION){
		            	saveAction();
		        	} else if (choice == JOptionPane.NO_OPTION){
		        		System.exit(0);
		        	} else {
		        		
		        	}
		        } else {
		        	System.exit(0);
		        }
		    }
		});
		
		contentPane = new JPanel();
		contentPane.setForeground(new Color(128, 0, 0));
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		
		setContentPane(contentPane);
		
		init();
	}
	
	public void init() {
		//Buttons
		ummmButton = new CustomButton("Ummm", "ummm...");
		ummmButton.setForeground(COLOR_FOREGROUND);
		ummmButton.setBackground(COLOR_BACKGROUND);
		ummmButton.addActionListener(this);
		
		understandButton = new CustomButton("Understand?", "understand?...");
		understandButton.setForeground(COLOR_FOREGROUND);
		understandButton.setBackground(COLOR_BACKGROUND);
		understandButton.addActionListener(this);
		
		okButton = new CustomButton("Ok", "ok...");
		okButton.setForeground(COLOR_FOREGROUND);
		okButton.setBackground(COLOR_BACKGROUND);
		okButton.addActionListener(this);
		
		noButton = new CustomButton("No?", "no...");
		noButton.setForeground(COLOR_FOREGROUND);
		noButton.setBackground(COLOR_BACKGROUND);
		noButton.addActionListener(this);
		
		huhButton = new CustomButton("Huh", "huh...");
		huhButton.setForeground(COLOR_FOREGROUND);
		huhButton.setBackground(COLOR_BACKGROUND);
		huhButton.addActionListener(this);
		
		staresButton = new CustomButton("*stares*", "*stares*...");
		staresButton.setForeground(COLOR_FOREGROUND);
		staresButton.setBackground(COLOR_BACKGROUND);
		staresButton.addActionListener(this);
		
		questionsButton = new CustomButton("Questions?", "Any questions?...");
		questionsButton.setForeground(COLOR_FOREGROUND);
		questionsButton.setBackground(COLOR_BACKGROUND);
		questionsButton.addActionListener(this);
		
		clearButton = new CustomButton("It is clear?", "Is it clear?...");
		clearButton.setForeground(COLOR_FOREGROUND);
		clearButton.setBackground(COLOR_BACKGROUND);
		clearButton.addActionListener(this);
		
		keyButton = new CustomButton("Key Point", "Did you understand this KEY POINT...");
		keyButton.setForeground(COLOR_FOREGROUND);
		keyButton.setBackground(COLOR_BACKGROUND);
		keyButton.addActionListener(this);
		
		buttons = new ArrayList<>();
		buttons.add(ummmButton);
		buttons.add(understandButton);
		buttons.add(okButton);
		buttons.add(noButton);
		buttons.add(huhButton);
		buttons.add(staresButton);
		buttons.add(questionsButton);
		buttons.add(clearButton);
		buttons.add(keyButton);
		
		copyButton = new JButton("Copy Text");
		copyButton.setForeground(COLOR_FOREGROUND);
		copyButton.setBackground(COLOR_BACKGROUND);
		copyButton.addActionListener(this);
		
		reportButton = new JButton("Get Report");
		reportButton.setForeground(COLOR_FOREGROUND);
		reportButton.setBackground(COLOR_BACKGROUND);
		reportButton.addActionListener(this);
		
		settingsButton = new JButton("");
		settingsButton.setForeground(Color.WHITE);
		settingsButton.setBackground(Color.WHITE);
		settingsButton.setIcon(new ImageIcon("img/settings.png"));
		settingsButton.setBounds(80, 0, 35, 35);
		settingsButton.setToolTipText("Settings");
		settingsButton.addActionListener(this);
		settingsButton.setBorderPainted(false);
		
		saveButton = new JButton("Save");
		saveButton.setForeground(COLOR_FOREGROUND);
		saveButton.setBackground(COLOR_BACKGROUND);
		saveButton.addActionListener(this);
		
		//Text Area
		textArea = new JTextArea();
		textArea.setFont(new Font("Dialog", Font.PLAIN, 14));
		textArea.setRows(20);
		textArea.setWrapStyleWord(true);
		textArea.setForeground(new Color(128, 0, 0));
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		
		//Panels
		topPanel = new JPanel();
		topPanel.setForeground(new Color(128, 0, 0));
		topPanel.setBackground(Color.WHITE);
		topPanel.setLayout(new GridLayout(0, 3, 10, 10));
		topPanel.add(ummmButton);
		topPanel.add(understandButton);
		topPanel.add(okButton);
		topPanel.add(noButton);
		topPanel.add(huhButton);
		topPanel.add(staresButton);
		topPanel.add(questionsButton);
		topPanel.add(clearButton);
		topPanel.add(keyButton);
		
		middlePanel = new JPanel();
		middlePanel.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), new LineBorder(new Color(0, 0, 0))));
		middlePanel.setForeground(new Color(128, 0, 0));
		middlePanel.setBackground(Color.WHITE);
		middlePanel.setLayout(new CardLayout(10, 10));
		middlePanel.add(textArea, "name_127379771693100");
		
		bottomLeftPanel = new JPanel();
		bottomLeftPanel.setBackground(Color.WHITE);
		FlowLayout fl_bottomLeftPanel = (FlowLayout) bottomLeftPanel.getLayout();
		fl_bottomLeftPanel.setHgap(25);
		bottomLeftPanel.add(copyButton);
		bottomLeftPanel.add(reportButton);
		bottomLeftPanel.add(saveButton);
		
		bottomRightPanel = new JPanel();
		bottomRightPanel.setBackground(Color.WHITE);
		bottomRightPanel.setLayout(null);
		bottomRightPanel.add(settingsButton);
		
		bottomPanel = new JPanel();
		bottomPanel.setForeground(new Color(128, 0, 0));
		bottomPanel.setBackground(Color.WHITE);
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));		
		bottomPanel.add(bottomLeftPanel);
		bottomPanel.add(bottomRightPanel);
		
		contentPane.add(topPanel, BorderLayout.NORTH);
		contentPane.add(middlePanel, BorderLayout.CENTER);
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
	}
	
	@Override
	public void actionPerformed (ActionEvent e) {
		if (e.getSource() == reportButton) {
			reportAction();
		} else if (e.getSource() == copyButton) {
			copyAction();
		} else if (e.getSource() == saveButton) {
			saveAction();
		} else if (e.getSource() == settingsButton) {
			settingsAction();
		} else {
			if (e.getSource() instanceof CustomButton) {
				CustomButton button = (CustomButton) e.getSource();
				textArea.setText(textArea.getText() + button.getMessage());
				button.inc();
				isSaved = false;
			}
		}
		
		this.requestFocus();
	}
	
	@Override
	public void keyPressed (KeyEvent e) {
		CustomButton selectedButton;
		
		if (e.getKeyCode() == KeyEvent.VK_NUMPAD1) {
			selectedButton = questionsButton;
		} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD2) {
			selectedButton = clearButton;
		} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD3) {
			selectedButton = keyButton;
		} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD4) {
			selectedButton = noButton;
		} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD5) {
			selectedButton = huhButton;
		} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD6) {
			selectedButton = staresButton;
		} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD7) {
			selectedButton = ummmButton;
		} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD8) {
			selectedButton = understandButton;
		} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD9) {
			selectedButton = okButton;
		} else {
			return;
		}
		
		textArea.setText(textArea.getText() + selectedButton.getMessage());
		selectedButton.inc();
		isSaved = false;
	}
	
	@Override
	public void keyTyped (KeyEvent e) {	}
	
	@Override
	public void keyReleased(KeyEvent e) { }
	
	public void reportAction() {
		StringBuilder sb = new StringBuilder();
		System.out.println(buttons.size());
		
		for (CustomButton b : buttons) {
			String buttonName = b.getText();
			String temp = buttonName.substring(0, 1);
			temp = temp.toUpperCase();
			
			String finalString = temp + buttonName.substring(1, buttonName.length()) + "(s): " + b.getCounter() + "\n";
			sb.append(finalString);
		}
		
		JOptionPane.showMessageDialog(null, sb, "Staines Counter Report", JOptionPane.PLAIN_MESSAGE);
	}
	
	public void copyAction() {
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(textArea.getText()), null);
	}
	
	public void saveAction() {
		try{
			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            Date date = new Date();
            
            File file = new File("logs");
            
            if (!file.exists()) {
            	file.mkdir();
            }
            
            BufferedWriter bw = new BufferedWriter (new FileWriter ("logs/" + dateFormat.format(date) + ".txt"));
            
            bw.write(dateFormat.format(date));
            bw.newLine();
            
            for (CustomButton b : buttons) {
            	String buttonName = b.getText();
    			String temp = buttonName.substring(0, 1);
    			temp = temp.toUpperCase();
    			
    			String finalString = temp + buttonName.substring(1, buttonName.length()) + "(s): " + b.getCounter() + "\n";
            	bw.write(finalString);
            }
          
            bw.close();
        } catch (Exception e){
            JOptionPane.showMessageDialog (null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
		
		isSaved = true;
	}
	
	public void settingsAction() {
		JOptionPane.showMessageDialog(null, "This feature is not yet available!", "Unavailable Feature", JOptionPane.PLAIN_MESSAGE);
	}
}