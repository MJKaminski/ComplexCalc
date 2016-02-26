package pl.edu.pw.elka.stud.M.J.Kaminski;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Model kalkulatora - odpowiada za przeprowadzanie obliczen i zapamietywanie stanu kalkulatora
 * @author Michal Kaminski
 *
 */

class CalcModel {
	private int realNumCount;
	private int imagNumCount;
	private int realFracCount;
	private int imagFracCount;
	
	private boolean startReal;
	private boolean startImag;
	private boolean fractionReal;
	private boolean fractionImag;
	private boolean secondArg;
	
	private ActiveField currField;
	private Command lastCommand;
	
	private Complex argument;
	private Complex result;
	
	private State state;
	
	/**
	 * Konstruktor modelu - nastawia odpowiedni stan poczatkowy
	 */
	public CalcModel(){
		realNumCount = imagNumCount = 1;
		realFracCount = imagFracCount = 0;
		startReal = startImag = true;
		fractionReal = fractionImag = false;
		lastCommand = Command.EQUALS;
		result = new Complex();
		argument = new Complex();
		currField = ActiveField.Real;
		state = State.INPUT;
		secondArg = false;
	}
	/**
	 * Glowna metoda klasy wykonujaca odpowiednie obliczenia w zaleznosci od operacji i stanu modelu
	 * @param command operacja do wykonania
	 * @return zwraca liczbe zespolona do wyswietlenia
	 */
	public Complex calculate(Command command){
		
		if(command == Command.CLEAR){
			realNumCount = imagNumCount = 1;
			realFracCount = imagFracCount = 0;
			startReal = startImag = true;
			fractionReal = fractionImag = false;
			lastCommand = Command.EQUALS;
			currField = ActiveField.Real;
			state = State.INPUT;
			secondArg = false;
			result.reset();
			argument.reset();
			return result;
		}
		
		
		else if(command == Command.NEGATE){
			
			
			if(state == State.INPUT){
				
				if(currField == ActiveField.Real)
					argument.setReal(argument.getReal().negate());
				else
					argument.setImaginary(argument.getImaginary().negate());
			}
			
			else if (state == State.CALCULATION){
				
				
				if(secondArg){
					if(currField == ActiveField.Real)
						argument.setReal(argument.getReal().negate());
					else
						argument.setImaginary(argument.getImaginary().negate());
				}
				else{
					result = new Complex(argument);
					secondArg = true;
					if(currField == ActiveField.Real)
						argument.setReal(argument.getReal().negate());
					else
						argument.setImaginary(argument.getImaginary().negate());
				}
			}
			
			else{
				state = State.INPUT;
				realNumCount = imagNumCount = 1;
				realFracCount = imagFracCount = 0;
				startReal = startImag = true;
				fractionReal = fractionImag = false;
				
				if(currField == ActiveField.Real){
					argument.setReal(result.getReal().negate());
					argument.setImaginary(result.getImaginary());
				}
				else{
					argument.setReal(result.getReal());
					argument.setImaginary(result.getImaginary().negate());
				}
				
				
			}
			return argument;
		}
		
		else if(command == Command.POINT){
			
			if (state == State.EXECUTE)
			{
				state = State.INPUT;
				result.reset();
				argument.reset();
				
				if(currField == ActiveField.Real)
					fractionReal = true;
			
				else
					fractionImag = true;
			}
			
			else if (state ==  State.CALCULATION){
				if(!secondArg){
					
					result = new Complex(argument);
					secondArg = true;
					argument.reset();
					realNumCount = imagNumCount = 1;
					realFracCount = imagFracCount = 0;
					
					if(currField == ActiveField.Real){
						fractionReal = true;
						fractionImag = false;
					}
				
					else{
						fractionReal = false;
						fractionImag = true;
					}
				}
				
				else{
					
					if(currField == ActiveField.Real)
						fractionReal = true;
				
					else
						fractionImag = true;
				}
			}
			
			if(currField == ActiveField.Real)
				fractionReal = true;
		
			else
				fractionImag = true;
			return argument;
				
		}
		
		else if(command == Command.EQUALS){
			if(state == State.INPUT){
				result = new Complex(argument);
				realNumCount = imagNumCount = 1;
				realFracCount = imagFracCount = 0;
				startReal = startImag = true;
				fractionReal = fractionImag = false;
				currField = ActiveField.Real;
				state = State.EXECUTE;
			}
			
			
			else if(state == State.CALCULATION){
				
				if(!secondArg){
					result = new Complex(argument);
					if(lastCommand == Command.ADD){
						result = result.add(argument);
					}
						
					else if(lastCommand == Command.SUBSTRACT){
						result = result.substract(argument);
					}
						
					else if(lastCommand == Command.MULTIPLE){
						result = result.multiply(argument);
					}
						
					else if(lastCommand == Command.DIVIDE){
						try{
							result = result.divide(argument);
						}
						
						catch (ArithmeticException e){
							argument.reset();
							result.reset();
							realNumCount = imagNumCount = 1;
							startReal = startImag = true;
							fractionReal = fractionImag = false;
							currField = ActiveField.Real;
							secondArg = false;
							
							state = State.INPUT;
							throw e;
						}
					}
				}
				
				else{
					
					if(lastCommand == Command.ADD){
						result = result.add(argument);
					}
						
					else if(lastCommand == Command.SUBSTRACT){
						result = result.substract(argument);
					}
						
					else if(lastCommand == Command.MULTIPLE){
						result = result.multiply(argument);
					}
						
					else if(lastCommand == Command.DIVIDE){
						try{
							result = result.divide(argument);
						}
						
						catch (ArithmeticException e){
							realNumCount = imagNumCount = 1;
							startReal = startImag = true;
							fractionReal = fractionImag = false;
							lastCommand = Command.EQUALS;
							currField = ActiveField.Real;
							secondArg = false;
							state = State.INPUT;
							result.reset();
							argument.reset();
							throw e;
						}
					}
					
				}
				
				state = State.EXECUTE;
			}
			
			else if(state == State.EXECUTE){
				if(lastCommand == Command.ADD){
					result = result.add(argument);
				}
					
				else if(lastCommand == Command.SUBSTRACT){
					result = result.substract(argument);
				}
					
				else if(lastCommand == Command.MULTIPLE){
					result = result.multiply(argument);
				}
					
				else if(lastCommand == Command.DIVIDE){
					try{
						result = result.divide(argument);
					}
					
					catch (ArithmeticException e){
						realNumCount = imagNumCount = 1;
						startReal = startImag = true;
						fractionReal = fractionImag = false;
						currField = ActiveField.Real;
						secondArg = false;
						state = State.INPUT;
						argument.reset();
						result.reset();
						throw e;
						
					}
				}
				
				else if(lastCommand == Command.SINE)
					result = result.sine();
					
				else if(lastCommand == Command.COSINE)
					result = result.cosine();
					
				else if(lastCommand== Command.TANGENT){
					try{
						result = result.tangent();
					}
					catch (ArithmeticException e){
						realNumCount = imagNumCount = 1;
						startReal = startImag = true;
						fractionReal = fractionImag = false;
						currField = ActiveField.Real;
						state = State.INPUT;
						secondArg = false;
						argument.reset();
						result.reset();
						throw e;
					}
				}
				
				else if(lastCommand == Command.COTANGENT){	
					try{
						result = result.cotangent();
					}
					catch (ArithmeticException e){
						realNumCount = imagNumCount = 1;
						startReal = startImag = true;
						fractionReal = fractionImag = false;
						currField = ActiveField.Real;
						state = State.INPUT;
						secondArg = false;
						argument.reset();
						result.reset();
						throw e;
					}
				}
					
				else if(lastCommand== Command.ROOT)
					result = result.squareRoot();
			}
			
			return result;
		}
		
		
		//Obsluga przyciskow obliczeniowych
		else {
			//Przyciski obliczeniowe, stan Input
				if(state == State.INPUT){
					switch(command){
					case ADD:
					case SUBSTRACT:
					case DIVIDE:
					case MULTIPLE:
						lastCommand = command;
						state = State.CALCULATION;
						result = new Complex(argument);
						secondArg = false;
						return result;
					default:
						break;
							
					}
					
					if(command == Command.SINE)
						argument = argument.sine();
						
					else if(command == Command.COSINE)
						argument = argument.cosine();
						
					else if(command== Command.TANGENT){
						try{
							argument = argument.tangent();
						}
						catch (ArithmeticException e){
							realNumCount = imagNumCount = 1;
							startReal = startImag = true;
							fractionReal = fractionImag = false;
							currField = ActiveField.Real;
							state = State.INPUT;
							secondArg = false;
							argument.reset();
							result.reset();
							throw e;
						}
					}
					
					else if(command == Command.COTANGENT){	
						try{
							argument = argument.cotangent();
						}
						catch (ArithmeticException e){
							realNumCount = imagNumCount = 1;
							startReal = startImag = true;
							fractionReal = fractionImag = false;
							currField = ActiveField.Real;
							state = State.INPUT;
							secondArg = false;
							argument.reset();
							result.reset();
							throw e;
						}
					}
						
					else if(command== Command.ROOT)
						argument = argument.squareRoot();

					
					realNumCount = imagNumCount = 1;
					startReal = startImag = true;
					fractionReal = fractionImag = false;
					currField = ActiveField.Real;
					secondArg = false;
					result = new Complex(argument);
					return result;

				}
				
				//Przyciski obliczeniowe, stan Execute
				else if (state==State.EXECUTE)
				{
					secondArg = false;
					state = State.CALCULATION;
					lastCommand = command;
					argument = new Complex(result);
					realNumCount = imagNumCount = 1;
					startReal = startImag = true;
					fractionReal = fractionImag = false;
					currField = ActiveField.Real;
					return argument;
					
				}
				
				else{
					
					//Przyciski obliczeniowe, stan input, okreslono drugi argument
					if(secondArg){
						if(command == Command.ROOT){
							argument = argument.squareRoot();
							return argument;
						}
						
						
						else if(command == Command.SINE){
							argument = argument.sine();
							return argument;
						}
							
						else if(command == Command.COSINE){	
							argument = argument.cosine();
							return argument;
						}
							
						else if(command == Command.TANGENT){
							try{
								argument = argument.tangent();
							}
							catch (ArithmeticException e){
								realNumCount = imagNumCount = 1;
								startReal = startImag = true;
								fractionReal = fractionImag = false;
								currField = ActiveField.Real;
								state = State.INPUT;
								secondArg = false;
								argument.reset();
								result.reset();
								throw e;
							}
							return argument;
						}
							
						else if(command == Command.COTANGENT){
							try{
								argument = argument.cotangent();
							}
							
							catch (ArithmeticException e){
								realNumCount = imagNumCount = 1;
								startReal = startImag = true;
								fractionReal = fractionImag = false;
								currField = ActiveField.Real;
								state = State.INPUT;
								secondArg = false;
								argument.reset();
								result.reset();
								throw e;
							}
							
							return argument;
						}
						
						
						if(lastCommand == Command.ADD){
							result = result.add(argument);
						}
							
						else if(lastCommand == Command.SUBSTRACT){
							result = result.substract(argument);
						}
							
						else if(lastCommand == Command.MULTIPLE){
							result = result.multiply(argument);
						}
							
						else if(lastCommand == Command.DIVIDE){
							
							try{
								result = result.divide(argument);
							}
							
							catch (ArithmeticException e){
								realNumCount = imagNumCount = 1;
								startReal = startImag = true;
								fractionReal = fractionImag = false;
								currField = ActiveField.Real;
								state = State.INPUT;
								secondArg = false;
								argument.reset();
								result.reset();
								throw e;
							}
						}
												
						
						lastCommand = command;
						secondArg =false;
					}
					
					//Przyciski obliczeniowe, stan input, nie okreslono drugiego argumentu
					else{
						
						if(command == Command.ROOT){
							result = new Complex(argument);
							argument = argument.squareRoot();
							return argument;
						}
						
						
						else if(command == Command.SINE){
							result = new Complex(argument);
							argument = argument.sine();
							return argument;
						}
							
						else if(command == Command.COSINE){
							result = new Complex(argument);
							argument = argument.cosine();
							return argument;
						}
							
						else if(command == Command.TANGENT){
							try{
								result = new Complex(argument);
								argument = argument.tangent();
							}
							catch (ArithmeticException e){
								realNumCount = imagNumCount = 1;
								startReal = startImag = true;
								fractionReal = fractionImag = false;
								currField = ActiveField.Real;
								state = State.INPUT;
								secondArg = false;
								argument.reset();
								result.reset();
								throw e;
							}
							return argument;
						}
							
						else if(command == Command.COTANGENT){
							try{
								result = new Complex(argument);
								argument = argument.cotangent();
							}
							
							catch (ArithmeticException e){
								realNumCount = imagNumCount = 1;
								startReal = startImag = true;
								fractionReal = fractionImag = false;
								currField = ActiveField.Real;
								state = State.INPUT;
								secondArg = false;
								argument.reset();
								result.reset();
								throw e;
							}
							
							return argument;
						}
						
						
						lastCommand = command;
					}
					
					return result;
				}
				

		}
	
	}
	/**
	 * Metoda wprawdzajaca dane do modelu
	 * @param number numer do wprowadzenia
	 * @return Zwraca liczbe zespolona do wyswietlenia
	 */
	public Complex enterNumber(int number){
		
		if(state == State.CALCULATION && !secondArg){
			secondArg = true;
			startReal = true;
			startImag = true;
			realNumCount = imagNumCount = 1;
			fractionReal = fractionImag = false;
			realFracCount = imagFracCount = 0;
		}
		
		else if(state==State.EXECUTE){
			state = State.INPUT;
			secondArg = false;
			startReal = true;
			startImag = true;
			realNumCount = imagNumCount = 1;
			fractionReal = fractionImag = false;
			realFracCount = imagFracCount = 0;
		}
		
		
		if(currField == ActiveField.Real){
			if(realNumCount+realFracCount<10){
				
				if(!fractionReal){
					if(startReal){
						if(number!=0)
							startReal= false;
						argument.setReal(BigDecimal.valueOf(number));
					}
					
					else{
						++realNumCount;
						argument.setReal(argument.getReal().multiply(BigDecimal.TEN).add(BigDecimal.valueOf(number)));
					}
				}
				else{

					++realFracCount;
					int scale = argument.getReal().scale();
					BigInteger i = argument.getReal().unscaledValue();
					i = i.multiply(BigInteger.TEN).add(BigInteger.valueOf(number));
					argument.setReal(new BigDecimal(i, scale+1));
					if(startReal){
						startReal= false;
					}
				}
			}
		}
		
		else{
			if(imagNumCount+imagFracCount<10){
				
				if(!fractionImag){
					if(startImag){
						if(number!=0)
							startImag= false;
						argument.setImaginary(BigDecimal.valueOf(number));
					}
					
					else{
						++imagNumCount;
						argument.setImaginary(argument.getImaginary().multiply(BigDecimal.TEN).add(BigDecimal.valueOf(number)));
					}
				}
				else{

					++imagFracCount;
					int scale = argument.getImaginary().scale();
					BigInteger i = argument.getImaginary().unscaledValue();
					i = i.multiply(BigInteger.TEN).add(BigInteger.valueOf(number));
					argument.setImaginary(new BigDecimal(i, scale+1));
					if(startImag){
						startImag= false;
					}
				}
			}
		}

		return argument;
		
	}
	
