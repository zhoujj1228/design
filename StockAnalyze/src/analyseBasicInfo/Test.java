package analyseBasicInfo;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Test {
	public static void main(String[] args){
		Test test = new Test();
		test.call();
	}

	private void call() {
		Long datetime = 1498752000000L;
		Date date2 = new Date(datetime);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		System.out.println(sdf.format(date2));
	}
}
