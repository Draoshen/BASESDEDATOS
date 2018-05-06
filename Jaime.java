

	
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;

public class Diagnostico {

	//ATRIBUTOS IMPRESCINDIMBLES
	private final String DATAFILE = "data/disease_data.data";
	private Connection conn = null;
	private void showMenu() {

		int option = -1;
		do {
			System.out.println("Bienvenido a sistema de diagn�stico\n");
			System.out.println("Selecciona una opci�n:\n");
			System.out.println("\t1. Creaci�n de base de datos y carga de datos.");
			System.out.println("\t2. Realizar diagn�stico.");
			System.out.println("\t3. Listar s�ntomas de una enfermedad.");
			System.out.println("\t4. Listar enfermedades y sus c�digos asociados.");
			System.out.println("\t5. Listar s�ntomas existentes en la BD y su tipo sem�ntico.");
			System.out.println("\t6. Mostrar estad�sticas de la base de datos.");
			System.out.println("\t7. Salir.");
			try {
				option = readInt();
				switch (option) {
				case 1:
					crearBD();
					break;
				case 2:
					realizarDiagnostico();
					break;
				case 3:
					listarSintomasEnfermedad();
					break;
				case 4:
					listarEnfermedadesYCodigosAsociados();
					break;
				case 5:
					listarSintomasYTiposSemanticos();
					break;
				case 6:
					mostrarEstadisticasBD();
					break;
				case 7:
					exit();
					break;
				}
			} catch (Exception e) {
				//System.err.println("Opci�n introducida no v�lida!");
				e.printStackTrace();
			}
		} while (option != 7);
		exit();
	}

	private void exit() {
		System.out.println("Saliendo.. �hasta otra!");
		System.exit(0);
	}
	
