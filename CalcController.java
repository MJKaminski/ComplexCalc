package pl.edu.pw.elka.stud.M.J.Kaminski;


/**
 * Klasa zarzadzajaca dzialaniem programu. Odpowiada za zlecanie zadan modelowi i zmiane zawartosci widoku
 * @author Michal Kaminski
 *
 */

class CalcController {

	private CalcView view;
	private CalcModel model;
	private ActiveField currField;
	private boolean error;
	
	
	/**
	 * Konstruktor przyjmujacy odnosniki do modelu i widoku 
	 * @param v widok
	 * @param m model
	 */
	public CalcController(CalcView v, CalcModel m){
		view = v;
		model = m;
		error = false;
		currField = ActiveField.Real;
		view.setScreenValue(ActiveField.Real, "0");
		view.setScreenValue(ActiveField.Imaginary, "0");
	}
	
	/**
	 * Funkcja powiadamiajaca zarzadce o nacisnieciu przycisku akcji. Kontroler wysyla informacje do modelu i modyfikuje widok 
	 * @param command akcja do przetworzena
	 */
	public void execute(String command){
		
		Complex result;
		
		if(command =="CE"){
			view.setScreenValue(ActiveField.Real, "0");
			view.setScreenValue(ActiveField.Imaginary, "0");
			currField = ActiveField.Real;
			view.setActiveScreen(currField);
			model.calculate(Command.CLEAR);
		}
		
		
		else if(command == "+/-"){
			result = model.calculate(Command.NEGATE);
			
			if(currField == ActiveField.Real)
				view.setScreenValue(currField, result.getReal().stripTrailingZeros().toPlainString());
			
			else
				view.setScreenValue(currField, result.getImaginary().stripTrailingZeros().toPlainString());
			
		}
		
		else if(command == ","){
			result = model.calculate(Command.POINT);
			System.out.println(result.getReal().toPlainString());
			System.out.println(result.getReal().scale());
			if(currField == ActiveField.Real)
				if(result.getReal().scale() == 0)
					view.setScreenValue(currField, result.getReal().toPlainString()+".");
				else
					view.setScreenValue(currField, result.getReal().toPlainString());
			else
				if(result.getImaginary().scale() == 0)
					view.setScreenValue(currField, result.getImaginary().toPlainString()+".");
				else
					view.setScreenValue(currField, result.getImaginary().toPlainString());
			
		}
		
		else {
			

				if(command == "+")
					result = model.calculate(Command.ADD);
					
				else if(command == "-")
					result = model.calculate(Command.SUBSTRACT);
					
				else if(command == "*")
					result = model.calculate(Command.MULTIPLE);
					
				else if(command == "/"){
					try{
						result = model.calculate(Command.DIVIDE);
					}
					
					catch (ArithmeticException e){
						currField = ActiveField.Real;
						view.setActiveScreen(currField);
						view.setError();
						error = true;
						return;
					}
				}
					
				else if(command == "sin")
					result = model.calculate(Command.SINE);
					
				else if(command == "cos")
					result = model.calculate(Command.COSINE);
					
				else if(command == "tg"){
					try{
						result = model.calculate(Command.TANGENT);
					}
					
					catch (ArithmeticException e){
						currField = ActiveField.Real;
						view.setActiveScreen(currField);
						view.setError();
						error = true;
						return;
					}
				}	
				
				else if(command == "ctg"){

					try{
						result = model.calculate(Command.COTANGENT);
					}
					
					catch (ArithmeticException e){
						currField = ActiveField.Real;
						view.setActiveScreen(currField);
						view.setError();
						error = true;
						return;
					}
				}
					
				else if(command == "="){
					
					try{
						result = model.calculate(Command.EQUALS);
					}
					
					catch (ArithmeticException e){
						currField = ActiveField.Real;
						view.setActiveScreen(currField);
						view.setError();
						error = true;
						return;
					}
				}	
				else
					result = model.calculate(Command.ROOT);

				view.setScreenValue(ActiveField.Real, result.getReal().stripTrailingZeros().toPlainString());
				view.setScreenValue(ActiveField.Imaginary, result.getImaginary().stripTrailingZeros().toPlainString());
			
				currField = ActiveField.Real;
				view.setActiveScreen(currField);
				model.changeActiveField(currField);

		}
	
	}
	
	/**
	 * Funkcja powiadamiajaca zarzadce o nacisnieciu przycisku numerycznego. Kontroler wysyla informacje do modelu i modyfikuje widok
	 * @param number cyfra do przetworzenia
	 */
	public void enterNumber(int number){
		Complex result = model.enterNumber(number);
		if(currField == ActiveField.Real){
			view.setScreenValue(ActiveField.Real, result.getReal().stripTrailingZeros().toPlainString());
			if(error){
				view.setScreenValue(ActiveField.Imaginary, "0");
				error = false;
			}
		}
		else{
			view.setScreenValue(ActiveField.Imaginary, result.getImaginary().stripTrailingZeros().toPlainString());
			if(error){
				view.setScreenValue(ActiveField.Real, "0");
				error = false;
			}
		}
		
	}
	
	/**
	 * Funkcja powiadamiajaca kontroler o zmianie aktywnego pola. Kontroler powiadamia o tym model
	 * @param a nowe aktywne pole
	 */
	public void screenClicked(ActiveField a){
		if(a != currField){
			currField = a;
			view.setActiveScreen(currField);
			model.changeActiveField(currField);
		}
	}
	
	
}





