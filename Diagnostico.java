
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
	
	                System.err.println("Opci�n introducida no v�lida!");
	                
	                //Esto hay que quitarlo antes de la entrega
	                e.printStackTrace();
	
	            }
	
	        } while (option != 7);
	
	        exit();
	
	    }
	
	
	    private void exit() {
	
	        System.out.println("Saliendo.. �hasta otra!");
	
	        System.exit(0);
	        try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
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
	
	        String db = "diagnostico";
	
	        String user = "bddx";
	
	        String pass = "bddx_pwd";
	
	        String url = "jdbc:mysql://" + serverAddress + "/";
	
	        
	
	             try {
	
	                conn =  DriverManager.getConnection(url, user, pass);
	
	            } catch (SQLException e1) {
	
	                e1.printStackTrace();
	
	            }
	
	    
	
	        
	
	        System.out.println("Conectado a la base de datos!");
	
	        
	
	          
	
	        
	
	    }
	
	
	    private void crearBD() throws Exception {

	    	//1 String Code= "codigo en SQL"

	    	        //2 PreparedStatement nombre = conn.prepareStament(Code);

	    	        //3 nombre.executeQuery

	    	        //consultoEn.setString(1,"hola");

	    	        //lo que hace esto es, DONDE ESTE LAL PRIMERA "?" pones "hola",  y hace la consulta en base a "hola"

	    	        //y as� para todo
	    	        //conectar();
	    	        //PreparedStatement stmt = conn.prepareStatement("DROP DATABASE diagnostico;");
	    	        //stmt.execute();
	    	        //conn.close();        
	    	        //PRIMERO CREO LA BASE DE DATOS Y LAS TABLAs
	    	    

	    	        conectar();

	    	        
	    	         String SQLcodigoA = "DROP DATABASE IF  EXISTS diagnostico;";
	    	         String SQLcodigoDB = "CREATE DATABASE IF NOT EXISTS diagnostico;";
	    	         
	    	         System.out.println("Base de datos creada con �xtio");
	    	         // vamos a usarla ahora por defecto
	    	         String SQLcodigoUSE = "USE diagnostico;";

	    	        

	    	         PreparedStatement SQLcodigo =  conn.prepareStatement(SQLcodigoA);
	    	         
	    	        
	    	         
	    	         PreparedStatement SQLcodigoDBst = conn.prepareStatement(SQLcodigoDB);
	    	         
	    	         PreparedStatement SQLcodigoDBUSE = conn.prepareStatement(SQLcodigoUSE);
	    	         //FIRST THINGS FIRST
	    	         SQLcodigo.execute();
	    	         
	    	         SQLcodigoDBst.execute();
	    	         
	    	         SQLcodigoDBUSE.execute();

	    		        conn.setAutoCommit(false);


	    		        

	    		         
	    		         

	    		         String SQLcodigoUSE1 = "USE  diagnostico;";

	    		         

	    		         String SQLcodigoEN  = "CREATE TABLE  `disease` (`disease_id` INT AUTO_INCREMENT NOT NULL ,`name` VARCHAR(255), PRIMARY KEY(`disease_id`))ENGINE=Inno08;";

	    		    

	    		         String SQLcodigoS  = "CREATE TABLE `symptom` (`cui` VARCHAR(45) NOT NULL,`name` VARCHAR(255), PRIMARY KEY(`cui`))ENGINE=Inno08;";

	    		        

	    		         String SQLcodigoC  = "CREATE TABLE `code` (`source_id` INT NOT NULL,`code` VARCHAR (255) NOT NULL,PRIMARY KEY(`code`), FOREIGN KEY(`source_id`) REFERENCES `source` (`source_id`))ENGINE=Inno08;";

	    		        

	    		         String SQLcodigoD  = "CREATE TABLE `disease_has_code` (`disease_id` INT NOT NULL, `code` VARCHAR (255) NOT NULL,`source_id` INT NOT NULL,FOREIGN KEY (`disease_id`) REFERENCES `disease` (`disease_id`),FOREIGN KEY(`code`) REFERENCES `code` (`code`),FOREIGN KEY (`source_id`) REFERENCES code (`source_id`))ENGINE=Inno08;";

	    		        

	    		         String SQLcodigoSE = "CREATE TABLE  `semantic_type` (`semantic_type_id` INT AUTO_INCREMENT  NOT NULL,`cui` VARCHAR(45) UNIQUE NOT NULL,PRIMARY KEY(`semantic_type_id` ))ENGINE=Inno08;";

	    		        

	    		         String SQLcodigoSS  = "CREATE TABLE  `symptom_semantic_type` (`semantic_type_id` INT NOT NULL,`cui` VARCHAR(25) NOT NULL,FOREIGN KEY (`cui`) REFERENCES `symptom` (`cui`),FOREIGN KEY  (`semantic_type_id` ) REFERENCES  `semantic_type` (`semantic_type_id`))ENGINE=Inno08;";

	    		        

	    		         String SQLcodigoSO  = "CREATE TABLE  `source` (`source_id` INT AUTO_INCREMENT  NOT NULL,`name` VARCHAR (255) UNIQUE NOT NULL,PRIMARY KEY (`source_id`))ENGINE=Inno08;";

	    		        

	    		         String SQLcodigoDS  = "CREATE TABLE `disease_symptom` (`disease_id` INT AUTO_INCREMENT NOT NULL,`cui` VARCHAR(25) NOT NULL,FOREIGN KEY (`disease_id`) REFERENCES `disease` (`disease_id`),FOREIGN KEY (`cui`) REFERENCES symptom (`cui`))ENGINE=Inno08;";


	    		         //lo pongo de esta manera pues parece que as� est� m�s limpio

	    		        

	    		          
	    		            PreparedStatement SQLcodigoENst = conn.prepareStatement(SQLcodigoEN);

	    		            PreparedStatement SQLcodigoUSEst = conn.prepareStatement(SQLcodigoUSE1);

	    		            PreparedStatement SQLcodigoSst =  conn.prepareStatement(SQLcodigoS );

	    		            PreparedStatement SQLcodigoCst  = conn.prepareStatement(SQLcodigoC  );

	    		            PreparedStatement SQLcodigoDst  = conn.prepareStatement(SQLcodigoD  );

	    		            PreparedStatement SQLcodigoSEst = conn.prepareStatement(SQLcodigoSE );

	    		            PreparedStatement SQLcodigoSSst = conn.prepareStatement(SQLcodigoSS );

	    		            PreparedStatement SQLcodigoSOst = conn.prepareStatement(SQLcodigoSO );

	    		            PreparedStatement SQLcodigoDSst = conn.prepareStatement(SQLcodigoDS  );

	    		            

	    		            //y claro est� ponems el commit por si alguno de los procesos falla

	    		            



	    		        

	    		            SQLcodigoUSEst.executeUpdate();

	    		        

	    		            SQLcodigoENst.executeUpdate();

	    		        

	    		             SQLcodigoSst.executeUpdate();

	    		        

	    		             SQLcodigoCst.executeUpdate();

	    		        

	    		             SQLcodigoDst.executeUpdate();

	    		        

	    		             SQLcodigoSEst.executeUpdate();

	    		        

	    		             SQLcodigoSSst.executeUpdate();

	    		        

	    		             SQLcodigoSOst.executeUpdate();

	    		        

	    		             SQLcodigoDSst.executeUpdate();

	    		        

	    		        
	    	         
	    	     

	
	    
	
	    }
	    private void realizarDiagnostico() throws Exception {


	        //RECUERDA LA RECETA

	        

	        //1 String Code= "codigo en SQL"

	        //2 PreparedStatement nombre = conn.prepareStament(Code);

	        //3 nombre.executeQuery

	        //consultoEn.setString(1,"hola");

	        //lo que hace esto es, DONDE ESTE LAL PRIMERA "?" pones "hola",  y hace la consulta en base a "hola"

	        //y as� para todo


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
           //INSERT INTO `diagnostico`.`disease` (`disease_id`, `name`) VALUES ('2', '1');
	            PreparedStatement codigoSQLEnst = conn.prepareStatement("INSERT INTO `diagnostico`.`disease` VALUES (?) ");
	            System.out.println(nomEnfermedad);

            codigoSQLEnst.setString(1, nomEnfermedad);

            codigoSQLEnst.executeUpdate();

    

            //elementos de CodVoc

            //Cod OJO 

            PreparedStatement codigoSQLCodVst = conn.prepareStatement("INSERT INTO `diagnostico`.`code` (`cui`) VALUES (?) ");

            codigoSQLCodVst.setString(1,CodVoc.get(0).get(0));

            codigoSQLCodVst.executeUpdate();

            

            //Voc   

            PreparedStatement codigoSQLdVocst = conn.prepareStatement("INSERT INTO `diagnostico`.`source`(`cui`)  VALUES (?) ");

            codigoSQLdVocst.setString(1,CodVoc.get(0).get(1));

            codigoSQLdVocst.executeUpdate();

            

             //SINTOMAS

              //nombre sintoma

            PreparedStatement codigoSQLnomSst = conn.prepareStatement("INSERT INTO `diagnostico`.`symptom` (`cui`) VALUES (?) ");

            codigoSQLnomSst.setString(1,arraysintomas.get(0).get(0));

            codigoSQLnomSst.executeUpdate();

        

             //codgo sintoma 

            PreparedStatement codigoSQLcodSst = conn.prepareStatement("INSERT INTO `diagnostico`.`symptom` (`cui`)  VALUES (?) ");

            codigoSQLcodSst.setString(1,arraysintomas.get(0).get(1));

            codigoSQLcodSst.executeUpdate();

            

             //semantic type sintoma 

            PreparedStatement codigoSQLSTst = conn.prepareStatement("INSERT INTO `diagnostico`.`semantic_type` (`cui`)  VALUES (?) ");

            codigoSQLSTst.setString(1,arraysintomas.get(0).get(2));

            codigoSQLSTst.executeUpdate();

            conn.commit();

            }

           

    } catch (Exception e) {

            

            e.printStackTrace();

            System.out.println("Fallo en disecion de data o en introducir los datos en tablas");

    }     
      //Encunciado //la base de datos [2 puntos]: El programa debe pedir por teclado que s�ntomas quiere

        //tener en cuenta para realizar el diagn�stico. Los s�ntomas deben solicitarse para ser

        //introducidos por teclado, y debe previamente mostrarse la lista de s�ntomas disponibles,

        //para que quien use el programa pueda introducirlos usando sus c�digos identificativos

        //(no a trav�s del nombre del s�ntoma). El sistema debe devolver aquellas enfermedades

        //que tenga asociados todos los s�ntomas que se hayan introducido.

                

        System.out.print("Introduzca los s�ntomas para realizar diagn�stico");

        //mostrar sintomas y sus codigos

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


	
	
	      private void listarSintomasEnfermedad() throws SQLException {
	    	 
	    	  //ESTO HAY QUE QUITARLO ANTES DE ENTREGAR LA PR�CTICA, ES PARA AHORRAR TIEMPO Y NO ESTAR CREANDO LA BASE DE DATOS TODO EL RATO
	    	  try {
				crearBD();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	  /*
	    	   * INSERT INTO `diagnostico`.`disease` (`disease_id`, `name`) VALUES ('363', 'Alluha hackbar');
	    	   * INSERT INTO `diagnostico`.`disease` (`disease_id`, `name`) VALUES ('364', 'Cirrosis');
	    	   */
	    	  //Estos son para las pruebas del ejercicio tres
	    	  String SQLlistarenfermedad =  "INSERT INTO `diagnostico`.`disease` (`disease_id`, `name`) VALUES ('363', 'Alluha hackbar');";
	    	  String SQLlistarenfermedad1 = "INSERT INTO `diagnostico`.`disease` (`disease_id`, `name`) VALUES ('362', 'Anorexia nervosa');";
	    	  String SQLlistarenfermedad2 = "INSERT INTO `diagnostico`.`disease` (`disease_id`, `name`) VALUES ('364', 'funciona!!!');";
	    	  
	    	  String SQLsintomas =  "INSERT INTO `diagnostico`.`disease_symptom` (`disease_id`, `cui`) VALUES ('362', 'Se�or roca');";
	    	  String SQLsintomas1 = "INSERT INTO `diagnostico`.`disease_symptom` (`disease_id`, `cui`) VALUES ('363', 'parece que funciona');";
	    	  String SQLsintomas2 = "INSERT INTO `diagnostico`.`disease_symptom` (`disease_id`, `cui`) VALUES ('364', 'Canta canciones sat�nicas');";
	    	  PreparedStatement SQLListarEnfermedades =  conn.prepareStatement(SQLlistarenfermedad);
	    	  PreparedStatement SQLListarEnfermedades1 = conn.prepareStatement(SQLlistarenfermedad1);
	    	  PreparedStatement SQLListarEnfermedades2 = conn.prepareStatement(SQLlistarenfermedad2);
	    	  
	    	  PreparedStatement SQLSINTOMAS = conn.prepareStatement(SQLsintomas);
	    	  PreparedStatement SQLSINTOMAS1 = conn.prepareStatement(SQLsintomas1);
	    	  PreparedStatement SQLSINTOMAS2 = conn.prepareStatement(SQLsintomas2);
	    	  
	    	  SQLListarEnfermedades.executeUpdate();
	    	  SQLListarEnfermedades1.executeUpdate();
	    	  SQLListarEnfermedades2.executeUpdate();
	    	  
	    	  SQLSINTOMAS.executeUpdate();
	    	  SQLSINTOMAS1.executeUpdate();
	    	  SQLSINTOMAS2.executeUpdate();


	    	  
	//       �QUITAR ANTES DE LA ENTREGA DE LA PR�TICA!(lo de la receta)
	    	  /* RECUERDA LA RECETA PARA HACER SELECTS
	    	   * ESTE PASO DE AQUI LO QUE HACE ES HACER LA QUERY
	    	   * Statement stmt = conn.createStatement();
	    	   * ResultSet rs = stmt.executeQuery("SELECT Lname FROM Customers WHERE Snum = 2001");
	    	   * AHORA HAY QUE LEERLA
	    	   * while (rs.next()) {
  					String lastName = rs.getString("Lname");
  					System.out.println(lastName + "\n");
							}
	    	   * con esto la vamos leyendo una a una la informacion contenida en la tabla
	    	   */
	    	
	    	String SQLEnfermedades = "SELECT disease_id, name FROM disease; ";
	    	PreparedStatement SQLEnfermedades1 = conn.prepareStatement(SQLEnfermedades);
	    	
	    	/*
	    	 * Este linkedList lo usaremos para saber si el c�digo que ha introducido es v�lido o 
	    	 * no est� presente en nuestra base de datos
	    	 * 
	    	 */
	    	
	    	LinkedList<Integer> enfermedades = new LinkedList<Integer>();
	    	boolean presenteBD = false;

	    	ResultSet resu =SQLEnfermedades1.executeQuery();
	    	while (resu.next()) {
	    		
	    		int diseaseID= resu.getInt("disease_id");
	    		
	    		//Vamos a introducirlos en el linkedList para posteriormente hacer la comprobaci�n 
	    		enfermedades.add(diseaseID);
	    		
	    		
	    		String name = resu.getString("name");
					System.out.print("disease id: " + diseaseID);
					System.out.println(", name : " + name);

						}
	    	resu.close();
	    		     
	    	System.out.println("Escriba ahora su c�digo");
	    	try {
	    		
	    		//Esta ser�a la parte de seleccionar el id
				int  n =readInt();
				//vamos a ver si est� presente en la BD
		    	for(int i=0;i<enfermedades.size();i++){
		    		if(n==enfermedades.get(i)){
		    			presenteBD= true;
		    		}
		    	}
		    	
				System.out.println("Este es tu c�digo: "+n);
				if(presenteBD==true){
				PreparedStatement rs= conn.prepareStatement("SELECT disease_id, cui FROM disease_symptom WHERE disease_id ="+n);
				ResultSet resultLectura =rs.executeQuery();
				while(resultLectura.next()){
					int diseaseID= resultLectura.getInt("disease_id");
		    		String sintomas = resultLectura.getString("cui");
						System.out.print("disease id: " + diseaseID);
						System.out.println(", sintomas : " + sintomas);
				}
				resultLectura.close();
				}
				else{
					System.out.println("El c�digo que Usted ha introducido no se haya presente en nuestra Base de Datos, intente con otro c�digo");
				}
		    	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
	    	
	    	//Para que no este todo apelmazado y darle un poco m�s de visibilidad
	    	System.out.println("");
	    	
	    			
	    }
	
	
	    private void listarEnfermedadesYCodigosAsociados() throws SQLException {
	    	try {
				crearBD();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	  String SQLlistarenfermedad =   "INSERT INTO `diagnostico`.`disease` (`disease_id`, `name`) VALUES ('363', 'Alluha hackbar');";
	    	  String SQLlistarenfermedad1 = "INSERT INTO `diagnostico`.`disease` (`disease_id`, `name`) VALUES ('362', 'Anorexia nervosa');";
	    	  String SQLlistarenfermedad2 = "INSERT INTO `diagnostico`.`disease` (`disease_id`, `name`) VALUES ('364', 'funciona!!!');";
	    	  

	    	 
	    	  String SQLsintomas =  "INSERT INTO `diagnostico`.`disease_has_code` (`disease_id`, `code`, `source_id`) VALUES ('362', 'JJUVKL', '23');";
	    	  String SQLsintomas1 = "INSERT INTO `diagnostico`.`disease_has_code` (`disease_id`, `code`, `source_id`) VALUES ('363', 'Uvogin', '32');";
	    	  String SQLsintomas2 = "INSERT INTO `diagnostico`.`disease_has_code` (`disease_id`, `code`, `source_id`) VALUES ('364', 'Kurapika', '33')";

	    	  PreparedStatement SQLListarEnfermedades =  conn.prepareStatement(SQLlistarenfermedad);
	    	  PreparedStatement SQLListarEnfermedades1 = conn.prepareStatement(SQLlistarenfermedad1);
	    	  PreparedStatement SQLListarEnfermedades2 = conn.prepareStatement(SQLlistarenfermedad2);
	    	  
	    	  PreparedStatement SQLSINTOMAS = conn.prepareStatement(SQLsintomas);
	    	  PreparedStatement SQLSINTOMAS1 = conn.prepareStatement(SQLsintomas1);
	    	  PreparedStatement SQLSINTOMAS2 = conn.prepareStatement(SQLsintomas2);
	    	  
	    	  SQLListarEnfermedades.executeUpdate();
	    	  SQLListarEnfermedades1.executeUpdate();
	    	  SQLListarEnfermedades2.executeUpdate();
	    	  
	    	  SQLSINTOMAS.executeUpdate();
	    	  SQLSINTOMAS1.executeUpdate();
	    	  SQLSINTOMAS2.executeUpdate();
	    	 
	        //  b. Listado de enfermedades de la base de datos y sus c�digos asociados [0.5
	
	        //  puntos]: El programa debe mostrar las enfermedades que contiene la base de
	
	        //  datos, y para cada enfermedad que c�digos tiene (y tipo de c�digo).
	    	  //mostramos las enfermedades por pantalla
	    	  
	    	try {
	    	LinkedList<String> diseasesNames = new LinkedList<String>();
	    	LinkedList<Integer> diseasesID = new LinkedList<Integer>();
	    	LinkedList<String> diseasesNames1 = new LinkedList<String>();
	    	String SQLEnfermedades =    "SELECT name, disease_id FROM disease; ";
	    	String SQLEnfermedadesCOD = "SELECT code, source_id FROM disease_has_code WHERE disease_id= "; 
	    	/*
	    	 * Aqu� introduciremos un numero, que va a ser el que hemos almacenado previamente 
	    	 * en el ArrayList de tipo enteros (Este array contiene de cada enfermedad su c�digo, en funci�n)
	    	 * del orden en el que est�n en la base de datos 
	    	*/
	    	PreparedStatement SQLEnfermedades1 = conn.prepareStatement(SQLEnfermedades);
	    	

	    	ResultSet resu =SQLEnfermedades1.executeQuery();
	    	while (resu.next()) {
	    			
	    			
	    			diseasesID.add(resu.getInt("disease_id"));//Este es el de los numeros
	    			diseasesNames.add(resu.getString("name"));
	  
					
	    			}
		
	    	resu.close();
	    	for(int i=0;i<diseasesID.size();i++){
	    		PreparedStatement SQLEnfermedadesCod = conn.prepareStatement(SQLEnfermedadesCOD+diseasesID.get(i));
	    		ResultSet rscodes = SQLEnfermedadesCod.executeQuery();
	    		
	    		while(rscodes.next()){
	    			diseasesNames1.add(diseasesNames.get(i)+"; este es su c�digo: "+rscodes.getString("code")+";  y este es su tipo de c�digo: "
	    					+rscodes.getInt("source_id"));
	    		}
	    		
	    	}
	    	/*
	    	 * AHORA VAMOS A IMPRIMIR UNA A UNA TODAS LAS ENFERMEDADES CON SUS RESPECTIVOS C�DIGOS 
	    	 * Y SUS RESPECTIVOS CAMPOS
	    	 */
	    	for(int i =0;i<diseasesNames1.size();i++){
	    	System.out.print("Este es el nombre de la enfermedad: ");
	    	System.out.println(diseasesNames1.get(i));
	    	}
	    
	    }catch(Exception e){
	    	e.printStackTrace();}
	    	
	    	//Para que no este todo apelmazado y darle un poco m�s de visibilidad
	    	System.out.println("");
	    }
	    
	    
	
	    private void listarSintomasYTiposSemanticos() throws SQLException {
	    	/*
	    	 * RECUERDA QUE TODO ESTO EST� REALIZADO CON UNOS DATOS QUE HEMOS 
	    	 * IDO INTRODUCIENDO NOSOTROS, Y QUE HABR� QUE QUITAR TODAS AQUELLAS PARTES
	    	 * HAY QUE BORRAR DE AQU�:
	    	 *  1. LA CREACION DE LA BASE DE DATOS
	    	 *  2. TODOS LOS INSERTS QUE SE HAYAN HECHO DESDE AQUI
	    	 *  3. HACER TODO ESTO CUANDO FUNCIONEN CORRECTAMENTE LOS INSERTS 
	    	 */
	       	try {
				crearBD();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	
	    	String SQLsymptom1 = "INSERT INTO `diagnostico`.`symptom` (`cui`, `name`) VALUES ('11', 'anorexia');";
	    	String SQLsymptom2 = "INSERT INTO `diagnostico`.`symptom` (`cui`, `name`) VALUES ('12', 'risa incontralada');";
	    	String SQLsymptom3 = "INSERT INTO `diagnostico`.`symptom` (`cui`, `name`) VALUES ('13', 'fuma');";
	    	PreparedStatement SQLSymptom1 = conn.prepareStatement(SQLsymptom1);
	    	PreparedStatement SQLSymptom2 = conn.prepareStatement(SQLsymptom2);
	    	PreparedStatement SQLSymptom3 = conn.prepareStatement(SQLsymptom3);
	    	
	    	SQLSymptom1.executeUpdate();
	    	SQLSymptom2.executeUpdate();
	    	SQLSymptom3.executeUpdate();
	    	
	    	
	    	
	    	
	    	String SQLsymptomsemantic1 = "INSERT INTO `diagnostico`.`symptom_semantic_type` (`semantic_type_id`, `cui`) VALUES ('44', '11');";
	    	String SQLsymptomsemantic2 = "INSERT INTO `diagnostico`.`symptom_semantic_type` (`semantic_type_id`, `cui`) VALUES ('55', '12');";
	    	String SQLsymptomsemantic3 = "INSERT INTO `diagnostico`.`symptom_semantic_type` (`semantic_type_id`, `cui`) VALUES ('66', '13');";
	    	
	    	
	    	PreparedStatement symptomsSemantic1 = conn.prepareStatement(SQLsymptomsemantic1);
	    	PreparedStatement symptomsSemantic2 = conn.prepareStatement(SQLsymptomsemantic2);
	    	PreparedStatement symptomsSemantic3 = conn.prepareStatement(SQLsymptomsemantic3);
	    	
	    	
	    	
	    	symptomsSemantic1.executeUpdate();
	    	symptomsSemantic2.executeUpdate();
	    	symptomsSemantic3.executeUpdate();
	    	
	    	//Primero cogemos los sintomas 
	    	LinkedList<String> symptoms = new LinkedList<String>();
	    	LinkedList<Integer> symptomsCUI = new LinkedList<Integer>();
	    	LinkedList<String> symptoms1 = new LinkedList<String>();

	    	
	    	String SQLSYMPTOM = "SELECT cui, name FROM symptom";
	    	PreparedStatement symptomSQL = conn.prepareStatement(SQLSYMPTOM);
	    	ResultSet resultSymptom =symptomSQL.executeQuery();
	    	
	    	/*
	    	 * Hacemos una query para obtener los nombres de cada s�ntoma y obtener sus c�digos asociados
	    	 * una vez hemos obtenido estos, podremos realizar una consulta con estos campos sem�nticos 
	    	 * y as� obtener de manera conjunta el nobre del s�ntoma y sus campos sem�nticos
	    	 */
	    	while(resultSymptom.next()){
	    		symptoms.add(resultSymptom.getString("name"));
	    		symptomsCUI.add(resultSymptom.getInt("cui"));
	    		
	    		
	    	}
	    	

	    	//Ahora los iremos seleccionando de uno en uno mediante sus sintomas
	    	
	    	for(int i=0;i<symptomsCUI.size();i++){
	    		String SQLSymptoms =  "SELECT semantic_type_id FROM symptom_semantic_type WHERE cui=";
	    		
	    		SQLSymptoms=SQLSymptoms+symptomsCUI.get(i);
	    		
	        PreparedStatement SQLsymptoms = conn.prepareStatement(SQLSymptoms);
	        ResultSet rsSymptoms = SQLsymptoms.executeQuery();
	        
	        while(rsSymptoms.next()){
	        	
	        	//
	        	
	        	symptoms1.add("Esta es la enfermedad: "+symptoms.get(i)+" y este es su campo sem�ntico: "+rsSymptoms.getInt("semantic_type_id"));
	          }
	        }
	    	
	    	//Ahora lo imprimimos todo por pantalla
	    	for(int i =0;i<symptoms1.size();i++)
	    	System.out.println(symptoms1.get(i));
	    	
	    	
	    	//Para que no este todo apelmazado y darle un poco m�s de visibilidad
	    	System.out.println("");
	
	    }
	
	
	    private void mostrarEstadisticasBD() {
	     
	    	
	        //Esto hay que eliminarlo antes de la prueba
	    	
	    	//Esto ya no
	    	try{
	    		String numberDiseases = "SELECT COUNT('name') FROM disease";		    	
	    		PreparedStatement Numdiseases = conn.prepareStatement(numberDiseases);
	    		ResultSet resultNum =Numdiseases.executeQuery();
	            resultNum.next();          
	            System.out.println("Esta es la cantidad de enfermedades que hay ahora mismo registradas en la base de datos:" + resultNum.getInt(1));
	            
	          //Para que no este todo apelmazado y darle un poco m�s de visibilidad
		    	System.out.println("");
	            
	          }
	          catch(SQLException s){
	            System.out.println(s);
	          }
	
	        //a. N�mero de enfermedades [0.5 puntos]: Un conteo del n�mero total de
	
	        //enfermedades que hay en la base de datos.
	    	
	    	
	    	
	    }    	
	    	
	
	        
	
	        
	
	       //    PreparedStament conteoEn= conn. prepareStatement
	
	        
	
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