	/**
	 * Metoda zmieniajaca aktywne pole (czesc rzeczywista/ urojona)
	 * @param a nowe aktywne pole
	 */
	
	public void changeActiveField(ActiveField a){
		if(a != currField){
			currField = a;
		}
	}
	

	
}

/**
 * Typ wyliczeniowy uzywany do komunikacji miedzy kontrolerem a modelem. Reprezentuje wszystkie mozliwe operacje
 * ktore model moze wykonac
 * @author Michal Kaminski
 *
 */
enum Command{
	ADD,
	SUBSTRACT,
	MULTIPLE,
	DIVIDE,
	SINE,
	COSINE,
	TANGENT,
	COTANGENT,
	NEGATE,
	CLEAR,
	ROOT,
	EQUALS,
	POINT
}

/**
 * Typ wyliczeniowy odpowiedzialny za reprezentacje aktywnego pola edycji (czesc rzeczywista lub czesc urojona)
 * @author Michal Kaminski
 *
 */

enum ActiveField{
	Real,
	Imaginary
};

/**
 * Reprezentuje stan kalkulatora
 * @author Michal Kaminski
 *
 */

enum State{
	INPUT,
	CALCULATION,
	EXECUTE
}



/**
 * Prosta implementacja liczb zespolonych wraz ze wszystkimi potrzebnymi dzialaniami
 * @author Michal Kaminski
 *
 */

