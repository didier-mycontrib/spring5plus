package tp.mySpringBatch.listener;

import org.springframework.batch.core.SkipListener;
//import org.springframework.batch.item.ItemReaderException;

import tp.mySpringBatch.model.Person;

//NB: org.springframework.batch.item.file.FlatFileParseException hérite de ItemReaderException
//ItemReaderException hérite de RuntimeException

//NB: il existe aussi l'interface SkipPolicy avec boolean shouldSkip(Throwable throwable, int skipCount)
//NB: ce listener est à enregistrer au niveau chunk ou tasklet ou step (mais pas job)
//NB: SkipListener hérite de StepListener
public class MySkippedErrorsListener  implements SkipListener<Person,Person>{

	@Override
	public void onSkipInRead(Throwable t) {
		System.err.println("SKIPPED_READ_ERROR:" + t.getMessage());
	}

	@Override
	public void onSkipInWrite(Person item, Throwable t) {
		System.err.println("SKIPPED_WRITE_ERROR:" + t.getMessage());
	}

	@Override
	public void onSkipInProcess(Person item, Throwable t) {
		System.err.println("SKIPPED_PROCESS_ERROR:" + t.getMessage());
	}

}
