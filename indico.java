import io.indico.Indico;
import io.indico.api.IndicoResult;
import io.indico.api.BatchIndicoResult;

public class indico {

    public static void main(String[] args) {
	// single example
	Indico indico = new Indico("fece1b6e9b05c702b93e7723de9b5298");
	IndicoResult single = indico.sentiment.predict("indico is so easy to use!");
	Double result = single.getSentiment();
	System.out.println(result);
	
	// batch example
	String[] example = {
	    "indico is so easy to use!", 
	    "everything is awesome!"
	};
	BatchIndicoResult multiple = indico.sentiment.predict(example);
	List<Double> results = multiple.getSentiment();
	System.out.println(results);
    }
}