class Complex{
	private BigDecimal _real;
	private BigDecimal _imag;
	private static final BigDecimal TWO =BigDecimal.valueOf(2l);
	
	/**
	 * Konstruktor domyslny - nastawia Re i Im na 0
	 */
	Complex(){
		_real =  BigDecimal.ZERO;
		_imag =  BigDecimal.ZERO;
	}
	
	/**
	 * Konstruktor - nastawia Re i Im na r i i
	 * @param r czesc rzeczywista
	 * @param i czesc urojona
	 */
	Complex(BigDecimal r, BigDecimal i){
		_real = r;
		_imag =i;
	}
	
	/**
	 * Konstruktor kopiujacy
	 * @param c liczba do skopiowania
	 */
	Complex(Complex c){
		_real = new BigDecimal(c._real.unscaledValue(), c._real.scale());
		_imag =new BigDecimal(c._imag.unscaledValue(), c._imag.scale());
	}
	
	/**
	 * Dodawanie 2 liczb zespolonych
	 * @param b liczba do dodania
	 * @return wynik dzialania
	 */
	public Complex add(Complex b){
		BigDecimal r = _real.add(b._real);
		BigDecimal i = _imag.add(b._imag);
		return new Complex(r,i);
	}
	/**
	 * Odejmowanie 2 liczb zespolonych
	 * @param b liczba do odjecia
	 * @return wynik dzialania
	 */
	public Complex substract(Complex b){
		BigDecimal r = _real.subtract(b._real);
		BigDecimal i = _imag.subtract(b._imag);
		return new Complex(r,i);
	}
	