	private void conectar() {
		String drv = "com.mysql.jdbc.Driver";
		
			try {
				Class.forName(drv);
			} catch (ClassNotFoundException e1) {
			
				e1.printStackTrace();
				System.out.println("Clase no encontrada!");
			}
	

		String serverAddress = "localhost:3306";
		String user = "bddx";
		String pass = "bddx_pwd";
		String url = "jdbc:mysql://" + serverAddress + "/" ;
		
			 try {
				conn =  DriverManager.getConnection(url, user, pass);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		System.out.println("Conectado a la base de datos!");

	}

	private void crearBD() throws Exception {
	
		//Creaci�n de la base de datos en base al esquema E-R y carga de los datos del fichero
		//.data (ambos proporcionados). [3 puntos]: Dado el fichero de datos (ver secci�n 6 para
		//	m�s informaci�n), se debe cargar su contenido usando c�digo Java, procesarlo y en base
		//	a la estructura del E-R y los datos contenidos, insertar la informaci�n en la base de
		//datos. Debe tenerse en cuenta el diagrama E-R para entender como son los datos y
		//	como deben por lo tanto almacenarse. Obligatoriamente, debe ejecutarse la creaci�n y
		//	carga de todos los datos como si fuera una �nica transacci�n, de tal forma que cualquier
		//	fallo intermedio de lugar a deshacer por completo los cambios anteriores.
		
		
		
		//RECUERDA LA RECETA
		
		//1 String Code= "codigo en SQL"
		//2 PreparedStatement nombre = conn.prepareStament(Code);
		//3 nombre.executeQuery
		//consultoEn.setString(1,"hola");
		//lo que hace esto es, DONDE ESTE LAL PRIMERA "?" pones "hola",  y hace la consulta en base a "hola"
		//y as� para todo
		
		
		conectar();
		PreparedStatement stmt = conn.prepareStatement("DROP DATABASE IF EXISTS diagnostico;");
		stmt.executeUpdate();
	

		
		//PRIMERO CREO LA BASE DE DATOS Y LAS TABLAs
	    conn.setAutoCommit(false);

		
		 String SQLcodigoDB = "CREATE DATABASE  diagnostico;";
		 
		 String SQLcodigoUSE = "USE  diagnostico;";
		 
		 String SQLcodigoEN  = "CREATE TABLE  `disease` (`disease_id` INT AUTO_INCREMENT NOT NULL ,`name` VARCHAR(255), PRIMARY KEY(`disease_id`))ENGINE=Inno08;";
	
		 String SQLcodigoS  = "CREATE TABLE `sympton` (`cui` VARCHAR(45) NOT NULL,`name` VARCHAR(255), PRIMARY KEY(`cui`))ENGINE=Inno08;";
		
		 String SQLcodigoC  = "CREATE TABLE `code` (`source_id` INT NOT NULL,`code` VARCHAR (255) NOT NULL,PRIMARY KEY(`code`), FOREIGN KEY(`source_id`) REFERENCES `source` (`source_id`))ENGINE=Inno08;";
		
		 String SQLcodigoD  = "CREATE TABLE `disease_has_code` (`disease_id` INT NOT NULL, `code` VARCHAR (255) NOT NULL,`source_id` INT NOT NULL,FOREIGN KEY (`disease_id`) REFERENCES `disease` (`disease_id`),FOREIGN KEY(`code`) REFERENCES `code` (`code`),FOREIGN KEY (`source_id`) REFERENCES code (`source_id`))ENGINE=Inno08;";
		
		 String SQLcodigoSE = "CREATE TABLE  `semantic_type` (`semantic_type_id` INT AUTO_INCREMENT  NOT NULL,`cui` VARCHAR(45) UNIQUE NOT NULL,PRIMARY KEY(`semantic_type_id` ))ENGINE=Inno08;";
		
		 String SQLcodigoSS  = "CREATE TABLE  `symptom_semantic_type` (`semantic_type_id` INT NOT NULL,`cui` VARCHAR(25) NOT NULL,FOREIGN KEY (`cui`) REFERENCES `symptom` (`cui`),FOREIGN KEY  (`semantic_type_id` ) REFERENCES  `semantic_type` (`semantic_type_id`))ENGINE=Inno08;";
		
		 String SQLcodigoSO  = "CREATE TABLE  `source` (`source_id` INT AUTO_INCREMENT  NOT NULL,`name` VARCHAR (255) UNIQUE NOT NULL,PRIMARY KEY (`source_id`))ENGINE=Inno08;";
		
		 String SQLcodigoDS  = "CREATE TABLE `disease_symptom` (`disease_id` INT AUTO_INCREMENT NOT NULL,`cui` VARCHAR(25) NOT NULL,FOREIGN KEY (`disease_id`) REFERENCES `disease` (`disease_id`),FOREIGN KEY (`cui`) REFERENCES symptom (`cui`))ENGINE=Inno08;";

		 //lo pongo de esta manera pues parece que as� est� m�s limpio
		
			PreparedStatement SQLcodigoDBst = conn.prepareStatement(SQLcodigoDB );
			PreparedStatement SQLcodigoENst = conn.prepareStatement(SQLcodigoEN);
			PreparedStatement SQLcodigoUSEst = conn.prepareStatement(SQLcodigoUSE);
			PreparedStatement SQLcodigoSst =  conn.prepareStatement(SQLcodigoS );
			PreparedStatement SQLcodigoCst  = conn.prepareStatement(SQLcodigoC  );
			PreparedStatement SQLcodigoDst  = conn.prepareStatement(SQLcodigoD  );
			PreparedStatement SQLcodigoSEst = conn.prepareStatement(SQLcodigoSE );
			PreparedStatement SQLcodigoSSst = conn.prepareStatement(SQLcodigoSS );
			PreparedStatement SQLcodigoSOst = conn.prepareStatement(SQLcodigoSO );
			PreparedStatement SQLcodigoDSst = conn.prepareStatement(SQLcodigoDS  );
			
			//y claro est� ponems el commit por si alguno de los procesos falla
			
			SQLcodigoDBst.executeUpdate();
		
			SQLcodigoUSEst.executeUpdate();
		
			SQLcodigoENst.executeUpdate();
		
			 SQLcodigoSst.executeUpdate();
		
			 SQLcodigoCst.executeUpdate();
		
			 SQLcodigoDst.executeUpdate();
		
			 SQLcodigoSEst.executeUpdate();
		
			 SQLcodigoSSst.executeUpdate();
		
			 SQLcodigoSOst.executeUpdate();
		
			 SQLcodigoDSst.executeUpdate();
		
		
		
		//SEGUNDO DISECCIONAMOS LOS DATOS Y LOS METEMOS EN LA TABLA
		
		try{
			
			LinkedList<String> contenido = readData();
		
			for(int i = 0; i < contenido.size(); i++){
			String datum = contenido.get(i);	
			String[] parte1 = datum.split("="); //separamos el String en 2 partes de "="
			String[] parte2=parte1[0].split(":"); //luego a la parte de izquierda de "=", volvemos a partir en dos,
			//pero esta vez en ":". 
			String nomEnfermedad=parte2[0]; //nos quedamos con la parte de la izquierda de ":", que es el nombre de la enfermedad
			String[]cachitosCodVoc=parte2[1].split(";"); // ahora cogemos la parte derecha de la parte izquierda de "=" , y de esa forma 
			//cortamos todo a cachitos de ";"
			ArrayList<ArrayList<String>> CodVoc = new ArrayList<>(); //hacemos un arrayList de arrayList's
			
			for (String cachitoCodVoc : cachitosCodVoc){ //para todo string (que llamamos "parteCodVoc") de parte 3 (para todos los cachitos que hemos separado) ->
			ArrayList<String> divisionCodVoc = new ArrayList<>(); //-> hacemos  otro array list->
			divisionCodVoc.add(cachitoCodVoc.split("@")[0]); // en el que metemos por separado la parte izquierda del cachito ya partido,
			                                               // que a su vez a vuelto a ser partido por "@", que es el codigo de vocabulario.
			divisionCodVoc.add(cachitoCodVoc.split("@")[1]);  // en el que metemos por separado la parte derecha del cachito ya partido,
            // que a su vez a vuelto a ser partido por "@", que es el  vocabulario en s�.    
			CodVoc.add(divisionCodVoc); //metemos el array list en el array list de arraylist's
			
			
			}
			//Ahora seguimos el mismo principio
			ArrayList<ArrayList<String>> arraysintomas = new ArrayList<>(); //hago un array list de arraylists's 
			String[]cachitosSinCodSe=parte1[1].split(";"); //aqu� los cachitos son de la forma "Sintoma1:codigoSintoma1:semanticTypeSintoma1"
			for (String cachitoSinCodSe : cachitosSinCodSe){ //hacemos un for para todos los cachitos de strings (que llamamos parteDatosSintoma")
				//que hemos hecho al dividir el string resultante de la parte 1 (la parte derecha de "=" ) en ";" 
				ArrayList<String> arraySintoma = new ArrayList<>(); //hacemos otro array List y seguimos el mismo principio que antes
				String[]cachitoSin_Cod_Se=cachitoSinCodSe.split(":");// separamos cada cachito ("parteDatosSintomas") en  "parte5"
				//Aqui los cachitos son la forma " Sintoma1 codigoSintoma1 semanticTypeSintoma1"
				
				for (String cachito: cachitoSin_Cod_Se){ // y para todo dato de parte 5 lo introducimos en el arraylist
					arraySintoma.add(cachito); //quedando que para todo arraylist 
					                         // 0-> es el nombre del sintoma
					                         //1 -> es el codido del sintoma
					                         //2-> es el semanticTypeSintoma
				}
				arraysintomas.add(arraySintoma);
			}
      //Sigo el mismo principio, atrincherarse en el commit
		
			//lo vuelvo hacer as� por limpieza m�s que nada
			
			//Nombre de Enfermedad
			PreparedStatement codigoSQLEnst = conn.prepareStatement("INSERT INTO `disease` VALUES (?) ");
			codigoSQLEnst.setString(1, nomEnfermedad);
			codigoSQLEnst.executeUpdate();
	
			//elementos de CodVoc
			//Cod OJO 
			PreparedStatement codigoSQLCodVst = conn.prepareStatement("INSERT INTO `code` (`cui`) VALUES (?) ");
			codigoSQLCodVst.setString(1,CodVoc.get(0).get(0));
			codigoSQLCodVst.executeUpdate();
			
			//Voc   
			PreparedStatement codigoSQLdVocst = conn.prepareStatement("INSERT INTO `source`(`cui`)  VALUES (?) ");
			codigoSQLdVocst.setString(1,CodVoc.get(0).get(1));
			codigoSQLdVocst.executeUpdate();
			
		     //SINTOMAS
              //nombre sintoma
			PreparedStatement codigoSQLnomSst = conn.prepareStatement("INSERT INTO `symptom` (`cui`) VALUES (?) ");
			codigoSQLnomSst.setString(1,arraysintomas.get(0).get(0));
			codigoSQLnomSst.executeUpdate();
		
			 //codgo sintoma 
			PreparedStatement codigoSQLcodSst = conn.prepareStatement("INSERT INTO `symptom` (`cui`)  VALUES (?) ");
			codigoSQLcodSst.setString(1,arraysintomas.get(0).get(1));
			codigoSQLcodSst.executeUpdate();
			
			 //semantic type sintoma 
			PreparedStatement codigoSQLSTst = conn.prepareStatement("INSERT INTO `semantic_type` (`cui`)  VALUES (?) ");
			codigoSQLSTst.setString(1,arraysintomas.get(0).get(2));
			codigoSQLSTst.executeUpdate();
			conn.commit();
			}
           
	} catch (Exception e) {
			
			e.printStackTrace();
			System.out.println("Fallo en disecion de data o en introducir los datos en tablas");
	}
		

		}

	private void realizarDiagnostico() throws Exception {
		
		//Encunciado //la base de datos [2 puntos]: El programa debe pedir por teclado que s�ntomas quiere
		//tener en cuenta para realizar el diagn�stico. Los s�ntomas deben solicitarse para ser
		//introducidos por teclado, y debe previamente mostrarse la lista de s�ntomas disponibles,
		//para que quien use el programa pueda introducirlos usando sus c�digos identificativos
		//(no a trav�s del nombre del s�ntoma). El sistema debe devolver aquellas enfermedades
		//que tenga asociados todos los s�ntomas que se hayan introducido.
				
		System.out.print("Introduzca los s�ntomas para realizar diagn�stico");
		//mostrar sintomas y sus codigos

		
		PreparedStatement consultoSin =  conn.prepareStatement("SELECT `sympton` FROM  `symptom`");
	    PreparedStatement consultoCo =  conn.prepareStatement("SELECT `code FROM  symptom ");
		
		
		
	
		//while (setSin.next() && setCo.next() ){
			//System.out.print("Sintoma : " + ((ResultSet) consultoSin).getString("sympton") + "Codigo de Sintoma : " + ((ResultSet) consultoCo).getString("disease_sympton"));
		//}
		
		 //meter codigos de sintomas 
		try{
		String sintomas = readString();
	    }catch (Exception e){
			throw new Exception("Error reading line");
		}
		//y con esos sintomas sacar enfermedades                                                        unico ? pues un �nico par�metro
		PreparedStatement consultoEn = (PreparedStatement) conn.prepareStatement("SELECT * FROM  ENFERMEDADES WHERE sintomas = ?" );
		
		//OJO 
		//consultoEn.setString(1,sintomas);
		//lo que hace esto es, DONDE ESTE LAL PRIMERA "?" pones "hola",  y hace la consulta en base a "hola"
		
		
		ResultSet setEn = consultoEn.executeQuery();
		
		//claro con esto especificamos que sintomas son de que cada enfermedad 
				//consultoEn = consultoEn.getString(sintomas);
				
		//consultoDia.close();
		//consultoCo.close();
		//consultoEn.close();
		
		
		
		
		
	}
	//3. Capacidad de listar [1.5 puntos]:

	private void listarSintomasEnfermedad() {
//		a. Los s�ntomas de una enfermedad de la base de datos [0.5 puntos]: Debe mostrar
		//	cuales son las enfermedades que hay en la base de datos, y el usuario debe
		//	introducir el ID que corresponda para seleccionar la enfermedad.
		
		
		
	}

	private void listarEnfermedadesYCodigosAsociados() {
//		b. Listado de enfermedades de la base de datos y sus c�digos asociados [0.5
		//	puntos]: El programa debe mostrar las enfermedades que contiene la base de
		//	datos, y para cada enfermedad que c�digos tiene (y tipo de c�digo).
		
		
		
		
	}

	private void listarSintomasYTiposSemanticos() {
		//	c. Listado de s�ntomas disponible en la base de datos y sus tipos sem�nticos
		//	asociados [0.5 puntos]: El programa debe mostrar los s�ntomas que contiene la
		//	base de datos y sus tipos sem�nticos asociados.
		
		
		
		
		
		
	}

	private void mostrarEstadisticasBD() {
		// ENUNCIADO
		
		//4. Estad�sticas de la base de datos y su contenido. La funcionalidad de estad�sticas debe
		//proporcionar [2 puntos]:
		
		//a. N�mero de enfermedades [0.5 puntos]: Un conteo del n�mero total de
		//enfermedades que hay en la base de datos.
		
		
	   //	PreparedStament conteoEn= conn. prepareStatement
		
		///b. N�mero de s�ntomas [0.5 puntos]: Un conteo del n�mero total de s�ntomas que
		//hay en la base de datos.
		
		//c. Enfermedad con m�s s�ntomas, con menos s�ntomas y n�mero medio de
		//s�ntomas [0.5 puntos]: Debe indicar cuales son las enfermedades con m�s y
		//menos s�ntomas y cu�l es el n�mero medio de s�ntomas asociados a las
		//enfermedades.
		
		
		//d. Tipos de semantic types en los s�ntomas y distribuci�n de cada semantic type
		//(cuantos s�ntomas hay de cada semantic type) [0.5 puntos]: Se debe indicar
		//cuales son los semantic types que hay en la base de datos, y cuantos s�ntomas
		//hay de cada semantic type.
		
		
		
		
		
		
		
	}

	/**
	 * M�todo para leer n�meros enteros de teclado.
	 * 
	 * @return Devuelve el n�mero le�do.
	 * @throws Exception
	 *             Puede lanzar excepci�n.
	 */
	private int readInt() throws Exception {
		try {
			System.out.print("> ");
			return Integer.parseInt(new BufferedReader(new InputStreamReader(System.in)).readLine());
		} catch (Exception e) {
			throw new Exception("Not number");
		}
	}

	/**
	 * M�todo para leer cadenas de teclado.
	 * 
	 * @return Devuelve la cadena le�da.
	 * @throws Exception
	 *             Puede lanzar excepci�n.
	 */
	private String readString() throws Exception {
		try {
			System.out.print("> ");
			return new BufferedReader(new InputStreamReader(System.in)).readLine();
		} catch (Exception e) {
			throw new Exception("Error reading line");
		}
	}

	/**
	 * M�todo para leer el fichero que contiene los datos.
	 * 
	 * @return Devuelve una lista de String con el contenido.
	 * @throws Exception
	 *             Puede lanzar excepci�n.
	 */
	private  LinkedList<String> readData() throws Exception {
		LinkedList<String> data = new LinkedList<String>();
		BufferedReader bL = new BufferedReader(new FileReader(DATAFILE));
		while (bL.ready()) {
			data.add(bL.readLine());
		}
		bL.close();
		return data;
	}
	

	public static void main(String args[]) {
		new Diagnostico().showMenu();
		
	}
}



