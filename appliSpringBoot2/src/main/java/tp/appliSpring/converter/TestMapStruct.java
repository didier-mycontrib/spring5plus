package tp.appliSpring.converter;

public class TestMapStruct {

	public static void main(String[] args) {
		ClassXy xyObj = new ClassXy(1,"labelXy");
		RecordXy xyRecord = MyMapStructMapper.INSTANCE.classXyToRecordXy(xyObj);
        System.out.println("xyRecord="+xyRecord);
	}

}