	/**
	 * Mnozenie 2 liczb zespolonych
	 * @param b mnoznik
	 * @return wynik dzialania
	 */
	public Complex multiply(Complex b){
		BigDecimal bReal = b._real;
		BigDecimal bImag = b._imag;
		BigDecimal r = _real.multiply(bReal,new MathContext(9, RoundingMode.CEILING)).subtract(_imag.multiply(bImag), new MathContext(9, RoundingMode.CEILING));
		BigDecimal i = _real.multiply(bImag,new MathContext(9, RoundingMode.CEILING)).add(_imag.multiply(bReal), new MathContext(9, RoundingMode.CEILING));
		r = r.setScale(9, RoundingMode.HALF_UP);
		i = i.setScale(9, RoundingMode.HALF_UP);
		return new Complex(r,i);
	}
	
	/**
	 * Dzielenie 2 liczb zespolonych
	 * @param b dzielnik
	 * @throws ArithmeticException jesli dzielnie przez 0
	 * @return wynik dzialania
	 */
	public Complex divide(Complex b) throws ArithmeticException{
		BigDecimal bReal = b._real;
		BigDecimal bImag = b._imag;
		BigDecimal r = _real.multiply(bReal).add( _imag.multiply(bImag) ).divide( bReal.pow(2).add(bImag.pow(2)),10,RoundingMode.HALF_UP).stripTrailingZeros();
		BigDecimal i = _imag.multiply(bReal).subtract( _real.multiply(bImag) ).divide( bReal.pow(2).add(bImag.pow(2)),10,RoundingMode.HALF_UP).stripTrailingZeros();
		return new Complex(r,i);
	}
	
