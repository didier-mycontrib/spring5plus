package tp.mySpringBatch.reader.custom;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractPersonGenerator {
	
	protected long dataSetSize=20; //ou 10000 ou autre
	protected long index=0;
	
	//most frequent lastNames list 
	protected List<String> lastNameList = Arrays.asList("Martin" , "Bernard" , "Thomas" , "Petit" , "Robert" , "Richard" , "Dubois" , "Durand" , 
			 "Moreau" , "Laurent" , "Simon" , "Michel" , "Lefebvre" , "Leroy" , "Roux" , "David" , "Bertrand" , "Morel" , "Fournier" , "Girard" ,
			 "Bonnet" , "Dupont" , "Lambert" , "Fontaine" , "Rousseau" , "Vincent" , "Muller" , "Faure" , "Andre" , "Mercier");
	
	//most frequent firstNames list
	protected List<String> firstNameList = Arrays.asList("Gabriel" , "Leo" , "Raphael" , "Mael" , "Louis" , "Noah" , "Jules" , "Arthur" , "Adam",
			"Lucas" , "Liam" , "Sacha" , "Isaac" , "Gabin" , "Eden" , "Hugo" , "Mohamed" , "Leon" , "Paul" , "Nathan" ,
			
			"Jade", "Louise" , "Ambre" , "Emma" , "Rose" , "Alice" , "Romy" , "Anna" , "Mia" , "Lou" , "Juila" , "Chloe" , "Alma" , 
			"Agathe" , "Iris" , "Juliette" , "Lea" , "Victoire" , "Nina" , "Zoe" , "Ines" , "Lucie" , "Lola" , "Alix" 
 			);
	
	//most frequent functions list
	protected List<String> functionList = Arrays.asList("worker" ,"developper" , "accountant" , "manager" );
	
	public AbstractPersonGenerator() {
		super();
	}

	public AbstractPersonGenerator(long dataSetSize) {
		super();
		this.dataSetSize = dataSetSize;
	}
	
	public long getDataSetSize() {
		return dataSetSize;
	}

	public void setDataSetSize(long dataSetSize) {
		this.dataSetSize = dataSetSize;
	}

}
