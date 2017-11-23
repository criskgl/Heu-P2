import java.io.*;
import java.util.Scanner;
import org.jacop.core.BooleanVar;
import org.jacop.core.Store;
import org.jacop.jasat.utils.structures.IntVec;
import org.jacop.satwrapper.SatWrapper;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMin;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;
import org.jacop.search.SimpleSelect;
import org.jacop.search.SmallestDomain;


public class FileReader {

	public static void main(String[] args) throws IOException {
		
		Store store = new Store();
		SatWrapper satWrapper = new SatWrapper(); 
		store.impose(satWrapper);					/* Importante: sat problem */
		
		String text = "";
		Scanner input = new Scanner(new File("src\\input.dat"));
		
		System.out.println();	
		
		while(input.hasNext()) {
			 String i = input.next();
	         text += i;
		}
		
		int st = Character.getNumericValue(text.charAt(0)); //number of streets
		int pl = Character.getNumericValue(text.charAt(1)); //number of parking lots per street
		
		text = text.substring(2);
		
		System.out.println(text);

		BooleanVar isEmpty[][] = new BooleanVar[st][pl];
		int cont = 0;
		
		for(int i = 0; i<st; i++) {
			for(int j = 0; j<pl; j++) {
				if(text.charAt(cont) == '_')
					isEmpty[i][j] = new BooleanVar(store, "La posición "+j+" de la calle "+i+" está: Vacía"); 
				else
					isEmpty[i][j] = new BooleanVar(store, "La posición "+j+" de la calle "+i+" está: Ocupada"); 
				cont+=2;
				
				System.out.println(isEmpty[i][j]);
			}
		}
		
		cont = 0;
		
		for(int i = 0; i < st; i++) {
			for(int j = 0; i < pl-1; j++) {
				if(text.charAt(cont)<text.charAt(cont+2))
				
				cont+=2;
			}
		}
		
		/* Creamos las variables binarias */
		BooleanVar x = new BooleanVar(store, "Hay un agente de seguridad en el nodo x");
		BooleanVar y = new BooleanVar(store, "Hay un agente de seguridad en el nodo y");
		BooleanVar z = new BooleanVar(store, "Hay un agente de seguridad en el nodo z");
		BooleanVar w = new BooleanVar(store, "Hay un agente de seguridad en el nodo w");


		/* Todas las variables: es necesario para el SimpleSelect */
		BooleanVar[] allVariables = new BooleanVar[]{x, y, z, w};


		/* Registramos las variables en el sat wrapper */
		satWrapper.register(x);
		satWrapper.register(y);
		satWrapper.register(z);
		satWrapper.register(w);


		/* Obtenemos los literales no negados de las variables */
		int xLiteral = satWrapper.cpVarToBoolVar(x, 1, true);
		int yLiteral = satWrapper.cpVarToBoolVar(y, 1, true);
		int zLiteral = satWrapper.cpVarToBoolVar(z, 1, true);
		int wLiteral = satWrapper.cpVarToBoolVar(w, 1, true);


		/* El problema se va a definir en forma CNF, por lo tanto, tenemos
		   que aÃ±adir una a una todas las clausulas del problema. Cada 
		   clausula serÃ¡ una disjunciÃ³n de literales. Por ello, sÃ³lo
		   utilizamos los literales anteriormente obtenidos. Si fuese
		   necesario utilizar un literal negado, Ã©ste se indica con un
		   signo negativo delante. Ejemplo: -xLiteral */


		/* Aristas */
		/* Por cada arista una clausula de los literales involucrados */
		addClause(satWrapper, xLiteral, yLiteral);		/* (x v y) */
		addClause(satWrapper, xLiteral, zLiteral);		/* (x v z) */
		addClause(satWrapper, yLiteral, zLiteral);		/* (y v z) */
		addClause(satWrapper, yLiteral, wLiteral);		/* (y v w) */
		addClause(satWrapper, zLiteral, wLiteral);		/* (z v w) */


		/* Max agentes */
		addClause(satWrapper, -xLiteral, -yLiteral, -zLiteral);		/* (-x v -y v -z) */
		addClause(satWrapper, -xLiteral, -yLiteral, -wLiteral);		/* (-x v -y v -w) */
		addClause(satWrapper, -xLiteral, -zLiteral, -wLiteral);		/* (-x v -z v -w) */
		addClause(satWrapper, -yLiteral, -zLiteral, -wLiteral);		/* (-y v -z v -w) */


		/* Resolvemos el problema */
	    Search<BooleanVar> search = new DepthFirstSearch<BooleanVar>();
		SelectChoicePoint<BooleanVar> select = new SimpleSelect<BooleanVar>(allVariables,
							 new SmallestDomain<BooleanVar>(), new IndomainMin<BooleanVar>());
		Boolean result = search.labeling(store, select);

		if (result) {
			System.out.println("Solution: ");

			if(x.dom().value() == 1){
				System.out.println(x.id());
			}

			if(y.dom().value() == 1){
				System.out.println(y.id());
			}

			if(z.dom().value() == 1){
				System.out.println(z.id());
			}

			if(w.dom().value() == 1){
				System.out.println(w.id());
			}

		} else{
			System.out.println("*** No");
		}

		System.out.println();
	}


	public static void addClause(SatWrapper satWrapper, int literal1, int literal2){
		IntVec clause = new IntVec(satWrapper.pool);
		clause.add(literal1);
		clause.add(literal2);
		satWrapper.addModelClause(clause.toArray());
	}


	public static void addClause(SatWrapper satWrapper, int literal1, int literal2, int literal3){
		IntVec clause = new IntVec(satWrapper.pool);
		clause.add(literal1);
		clause.add(literal2);
		clause.add(literal3);
		satWrapper.addModelClause(clause.toArray());
	}
}