	/**
	 * Pierwiastek kwadratowy
	 * @return pierwiastek liczby
	 */
	public Complex squareRoot(){
		BigDecimal r;
		BigDecimal i;
		if(_imag.equals(BigDecimal.ZERO)){
		
			int sign = _real.signum();
			if(sign == 1 || sign == 0){
				r = sqrt(_real);
				i = BigDecimal.ZERO;
			}
			else{
				r = BigDecimal.ZERO;
				i = sqrt(_real.negate());
			}

		}
		else{
			r = sqrt( _real.add( sqrt(_real.pow(2).add(_imag.pow(2) ) ) ).divide(TWO,10,RoundingMode.HALF_UP));
			
			BigDecimal tmp;
			if( _imag.signum() ==1)
				tmp = _imag;
			else
				tmp = _imag.negate();
			
			i = tmp.multiply( sqrt( _real.negate().add( sqrt(_real.pow(2).add(_imag.pow(2) ) ) ).divide(TWO,10,RoundingMode.HALF_UP)) );
		}
	
			r = r.setScale(9, RoundingMode.HALF_UP);
			i = i.setScale(9, RoundingMode.HALF_UP);
		return new Complex(r,i);
	}
	
	/**
	 * Sinus liczby zespolonej
	 * @return sinus danej liczby
	 */
	public Complex sine(){
		double a = _real.doubleValue()*StrictMath.PI/180;
		double b = _imag.doubleValue()*StrictMath.PI/180;
		double r = StrictMath.sin(a) * StrictMath.cosh(b);
		double i = StrictMath.cos(a) * StrictMath.sinh(b);
		
		BigDecimal real = BigDecimal.valueOf(r).setScale(9, RoundingMode.HALF_UP);
		BigDecimal imag = BigDecimal.valueOf(i).setScale(9, RoundingMode.HALF_UP);
		return new Complex(real, imag);
	}
	
	
	/**
	 * Cosinus liczby zespolonej
	 * @return cosinus danej liczby
	 */
	public Complex cosine(){
		double a = _real.doubleValue()*StrictMath.PI/180;
		double b = _imag.doubleValue()*StrictMath.PI/180;
		double r = StrictMath.cos(a) * StrictMath.cosh(b);
		double i = -1 * StrictMath.sin(a) * StrictMath.sinh(b);
		
		BigDecimal real = BigDecimal.valueOf(r).setScale(9, RoundingMode.HALF_UP);
		BigDecimal imag = BigDecimal.valueOf(i).setScale(9, RoundingMode.HALF_UP);
		return new Complex(real, imag);
	}
	
