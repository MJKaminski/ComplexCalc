package pl.edu.pw.elka.stud.M.J.Kaminski;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


/**
 * Klasa odpowiedzialna z czesc  wizualna- widok programu. Zawiera metody pozwalajace na modyfikacje wyswietlanych danych a takze nasluchuje zdarzen i powiadamia o nich
 * kontroler. 
 * @author Michal Kaminski
 *
 */

public class CalcView{
	

	private JFrame frame;
	private JPanel panel;
	private JPanel screen;
	private	JTextField real;
	private	JTextField imaginary;
	private JTextField currField;
	private CommandListener cmdList;
	private NumberListener numList;
	private CalcController controller;
	private Keyboard keyboard;
	
	private final int HEIGHT = 250;
	private final int WIDTH = 500;
	
	public CalcView(){
		
		frame = new JFrame("Kalkulator Liczb Zespolonych");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		panel = new JPanel(new GridBagLayout());
		screen = new JPanel();
		cmdList = new CommandListener();
		numList = new NumberListener();
		keyboard = new Keyboard();
		
		Font f = new Font("Sarif", 0, 25);
		
		real = new JTextField(10);
		real.setEditable(false);
		real.setHorizontalAlignment(JTextField.CENTER);
		real.setFont(f);
		real.setBackground(Color.CYAN);
		real.setFocusTraversalKeysEnabled(false);
		real.addMouseListener(new ScreenClickedListener());
		real.addKeyListener(keyboard);

		imaginary = new JTextField(10);
		imaginary.setEditable(false);
		imaginary.setHorizontalAlignment(JTextField.CENTER);
		imaginary.setFont(f);
		imaginary.setBackground(Color.WHITE);
		imaginary.setFocusTraversalKeysEnabled(false);
		imaginary.addMouseListener(new ScreenClickedListener());
		imaginary.addKeyListener(keyboard);

		currField = real;
		screen.add (real);
		
		JLabel l = new JLabel("+");
		l.setFont(f);
		screen.add(l);
		

		screen.add (imaginary);
		
		l = new JLabel("i");
		l.setFont(f);
		screen.add(l);

		
		for (int i = 1, col = 0, row = 2; i<=9 ; ++i)
		{
			if(i%3 ==0)
				addNumberButton(""+i, new GBC(col,row,1,1).setInsets(0, 0, 0, 10));
			
			else
				addNumberButton(""+i, new GBC(col,row,1,1));
			
			col = ++col % 3;
			if(col == 0)
				--row;
		}
		
		addNumberButton("0", new GBC(1,3,1,1));
		
		addCommandButton("+", new GBC(3,0,1,1));
		addCommandButton("+/-", new GBC(4,0,1,1).setInsets(0, 0, 0, 10));
		addCommandButton("sin",new GBC(5,0,1,1));
		addCommandButton("-", new GBC(3,1,1,1));
		addCommandButton("\u221A", new GBC(4,1,1,1).setInsets(0, 0, 0, 10));
		addCommandButton("cos",new GBC(5,1,1,1));
		addCommandButton("*", new GBC(3,2,1,1));
		addCommandButton("=", new GBC(4,2,1,2).setInsets(0, 0, 0, 10));
		addCommandButton("tg",new GBC(5,2,1,1));
		addCommandButton("CE", new GBC(0,3,1,1));
		addCommandButton(",", new GBC(2,3,1,1).setInsets(0, 0, 0, 10));
		addCommandButton("/", new GBC(3,3,1,1));
		addCommandButton("ctg",new GBC(5,3,1,1));
		

		frame.add(screen, BorderLayout.NORTH);
		frame.add(panel, BorderLayout.CENTER);
		
		frame.setBounds(0, 0, WIDTH, HEIGHT);
		frame.addKeyListener(keyboard);
		frame.setResizable(false);
		frame.setFocusTraversalKeysEnabled(false);
		frame.setVisible(true);
		
	}
	
	/**
	 * Ustawia kontrolera widoku
	 * @param c odnosnik do kontrolera 
	 */
	
	public void setController(CalcController c){
		controller = c;
	}
	
	/**
	 * Ustawia i podswietla aktywny ekran
	 * @param t Pole do ustawienia
	 */
	
	public void setActiveScreen(ActiveField t){
		switch (t){
		case Real:
			real.setBackground(Color.CYAN);
			imaginary.setBackground(Color.WHITE);
			break;
			
		case Imaginary:
			real.setBackground(Color.WHITE);
			imaginary.setBackground(Color.CYAN);
			break;
		}
	}
	
	
	/**
	 * Modyfikuje zawartosc wybranego wyswietlacza
	 * @param screen wyswietlacz do ustawienie
	 * @param value wartosc do ustawienia
	 */
	public void setScreenValue(ActiveField screen, String value){
		if(screen== ActiveField.Real){
			real.setText(value);
		}
		
		else{
			imaginary.setText(value);
		}
	}
	/**
	 * Wyswietla na obu wyswietlaczach komunikat o bledzie
	 */
	public void setError(){
		real.setText("ERROR");
		imaginary.setText("ERROR");
	}
	
