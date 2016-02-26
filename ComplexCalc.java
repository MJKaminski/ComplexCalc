package pl.edu.pw.elka.stud.M.J.Kaminski;
import java.awt.EventQueue;


/**
 * Klasa odpowiedzialna za start proramu
 * @author Michal Kaminski
 *
 */
public class ComplexCalc
{
	/**
	 * Metoda startowa programu
	 * @param args tablica parametrow poczatkowych
	 */
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				CalcView view = new CalcView();
				CalcModel model = new CalcModel();
				CalcController controller = new CalcController(view, model);
				view.setController(controller);

			}
		});
	}

}