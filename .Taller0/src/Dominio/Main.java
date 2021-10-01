package Dominio;

/**
 * @author Vicente Rojas
 * @author Claudio Cortes 
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		String infoClientes[][] = new String[9999][4];
		String rutSimple[] = new String[9999];
		int saldos[] = new int[9999];
		boolean pMovilidad[] = new boolean[9999];
		String iBoletos[] = new String[9999];
		int cantBoletos[] = new int[9999];

		String infoCartelera_A[][] = new String[6][4];
		int infoCartelera_B[][] = new int[6][3];
		int recaudadoDiario[] = new int[6];

		int M1[][] = crearSala();
		int M2[][] = crearSala();
		int M3[][] = crearSala();
		int T1[][] = crearSala();
		int T2[][] = crearSala();
		int T3[][] = crearSala();
		int contadorClientes = leerClientes(infoClientes, rutSimple, saldos);
		pMovilidad = leerStatus(pMovilidad, infoClientes, contadorClientes);
		leerPeliculas(infoCartelera_A, infoCartelera_B);
		contadorClientes = menu(M1, M2, M3, T1, T2, T3, cantBoletos, iBoletos, saldos, pMovilidad, infoClientes,
		contadorClientes, rutSimple, recaudadoDiario, infoCartelera_A, infoCartelera_B);
		System.out.println("Cerrando el sistema, guardando...");
		Save1(infoClientes, contadorClientes, saldos);
		Save2(infoClientes, contadorClientes, pMovilidad);
		Save3(infoCartelera_A, infoCartelera_B, recaudadoDiario, pMovilidad);
		System.out.println("Guardado exitoso.");
	}

	// RF1
	/**
	 * This function reads the text file "customers.txt" and saves the customer
	 * information in a matrix and vectors
	 * 
	 * @param MatrizIC
	 * @param VectorRS
	 * @param VectorS
	 * @return
	 * @throws FileNotFoundException
	 */
	public static int leerClientes(String MatrizIC[][], String VectorRS[], int VectorS[]) throws FileNotFoundException {
		File file = new File("clientes.txt");
		Scanner arch = new Scanner(file);
		int contador = 0;
		while (arch.hasNextLine()) {
			String line = arch.nextLine();
			String partes[] = line.split(",");

			String nombre = partes[0];
			MatrizIC[contador][0] = nombre;

			String apellido = partes[1];
			MatrizIC[contador][1] = apellido;

			String rut = partes[2];
			MatrizIC[contador][2] = rut;

			String rutS = getrutSimpleimplificado(rut);
			VectorRS[contador] = rutS;

			String contraseña = partes[3];
			MatrizIC[contador][3] = contraseña;

			int saldo = Integer.parseInt(partes[4]);
			VectorS[contador] = saldo;

			contador++;
		}
		arch.close();
		return contador;
	}

	/**
	 * This function reads the text file "status.txt" and saves the information of
	 * the mobility pass of each client in a boolean vector
	 * 
	 * @param VectorPM
	 * @param MatrizIC
	 * @param contadorClientes
	 * @return
	 * @throws FileNotFoundException
	 */
	public static boolean[] leerStatus(boolean VectorPM[], String MatrizIC[][], int contadorClientes)
			throws FileNotFoundException {
		File file = new File("status.txt");
		Scanner arch = new Scanner(file);
		while (arch.hasNextLine()) {
			String line = arch.nextLine();
			String partes[] = line.split(",");
			String rut = partes[0];
			String estado = partes[1];
			int IndexRut = indexM(rut, MatrizIC, contadorClientes, 2);
			if (estado.equals("HABILITADO")) {
				VectorPM[IndexRut] = true;
			}
			if (estado.equals("NO HABILITADO")) {
				VectorPM[IndexRut] = false;
			}
		}
		arch.close();
		return VectorPM;
	}

	/**
	 * This function reads the text file “movies.txt” and saves the information of
	 * the movies in arrays
	 * 
	 * @param MatrizIC_A
	 * @param MatrizIC_B
	 * @throws FileNotFoundException
	 */
	public static void leerPeliculas(String MatrizIC_A[][], int MatrizIC_B[][]) throws FileNotFoundException {
		File file = new File("peliculas.txt");
		Scanner arch = new Scanner(file);
		int contador = 0;
		while (arch.hasNextLine()) {
			String line = arch.nextLine();
			String partes[] = line.split(",");
			int a = partes.length;
			String nombre = partes[0];
			String tipo = partes[1];
			int recaudacion = Integer.parseInt(partes[2]);
			int N_sala = Integer.parseInt(partes[3]);
			String horario = partes[4];
			MatrizIC_B[contador][1] = N_sala;
			MatrizIC_A[contador][2] = horario;
			MatrizIC_A[contador][0] = nombre;
			MatrizIC_A[contador][1] = tipo;
			MatrizIC_B[contador][0] = recaudacion;
			MatrizIC_B[contador][2] = 260;
			MatrizIC_A[contador][3] = horario + Integer.toString(N_sala);
			if (partes.length > 5) {
				contador++;
				for (int i = 5; i < partes.length; i += 2) {
					N_sala = Integer.parseInt(partes[i]);
					horario = partes[i + 1];
					MatrizIC_B[contador][1] = N_sala;
					MatrizIC_A[contador][2] = horario;
					MatrizIC_A[contador][0] = nombre;
					MatrizIC_A[contador][1] = tipo;
					MatrizIC_B[contador][0] = 0;
					MatrizIC_B[contador][2] = 260;
					MatrizIC_A[contador][3] = horario + Integer.toString(N_sala);
				}
			}
			contador++;
		}
		arch.close();
	}

	// RF2
	/**
	 * This function shows a menu which is necessary to access the different menus
	 * available
	 * 
	 * @param M1
	 * @param M2
	 * @param M3
	 * @param T1
	 * @param T2
	 * @param T3
	 * @param cantBoletos
	 * @param iBoletos
	 * @param saldos
	 * @param pMovilidad
	 * @param infoClientes
	 * @param contadorClientes
	 * @param rutSimpleimple
	 * @param recaudadoDiario
	 * @param infoCartelera_A
	 * @param infoCartelera_B
	 * @return
	 */
	public static int menu(int M1[][], int M2[][], int M3[][], int T1[][], int T2[][], int T3[][], int cantBoletos[],
			String iBoletos[], int saldos[], boolean pMovilidad[], String infoClientes[][], int contadorClientes,
			String rutSimpleimple[], int recaudadoDiario[], String infoCartelera_A[][], int infoCartelera_B[][]) {
		System.out.println("Bienvenido, ingrese una opcion para seguir");
		String MainOption = ingresarOpcion("Iniciar Sesion", "Registrarse", "Salir", "");
		while (MainOption.equals("3") == false) {
			if (MainOption.equals("1")) {
				System.out.print("Ingrese su rut para iniciar sesión: ");
				Scanner S0 = new Scanner(System.in);
				String rut = S0.nextLine();
				String rutSim = getrutSimpleimplificado(rut);
				while (conditionsLog(rutSimpleimple, contadorClientes, rutSim, rut)) {
					System.out.println("Por favor ingrese una opcion valida: ");
					Scanner S1 = new Scanner(System.in);
					String ruttry = S1.nextLine();
					String rutsimtry = getrutSimpleimplificado(rut);
					rut = ruttry;
					rutSim = rutsimtry;
					if (rut.equals("3")) {
					}
				}
				if (rut.equals("ADMIN")) {
					logAdmin(cantBoletos, infoClientes, saldos, iBoletos, contadorClientes, rutSimpleimple, recaudadoDiario,
							infoCartelera_A, infoCartelera_B);
				}
				if (index(rutSimpleimple, contadorClientes, rutSim) != -1) {
					int indexClientesuario = index(rutSimpleimple, contadorClientes, rutSim);
					logUsuario(recaudadoDiario, cantBoletos, pMovilidad, iBoletos, indexClientesuario, infoClientes,
							infoCartelera_B, infoCartelera_A, saldos, M1, M2, M3, T1, T2, T3);
				}
			}
			if (MainOption.equals("2")) {
				RegisterRut(contadorClientes, rutSimpleimple, infoClientes, rutSimpleimple, pMovilidad);
				contadorClientes++;
			}
			MainOption = ingresarOpcion("Iniciar Sesion", "Registrarse", "Salir", "");
		}
		return contadorClientes;
	}

	/**
	 * This function registers a new user asking for personal data in order to save
	 * it in the matrix and vectors
	 * 
	 * @param contadorClientes
	 * @param rutSimple
	 * @param InfoP_A
	 * @param Ruts
	 * @param pMovilidad
	 */
	public static void RegisterRut(int contadorClientes, String rutSimple[], String InfoP_A[][], String Ruts[], boolean pMovilidad[]) {
		System.out.print("Ingresa tu nombre: ");
		Scanner S1 = new Scanner(System.in);
		String nombre = S1.nextLine();
		System.out.print("Ingresa apellido: ");
		Scanner S2 = new Scanner(System.in);
		String apellido = S2.nextLine();
		System.out.print("Ingresa tu rut (Con . y -) (Ejemplo:XX.XXX.XXX-X): ");
		Scanner S3 = new Scanner(System.in);
		String rut = S3.nextLine();
		String rutSimpleim = getrutSimpleimplificado(rut);
		while (ConditionsRegisterUsu(rut, Ruts, contadorClientes, rutSimpleim) == false) {
			System.out.print("Por favor intentelo denuevo: ");
			Scanner S3T = new Scanner(System.in);
			String ruttry = S3T.nextLine();
			rut = ruttry;
		}
		System.out.print("Ingresa tu nueva contraseña (Mínimo 4 digitos): ");
		Scanner S4 = new Scanner(System.in);
		String contraseña = S4.nextLine();
		while (contraseña.length() <= 3) {
			System.out.println("Intentelo nuevamente: ");
			Scanner S4T = new Scanner(System.in);
			String passwordtry = S4T.nextLine();
			contraseña = passwordtry;
		}
		System.out.print("Ingrese el estado de su pase de movilidad (0 para NO HABILITADO y 1 para HABILITADO): ");
		Scanner S5 = new Scanner(System.in);
		String paseMovilidad = S5.nextLine();
		while (paseMovilidad.equals("0") == false && paseMovilidad.equals("1") == false) {
			System.out.println("Intentelo nuevamente: ");
			Scanner S5T = new Scanner(System.in);
			String paseTry = S5T.nextLine();
			paseMovilidad = paseTry;
		}
		boolean estado;
		if (paseMovilidad.equals("1")) {
			estado = true;
		} else {
			estado = false;
		}
		InfoP_A[contadorClientes][0] = nombre;
		InfoP_A[contadorClientes][1] = apellido;
		InfoP_A[contadorClientes][2] = rut;
		InfoP_A[contadorClientes][3] = contraseña;
		pMovilidad[contadorClientes] = estado;
		rutSimple[contadorClientes] = rutSimpleim;
		return;
	}

	// RF3
	/**
	 * This function shows the options that a client registered in the program can
	 * choose
	 * 
	 * @param recaudadoDiario
	 * @param cantBoletos
	 * @param pMovilidad
	 * @param iBoletos
	 * @param indexCliente
	 * @param infoCliente
	 * @param infoCartelera_B
	 * @param infoCartelera_A
	 * @param saldos
	 * @param M1
	 * @param M2
	 * @param M3
	 * @param T1
	 * @param T2
	 * @param T3
	 */
	public static void logUsuario(int recaudadoDiario[], int cantBoletos[], boolean pMovilidad[], String iBoletos[],
			int indexCliente, String infoCliente[][], int infoCartelera_B[][], String infoCartelera_A[][], int saldos[],
			int M1[][], int M2[][], int M3[][], int T1[][], int T2[][], int T3[][]) {
		String option;
		System.out.print("Ingrese su contraseña (3 para salir): ");
		Scanner S1 = new Scanner(System.in);
		String contraseña = S1.nextLine();
		while (infoCliente[indexCliente][3].equals(contraseña) == false && contraseña.equals("3") == false) {
			System.out.print("Contraseña incorrecta, intentelo denuevo: ");
			Scanner S1T = new Scanner(System.in);
			String P = S1T.nextLine();
			contraseña = P;
		}
		if (contraseña.equals("3")) {
			System.out.println("Volviendo al menu...");
			return;
		} else {
			System.out.println("Bienvenido " + infoCliente[indexCliente][0] + " " + infoCliente[indexCliente][1]);
			option = ingresarOpcion("Comprar entrada", "Información usuario", "Devolución", "Cartelera");
		}
		while (option.equals("5") == false) {
			if (option.equals("1")) {
				comprarEntrada(recaudadoDiario, iBoletos, cantBoletos, saldos, pMovilidad, indexCliente, infoCartelera_A,
						infoCartelera_B, M1, M2, M3, T1, T2, T3);
			}
			if (option.equals("2")) {
				desplegarInformacion(cantBoletos, infoCartelera_B, infoCartelera_A, infoCliente, indexCliente, saldos,
						iBoletos);
			}
			if (option.equals("3")) {
				devolucionBoletos(recaudadoDiario, saldos, pMovilidad, cantBoletos, infoCartelera_B, infoCartelera_A,
						iBoletos, indexCliente, M1, M2, M3, T1, T2, T3);
			}
			if (option.equals("4")) {
				desplegarCartelera(infoCartelera_A, infoCartelera_B);
			}
			option = ingresarOpcion("Comprar entrada", "Información usuario", "Devolución", "Cartelera");
		}
		return;
	}

	/**
	 * This function searches for the room and function that the customer wants to
	 * buy
	 * 
	 * @param recaudadoDiario
	 * @param iBoletos
	 * @param cantBoletos
	 * @param saldos
	 * @param pMovilidad
	 * @param indexCliente
	 * @param infoCartelera_A
	 * @param infoCartelera_B
	 * @param M1
	 * @param M2
	 * @param M3
	 * @param T1
	 * @param T2
	 * @param T3
	 */
	public static void comprarEntrada(int recaudadoDiario[], String iBoletos[], int cantBoletos[], int saldos[],
			boolean pMovilidad[], int indexCliente, String infoCartelera_A[][], int infoCartelera_B[][], int M1[][],
			int M2[][], int M3[][], int T1[][], int T2[][], int T3[][]) {
		System.out.println("Ingrese el nombre de la pelicula: ");
		Scanner S0 = new Scanner(System.in);
		String pelicula = S0.nextLine();
		while (indexM(pelicula, infoCartelera_A, 5, 0) == -1 && pelicula.equals("3") == false) {
			System.out.print("Ingrese el nombre de la pelicula nuevamente: ");
			Scanner S0T = new Scanner(System.in);
			String P = S0T.nextLine();
			pelicula = P;
		}
		if (pelicula.equals("3")) {
			return;
		}
		System.out.println("Ingrese el horario(M o T): ");
		Scanner S1 = new Scanner(System.in);
		String horario = S0.nextLine();

		while (buscarFuncion(pelicula, horario, infoCartelera_A) == -1 && horario.equals("3") == false) {
			System.out.print("Ingrese un horario valido(M o T): ");
			Scanner S1T = new Scanner(System.in);
			String P = S1T.nextLine();
			horario = P;
		}
		if (horario.equals("3")) {
			return;
		}
		int indexFuncion = buscarFuncion(pelicula, horario, infoCartelera_A);
		String codigo = infoCartelera_A[indexFuncion][3];
		if (darSala(codigo, M1, M2, M3, T1, T2, T3) == null) {
			return;
		}
		int Sala[][] = darSala(codigo, M1, M2, M3, T1, T2, T3);
		elegirAsiento(M1, M2, M3, T1, T2, T3, recaudadoDiario, iBoletos, cantBoletos, codigo, saldos, indexFuncion,
				infoCartelera_A, infoCartelera_B, indexCliente, Sala, pMovilidad);
	}

	/**
	 * This function is in charge of giving the customer the option of choosing the
	 * seats they want to buy for the function
	 * 
	 * @param M1
	 * @param M2
	 * @param M3
	 * @param T1
	 * @param T2
	 * @param T3
	 * @param recaudadoDiario
	 * @param iBoletos
	 * @param cantBoletos
	 * @param codigo
	 * @param saldos
	 * @param indexFuncion
	 * @param infoCartelera_A
	 * @param infoCartelera_B
	 * @param indexCliente
	 * @param Sala
	 * @param pMovilidad
	 */
	public static void elegirAsiento(int M1[][], int M2[][], int M3[][], int T1[][], int T2[][], int T3[][],
			int recaudadoDiario[], String iBoletos[], int cantBoletos[], String codigo, int saldos[], int indexFuncion,
			String infoCartelera_A[][], int infoCartelera_B[][], int indexCliente, int Sala[][], boolean pMovilidad[]) {
		boolean repetir = true;
		int totalPagar = 0;
		String Boleto = iBoletos[indexCliente];
		while (repetir) {
			printSala(Sala);
			System.out.println("Ingrese la fila(A-J): ");
			Scanner S1 = new Scanner(System.in);
			String fila = S1.nextLine();
			while (indexFila(fila) == -1) {
				System.out.print("Ingrese la fila nuevamente: ");
				Scanner S1T = new Scanner(System.in);
				String F = S1T.nextLine();
				fila = F;
			}
			System.out.println("Ingrese la columna, de(1-30): ");
			Scanner S2 = new Scanner(System.in);
			String columna = S2.nextLine();
			while (indexColumna(columna) == -1) {
				System.out.print("Ingrese la columna nuevamente: ");
				Scanner S2T = new Scanner(System.in);
				String C = S2T.nextLine();
				columna = C;
			}
			while (Sala[indexColumna(columna)][indexFila(fila)] != -1) {
				System.out.println("Ese asiento no esta disponible");
				System.out.println("Ingrese la fila: ");
				Scanner S3 = new Scanner(System.in);
				fila = S3.nextLine();
				while (indexFila(fila) == -1) {
					System.out.print("Ingrese la fila nuevamente: ");
					Scanner S3T = new Scanner(System.in);
					String F = S3T.nextLine();
					fila = F;
				}
				System.out.println("Ingrese la columna: ");
				Scanner S4 = new Scanner(System.in);
				columna = S4.nextLine();
				while (indexColumna(columna) == -1) {
					System.out.print("Ingrese la columna nuevamente: ");
					Scanner S4T = new Scanner(System.in);
					String C = S4T.nextLine();
					columna = C;
				}
			}
			int indexF = indexFila(fila);
			int indexC = indexColumna(columna);
			Sala = asignarAsiento(indexCliente, Sala, indexC, indexF);
			totalPagar += calcularPago(indexCliente, indexFuncion, infoCartelera_A, pMovilidad);
			Boleto = crearBoletos(Boleto, fila, columna, codigo, cantBoletos, indexCliente);
			System.out.println("Costo: " + totalPagar);
			System.out.println("¿Desea agregar otra compra? (Y o N)");
			Scanner S5 = new Scanner(System.in);
			String OpcionR = S5.nextLine();
			while (OpcionR.equalsIgnoreCase("Y") == false && OpcionR.equalsIgnoreCase("N") == false) {
				System.out.println("¿Desea agregar otra compra? (Y o N)");
				Scanner S5T = new Scanner(System.in);
				String O = S5T.nextLine();
				OpcionR = O;
			}
			if (OpcionR.equalsIgnoreCase("N")) {
				repetir = false;
			}
		}
		System.out.println("Total a pagar: " + totalPagar);
		System.out.println("Su saldo es  : " + saldos[indexCliente]);
		while (totalPagar > saldos[indexCliente]) {
			System.out.println("Saldo insuficiente ¿Desea agregar saldo a su cuenta? (Y o N)");
			Scanner S6 = new Scanner(System.in);
			String OpcionR2 = S6.nextLine();
			while (OpcionR2.equalsIgnoreCase("Y") == false && OpcionR2.equalsIgnoreCase("N") == false) {
				System.out.println("Ingrese una opcion valida (Y o N): ");
				Scanner S6T = new Scanner(System.in);
				String O = S6T.nextLine();
				OpcionR2 = O;
			}
			if (OpcionR2.equalsIgnoreCase("N")) {
				return;
			} else {
				System.out.println("Introduzca la cantidad de saldo que desea agregar: ");
				Scanner S7 = new Scanner(System.in);
				String agregarSaldoS = S7.nextLine();
				int agregarSaldo = 0;
				if (correctNumber(agregarSaldoS) == true) {
					agregarSaldo = Integer.parseInt(agregarSaldoS);
				}
				while (agregarSaldo < 0 && agregarSaldo + saldos[indexCliente] < totalPagar
						&& correctNumber(agregarSaldoS) == false) {
					if (agregarSaldo < 0) {
						System.out.println("Introduzca un valor valido ");
					}
					if (agregarSaldo + saldos[indexCliente] < totalPagar) {
						System.out.println("El total del dinero sigue siendo insuficiente para realizar el pago");
					}
					Scanner S7T = new Scanner(System.in);
					String agregarSaldo2 = S7T.nextLine();
					if (correctNumber(agregarSaldo2) == true) {
						agregarSaldo = Integer.parseInt(agregarSaldo2);
					}
				}
				saldos[indexCliente] += agregarSaldo;
			}
		}
		System.out.println("Procesando pago... ");
		saldos[indexCliente] -= totalPagar;
		recaudadoDiario[indexFuncion] += totalPagar;
		iBoletos[indexCliente] = Boleto;
		guardarSala(Sala, codigo, M1, M2, M3, T1, T2, T3);
		calcularAsientoDisponibles(Sala, infoCartelera_B, indexFuncion);
		System.out.println("Pago realizado, volviendo al menú");
	}

	/**
	 * This function shows on the screen all the information of the "rut" that is
	 * entered by parameter
	 * 
	 * @param cantBoletos
	 * @param infoCartelera_B
	 * @param infoCartelera_A
	 * @param infoCliente
	 * @param indexCliente
	 * @param saldos
	 * @param iBoletos
	 */
	public static void desplegarInformacion(int cantBoletos[], int infoCartelera_B[][], String infoCartelera_A[][],
			String infoCliente[][], int indexCliente, int saldos[], String iBoletos[]) {
		System.out.println("Rut: " + infoCliente[indexCliente][2] + "	Nombre: " + infoCliente[indexCliente][0] + "	Apellido: "
				+ infoCliente[indexCliente][1]);
		System.out.println("Saldo: $" + saldos[indexCliente]);
		desplegarBoletos(cantBoletos, infoCartelera_B, infoCartelera_A, iBoletos, indexCliente);

	}

	/**
	 * This function will return a part of the money from the ticket purchases that
	 * the user has made and will make those seats available
	 * 
	 * @param recaudadoDiario
	 * @param saldos
	 * @param pMovilidad
	 * @param cantBoletos
	 * @param infoCartelera_B
	 * @param infoCartelera_A
	 * @param iBoletos
	 * @param indexCliente
	 * @param M1
	 * @param M2
	 * @param M3
	 * @param T1
	 * @param T2
	 * @param T3
	 */
	public static void devolucionBoletos(int recaudadoDiario[], int saldos[], boolean pMovilidad[], int cantBoletos[],
			int infoCartelera_B[][], String infoCartelera_A[][], String iBoletos[], int indexCliente, int M1[][], int M2[][],
			int M3[][], int T1[][], int T2[][], int T3[][]) {
		desplegarBoletos(cantBoletos, infoCartelera_B, infoCartelera_A, iBoletos, indexCliente);
		if (cantBoletos[indexCliente] == 0) {
			return;
		}
		String vectorBoletos[] = crearVectorBoletos(cantBoletos, iBoletos, indexCliente);
		int devolucion = 0;
		int newCant = cantBoletos[indexCliente];
		System.out.println("Ingrese el nombre de la pelicula de la que desea la devolución: ");
		Scanner S0 = new Scanner(System.in);
		String pelicula = S0.nextLine();
		while (indexM(pelicula, infoCartelera_A, 5, 0) == -1 && pelicula.equals("3") == false) {
			System.out.print("Ingrese el nombre de la pelicula nuevamente: ");
			Scanner S0T = new Scanner(System.in);
			String P = S0T.nextLine();
			pelicula = P;
		}
		if (pelicula.equals("3")) {
			return;
		}
		System.out.print("Ingrese el horario(M o T): ");
		Scanner S1 = new Scanner(System.in);
		String horario = S1.nextLine();

		while (buscarFuncion(pelicula, horario, infoCartelera_A) == -1 && horario.equals("3") == false) {
			System.out.print("Ingrese un horario valido(M o T): ");
			Scanner S1T = new Scanner(System.in);
			String P = S1T.nextLine();
			horario = P;
		}
		if (horario.equals("3")) {
			return;
		}
		int indexFuncion = buscarFuncion(pelicula, horario, infoCartelera_A);
		String codigo = infoCartelera_A[indexFuncion][3];
		if (darSala(codigo, M1, M2, M3, T1, T2, T3) == null) {
			return;
		}
		int Sala[][] = darSala(codigo, M1, M2, M3, T1, T2, T3);
		int cantidadAS = comprobarCantAsientosSala(Sala, indexCliente);
		if (cantidadAS == 0) {
			System.out.print("Usted no tiene ningun asiento para esta función, regresando al menú ...");
			return;
		} else {
			System.out.println("Usted tiene " + cantidadAS + " asiento/s para esta funcion");
			System.out.println(
					"==========================================================================================");
			String boletosSala[] = comprobarBoletosSala(vectorBoletos, codigo);
			for (int i = 0; i < cantidadAS; i++) {
				String boleto[] = boletosSala[i].split("-");
				System.out.println(
						"Sala " + boleto[0] + " Horario " + boleto[1] + " Fila " + boleto[2] + " Columna " + boleto[3]);
				System.out.println("Codigo: " + boletosSala[i]);
			}
			System.out.print("Ingrese la cantidad de boletos que desea rembolsar: ");
			Scanner S2 = new Scanner(System.in);
			int cantAs = 0;
			String ScantAS = S2.nextLine();
			if (correctNumber(ScantAS) == true) {
				cantAs = Integer.parseInt(ScantAS);
			}
			while (correctNumber(ScantAS) == false && cantAs == 0 || cantAs > cantidadAS) {
				System.out.print("Ingrese una cantidad valida: ");
				Scanner S2T = new Scanner(System.in);
				String O = S2T.nextLine();
				if (correctNumber(O) == true) {
					cantAs = Integer.parseInt(O);
					ScantAS = O;
				}
			}
			for (int i = 0; i < cantAs; i++) {
				System.out.print("Ingrese el codigo del asiento que desea la devolucion: ");
				Scanner S3 = new Scanner(System.in);
				String codigoDel = S3.nextLine();
				while (index(vectorBoletos, cantBoletos[indexCliente], codigoDel) == -1) {
					System.out.print("Codigo invalido, intentelo denuevo: ");
					Scanner S3T = new Scanner(System.in);
					String CD = S3T.nextLine();
					codigoDel = CD;
				}
				String partesBoletoDel[] = vectorBoletos[index(vectorBoletos, cantBoletos[indexCliente], codigoDel)]
						.split("-");
				Sala = desasignarAsiento(Sala, indexColumna(partesBoletoDel[3]), indexFila(partesBoletoDel[2]));
				vectorBoletos[index(vectorBoletos, cantBoletos[indexCliente], codigoDel)] = "";
				devolucion += devolucionDinero(indexFuncion, infoCartelera_A, pMovilidad, indexCliente);
				newCant--;
			}
			saldos[indexCliente] += devolucion;
			recaudadoDiario[indexFuncion] -= devolucion;
			iBoletos[indexCliente] = darBoleto(vectorBoletos, cantBoletos, indexCliente);
			cantBoletos[indexCliente] = newCant;
			guardarSala(Sala, codigo, M1, M2, M3, T1, T2, T3);
			System.out.println("La devolucion se ha hecho con exito. Monto devuelto: " + devolucion
					+ "	Su nuevo saldo es: " + saldos[indexCliente]);
			return;
		}
	}

	/**
	 * This function will show all the functions with their respective available
	 * seats.
	 * 
	 * @param infoCartelera_A
	 * @param infoCartelera_B
	 */
	public static void desplegarCartelera(String infoCartelera_A[][], int infoCartelera_B[][]) {
		for (int i = 0; i <= 5; i++) {
			if (infoCartelera_B[i][0] > 0) {
				System.out.println("- " + infoCartelera_A[i][0] + " :");
				for (int j = 0; j <= 5; j++) {
					if (infoCartelera_A[j][0].equals(infoCartelera_A[i][0])) {
						System.out.println("  Sala " + infoCartelera_B[j][1] + " Horario " + infoCartelera_A[j][2]
								+ "	Asientos disponibles: " + infoCartelera_B[j][2]);
					}
				}
			}
		}
	}

	// RF4
	/**
	 * This function is necessary to access the administrator options
	 * 
	 * @param cantBoletos
	 * @param infoCliente
	 * @param saldos
	 * @param iBoletos
	 * @param contadorClientes
	 * @param rutSimpleimple
	 * @param recaudadoDiario
	 * @param infoCartelera_A
	 * @param infoCartelera_B
	 */
	public static void logAdmin(int cantBoletos[], String infoCliente[][], int saldos[], String iBoletos[],
			int contadorClientes, String rutSimpleimple[], int recaudadoDiario[], String infoCartelera_A[][],
			int infoCartelera_B[][]) {
		System.out.print("Ingrese la contraseña: ");
		Scanner S0 = new Scanner(System.in);
		String contraseña = S0.nextLine();
		boolean AdminOption = true;
		while (contraseña.equals("ADMIN") == false) {
			System.out.print("Contraseña incorrecta, intentelo denuevo o escriba 3 para salir:");
			Scanner S0T = new Scanner(System.in);
			String passtry = S0T.nextLine();
			contraseña = passtry;
			if (contraseña.equals("3")) {
				return;
			}
		}
		if (AdminOption) {
			System.out.println("Bienvenido al menu de Administrador");
			while (optionAdmin(cantBoletos, infoCliente, saldos, iBoletos, contadorClientes, rutSimpleimple, recaudadoDiario,
					infoCartelera_A, infoCartelera_B)) {
			}
		}
	}

	/**
	 * This function will show all the options available in administrator mode.
	 * 
	 * @param cantBoletos
	 * @param infoCliente
	 * @param saldos
	 * @param iBoletos
	 * @param contadorClientes
	 * @param rutSimpleimple
	 * @param recaudadoDiario
	 * @param infoCartelera_A
	 * @param infoCartelera_B
	 * @return
	 */
	public static boolean optionAdmin(int cantBoletos[], String infoCliente[][], int saldos[], String iBoletos[],
			int contadorClientes, String rutSimpleimple[], int recaudadoDiario[], String infoCartelera_A[][],
			int infoCartelera_B[][]) {
		System.out.println("¿Que accion quiere realizar?");
		String option = ingresarOpcion("Ver Taquilla", "Buscar Cliente", "Salir", "");
		if (option.equals("1")) {
			for (int i = 0; i <= 5; i++) {
				if (infoCartelera_B[i][0] > 0) {
					System.out.println("- " + infoCartelera_A[i][0] + " :");
					int recaudototaldiario = 0;
					for (int j = 0; j <= 5; j++) {
						if (infoCartelera_A[j][0].equals(infoCartelera_A[i][0])) {
							System.out.println("- Sala " + infoCartelera_B[j][1] + " Horario " + infoCartelera_A[j][2]
									+ " : $" + recaudadoDiario[j]);
							recaudototaldiario += recaudadoDiario[j];
						}
					}
					System.out.println("Recaudo diario : $" + recaudototaldiario + "		Total : $"
							+ (infoCartelera_B[i][0] + recaudototaldiario));
				}
			}

			return true;
		}
		if (option.equals("2")) {
			System.out.print("Ingrese el rut del cliente (ADMIN o 3 para volver): ");
			Scanner S0 = new Scanner(System.in);
			String rut = S0.nextLine();
			String rutSim = getrutSimpleimplificado(rut);
			while (conditionsLog(rutSimpleimple, contadorClientes, rutSim, rut)) {
				System.out.println("Por favor ingrese una opcion valida: ");
				Scanner S1 = new Scanner(System.in);
				String ruttry = S1.nextLine();
				String rutsimtry = getrutSimpleimplificado(rut);
				rut = ruttry;
				rutSim = rutsimtry;
				if (rut.equals("3")) {
					return true;
				}
				if (rut.equals("ADMIN")) {
					return true;
				}

			}
			int indexCliente = index(rutSimpleimple, contadorClientes, rutSim);
			desplegarInformacion(cantBoletos, infoCartelera_B, infoCartelera_A, infoCliente, indexCliente, saldos, iBoletos);
			return true;
		}
		return false;
	}

	// RF5
	/**
	 * This function will save the customer data and overwrite the new information
	 * in the text file "customers.txt".
	 * 
	 * @param infoClientes
	 * @param contadorClientes
	 * @param saldos
	 * @throws IOException
	 */
	public static void Save1(String infoClientes[][], int contadorClientes, int saldos[]) throws IOException {
		FileWriter file = new FileWriter("clientes.txt");
		PrintWriter escritura = new PrintWriter(file);
		for (int i = 0; i < contadorClientes; i++) {
			escritura.println(infoClientes[i][0] + "," + infoClientes[i][1] + "," + infoClientes[i][2] + ","
					+ infoClientes[i][3] + "," + saldos[i]);
		}
		escritura.close();
	}

	/**
	 * This function will be in charge of saving the customer data regarding the
	 * mobility pass and overwriting the new information in the text file
	 * "status.txt"
	 * 
	 * @param infoCliente
	 * @param contadorClientes
	 * @param pMovilidad
	 * @throws IOException
	 */
	public static void Save2(String infoCliente[][], int contadorClientes, boolean pMovilidad[]) throws IOException {
		FileWriter file = new FileWriter("status.txt");
		PrintWriter escritura = new PrintWriter(file);
		for (int i = 0; i < contadorClientes; i++) {
			String status;
			if (pMovilidad[i] == true) {
				status = "HABILITADO";
			} else {
				status = "NO HABILITADO";
			}
			escritura.println(infoCliente[i][2] + "," + status);
		}
		escritura.close();
	}

	/**
	 * This function will be in charge of saving the data of the functions and
	 * overwriting the new information in the text file "movies.txt"
	 * 
	 * @param infoCartelera_A
	 * @param infoCartelera_B
	 * @param recaudadoDiario
	 * @param pMovilidad
	 * @throws IOException
	 */
	public static void Save3(String infoCartelera_A[][], int infoCartelera_B[][], int recaudadoDiario[],
			boolean pMovilidad[]) throws IOException {
		FileWriter file = new FileWriter("peliculas.txt");
		PrintWriter escritura = new PrintWriter(file);
		for (int i = 0; i < 6; i++) {
			if (infoCartelera_B[i][0] != 0) {
				int total = (infoCartelera_B[i][0] + recaudadoDiario[i]);
				String horarios = "";
				for (int j = 0; j < 6; j++) {
					if (infoCartelera_A[i][0].equals(infoCartelera_A[j][0])) {
						horarios += "," + infoCartelera_B[j][1] + "," + infoCartelera_A[j][2];
					}
				}
				escritura.println(infoCartelera_A[i][0] + "," + infoCartelera_A[i][1] + "," + total + horarios);
			}
		}
		escritura.close();
	}

	// FUNCIONES GEBERALES
	/**
	 * This function calculates the seats available in a room
	 * 
	 * @param Sala
	 * @param infoCartelera_B
	 * @param indexFuncion
	 */
	public static void calcularAsientoDisponibles(int Sala[][], int infoCartelera_B[][], int indexFuncion) {
		int cant = 0;
		for (int i = 0; i < 30; i++) {
			for (int j = 0; j < 10; j++) {
				if (Sala[i][j] == -1) {
					cant++;
				}
			}
		}
		infoCartelera_B[indexFuncion][2] = cant;
	}

	/**
	 * This function create a vector with the tickets of the client
	 * 
	 * @param cantBoletos
	 * @param iBoletos
	 * @param indexCliente
	 * @return
	 */
	public static String[] crearVectorBoletos(int cantBoletos[], String iBoletos[], int indexCliente) {
		String InventarioBoletos[];
		if (cantBoletos[indexCliente] == 1) {
			InventarioBoletos = new String[1];
			InventarioBoletos[0] = iBoletos[indexCliente];
			return InventarioBoletos;
		} else {
			InventarioBoletos = iBoletos[indexCliente].split(",");
			return InventarioBoletos;
		}
	}

	/**
	 * This function takes care of marking the seats as occupied
	 * 
	 * @param Sala
	 * @param x
	 * @param y
	 * @return
	 */
	public static int[][] desasignarAsiento(int Sala[][], int x, int y) {
		if ((x <= 29) && (y >= 4)) {
			Sala[x][y] = -1;
			if (x == 0) {
				if (Sala[x + 2][y] == -1) {
					Sala[x + 1][y] = -1;
					return Sala;
				}
			}
			if (x == 29) {
				if (Sala[x - 2][y] == -1) {
					Sala[x - 1][y] = -1;
					return Sala;
				}
			}
			if (x > 0 && x < 29) {
				if (Sala[x + 2][y] == -1) {
					Sala[x + 1][y] = -1;
				}
				if (Sala[x - 2][y] == -1) {
					Sala[x - 1][y] = -1;
				}
			}
		}
		if ((x >= 5 && x <= 25) && (y <= 3)) {
			Sala[x][y] = -1;
			if (x == 5) {
				if (Sala[x + 2][y] == -1) {
					Sala[x + 1][y] = -1;
					return Sala;
				}
			}
			if (x == 24) {
				if (Sala[x - 2][y] == -1) {
					Sala[x - 1][y] = -1;
					return Sala;
				}
			}
			if (x > 4 && x < 24) {
				if (Sala[x + 2][y] == -1) {
					Sala[x + 1][y] = -1;
				}
				if (Sala[x - 2][y] == -1) {
					Sala[x - 1][y] = -1;
				}
			}
		}
		return Sala;
	}

	/**
	 * This function returns a String that is going to be placed in the inventory of
	 * the client
	 * 
	 * @param vectorBoletos
	 * @param cantBoletos
	 * @param indexCliente
	 * @return
	 */
	public static String darBoleto(String vectorBoletos[], int cantBoletos[], int indexCliente) {
		String boleto = "";
		int cant = 0;
		boolean first = true;
		for (int i = 0; i < cantBoletos[indexCliente]; i++) {
			if (cant != 0 && vectorBoletos[i] != "" && first == false) {
				boleto = boleto + "," + vectorBoletos[i];
				cant++;
			}
			if (cant == 0 && vectorBoletos[i] != "" && first == true) {
				boleto = vectorBoletos[i];
				first = false;
				cant++;
			}
		}
		return boleto;
	}

	/**
	 * This function return the cash of a seat
	 * 
	 * @param indexFuncion
	 * @param infoCartelera_A
	 * @param pMovilidad
	 * @param indexCliente
	 * @return
	 */
	public static int devolucionDinero(int indexFuncion, String infoCartelera_A[][], boolean pMovilidad[], int indexCliente) {
		if (infoCartelera_A[indexFuncion][1].equals("estreno")) {
			if (pMovilidad[indexCliente] = true) {
				return 3780;
			} else {
				return 4400;
			}
		}
		if (infoCartelera_A[indexFuncion][1].equals("estreno")) {
			if (pMovilidad[indexCliente] = true) {
				return 2720;
			} else {
				return 3200;
			}
		}
		return 0;
	}

	/**
	 * This function returns a vector with the tickets of the client that are linked
	 * to the function
	 * 
	 * @param vectorB
	 * @param codigo
	 * @return
	 */
	public static String[] comprobarBoletosSala(String vectorB[], String codigo) {
		int cant = 0, aux = 0;
		for (int i = 0; i < vectorB.length; i++) {
			String boleto[] = vectorB[i].split("-");
			String codigoBoleto = boleto[1] + boleto[0];
			if (codigoBoleto.equals(codigo)) {
				cant++;
			}
		}
		String vectorBSala[] = new String[cant];
		for (int i = 0; i < cant; i++) {
			String boleto[] = vectorB[i].split("-");
			String codigoBoleto = boleto[1] + boleto[0];
			if (codigoBoleto.equals(codigo)) {
				vectorBSala[aux] = vectorB[i];
				aux++;
			}
		}
		return vectorBSala;
	}

	/**
	 * This function returns the seats available in a room
	 * 
	 * @param Sala
	 * @param indexCliente
	 * @return
	 */
	public static int comprobarCantAsientosSala(int Sala[][], int indexCliente) {
		int cant = 0;
		for (int j = 0; j <= 9; j++) {
			for (int i = 0; i <= 29; i++) {
				if (Sala[i][j] == indexCliente) {
					cant++;
				}
			}
		}
		return cant;
	}

	/**
	 * This function deploys all the tickets of a client
	 * 
	 * @param cantBoletos
	 * @param infoCartelera_B
	 * @param infoCartelera_A
	 * @param iBoletos
	 * @param indexCliente
	 */
	public static void desplegarBoletos(int cantBoletos[], int infoCartelera_B[][], String infoCartelera_A[][],
			String iBoletos[], int indexCliente) {
		String inventarioPeliculas = iBoletos[indexCliente];
		int IndexP;
		if (cantBoletos[indexCliente] == 0) {
			System.out.println("No tiene boletos para ninguna funcion");
			return;
		}
		if (cantBoletos[indexCliente] == 1) {
			System.out.println("Cantidad de boletos: " + cantBoletos[indexCliente]);
			String partesP[] = inventarioPeliculas.split("-");
			String codigo = partesP[1] + partesP[0];
			IndexP = indexM(codigo, infoCartelera_A, 5, 3);
			System.out.println("	- " + infoCartelera_A[IndexP][0] + "		N° Sala: " + infoCartelera_B[IndexP][1]
					+ "		Horario: " + infoCartelera_A[IndexP][2]);
			System.out.println("	Fila: " + partesP[2] + "		Columna: " + partesP[3]);
			return;
		}
		System.out.println("Cantidad de boletos: " + cantBoletos[indexCliente]);
		String peliculas[] = inventarioPeliculas.split(",");
		for (int i = 0; i < peliculas.length; i++) {
			String partesP[] = peliculas[i].split("-");
			String codigo = partesP[1] + partesP[0];
			IndexP = indexM(codigo, infoCartelera_A, 5, 3);
			System.out.println("	- " + infoCartelera_A[IndexP][0] + "		N° Sala: " + infoCartelera_B[IndexP][1]
					+ "		Horario: " + infoCartelera_A[IndexP][2]);
			System.out.println("	Fila: " + partesP[2] + "		Columna: " + partesP[3]);
		}
	}

	/**
	 * This function checks if a string can be a number
	 * 
	 * @param numero
	 * @return
	 */
	public static boolean correctNumber(String numero) {
		String characters[] = numero.split("");
		for (int i = 0; i < characters.length; i++) {
			if (characters[i].equals("0") == false && characters[i].equals("1") == false
					&& characters[i].equals("2") == false && characters[i].equals("3") == false
					&& characters[i].equals("4") == false && characters[i].equals("5") == false
					&& characters[i].equals("6") == false && characters[i].equals("7") == false
					&& characters[i].equals("8") == false && characters[i].equals("9") == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * This function save the changes of the room
	 * 
	 * @param Sala
	 * @param codigo
	 * @param M1
	 * @param M2
	 * @param M3
	 * @param T1
	 * @param T2
	 * @param T3
	 */
	public static void guardarSala(int Sala[][], String codigo, int M1[][], int M2[][], int M3[][], int T1[][],
			int T2[][], int T3[][]) {
		if (codigo.equals("M1")) {
			M1 = Sala;
		}
		if (codigo.equals("M2")) {
			M2 = Sala;
		}
		if (codigo.equals("M3")) {
			M3 = Sala;
		}
		if (codigo.equals("T1")) {
			T1 = Sala;
		}
		if (codigo.equals("T2")) {
			T2 = Sala;
		}
		if (codigo.equals("T3")) {
			T3 = Sala;
		}
		return;
	}

	/**
	 * This function create a String with the tickets of a client
	 * 
	 * @param Boleto
	 * @param fila
	 * @param columna
	 * @param codigo
	 * @param cantBoletos
	 * @param indexCliente
	 * @return
	 */
	public static String crearBoletos(String Boleto, String fila, String columna, String codigo, int cantBoletos[],
			int indexCliente) {
		String partes[] = codigo.split("");
		String horario = partes[0];
		String N_Sala = partes[1];
		if (cantBoletos[indexCliente] == 0) {
			cantBoletos[indexCliente]++;
			return N_Sala + "-" + horario + "-" + fila + "-" + columna;
		} else {
			cantBoletos[indexCliente]++;
			return Boleto + "," + N_Sala + "-" + horario + "-" + fila + "-" + columna;
		}
	}

	/**
	 * This function return the cash that needs to be paid for ticket
	 * 
	 * @param indexCliente
	 * @param indexFuncion
	 * @param infoCartelera_A
	 * @param pMovilidad
	 * @return
	 */
	public static int calcularPago(int indexCliente, int indexFuncion, String infoCartelera_A[][], boolean pMovilidad[]) {
		if (infoCartelera_A[indexFuncion][1].equalsIgnoreCase("estreno")) {
			if (pMovilidad[indexCliente]) {
				return 4675;
			} else {
				return 5500;
			}
		}
		if (infoCartelera_A[indexFuncion][1].equalsIgnoreCase("liberada")) {
			if (pMovilidad[indexCliente]) {
				return 3400;
			} else {
				return 4000;
			}
		}
		return 0;
	}

	/**
	 * This function assigns the seats to be occupied by the client
	 * 
	 * @param indexCliente
	 * @param Sala
	 * @param x
	 * @param y
	 * @return
	 */
	public static int[][] asignarAsiento(int indexCliente, int Sala[][], int x, int y) {
		if ((x >= 0 && x <= 29) && (y >= 4)) {
			Sala[x][y] = indexCliente;
			if (x == 0) {
				if (Sala[x + 1][y] == -1) {
					Sala[x + 1][y] = -2;
					return Sala;
				}
			}
			if (x == 29) {
				if (Sala[x - 1][y] == -1) {
					Sala[x - 1][y] = -2;
					return Sala;
				}
			} else {
				if (Sala[x + 1][y] == -1) {
					Sala[x + 1][y] = -2;
				}
				if (Sala[x - 1][y] == -1) {
					Sala[x - 1][y] = -2;
				}
			}
		}
		if ((x >= 5 && x <= 24) && (y <= 3)) {
			Sala[x][y] = indexCliente;
			if (x == 5) {
				if (Sala[x + 1][y] == -1) {
					Sala[x + 1][y] = -2;
					return Sala;
				}
			}
			if (x == 24) {
				if (Sala[x - 1][y] == -1) {
					Sala[x - 1][y] = -2;
					return Sala;
				}
			} else {
				if (Sala[x + 1][y] == -1) {
					Sala[x + 1][y] = -2;
				}
				if (Sala[x - 1][y] == -1) {
					Sala[x - 1][y] = -2;
				}
			}
		}
		return Sala;
	}

	/**
	 * This function return a index
	 * 
	 * @param Fila
	 * @return
	 */
	public static int indexFila(String Fila) {
		if (Fila.equals("A")) {
			return 0;
		}
		if (Fila.equals("B")) {
			return 1;
		}
		if (Fila.equals("C")) {
			return 2;
		}
		if (Fila.equals("D")) {
			return 3;
		}
		if (Fila.equals("E")) {
			return 4;
		}
		if (Fila.equals("F")) {
			return 5;
		}
		if (Fila.equals("G")) {
			return 6;
		}
		if (Fila.equals("H")) {
			return 7;
		}
		if (Fila.equals("I")) {
			return 8;
		}
		if (Fila.equals("J")) {
			return 9;
		}
		return -1;
	}

	/**
	 * This function return a index
	 * 
	 * @param columna
	 * @return
	 */
	public static int indexColumna(String columna) {
		if (columna.equalsIgnoreCase("1")) {
			return 0;
		}
		if (columna.equalsIgnoreCase("2")) {
			return 1;
		}
		if (columna.equalsIgnoreCase("3")) {
			return 2;
		}
		if (columna.equalsIgnoreCase("4")) {
			return 3;
		}
		if (columna.equalsIgnoreCase("5")) {
			return 4;
		}
		if (columna.equalsIgnoreCase("6")) {
			return 5;
		}
		if (columna.equalsIgnoreCase("7")) {
			return 6;
		}
		if (columna.equalsIgnoreCase("8")) {
			return 7;
		}
		if (columna.equalsIgnoreCase("9")) {
			return 8;
		}
		if (columna.equalsIgnoreCase("10")) {
			return 9;
		}
		if (columna.equalsIgnoreCase("11")) {
			return 10;
		}
		if (columna.equalsIgnoreCase("12")) {
			return 11;
		}
		if (columna.equalsIgnoreCase("13")) {
			return 12;
		}
		if (columna.equalsIgnoreCase("14")) {
			return 13;
		}
		if (columna.equalsIgnoreCase("15")) {
			return 14;
		}
		if (columna.equalsIgnoreCase("16")) {
			return 15;
		}
		if (columna.equalsIgnoreCase("17")) {
			return 16;
		}
		if (columna.equalsIgnoreCase("18")) {
			return 17;
		}
		if (columna.equalsIgnoreCase("19")) {
			return 18;
		}
		if (columna.equalsIgnoreCase("20")) {
			return 19;
		}
		if (columna.equalsIgnoreCase("21")) {
			return 20;
		}
		if (columna.equalsIgnoreCase("22")) {
			return 21;
		}
		if (columna.equalsIgnoreCase("23")) {
			return 22;
		}
		if (columna.equalsIgnoreCase("24")) {
			return 23;
		}
		if (columna.equalsIgnoreCase("25")) {
			return 24;
		}
		if (columna.equalsIgnoreCase("26")) {
			return 25;
		}
		if (columna.equalsIgnoreCase("27")) {
			return 26;
		}
		if (columna.equalsIgnoreCase("28")) {
			return 27;
		}
		if (columna.equalsIgnoreCase("29")) {
			return 28;
		}
		if (columna.equalsIgnoreCase("30")) {
			return 29;
		}
		return -1;
	}

	/**
	 * This function returns the matrix of a room
	 * 
	 * @param codigo
	 * @param M1
	 * @param M2
	 * @param M3
	 * @param T1
	 * @param T2
	 * @param T3
	 * @return
	 */
	public static int[][] darSala(String codigo, int M1[][], int M2[][], int M3[][], int T1[][], int T2[][],
			int T3[][]) {
		if (codigo.equals("M1")) {
			return M1;
		}
		if (codigo.equals("M2")) {
			return M2;
		}
		if (codigo.equals("M3")) {
			return M3;
		}
		if (codigo.equals("T1")) {
			return T1;
		}
		if (codigo.equals("T2")) {
			return T2;
		}
		if (codigo.equals("T3")) {
			return T3;
		}
		return null;
	}

	/**
	 * This function return the index of a function
	 * 
	 * @param pelicula
	 * @param horario
	 * @param MatrizInfCar_A
	 * @return
	 */
	public static int buscarFuncion(String pelicula, String horario, String MatrizInfCar_A[][]) {
		for (int i = 0; i < 6; i++) {
			if (MatrizInfCar_A[i][0].equalsIgnoreCase(pelicula) && MatrizInfCar_A[i][2].equals(horario)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * This function verify if a rut is well written
	 * 
	 * @param rut
	 * @param Ruts
	 * @param contadorClientes
	 * @param rutSimpleim
	 * @return
	 */
	public static boolean ConditionsRegisterUsu(String rut, String Ruts[], int contadorClientes, String rutSimpleim) {
		if (CorrectRut(rut) == false) {
			System.out.println("Por favor ingrese el rut correctamente");
			return false;
		}
		if ((index(Ruts, contadorClientes, rutSimpleim) != -1)) {
			System.out.println("Este rut ya esta registrado");
			return false;
		}
		if (rutSimpleim.equals("ADMIN")) {
			System.out.println("Rut invalido");
			return false;
		}
		return true;
	}

	/**
	 * This function verify if a rut is well written with the dots and commas
	 * 
	 * @param Rut
	 * @return
	 */
	public static boolean CorrectRut(String Rut) {
		String rut = Rut;
		String partes[] = rut.split("\\.");
		if (partes.length == 3) {
			String parte3 = partes[2];
			String partesgion[] = parte3.split("-");
			if (partesgion.length == 2) {
				if (partes.length == 3 && partesgion.length == 2) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * This function gives options for the menu
	 * 
	 * @param A
	 * @param B
	 * @param C
	 * @param D
	 * @return
	 */
	public static String ingresarOpcion(String A, String B, String C, String D) {
		String MainOption;
		if (D.equals("")) {
			System.out.println(
					"==========================================================================================");
			System.out.println("1) " + A + "		2) " + B + "	3) " + C);
			System.out.println(
					"==========================================================================================");
			Scanner S = new Scanner(System.in);
			MainOption = S.nextLine();
			while ((MainOption.equals("1")) == false && (MainOption.equals("2")) == false
					&& (MainOption.equals("3")) == false) {
				System.out.print("Opcion invalida");
				Scanner S1 = new Scanner(System.in);
				String MainOptiontry = S1.nextLine();
				MainOption = MainOptiontry;
			}
		} else {
			System.out.println(
					"==========================================================================================");
			System.out.println("1) " + A + "	2) " + B + "	3) " + C + "	4)" + D + "	5) Salir");
			System.out.println(
					"==========================================================================================");
			Scanner S = new Scanner(System.in);
			MainOption = S.nextLine();
			while ((MainOption.equals("1")) == false && (MainOption.equals("2")) == false
					&& (MainOption.equals("3")) == false && (MainOption.equals("4")) == false
					&& (MainOption.equals("5")) == false) {
				System.out.print("Opcion invalida");
				Scanner S1 = new Scanner(System.in);
				String MainOptiontry = S1.nextLine();
				MainOption = MainOptiontry;
			}
		}
		return MainOption;
	}

	/**
	 * This function creates the matrix of a room
	 * 
	 * @return
	 */
	public static int[][] crearSala() {
		int Sala[][] = new int[30][10];
		for (int i = 0; i <= 29; i++) {
			for (int j = 0; j <= 9; j++) {
				Sala[i][j] = -1;
			}
		}

		for (int i = 0; i <= 4; i++) {
			for (int j = 0; j < 4; j++) {
				Sala[i][j] = -3;
			}
		}
		for (int i = 25; i <= 29; i++) {
			for (int j = 0; j < 4; j++) {
				Sala[i][j] = -3;
			}
		}
		return Sala;
	}

	/**
	 * This function print the matrix of a room
	 * 
	 * @param Sala
	 */
	public static void printSala(int Sala[][]) {
		System.out
				.println("========================================Pantalla==========================================");
		for (int j = 0; j <= 9; j++) {
			System.out.println();
			for (int i = 0; i <= 29; i++) {
				if (Sala[i][j] == -3) {
					System.out.print("   ");
				}
				if (Sala[i][j] == -1) {
					System.out.print(" O ");
				}
				if (Sala[i][j] == -2) {
					System.out.print(" - ");
				}
				if (Sala[i][j] >= 0) {
					System.out.print(" # ");
				}
			}
		}
		System.out.println("");
		System.out
				.println("==========================================================================================");

	}

	/**
	 * This function returns a simplified rut
	 * 
	 * @param Rut
	 * @return
	 */
	public static String getrutSimpleimplificado(String Rut) {
		Rut = Rut.replace(".", "");
		Rut = Rut.replace("-", "");
		return Rut;
	}

	/**
	 * This function returns the index of a matrix
	 * 
	 * @param dato
	 * @param Matriz
	 * @param cant
	 * @param columna
	 * @return
	 */
	public static int indexM(String dato, String Matriz[][], int cant, int columna) {
		for (int i = 0; i <= cant; i++) {
			if (Matriz[i][columna].equalsIgnoreCase(dato)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * This function creates the conditions to log in
	 * 
	 * @param rutSimple
	 * @param contadorClientes
	 * @param rutSim
	 * @param rut
	 * @return
	 */
	public static boolean conditionsLog(String rutSimple[], int contadorClientes, String rutSim, String rut) {
		if (index(rutSimple, contadorClientes, rutSim) != -1) {
			return false;
		}
		if (rut.equals("ADMIN")) {
			return false;
		}
		return true;
	}

	/**
	 * This function returns the index of a vector2
	 * 
	 * @param lista
	 * @param contador
	 * @param dato
	 * @return
	 */
	public static int index(String lista[], int contador, String dato) {
		for (int i = 0; i < contador; i++) {
			if (lista[i].equalsIgnoreCase(dato)) {
				return i;
			}
		}
		return -1;
	}
}