	/**
	 * Metoda ulatwiajaca utworzenie przycisku numerycznego o numerze num i parametrach constrain
	 * @param num numer do nastawienia jako nazwa
	 * @param constrain zbior parametrow uzywanych przez klase GBC
	 */
	private void addNumberButton(String num, GBC constrain){
		JButton b = new JButton(num);
		b.setFocusTraversalKeysEnabled(false);
		b.addActionListener(numList);
		b.addKeyListener(keyboard);
		panel.add(b, constrain);
	}
	
	/**
	 * Metoda ulatwiajaca utworzenie przycisku akcji o nazwie i parametrach constrain
	 * @param num akcja do nastawienia jako nazwa
	 * @param constrain zbior parametrow uzywanych przez klase GBC
	 */
	private void addCommandButton(String num, GBC constrain){
		JButton b = new JButton(num);
		b.setFocusTraversalKeysEnabled(false);
		b.addActionListener(cmdList);
		b.addKeyListener(keyboard);
		panel.add(b, constrain);
	}
	
	/**
	 * Klasa pomocznicza ulatwiajaca prace z klasa GridBagConstraints
	 * @author Michal Kaminski
	 *
	 */
	private class GBC extends GridBagConstraints{
		private static final long serialVersionUID = -7281809264301550223L;
		
		/**
		 * Konstruktor tworzacy uproszczony element typu GridBagConstraints
		 * @param grix pozycja x w siatce
		 * @param griy pozycja y w siatce
		 * @param gridwidth szerokosc elementu
		 * @param gridheight wysokosc elementu
		 */
		public GBC(int gridx, int gridy, int gridwidth, int gridheight){
			this.gridx = gridx;
			this.gridy = gridy;
			this.gridwidth = gridwidth;
			this.gridheight = gridheight;
			this.weightx = 100;
			this.weighty = 100;
			this.fill = BOTH;
		}
		
		/**
		 * Metoda ustawiajaca odstep danego elementu od pozostalych w siatce
		 * @param top odleglosc u gory
		 * @param left odleglosc na lewo
		 * @param bottom odleglosc na dole
		 * @param right odleglosc na prawa
		 * @return obiekt GBC o podanych odstepach
		 */
		public GBC setInsets(int top, int left, int bottom, int right){
			this.insets = new Insets(top, left, bottom, right);
			return this;
		}
		
	}
	
	
	/**
	 * Nasluchiwacz przyciskow numerycznych
	 * @author Michal Kaminski
	 *
	 */
	
	private class NumberListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			JButton b = (JButton)arg0.getSource();
			controller.enterNumber(Integer.parseInt(b.getText()));
			
		}
		
	}
	
	/**
	 * Nasluchiwacz przyciskow akcji
	 * @author Michal Kaminski
	 *
	 */
	
	private class CommandListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			JButton b = (JButton)arg0.getSource();
			controller.execute(b.getText());
		}
		
	}
	
	/**
	 * Nasluchiwacz klawiatury
	 * @author Michal Kaminski
	 *
	 */
	
	private class Keyboard implements KeyListener{

		public void keyTyped(KeyEvent e) {
			if( e.getKeyCode() == KeyEvent.VK_TAB){
				if(currField == imaginary){
					controller.screenClicked(ActiveField.Real);
					currField = real;
				}
				
				else{
					controller.screenClicked(ActiveField.Imaginary);
					currField = imaginary;
				}
			}
			
		}

		public void keyPressed(KeyEvent e) {
			if( e.getKeyCode() == KeyEvent.VK_TAB){
				if(currField == imaginary){
					controller.screenClicked(ActiveField.Real);
					currField = real;
				}
				
				else{
					controller.screenClicked(ActiveField.Imaginary);
					currField = imaginary;
				}
			}
				
		}

		public void keyReleased(KeyEvent e) {
			
		}
		
	}
	
	/**
	 * Nasluchiwacz zdarzen zwiazanych ze zmiana aktywnego wyswietlacza
	 * @author Michal Kaminski
	 *
	 */
	
	private class ScreenClickedListener extends MouseAdapter {
		public void mousePressed(MouseEvent evt) {
			JTextField tmp = (JTextField) evt.getSource();
			if(tmp == imaginary){
				controller.screenClicked(ActiveField.Imaginary);
				currField = imaginary;
			}
			
			else{
				controller.screenClicked(ActiveField.Real);
				currField = real;
			}
		}
	}
	
	

}




