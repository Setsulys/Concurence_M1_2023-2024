package fr.uge.exam.exo3;

public class MaxRecorderMain {

	public static void main(String[] args) {
//		var recorder = new MaxRecorderOne();
//		System.out.println(recorder.process(1)); // => true   
//		System.out.println(recorder.process(2)); // => true
//		System.out.println(recorder.process(0)); // => false
//		System.out.println(recorder.process(5)); // => true
//		System.out.println(recorder.process(4)); // => false
//		System.out.println(recorder.process(7)); // => true
		var recorderLF = new MaxRecorderLockFree(2);
		System.out.println(recorderLF.process(1)); // => true   
		System.out.println(recorderLF.process(2)); // => true
		System.out.println(recorderLF.process(0)); // => false
		System.out.println(recorderLF.process(5)); // => true
		System.out.println(recorderLF.process(4)); // => true
		System.out.println(recorderLF.process(3)); // => false
	}
}