	/**
	 * Tangens liczby zespolonej
	 * @throws ArithmeticException jesli tangens nie istnieje 
	 * @return Tanges danej liczby
	 */
	public Complex tangent() throws ArithmeticException{
		double a = _real.doubleValue()*StrictMath.PI/180;
		double b = _imag.doubleValue()*StrictMath.PI/180;
		double r = StrictMath.sin(2*a) / (StrictMath.cos(2*a) +  StrictMath.cosh(2*b));
		double i = StrictMath.sinh(2*a) / (StrictMath.cos(2*a) +  StrictMath.cosh(2*b));
		
		if(r == Double.POSITIVE_INFINITY || r==Double.NEGATIVE_INFINITY || r == Double.NaN)
			throw new ArithmeticException();
		
		BigDecimal real = BigDecimal.valueOf(r).setScale(9, RoundingMode.HALF_UP);
		BigDecimal imag = BigDecimal.valueOf(i).setScale(9, RoundingMode.HALF_UP);
		return new Complex(real, imag);
	}
	
	/**
	 * Cotangens liczby zespolonej
	 * @throws ArithmeticException jesli cotangens nie istnieje
	 * @return Cotangens danej liczby
	 */
	public Complex cotangent() throws ArithmeticException{
		double a = _real.doubleValue()*StrictMath.PI/180;
		double b = _imag.doubleValue()*StrictMath.PI/180;
		double r = -1 * StrictMath.sin(2*a) / (StrictMath.cos(2*a) -  StrictMath.cosh(2*b));
		double i = StrictMath.sinh(2*a) / (StrictMath.cos(2*a) -  StrictMath.cosh(2*b));
		
		if(r == Double.POSITIVE_INFINITY || r==Double.NEGATIVE_INFINITY || r == Double.NaN)
				throw new ArithmeticException();
		
		BigDecimal real = BigDecimal.valueOf(r).setScale(9, RoundingMode.HALF_UP);
		BigDecimal imag = BigDecimal.valueOf(i).setScale(9, RoundingMode.HALF_UP);
		return new Complex(real, imag);
	}
	
	/**
	 * Zwraca czesc rzeczywista liczby
	 * @return Re(z)
	 */
	public BigDecimal getReal(){
		return _real;
	}
	
	/**
	 * Zwraca czesc urojona liczby
	 * @return Im(z)
	 */
	public BigDecimal getImaginary(){
		return _imag;
	}
	
	/**
	 * Ustawia czesc rzeczywista liczby
	 * @param real nowe Re(z)
	 */
	public void setReal(BigDecimal real){
		_real = real;
	}
	
	/**
	 * Ustawia czesc urojonaa liczby
	 * @param imag nowe Im(z)
	 */
	public void setImaginary(BigDecimal imag){
		_imag = imag;
	}
	
	/**
	 * Resetuje wartosc liczby do 0 + 0i
	 */
	public void reset(){
		_real = BigDecimal.ZERO;
		_imag = BigDecimal.ZERO;
	}
	
	
	/**
	 * Pomocnicza funkcja liczaca pierwiastek z liczby typu BigDecimal
	 * @param number liczba do spierwistkowania
	 */
	private static BigDecimal sqrt(BigDecimal number) {
		BigDecimal result = BigDecimal.valueOf(StrictMath.sqrt(number.doubleValue()));
		result = result.setScale(9, RoundingMode.HALF_UP);
		return result;
	}
	
	
}