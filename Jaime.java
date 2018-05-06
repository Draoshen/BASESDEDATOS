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




        private final String DATAFILE = "data/disease_data.data";
        private Connection conn;
        
        
    
    //necesitamos esto para operar
        private static ArrayList<String> nomEnfermedad = new ArrayList<>(); 
        private static LinkedList<String[]> CodVoc = new LinkedList<String[]>();
        private static LinkedList<String[]>arraysintomas = new LinkedList<String[]>();  
        
        
        
        
//        private static ArrayList<String[]> CodVoc = new ArrayList<String[]>();
        //private static ArrayList<String[]>arraysintomas = new ArrayList<String[]>();  
        
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
                //        más información), se debe cargar su contenido usando código Java, procesarlo y en base
                //        a la estructura del E-R y los datos contenidos, insertar la información en la base de
                //datos. Debe tenerse en cuenta el diagrama E-R para entender como son los datos y
                //        como deben por lo tanto almacenarse. Obligatoriamente, debe ejecutarse la creación y
                //        carga de todos los datos como si fuera una única transacción, de tal forma que cualquier
                //        fallo intermedio de lugar a deshacer por completo los cambios anteriores.
                
                
                
                //RECUERDA LA RECETA
                
                //1 String Code= "codigo en SQL"
                //2 PreparedStatement nombre = conn.prepareStament(Code);
                //3 nombre.executeQuery
                //consultoEn.setString(1,"hola");
                //lo que hace esto es, DONDE ESTE LAL PRIMERA "?" pones "hola",  y hace la consulta en base a "hola"
                //y así para todo
                
                
                conectar();
                
                
            conn.setAutoCommit(false);
        //Para poder ejecutar más de una vez
            PreparedStatement stmt = conn.prepareStatement("DROP DATABASE IF EXISTS `diagnostico`;");
                stmt.executeUpdate();
                
                //CAMIBAR UN PAR DE LETRAS DE code_ibfk_1 POR code_opty_1 si da error
        
                /*
                 //USANOD EL USE
                NOS SALTA java.sql.SQLException: Can't create table 'diagnostico.code' (errno: 150)
                
         String SQLcodigoDB = "CREATE SCHEMA IF NOT EXISTS `diagnostico`;";
                 PreparedStatement SQLcodigoDBst = conn.prepareStatement(SQLcodigoDB );
         SQLcodigoDBst.executeUpdate();
                 
        String SQLcodigoUSE = "USE  `diagnostico`;"; //PARA NO ESTAR PONIENDO  `diagnostico`.`nombre_table` todo el rato
         PreparedStatement SQLcodigoUSEst = conn.prepareStatement(SQLcodigoUSE);
                 SQLcodigoUSEst.executeUpdate();
                 
                 // "CREATE TABLE IF NOT EXISTS `diagnostico`.`disease` (` OJO   disease_id` INT(11)   OJO NOT NULL,`name` VARCHAR(255) NOT NULL,PRIMARY KEY (`disease_id`))ENGINE = InnoDB;";
         String SQLcodigoEN  =  "CREATE TABLE  `disease` (`disease_id` INT NOT NULL,`name` VARCHAR(255) NOT NULL,PRIMARY KEY (`disease_id`))ENGINE = InnoDB;";
         PreparedStatement SQLcodigoENst = conn.prepareStatement(SQLcodigoEN);
                 SQLcodigoENst.executeUpdate();
                 
         // "CREATE TABLE IF NOT EXISTS `diagnostico`.`symptom` (OJO   `cui` VARCHAR(25) NOT NULL    OJO,`name` VARCHAR(255) NOT NULL,PRIMARY KEY (`cui`))ENGINE = InnoDB;";
                 String SQLcodigoS  = "CREATE TABLE  `symptom` (`cui` VARCHAR(25) NOT NULL,`name` VARCHAR(255) NOT NULL,PRIMARY KEY (`cui`))ENGINE = InnoDB;";
                 PreparedStatement SQLcodigoSst =  conn.prepareStatement(SQLcodigoS);                                
                 SQLcodigoSst.executeUpdate();
                 
                //"CREATE TABLE IF NOT EXISTS `diagnostico`.`code` (`code` VARCHAR(255) NOT NULL, OJO    `source_id` VARCHAR(25) NOT NULL      OJO ,PRIMARY KEY (`code`, `source_id`),CONSTRAINT `code_ibfk_1` FOREIGN KEY (`source_id`) REFERENCES `diagnostico`.`source` (`source_id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB;";
                 String SQLcodigoC  = "CREATE TABLE  `code` (`code` VARCHAR(255) NOT NULL, `source_id` VARCHAR(25) NOT NULL ,PRIMARY KEY (`code`,`source_id`), FOREIGN KEY (`source_id`) REFERENCES `source` (`source_id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB;";
        PreparedStatement SQLcodigoCst  = conn.prepareStatement(SQLcodigoC);
                SQLcodigoCst.executeUpdate();
                 
        //"CREATE TABLE IF NOT EXISTS `diagnostico`.`disease_has_code` (OJO    `disease_id` INT(11) NOT NULL     OJO,`code` VARCHAR(255) NOT NULL,`source_id` VARCHAR(45) NOT NULL, PRIMARY KEY (`disease_id`, `code`, `source_id`), CONSTRAINT `fk_disease_id2`FOREIGN KEY (`disease_id`) REFERENCES `diagnostico`.`disease` (`disease_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,CONSTRAINT `fk_code2` FOREIGN KEY (`code`) REFERENCES `diagnostico`.`code` (`code`)ON DELETE NO ACTION ON UPDATE NO ACTION,CONSTRAINT `fk_source_id2` FOREIGN KEY (`source_id`) REFERENCES `diagnostico`.`code` (`source_id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB;";
         String SQLcodigoD  = "CREATE TABLE  `disease_has_code` (`disease_id` INT(11) NOT NULL ,`code` VARCHAR(255) NOT NULL,`source_id` VARCHAR(45) NOT NULL, PRIMARY KEY (`disease_id`, `code`, `source_id`), CONSTRAINT `fk_disease_id2`FOREIGN KEY (`disease_id`) REFERENCES `disease` (`disease_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,CONSTRAINT `fk_code2` FOREIGN KEY (`code`) REFERENCES `code` (`code`)ON DELETE NO ACTION ON UPDATE NO ACTION,CONSTRAINT `fk_source_id2` FOREIGN KEY (`source_id`) REFERENCES `code` (`source_id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB;";
         PreparedStatement SQLcodigoDst  = conn.prepareStatement(SQLcodigoD);
                SQLcodigoDst.executeUpdate();
        
                 
                 //"CREATE TABLE IF NOT EXISTS`diagnostico`.`disease_symptom` (`disease_id` INT NOT NULL, OJO      `cui` VARCHAR(25) NOT NULL    OJO ,PRIMARY KEY (`disease_id`, `cui`),CONSTRAINT `fk_disease_id`FOREIGN KEY (`disease_id`) REFERENCES `diagnostico`.`disease` (`disease_id`) ON DELETE NO ACTION ON UPDATE NO ACTION, CONSTRAINT `fk_cui` FOREIGN KEY (`cui`) REFERENCES `diagnostico`.`symptom` (`cui`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB;";                                                         
         String SQLcodigoSE = "CREATE TABLE  `disease_symptom` (`disease_id` INT NOT NULL, `cui` VARCHAR(25) NOT NULL ,PRIMARY KEY (`disease_id`, `cui`),CONSTRAINT `fk_disease_id`FOREIGN KEY (`disease_id`) REFERENCES `disease` (`disease_id`) ON DELETE NO ACTION ON UPDATE NO ACTION, CONSTRAINT `fk_cui` FOREIGN KEY (`cui`) REFERENCES `symptom` (`cui`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB;";
                          PreparedStatement SQLcodigoSEst = conn.prepareStatement(SQLcodigoSE);
          SQLcodigoSEst.executeUpdate();
           
                //// "CREATE TABLE IF NOT EXISTS `diagnostico`.`symptom_semantic_type` (`cui` VARCHAR(25) NOT NULL, OJO   `semantic_type_id` INT(11)      OJO NOT NULL,PRIMARY KEY (`cui`, `semantic_type_id`), CONSTRAINT `symptom_semantic_type_ibfk_1` FOREIGN KEY (`cui`) REFERENCES `diagnostico`.`symptom` (`cui`) ON DELETE NO ACTION ON UPDATE NO ACTION, CONSTRAINT `symptom_semantic_type_ibfk_2` FOREIGN KEY (`semantic_type_id`) REFERENCES `diagnostico`.`semantic_type` (`semantic_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB;";
 String SQLcodigoSS  = "CREATE TABLE  `symptom_semantic_type` (`cui` VARCHAR(25) NOT NULL, `semantic_type_id` INT(11)  NOT NULL,PRIMARY KEY (`cui`, `semantic_type_id`), CONSTRAINT `symptom_semantic_type_ibfk_1` FOREIGN KEY (`cui`) REFERENCES `symptom` (`cui`) ON DELETE NO ACTION ON UPDATE NO ACTION, CONSTRAINT `symptom_semantic_type_ibfk_2` FOREIGN KEY (`semantic_type_id`) REFERENCES `semantic_type` (`semantic_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB;";
 PreparedStatement SQLcodigoSSst = conn.prepareStatement(SQLcodigoSS);
         SQLcodigoSSst.executeUpdate();
                 
//                 //"CREATE TABLE IF NOT EXISTS `diagnostico`.`source` (OJO        `source_id` VARCHAR(25) NOT NULL      OJO,`name` VARCHAR(255) NOT NULL, PRIMARY KEY (`source_id`)) ENGINE = InnoDB;";
                 String SQLcodigoSO  = "CREATE TABLE `source` (`source_id` VARCHAR(25) NOT NULL ,`name` VARCHAR(255) NOT NULL, PRIMARY KEY (`source_id`)) ENGINE = InnoDB;";
         PreparedStatement SQLcodigoSOst = conn.prepareStatement(SQLcodigoSO);
         SQLcodigoSOst.executeUpdate();
                 
                 
                 //// "CREATE TABLE IF NOT EXISTS `diagnostico`.`symptom_semantic_type` (`cui` VARCHAR(25) NOT NULL, OJO    `semantic_type_id` INT(11)    OJO NOT NULL,PRIMARY KEY (`cui`, `semantic_type_id`), CONSTRAINT `symptom_semantic_type_ibfk_1` FOREIGN KEY (`cui`) REFERENCES `diagnostico`.`symptom` (`cui`) ON DELETE NO ACTION ON UPDATE NO ACTION, CONSTRAINT `symptom_semantic_type_ibfk_2` FOREIGN KEY (`semantic_type_id`) REFERENCES `diagnostico`.`semantic_type` (`semantic_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB;";
                 String SQLcodigoDS  = "CREATE TABLE  `symptom_semantic_type` (`cui` VARCHAR(25) NOT NULL, `semantic_type_id` INT(11) NOT NULL,PRIMARY KEY (`cui`, `semantic_type_id`), CONSTRAINT `symptom_semantic_type_ibfk_1` FOREIGN KEY (`cui`) REFERENCES `symptom` (`cui`) ON DELETE NO ACTION ON UPDATE NO ACTION, CONSTRAINT `symptom_semantic_type_ibfk_2` FOREIGN KEY (`semantic_type_id`) REFERENCES `semantic_type` (`semantic_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB;";
         PreparedStatement SQLcodigoDSst = conn.prepareStatement(SQLcodigoDS);
                SQLcodigoDSst.executeUpdate();


        */
        //sin usar el USE
                 String query;


                        Statement st = conn.createStatement();
                query = "DROP SCHEMA IF EXISTS `diagnostico`";
                st = conn.createStatement();
                st.executeUpdate(query);
                        query = "CREATE SCHEMA IF NOT EXISTS `diagnostico`;";
                        st = conn.createStatement();
                        st.executeUpdate(query);
                        query = "CREATE TABLE IF NOT EXISTS `diagnostico`.`source` (`source_id` VARCHAR(25) NOT NULL,`name` VARCHAR(255) NOT NULL, PRIMARY KEY (`source_id`)) ENGINE = InnoDB;";
                        st = conn.createStatement();
                        st.executeUpdate(query);
                        query = "CREATE TABLE IF NOT EXISTS `diagnostico`.`disease` (`disease_id` INT(11) NOT NULL,`name` VARCHAR(255) NOT NULL,PRIMARY KEY (`disease_id`))ENGINE = InnoDB;";
        st = conn.createStatement();
                        st.executeUpdate(query);
                        query = "CREATE TABLE IF NOT EXISTS `diagnostico`.`symptom` (`cui` VARCHAR(25) NOT NULL,`name` VARCHAR(255) NOT NULL,PRIMARY KEY (`cui`))ENGINE = InnoDB;";
                        st = conn.createStatement();
                        st.executeUpdate(query);
                        query = "CREATE TABLE IF NOT EXISTS `diagnostico`.`semantic_type` (`semantic_type_id` INT(11) NOT NULL,`cui` VARCHAR(45) NOT NULL,PRIMARY KEY (`semantic_type_id`))ENGINE = InnoDB;";
                        st = conn.createStatement();
                        st.executeUpdate(query);
                        query = "CREATE TABLE IF NOT EXISTS `diagnostico`.`code` (`code` VARCHAR(255) NOT NULL,`source_id` VARCHAR(25) NOT NULL,PRIMARY KEY (`code`, `source_id`), FOREIGN KEY (`source_id`) REFERENCES `diagnostico`.`source` (`source_id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB;";
                        st = conn.createStatement();
                        st.executeUpdate(query);
                        query = "CREATE TABLE IF NOT EXISTS `diagnostico`.`symptom_semantic_type` (`cui` VARCHAR(25) NOT NULL,`semantic_type_id` INT(11) NOT NULL,PRIMARY KEY (`cui`, `semantic_type_id`), CONSTRAINT `symptom_semantic_type_ibfk_1` FOREIGN KEY (`cui`) REFERENCES `diagnostico`.`symptom` (`cui`) ON DELETE NO ACTION ON UPDATE NO ACTION, CONSTRAINT `symptom_semantic_type_ibfk_2` FOREIGN KEY (`semantic_type_id`) REFERENCES `diagnostico`.`semantic_type` (`semantic_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB;";
                        st = conn.createStatement();
                st.executeUpdate(query);
                query = "CREATE TABLE IF NOT EXISTS`diagnostico`.`disease_symptom` (`disease_id` INT NOT NULL,`cui` VARCHAR(25) NOT NULL,PRIMARY KEY (`disease_id`, `cui`),CONSTRAINT `fk_disease_id`FOREIGN KEY (`disease_id`) REFERENCES `diagnostico`.`disease` (`disease_id`) ON DELETE NO ACTION ON UPDATE NO ACTION, CONSTRAINT `fk_cui` FOREIGN KEY (`cui`) REFERENCES `diagnostico`.`symptom` (`cui`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB;";
        st = conn.createStatement();
                st.executeUpdate(query);
                        query = "CREATE TABLE IF NOT EXISTS `diagnostico`.`disease_has_code` (`disease_id` INT(11) NOT NULL,`code` VARCHAR(255) NOT NULL,`source_id` VARCHAR(45) NOT NULL, PRIMARY KEY (`disease_id`, `code`, `source_id`), CONSTRAINT `fk_disease_id2`FOREIGN KEY (`disease_id`) REFERENCES `diagnostico`.`disease` (`disease_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,CONSTRAINT `fk_code2` FOREIGN KEY (`code`) REFERENCES `diagnostico`.`code` (`code`)ON DELETE NO ACTION ON UPDATE NO ACTION,CONSTRAINT `fk_source_id2` FOREIGN KEY (`source_id`) REFERENCES `diagnostico`.`code` (`source_id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB;";
                st = conn.createStatement();
                st.executeUpdate(query);
        
        /*         
        //DE STACKOVERFLOW 
                //si no ponemos esto  salta com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Cannot add or update a child row: a foreign key constraint fails (`diagnostico`.`code`, CONSTRAINT 
                 * `code_ibfk_1` FOREIGN KEY (`source_id`) REFERENCES `source` (`source_id`) ON DELETE NO ACTION ON UPDATE NO ACTION)
                st.executeUpdate(query);
                query = "SET FOREIGN_KEY_CHECKS=0";
        st = conn.createStatement();
        st.executeUpdate(query);
        */        
        
                        conn.commit();
                        conn.setAutoCommit(true);
                
        
                
                try{
                        
                        
                        
               LinkedList<String> contenido = readData();
                
                        
               for (int i = 0; i < contenido.size(); i++) {
                        


                                String datum = contenido.get(i);


                                String parte1[] = datum.split("=");
                                String parte1Iz =parte1[0];
                                String parte1De = parte1[1];
                                
                                // PARTE IZQUIERDA de =


                                String CodigosVoc[] = parte1Iz.split(":");
                                String nomEnfe = CodigosVoc[0];
                                nomEnfermedad.add(nomEnfe);
                                String codigos[] = CodigosVoc[1].split(";");


                        
                                        for (String cachoCodigo : codigos) {


                                        String[] div =cachoCodigo.split("@");
                                        CodVoc.add(div);
                                        
                                }


                                // PARTE DERECHA de =
                                String datosSintoma[] = parte1De.split(";");


                                //for (int j = 0; j < datosSintoma.length; j++) {
                                        
                                        for (int j = 0; j < datosSintoma.length; j++) {


                                        String separar[] = datosSintoma[j].split(":");
                                        arraysintomas.add(separar);
                                        
                                }


                        
                        
                        
                        }
                        
        
                        //ACLARACIONES 
                        //nombre el que viene
                        //codigo (de enfermedad)---code
                        //vocabulario---name DE SOURCE
                        
                        //nombre el que viene 
                        //codigo sintoma---cui
                        // semantic type sintoma ---semantic type sintoma
                        //LOS ID'S LOS ASIGNAMOS NOSOTROS CON LAS PASADAS DE LOS BUCLES
        
                        
                        //stack OVERFLOW
                        query = "SET FOREIGN_KEY_CHECKS=0";
                st = conn.createStatement();
                st.executeUpdate(query);
                        
                        
                        
                                
                        for(int i = 0; i < nomEnfermedad.size(); i++){
                                                
                        //TABLA disease
                        
                        //Nombre de Enfermedad                                                     
                        PreparedStatement codigoSQLEnst = conn.prepareStatement("INSERT INTO diagnostico.disease (disease_id,name) VALUES (?,?) ;");
                  //Metemos el id que es el entero
                        codigoSQLEnst.setInt(1,i+1);
                        codigoSQLEnst.setString(2,nomEnfermedad.get(i));
                        codigoSQLEnst.executeUpdate();


                        
                        }
                        
                        
          //Tenemos que hacer que recorran todos los codigos y voc de Enfermedades, 
                  //y todos los cui y sem de sintomas
                         
                        
                                
                        //ENFERMEDADES
                        for(int k = 0;k<CodVoc.size(); k++){
                                
                        //TABLA code
                                
                        //Cod 
                        PreparedStatement codigoSQLCodVst = conn.prepareStatement("INSERT INTO diagnostico.code (code,source_id) VALUES (?,?) ;");
                        
                        codigoSQLCodVst.setString(1,CodVoc.get(k)[0]);
                        codigoSQLCodVst.setInt(2,k+1);
                        codigoSQLCodVst.executeUpdate();
                        }
                        
                        for(int k = 0;k<CodVoc.size(); k++){
                        //TABLA source
                        
                        //Voc   
                        PreparedStatement codigoSQLdVocst = conn.prepareStatement("INSERT INTO diagnostico.source (source_id,name) VALUES (?,?) ;");
                        
                        codigoSQLdVocst.setInt(1,k+1);
                        codigoSQLdVocst.setString(2,CodVoc.get(k)[1]);
                        codigoSQLdVocst.executeUpdate();
                        }
                        for(int k = 0;k<CodVoc.size(); k++){


                        //TABLA disease_has_code 
                        PreparedStatement codigoSQLHCst = conn.prepareStatement("INSERT INTO diagnostico.disease_has_code (disease_id,code,source_id) VALUES (?,?,?);");
                        
                        codigoSQLHCst.setInt(1,k+1);
                        codigoSQLHCst.setString(2,CodVoc.get(k)[1]);
                        codigoSQLHCst.setInt(3,k+1);
                        
                        codigoSQLHCst.executeUpdate();
                
                        }
                
                
                        /*
                         * 
                         * ESTO DA ERROR
                    //SINTOMAS
                        for(int j = 0; j<arraysintomas.size(); j++){


                        //TABLA symptom
                        
            //nombre sintoma
                        PreparedStatement codigoSQLnomSst = conn.prepareStatement( "INSERT INTO diagnostico.symptom (cui,name) VALUES (?,?) ;");
                        codigoSQLnomSst.setString(1,arraysintomas.get(j)[1]);
                        codigoSQLnomSst.setString(2,arraysintomas.get(j)[0]);
                        codigoSQLnomSst.executeUpdate();
                
                        }
                        */
                        for(int j = 0; j<arraysintomas.size(); j++){
                        //TABLA symptom_semantic_type
                        
                         //codgo sintoma symptom_semantic_type
                        PreparedStatement codigoSQLcodSst = conn.prepareStatement("INSERT INTO diagnostico.symptom_semantic_type (cui,semantic_type_id) VALUES (?,?) ;");
                        codigoSQLcodSst.setString(1,arraysintomas.get(j)[1]);
                        codigoSQLcodSst.setInt(2,j+1);
                        
                        codigoSQLcodSst.executeUpdate();
                        }
                        //TABLA semantic_type 
                        for(int j = 0; j<arraysintomas.size(); j++){
                         //semantic type sintoma                                                           //ojo aquí cui es el string que sacamos sematic_type
                        PreparedStatement codigoSQLSTst = conn.prepareStatement("INSERT INTO diagnostico.semantic_type (semantic_type_id,cui)  VALUES (?,?);");
                        
                        codigoSQLSTst.setInt(1,j+1);
                        codigoSQLSTst.setString(2,arraysintomas.get(j)[2]);
                        
                        codigoSQLSTst.executeUpdate();
                        }
                        for(int j = 0; j<arraysintomas.size(); j++){
                        
                        //TABLA disease_symptom 
                        PreparedStatement codigoSQLDSst = conn.prepareStatement("INSERT INTO diagnostico.disease_symptom (disease_id,cui) VALUES (?,?);");
                        
                        codigoSQLDSst.setInt(1,j+1);
                        codigoSQLDSst.setString(2,arraysintomas.get(j)[1]);
                        
                         codigoSQLDSst.executeUpdate();
                        
                        }
                        
                // DE STACKOVERFLOW        
                        
                        query = "SET FOREIGN_KEY_CHECKS=1";
                st = conn.createStatement();
                st.executeUpdate(query);
                        
         
        } catch (Exception e) {
                        
                        e.printStackTrace();
                        System.out.println("Fallo en disecion de data o en introducir los datos en tablas");
        }
                


                }




        private void realizarDiagnostico() throws Exception {
                
                //Encunciado //la base de datos [2 puntos]: El programa debe pedir por teclado que síntomas quiere
                //tener en cuenta para realizar el diagnóstico. Los síntomas deben solicitarse para ser
                //introducidos por teclado, y debe previamente mostrarse la lista de síntomas disponibles,
                //para que quien use el programa pueda introducirlos usando sus códigos identificativos
                //(no a través del nombre del síntoma). El sistema debe devolver aquellas enfermedades
                //que tenga asociados todos los síntomas que se hayan introducido.
                                
                System.out.print("Introduzca los síntomas para realizar diagnóstico");
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
                //y con esos sintomas sacar enfermedades                                                        unico ? pues un único parámetro
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
//                a. Los síntomas de una enfermedad de la base de datos [0.5 puntos]: Debe mostrar
                //        cuales son las enfermedades que hay en la base de datos, y el usuario debe
                //        introducir el ID que corresponda para seleccionar la enfermedad.
                
                
                
        }


        private void listarEnfermedadesYCodigosAsociados() {
//                b. Listado de enfermedades de la base de datos y sus códigos asociados [0.5
                //        puntos]: El programa debe mostrar las enfermedades que contiene la base de
                //        datos, y para cada enfermedad que códigos tiene (y tipo de código).
                
                
                
                
        }


        private void listarSintomasYTiposSemanticos() {
                //        c. Listado de síntomas disponible en la base de datos y sus tipos semánticos
                //        asociados [0.5 puntos]: El programa debe mostrar los síntomas que contiene la
                //        base de datos y sus tipos semánticos asociados.
                
                
                
                
                
                
        }


        private void mostrarEstadisticasBD() {
                // ENUNCIADO
                
                //4. Estadísticas de la base de datos y su contenido. La funcionalidad de estadísticas debe
                //proporcionar [2 puntos]:
                
                //a. Número de enfermedades [0.5 puntos]: Un conteo del número total de
                //enfermedades que hay en la base de datos.
                
                
           //        PreparedStament conteoEn= conn. prepareStatement
                
                ///b. Número de síntomas [0.5 puntos]: Un conteo del número total de síntomas que
                //hay en la base de datos.
                
                //c. Enfermedad con más síntomas, con menos síntomas y número medio de
                //síntomas [0.5 puntos]: Debe indicar cuales son las enfermedades con más y
                //menos síntomas y cuál es el número medio de síntomas asociados a las
                //enfermedades.
                
                
                //d. Tipos de semantic types en los síntomas y distribución de cada semantic type
                //(cuantos síntomas hay de cada semantic type) [0.5 puntos]: Se debe indicar
                //cuales son los semantic types que hay en la base de datos, y cuantos síntomas
                //hay de cada semantic type.
                
                
                
                
                
                
                
        }


        /**
         * Método para leer números enteros de teclado.
         * 
         * @return Devuelve el número leído.
         * @throws Exception
         *             Puede lanzar excepción.
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
        


        public static void main(String args[]) {
                new Diagnostico().showMenu();
                
        }
}