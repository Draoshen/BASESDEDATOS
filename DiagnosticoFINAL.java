import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;






public class Diagnostico1{



    private final String DATAFILE = "data/disease_data.data";

    private Connection conn;

    

    

    void showMenu() {


        int option = -1;

        do {

            System.out.println("Bienvenido a sistema de diagnóstico\n");

            System.out.println("Selecciona una opción:\n");

            System.out.println("\t1. Creación de base de datos y carga de datos.");

            System.out.println("\t2. Realizar diagnóstico.");

            System.out.println("\t3. Listar síntomas de una enfermedad.");

            System.out.println("\t4. Listar enfermedades y sus códigos asociados.");

            System.out.println("\t5. Listar síntomas existentes en la BD y su tipo semántico.");

            System.out.println("\t6. Mostrar estadísticas de la base de datos.");

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

                //System.err.println("Opción introducida no válida!");

                e.printStackTrace();

            }

        } while (option != 7);

        exit();

    }


    private void exit() {

        System.out.println("Saliendo.. ¡hasta otra!");

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

    

        //Creación de la base de datos en base al esquema E-R y carga de los datos del fichero

        //.data (ambos proporcionados). [3 puntos]: Dado el fichero de datos (ver sección 6 para

        //    más información), se debe cargar su contenido usando código Java, procesarlo y en base

        //    a la estructura del E-R y los datos contenidos, insertar la información en la base de

        //datos. Debe tenerse en cuenta el diagrama E-R para entender como son los datos y

        //    como deben por lo tanto almacenarse. Obligatoriamente, debe ejecutarse la creación y

        //    carga de todos los datos como si fuera una única transacción, de tal forma que cualquier

        //    fallo intermedio de lugar a deshacer por completo los cambios anteriores.

        

        

        

        

        

        conectar();

        

        

        conn.setAutoCommit(false);

      //Para poder ejecutar más de una vez

        PreparedStatement stmt = conn.prepareStatement("DROP DATABASE IF EXISTS `diagnostico`;");

        stmt.executeUpdate();

    

         String SQLcodigoDB = "CREATE SCHEMA IF NOT EXISTS `diagnostico`;";

         PreparedStatement SQLcodigoDBst = conn.prepareStatement(SQLcodigoDB );

        SQLcodigoDBst.executeUpdate();

     

     //Y COMO DOPREO LA BASE DE DATOS NO HACE FALTA VOLVER  PONER CREATE TABLE "IF NOT EXISTS"

         

        String SQLcodigoUSE = "USE  `diagnostico`;"; //PARA NO ESTAR PONIENDO  `diagnostico`.`nombre_table` todo el rato

        PreparedStatement SQLcodigoUSEst = conn.prepareStatement(SQLcodigoUSE);

         SQLcodigoUSEst.executeUpdate();

         

         

         String SQLcodigoSO  = "CREATE TABLE `source` (`source_id` VARCHAR(25) NOT NULL ,`name` VARCHAR(255) NOT NULL, PRIMARY KEY (`source_id`)) ENGINE = InnoDB;";

         PreparedStatement SQLcodigoSOst = conn.prepareStatement(SQLcodigoSO);

          SQLcodigoSOst.executeUpdate();

         

         String SQLcodigoEN  =  "CREATE TABLE  `disease` (`disease_id` INT(11)  NOT NULL,`name` VARCHAR(255) NOT NULL,PRIMARY KEY (`disease_id`))ENGINE = InnoDB;";

         PreparedStatement SQLcodigoENst = conn.prepareStatement(SQLcodigoEN);

         SQLcodigoENst.executeUpdate();

         

         String SQLcodigoS  = "CREATE TABLE  `symptom` (`cui` VARCHAR(25) NOT NULL,`name` VARCHAR(255) NOT NULL,PRIMARY KEY (`cui`))ENGINE = InnoDB;";

         PreparedStatement SQLcodigoSst =  conn.prepareStatement(SQLcodigoS);                                

         SQLcodigoSst.executeUpdate();

        

           String SQLcodigoST  =  "CREATE TABLE `semantic_type` (`semantic_type_id` INT(11) NOT NULL,`cui` VARCHAR(45) NOT NULL,PRIMARY KEY (`semantic_type_id`))ENGINE = InnoDB;";

            PreparedStatement SQLcodigoSTst = conn.prepareStatement(SQLcodigoST);

             SQLcodigoSTst.executeUpdate();

             

             

       String SQLcodigoSS  = "CREATE TABLE  `symptom_semantic_type` (`cui` VARCHAR(25) NOT NULL, `semantic_type_id` INT(11)  NOT NULL,PRIMARY KEY (`cui`, `semantic_type_id`), CONSTRAINT `symptom_semantic_type_ibfk_1` FOREIGN KEY (`cui`) REFERENCES `symptom` (`cui`) ON DELETE NO ACTION ON UPDATE NO ACTION, CONSTRAINT `symptom_semantic_type_frtk_2` FOREIGN KEY (`semantic_type_id`) REFERENCES `semantic_type` (`semantic_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB;";

      PreparedStatement SQLcodigoSSst = conn.prepareStatement(SQLcodigoSS);

         SQLcodigoSSst.executeUpdate();

         

    

     

         String SQLcodigoC  = "CREATE TABLE  `code` (`code` VARCHAR(255) NOT NULL, `source_id` VARCHAR(25) NOT NULL ,PRIMARY KEY (`code`,`source_id`), FOREIGN KEY (`source_id`) REFERENCES `source` (`source_id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB;";

          PreparedStatement SQLcodigoCst  = conn.prepareStatement(SQLcodigoC);

         SQLcodigoCst.executeUpdate();

     

        

         

           String SQLcodigoSE = "CREATE TABLE  `disease_symptom` (`disease_id` INT NOT NULL, `cui` VARCHAR(25) NOT NULL ,PRIMARY KEY (`disease_id`, `cui`),CONSTRAINT `iu_disease_id`FOREIGN KEY (`disease_id`) REFERENCES `disease` (`disease_id`) ON DELETE NO ACTION ON UPDATE NO ACTION, CONSTRAINT `fk_cui` FOREIGN KEY (`cui`) REFERENCES `symptom` (`cui`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB;";

              PreparedStatement SQLcodigoSEst = conn.prepareStatement(SQLcodigoSE);

          SQLcodigoSEst.executeUpdate();

         


           String SQLcodigoD  = "CREATE TABLE  `disease_has_code` (`disease_id` INT(11) NOT NULL ,`code` VARCHAR(255) NOT NULL,`source_id` VARCHAR(45) NOT NULL, PRIMARY KEY (`disease_id`, `code`, `source_id`), CONSTRAINT `iu_disease_id2`FOREIGN KEY (`disease_id`) REFERENCES `disease` (`disease_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,CONSTRAINT `fk_code2` FOREIGN KEY (`code`) REFERENCES `code` (`code`)ON DELETE NO ACTION ON UPDATE NO ACTION,CONSTRAINT `op_source_id2` FOREIGN KEY (`source_id`) REFERENCES `code` (`source_id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB;";

           PreparedStatement SQLcodigoDst  = conn.prepareStatement(SQLcodigoD);

            SQLcodigoDst.executeUpdate();

            

            conn.commit();

            conn.setAutoCommit(true);

        

    

        

        try{

            

            //Creamos las listas cada vez que iniciamos para siempre tenerlas vacías

                   LinkedList<Sintoma> listaSintomasGLOBAL = new LinkedList<Sintoma>();

                 LinkedList<String> Vocs = new LinkedList<String>();

                 LinkedList<String> listaSemantic = new LinkedList<String>();

                 LinkedList<Codigo> listaCodes = new LinkedList<Codigo>();

                 LinkedList<Enfermedad> Enfermedades = new LinkedList<Enfermedad>();

                 LinkedList<String> listaCuis = new LinkedList<String>();

            

           LinkedList<String> contenido = readData();

    

            

           for (int i = 0; i < contenido.size(); i++) {

               

               

               LinkedList<Sintoma> sintomasLOCAL = new LinkedList<Sintoma>();

               

                 String datum = contenido.get(i);

                String parte1[] = datum.split("=");

                String parte1Iz =parte1[0];

                String parte1De = parte1[1];

            

                

                

                // IZQUIERDA de =

                

                String CodigosVoc[] = parte1Iz.split(":");

                String nomEnfe = CodigosVoc[0];

                String codes[] = CodigosVoc[1].split(";");


            

                    for (String cachoCodigo : codes) {

                    String[] div =cachoCodigo.split("@");

                    

                    Codigo code = new Codigo(div[0], div[1], i + 1);

                    listaCodes.add(code);  //cod--0

                                            //voc--1

                    

                //COMO NO NOS PARA DE DAR ERROR NO QUEDA MÁS REMEDIO QUE HACER

                    if (!estaVoc(Vocs, div[1])){

                        Vocs.add(div[1]);

                    }

                }

                    


                // DERECHA de =

                String datosSintoma[] = parte1De.split(";");

                

                    for (String cachoCodigo : datosSintoma) {

                    

                    String separar[] = cachoCodigo.split(":");

                    Sintoma sintoma = new Sintoma(separar[0], separar[1], separar[2]);

                    sintomasLOCAL.add(sintoma);

                    

                    

                    //Hemos de meter todo con sumo cuidado y que encaje, porque sino nos salta

                    if (!estaSin(listaSintomasGLOBAL, sintoma)){

                        listaSintomasGLOBAL.add(sintoma);

                    }

                    if (!estaSemantic(listaSemantic,separar[2])){

                     listaSemantic.add(separar[2]);

                    }

                }


                Enfermedad enfermedad = new Enfermedad(nomEnfe, i + 1,

                        sintomasLOCAL,listaCodes);

                Enfermedades.add(enfermedad);

                

            }

            

           

            //ACLARACIONES 

            //nombre el que viene

            //codigo (de enfermedad)---code

            //vocabulario---name DE SOURCE

            

            //nombre el que viene 

            //codigo sintoma---cui

            // semantic type sintoma ---semantic type sintoma

            //LOS ID'S LOS ASIGNAMOS NOSOTROS CON LAS PASADAS DE LOS BUCLES

           

    

        

               

                

            for(int i = 0; i < Enfermedades.size(); i++){

            //Nombre de Enfermedad                                                     

            PreparedStatement codigoSQLEnst = conn.prepareStatement("INSERT INTO disease (disease_id,name) VALUES (?,?) ;");

            codigoSQLEnst.setInt(1,Enfermedades.get(i).getId());

            codigoSQLEnst.setString(2,Enfermedades.get(i).getNombre());

            codigoSQLEnst.executeUpdate();


            

            }

            //TABLA semantic_type 

            for(int k = 0; k<listaSemantic.size(); k++){

             //semantic type sintoma                                                           //ojo aquí cui es el string que sacamos sematic_type

            PreparedStatement codigoSQLSTst = conn.prepareStatement("INSERT INTO semantic_type (semantic_type_id,cui)  VALUES (?,?);");

            codigoSQLSTst.setInt(1,k+1);

            codigoSQLSTst.setString(2,listaSemantic.get(k));

            codigoSQLSTst.executeUpdate();

            

            }

            

            

             for(int k = 0; k<listaSintomasGLOBAL.size(); k++){

                   PreparedStatement codigoSQLnomSst = conn.prepareStatement( "INSERT INTO  symptom (cui,name) VALUES (?,?) ;");

                   

                   codigoSQLnomSst.setString(1,listaSintomasGLOBAL.get(k).getCui());

                   codigoSQLnomSst.setString(2,listaSintomasGLOBAL.get(k).getNombreSin());

                   codigoSQLnomSst.executeUpdate();

                   

                   }

                   

             for(int k = 0;k<Vocs.size(); k++){

                 //TABLA source

                 //Voc   

                 PreparedStatement codigoSQLdVocst = conn.prepareStatement("INSERT INTO source (source_id,name) VALUES (?,?) ;");

                 codigoSQLdVocst.setInt(1,k+1);

                 codigoSQLdVocst.setString(2,Vocs.get(k));

                 codigoSQLdVocst.executeUpdate();

                 }

             for(int k = 0;k<listaCodes.size(); k++){

                    

                 //TABLA code

                 PreparedStatement codigoSQLCodVst = conn.prepareStatement("INSERT INTO code (code,source_id) VALUES (?,?) ;");

                 codigoSQLCodVst.setString(1,listaCodes.get(k).getCode());

                 codigoSQLCodVst.setInt(2,buscarId(Vocs, listaCodes.get(k).getSource()) + 1);

                 ///usamos el buscaId para que encajen los id, ya que es un primary key

                 codigoSQLCodVst.executeUpdate();

             

                 }

             


                for(int k = 0; k<listaSintomasGLOBAL.size(); k++){

                //TABLA symptom_semantic_type

                

                 //codgo sintoma symptom_semantic_type

                PreparedStatement codigoSQLcodSst = conn.prepareStatement("INSERT INTO symptom_semantic_type (cui,semantic_type_id) VALUES (?,?) ;");

                

                codigoSQLcodSst.setString(1,listaSintomasGLOBAL.get(k).getCui());

                codigoSQLcodSst.setInt(2,buscarId(Vocs, listaSintomasGLOBAL.get(k).getSemantic()) + 1);

                //OJO igual que antes

                codigoSQLcodSst.executeUpdate();

                }

                

                

                for(int k = 0;k<Vocs.size(); k++){


                //TABLA disease_has_code 

                PreparedStatement codigoSQLHCst = conn.prepareStatement("INSERT INTO disease_has_code (disease_id,code,source_id) VALUES (?,?,?);");

                

                codigoSQLHCst.setInt(1,listaCodes.get(k).getEnf());

                codigoSQLHCst.setString(2,listaCodes.get(k).getCode());

                codigoSQLHCst.setInt(3,buscarId(Vocs, listaCodes.get(k).getSource()) + 1);

                //OJO todo tiene que encajar

                codigoSQLHCst.executeUpdate();

            

                }

            

                

            for(int k = 0; k<Enfermedades.size(); k++){    

                PreparedStatement codigoSQLDSst = conn.prepareStatement("INSERT INTO disease_symptom (disease_id,cui) VALUES (?,?);");

                

                //y para que encaje todo tenemos que cogerlo de lo ya hecho

                for(int j = 0; j<Enfermedades.get(k).getSintomas().size(); j++){

                    

                codigoSQLDSst.setInt(1,Enfermedades.get(k).getId());

                codigoSQLDSst.setString(2,Enfermedades.get(k).getSintomas().get(j).getCui());

                 codigoSQLDSst.executeUpdate();

                }

                }

        

       

    } catch (Exception e) {

            

            e.printStackTrace();

            System.out.println("Fallo en disecion de data o en introducir los datos en tablas");

    }

        


        }


    private void realizarDiagnostico() throws Exception {

    	 LinkedList<String> listaCUI= new LinkedList<String>();
		 LinkedList<String> listaNOMBRES= new LinkedList<String>();
		 LinkedList<Integer> enfermedadesIDQuery= new LinkedList<Integer>();
		 LinkedList<Integer> enfermedadesIDQueryFinal= new LinkedList<Integer>();	
		
		
		/*
		 * Primero voy a seleccionar los síntomas haciendo una query, para de esta manera tenerlos almacenados en un string de java, y
		 * posteriormente hacer ahí la comprobación
		 */
		System.out.println("\n\n\n Lista de codigos asociados a los sintomas:\n");
		String query = "SELECT * FROM `diagnostico`.`symptom`";
		PreparedStatement st;
		try {
			st = conn.prepareStatement(query);
			ResultSet resul = st.executeQuery(query);
			System.out.printf("CÓDIGO","NOMBRE DEL SINTOMA");
			

			while (resul.next()) {
				listaCUI.add(resul.getString(1)) ;
				listaNOMBRES.add(resul.getString(2));
				
			}
			
			for(int i=0;i<listaCUI.size();i++){
				System.out.print("Estos son los síntomas: ---"+listaNOMBRES.get(i)+"--- y estos son los cui´s: ---"+listaCUI.get(i)+"---\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("\n\n----------------------------------------------------------\n\n");
		
		
		
		System.out.println("Introduzca los codigos de los síntomas para realizar diagnóstico separados  por una coma"); //ojo muy importante
		String userQUERY = readString();
		String [] sintomasUser = userQUERY.split(",", 0);
		
	/*
	 * Ahora vamos a seleccionar las enfermedades que tienen los síntomas que nos ha pasado el usuario
	 */
		
	try{
		for(int i=0;i<sintomasUser.length;i++){
			PreparedStatement rs= conn.prepareStatement( "SELECT disease_id FROM disease_symptom WHERE cui=? ");
			rs.setString(1,sintomasUser[i]);
			ResultSet resultLectura =rs.executeQuery();
		
		while(resultLectura.next()){
			enfermedadesIDQuery.add(resultLectura.getInt("disease_id"));
    		
		
				}
		resultLectura.close();
			}
		
		
	}catch(Exception e){
			e.printStackTrace();
						}
	//Ahora seleccionaremos los nombres de las enfermedades
	
	enfermedadesIDQueryFinal=apariciones(enfermedadesIDQuery,sintomasUser.length);
	
	
	try{
		for(int i=0;i<enfermedadesIDQueryFinal.size();i++){
			PreparedStatement rs1= conn.prepareStatement( "SELECT name FROM disease WHERE disease_id=? ");
			rs1.setInt(1,enfermedadesIDQueryFinal.get(i));
			ResultSet resultLectura1 =rs1.executeQuery();
		
		while(resultLectura1.next()){
			System.out.println("Estas son las enfermedades (aparecen repetidas tantas veces como inserciones"
					+ "se hayan realizado en este método, si es que hay alguna que lo cumple) : "+resultLectura1.getString("name")+"\n");
    		
		
				}
		resultLectura1.close();
			}
		
		
	}catch(Exception e){
			e.printStackTrace();
						}
		
	      

    }
    
    private void listarSintomasEnfermedad() {

	    	
	    	
	    	/*
	    	 * Este linkedList lo usaremos para saber si el código que ha introducido es válido o 
	    	 * no está presente en nuestra base de datos, en el caso de que no este presente le diremos que pruebe otra vez
	    	 * con un código distinto, de esta manera evitamos hacer una query que vaya a dar un error
	    	 * 
	    	 */
    	LinkedList<Integer> enfermedadesID = new LinkedList<Integer>();
    	LinkedList<String> 	enfermedades = new LinkedList<String>();
    	LinkedList<String> 	symptomsCUI = new LinkedList<String>();
    	LinkedList<String> 	symptoms = new LinkedList<String>();
    	boolean presenteBD = false;

    	try{
    		String SQLEnfermedades = "SELECT disease_id, name FROM disease; ";
	    	PreparedStatement SQLEnfermedades1 = conn.prepareStatement(SQLEnfermedades);
	    	ResultSet resu =SQLEnfermedades1.executeQuery();
	    	while (resu.next()) {
    		
    		int diseaseID= resu.getInt("disease_id");
    		
    		//Vamos a introducirlos en el linkedList para posteriormente hacer la comprobación 
    		enfermedadesID.add(diseaseID);
    		enfermedades.add(resu.getString("name"));
    		
    		String name = resu.getString("name");
				System.out.print("disease id: " + diseaseID);
				System.out.println(", name : " + name);

					}
    	resu.close();
    		     
    		System.out.println("Escriba ahora su ID");
    		try {
    		
    		//Esta sería la parte de seleccionar el id
			int  n =readInt();
			//vamos a ver si está presente en la BD
	    	for(int i=0;i<enfermedadesID.size();i++){
	    		if(n==enfermedadesID.get(i)){
	    			presenteBD= true;
	    		}
	    	}
	    	
			System.out.println("Este es tu código: "+n);
			if(presenteBD==true){
			
			PreparedStatement rs= conn.prepareStatement("SELECT disease_id, cui FROM disease_symptom WHERE disease_id =?");
			rs.setInt(1,n);
			ResultSet resultLectura =rs.executeQuery();
			
			while(resultLectura.next()){
				
				symptomsCUI.add(resultLectura.getString("cui"));
				
					
			}
			resultLectura.close();
			
			//Una vez he obtenido los cui falta hacer ahora la conversón de esos cui´s a sus respectivos nombres
		
			for(int i =0;i<symptomsCUI.size();i++){
				symptoms.add(getSymptomName(symptomsCUI.get(i)));
				
				
				
			}
			//VALE, ahora que tenmemos todo lo sacamos por pantalla
			for(int i=0;i<symptoms.size();i++){
				System.out.println("Esta es la enfermedad: "+enfermedades.get(n-1)+" ; este es su ID: "+n+" ;  y este es uno de los síntomas que presenta: "+symptoms.get(i));
			}
			//Aqui acaba el if
			}
			
			else{
				System.out.println("El código que Usted ha introducido no se haya presente en nuestra Base de Datos, intente con otro código");
			}
	    	
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
    	
    	//Para que no este todo apelmazado y darle un poco más de visibilidad
    	System.out.println("");
    	
    	}catch(Exception e){
    		e.printStackTrace();
    	}
        

    }


    private void listarEnfermedadesYCodigosAsociados() {
    	/*
    	 *Comentarios para antes de la entrega el enunciado de la práctica, no lo entiendo del todo bien
    	 *no sé a qué se refiere con lo de "El programa debe mostrar las enfermedades que contiene la base de
    	 *de datos, y para cada enfermedad qué códigos tiene (y tipo de código)" el tipo de código no sé
    	 *que es exactamente, deduzco que debe de ser el source_id que luego hay que ponerle el nombre. 
    	 */
    	try {
	    	LinkedList<String> diseasesNames = new LinkedList<String>();
	    	LinkedList<Integer> diseasesID = new LinkedList<Integer>();
	    	LinkedList<String> sourceNames = new LinkedList<String>();
	    	LinkedList<Integer> sourceCodes = new LinkedList<Integer>();
	    	LinkedList<String> diseasesNames1 = new LinkedList<String>();
	    	
	    	
	    	String SQLEnfermedades =    "SELECT name, disease_id FROM disease; ";
	    	String SQLEnfermedadesCOD = "SELECT code, source_id FROM disease_has_code WHERE disease_id= "; 
	    	/*
	    	 * Aquí,en este String SQLEnfermedadesCOD introduciremos un numero, que va a ser el que vamos ha almacenar ahora 
	    	 * en el ArrayList de tipo enteros (Este array contiene de cada enfermedad su código, en función)
	    	 * del orden en el que estén en la base de datos 
	    	*/
	    	
	    	
	    	
	    	
	    	PreparedStatement SQLEnfermedades1 = conn.prepareStatement(SQLEnfermedades);
	    	ResultSet resu =SQLEnfermedades1.executeQuery();
	    	//Ahora hacemos esa query, de esta manera los vamos almacenando
	    	while (resu.next()) {
	    			
	    			
	    			diseasesID.add(resu.getInt("disease_id"));
	    			diseasesNames.add(resu.getString("name"));
	  
					
	    			}
		
	    	resu.close();
	    	
	    	/*
	    	 * Esta query la hago para almacenar los sources con sus id´s asoicados
	    	 */
	    	PreparedStatement SQLsources = conn.prepareStatement("SELECT name, source_id FROM source;");
	    	ResultSet resu1 =SQLsources.executeQuery();
	    	//Ahora hacemos esa query, de esta manera los vamos almacenando
	    	while (resu1.next()) {
	    			
	    			
	    			sourceNames.add(resu1.getString("name"));
	    			sourceCodes.add(resu1.getInt("source_id"));
	  
					
	    			}
		
	    	resu.close();
	    	
	    	for(int i=0;i<diseasesID.size();i++){
	    		PreparedStatement SQLEnfermedadesCod = conn.prepareStatement(SQLEnfermedadesCOD+diseasesID.get(i));
	    		ResultSet rscodes = SQLEnfermedadesCod.executeQuery();
	    		
	    		while(rscodes.next()){
	    			diseasesNames1.add(diseasesNames.get(i)+"; este es su código: "+rscodes.getString("code")+";  y este es su tipo de código: "
	    					+sourceNames.get(rscodes.getInt("source_id")));
	    		}
	    		
	    	}
	    	/*
	    	 * AHORA VAMOS A IMPRIMIR UNA A UNA TODAS LAS ENFERMEDADES CON SUS RESPECTIVOS CÓDIGOS 
	    	 * Y SUS RESPECTIVOS CAMPOS
	    	 */
	    	for(int i =0;i<diseasesNames1.size();i++){
	    	System.out.print("Este es el nombre de la enfermedad: ");
	    	System.out.println(diseasesNames1.get(i)+"\n");
	    	}
	    
	    }catch(Exception e){
	    	e.printStackTrace();}
	    	


        
    }
        

        

        

    
    private void listarSintomasYTiposSemanticos() throws SQLException {
    	
    	//En el primero almacenamos los nombres de los síntomas
    	LinkedList<String> symptomsNombre = new LinkedList<String>();
    	//Almacenamos los cui´s
    	LinkedList<String> symptomsCUI = new LinkedList<String>();
    	//En este almacenaremos los sintomas junto con sus tipos semánticos, posteriormente lo llamaremos para imprimirlo por pantalla
    	LinkedList<String> symptoms1 = new LinkedList<String>();

    	try{
    	String SQLSYMPTOM = "SELECT cui, name FROM symptom";
    	PreparedStatement symptomSQL = conn.prepareStatement(SQLSYMPTOM);
    	ResultSet resultSymptom =symptomSQL.executeQuery();
    	
    	
    	/*
    	 * Hacemos una query para obtener los nombres de cada síntoma y obtener sus códigos asociados
    	 * una vez hemos obtenido estos, podremos realizar una consulta con estos campos semánticos 
    	 * y así obtener de manera conjunta el nobre del síntoma y sus campos semánticos
    	 */
    	while(resultSymptom.next()){
    		symptomsNombre.add(resultSymptom.getString("name"));
    		symptomsCUI.add(resultSymptom.getString("cui"));
    		
    		
    	}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	

    	//Ahora los iremos seleccionando de uno en uno mediante sus sintomas
    	
    	for(int i=0;i<symptomsCUI.size();i++){
    		String SQLSymptoms =  "SELECT semantic_type_id FROM symptom_semantic_type WHERE cui='";
    		
    		SQLSymptoms+=symptomsCUI.get(i)+"'";
    	try{	
        PreparedStatement SQLsymptoms = conn.prepareStatement(SQLSymptoms);
        ResultSet rsSymptoms = SQLsymptoms.executeQuery();
        
        while(rsSymptoms.next()){
        	
        	//
        	
        	symptoms1.add("Esta es la enfermedad: ----"+symptomsNombre.get(i)+"---- y este es su tipo semántico: "+rsSymptoms.getInt("semantic_type_id")+"\n");
          		}
        
        	}catch(Exception e){
        		e.printStackTrace();
        	}
    	}
    	//Ahora lo imprimimos todo por pantalla
    	for(int i =0;i<symptoms1.size();i++)
    	System.out.println(symptoms1.get(i));
    	
    	
    	//Para que no este todo apelmazado y darle un poco más de visibilidad
    	System.out.println("");


        

        

        


    	}
    


    private void mostrarEstadisticasBD() {
    	
    	/*
    	 * FALTAN LOS APARTADOS 4C Y 4D
    	 */
    	LinkedList<Integer>sintomasContador= new LinkedList<Integer>();
    	int numDiseases=0;
    	try{
    		String numberDiseases = "SELECT COUNT('name') FROM disease";		    	
    		PreparedStatement Numdiseases = conn.prepareStatement(numberDiseases);
    		ResultSet resultNum =Numdiseases.executeQuery();
            resultNum.next();          
            System.out.println("Esta es la cantidad de enfermedades que hay ahora mismo registradas en la base de datos:" + resultNum.getInt(1));
            numDiseases=resultNum.getInt(1);
          //Para que no este todo apelmazado y darle un poco más de visibilidad
	    	System.out.println("");
            
          }catch(SQLException s){
            System.out.println(s);
          }
    	
   try{
		String numberSymptoms = "SELECT COUNT('name') FROM symptom";		    	
		PreparedStatement Numsymptoms = conn.prepareStatement(numberSymptoms);
		ResultSet resultNumSym =Numsymptoms.executeQuery();
        resultNumSym.next();          
        System.out.println("Esta es la cantidad de síntomas que hay ahora mismo registradas en la base de datos:" + resultNumSym.getInt(1)+"\n");
        
     
        
      }
      catch(SQLException s){
        System.out.println(s);
      }
    
    

    /*Para esta parte hemos contando cuantos síntomas tenía cada una de las enfermedades de nuestra BD, y posteriormente 
     * hemos dado tanto con el máximo como con el mínimo
     * 
     */
   
    //Esta es la parte en la que se selecciona la enfermedad con myor cantidad de síntomas registrados
 
    try{
		String diseaseCount = "SELECT COUNT(*) as count FROM disease_symptom WHERE disease_id='";
		for(int i=1;i<numDiseases+1;i++){
		PreparedStatement count = conn.prepareStatement(diseaseCount+i+"';");
		ResultSet resultMaxSym =count.executeQuery();
        resultMaxSym.next();          
        sintomasContador.add(resultMaxSym.getInt(1));
        
		}
		int posMax=getMaxValue(sintomasContador);
		int posMin=getMinValue(sintomasContador);
		
		
		System.out.println("La enfermedad con mayor número de síntomas:  "+getDisease(posMax)+"   tiene un total de :"+sintomasContador.get(posMax));
		System.out.println("\n La enfermedad con menor número de síntomas:  "+getDisease(posMin)+"   tiene un total de :"+sintomasContador.get(posMin));
		System.out.println("\n Las enfermedadades tienen un promedio de síntomas de un total de :"+media(sintomasContador)+"\n\n");
      }
      catch(SQLException s){
        System.out.println(s);
      }
    /*Se hace igual para seleccionar la enfermedad con menor cantidad de síntomas registrados
    try{
		String DiseaseMin = "SELECT MIN('source_id') FROM  disease_has_code";		    	
		PreparedStatement MINdisease = conn.prepareStatement(DiseaseMin);
		ResultSet resultMinSym =MINdisease.executeQuery();
        resultMinSym.next();          
        System.out.println("Esta es la enfermedad: " +  " con la menor cantidad de síntomas que hay ahora mismo registradas en la base de datos:" + resultMinSym.getInt("source_id"));
        
      //Para que no este todo apelmazado y darle un poco más de visibilidad
    	System.out.println("");
        
      }
      catch(SQLException s){
        System.out.println(s);
      }  

    }
*/


    /**

     * Método para leer números enteros de teclado.

     * 

     * @return Devuelve el número leído.

     * @throws Exception

     *             Puede lanzar excepción.

     */
    }
    private int readInt() throws Exception {

        try {

            System.out.print("> ");

            return Integer.parseInt(new BufferedReader(new InputStreamReader(System.in)).readLine());

        } catch (Exception e) {

            throw new Exception("Not number");

        }

    }


    /**

     * Método para leer cadenas de teclado.

     * 

     * @return Devuelve la cadena leída.

     * @throws Exception

     *             Puede lanzar excepción.

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

     * Método para leer el fichero que contiene los datos.

     * 

     * @return Devuelve una lista de String con el contenido.

     * @throws Exception

     *             Puede lanzar excepción.

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


    private boolean estaVoc(LinkedList<String> listaVoc, String voc) {

        boolean esta=false;

        for (int i = 0; i < listaVoc.size(); i++) {

            if (listaVoc.get(i).compareTo(voc)==0){

                esta = true;

            }

        }

        return esta;

    }

    

    private boolean estaSin(LinkedList<Sintoma> listaSintomaGLOBAL, Sintoma sintoma) {

        boolean esta=false;

        for (int i = 0; i < listaSintomaGLOBAL.size() ; i++) {

            if (listaSintomaGLOBAL.get(i).getNombreSin().compareTo(sintoma.getNombreSin())==0){

                esta = true;

            }

        }

        return esta;

    }

    

    private boolean estaSemantic(LinkedList<String> listaSemantic,String semantic) {

          boolean esta = false;

          for (int i = 0; i < listaSemantic.size(); i++) {

                if (listaSemantic.get(i).compareTo(semantic)==0){

                    esta = true;

                }

            }

            

          return esta;

      }

    



    private int buscarId(LinkedList<String> Vocs, String voc) {

        

        int pos = 0;

        for (int k = 0; k < Vocs.size(); k++) {

            if (Vocs.get(k).compareTo(voc)==0) {

                pos = k;

            }

        }

        return pos;

    }
    private class Enfermedad {

        

        private String nomEnf;

        LinkedList<Sintoma> sintomas = new LinkedList<Sintoma>();

        


        private int id;


        Enfermedad(String nombre, int id, LinkedList<Sintoma> sintomas, LinkedList<Codigo> codigos) {

            this.nomEnf = nombre;

            this.id = id;    

            this.sintomas = sintomas;

            

        }


        public String getNombre() {

            return this.nomEnf;

        }


        public int getId() {

            return this.id;

        }


        public LinkedList<Sintoma> getSintomas() {

            return this.sintomas;

        }

        

        

    }


    private class Sintoma {

        private String cui;

        private String nombreSin;

        private String semantic;


        public Sintoma(String nombre, String cui, String semantic) {


            this.nombreSin = nombre;

            this.cui = cui;

            this.semantic = semantic;

        }


        public String getNombreSin() {

            return this.nombreSin;

        }


        public String getCui() {

            return this.cui;

        }


        public String getSemantic() {

            return this.semantic;

        }

    }


    private class Codigo {

        private String code;

        private String source;

        private int disease_id;


        public Codigo(String code, String source, int disease_id) {

            this.code = code;

            this.source = source;

            this.disease_id = disease_id;

        }


        public String getCode() {

            return this.code;

        }


        public String getSource() {

            return this.source;

        }

        public int getEnf() {

            return this.disease_id;

        }


    }public String getDisease(int ID){
    	String diseaseName="";
    	try{
    		
    	PreparedStatement rsSymptoms= conn.prepareStatement("SELECT name FROM disease WHERE disease_id ="+"'"+ID+"'");
		ResultSet QuerySymptoms =rsSymptoms.executeQuery();
		
		while(QuerySymptoms.next()){
			
			diseaseName= QuerySymptoms.getString("name");
				
				}
		
		QuerySymptoms.close();
    	}catch(Exception e){
    	e.printStackTrace();
    	
    		}
		return diseaseName;

    }  
    

    public String getSymptomName(String cui){
    	String symptomName="";
    	try{
    		
    	PreparedStatement rsSymptoms= conn.prepareStatement("SELECT name FROM symptom WHERE cui ="+"'"+cui+"'");
		ResultSet QuerySymptoms =rsSymptoms.executeQuery();
		
		while(QuerySymptoms.next()){
			
			 symptomName= QuerySymptoms.getString("name");
				
				}
		
		QuerySymptoms.close();
    	}catch(Exception e){
    	e.printStackTrace();
    	
    		}
		return symptomName;

    }  
    public static LinkedList<Integer> apariciones(LinkedList<Integer> vector, int nveces){
	 	LinkedList<Integer> j = new LinkedList<Integer>();
    	
    	int[] contador= new int[vector.size()];
    	for(int i=0;i<vector.size();i++){
    		for(int h=0;h<vector.size();h++){
    			if(vector.get(i)==vector.get(h)&&contador[i]<nveces){
    				contador[i]++;
    				if(contador[i]==nveces){
    					j.add(vector.get(i));
    				}
    					
    			}
    		}
    		
    	
    }

    	return j;
 }
 // getting the maximum value
    public static int getMaxValue(LinkedList<Integer>vector) {
    	int pos=-1;
        int maxValue = vector.get(0);
        for (int i = 1; i < vector.size(); i++) {
            if (vector.get(i) > maxValue) {
                maxValue = vector.get(i);
                pos=i;
            }
        }
        return pos;
    }

    // getting the miniumum value
    public static int getMinValue(LinkedList<Integer>vector) {
    	int pos=-1;
        int minValue = vector.get(0);
        for (int i = 1; i < vector.size(); i++) {
            if (vector.get(i) < minValue) {
                minValue = vector.get(i);
                pos=i;
            }
        }
        return pos;
    }
    public static double media(LinkedList<Integer>vector){
    	
    	int sumador=0;
    	for (int i = 1; i < vector.size(); i++) {
            sumador+=vector.get(i);
             
    	}
    	return sumador/vector.size();
    }

    public static void main(String args[]) {

        new Diagnostico1().showMenu();

        

    }

    

}




