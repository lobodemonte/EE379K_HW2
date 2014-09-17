package q4;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PSearch implements Callable<Integer> {
    // Declare variables or constructors here;
    // however, they will not be access by TA's test drvier.
	
	//static boolean found = false;
	static int x;
	private int[] A;
	int offset;
	
	public PSearch(int[] A, int offset){
		this.A = A.clone();
		this.offset = offset;
	}
	
	public static int parallelSearch(int x, int[] A, int numThreads) {
        // your search algorithm goes here
		PSearch.x = x;
		int i = -1;
		
		if (numThreads <= 0) throw new IllegalArgumentException();
		if (A.length <= 0) return -1;
		
		int[][] chunks = chunkArray(A, numThreads);
		
		ExecutorService threadPool = Executors.newFixedThreadPool(numThreads);
		List<Future<Integer>> futures = new ArrayList<Future<Integer>>(numThreads);
		int offset = 0;
		
		for (int n = 0; n < numThreads; n++){
			futures.add(threadPool.submit(new PSearch(chunks[n], offset)));
			offset+=chunks[n].length;
			//System.out.println(chunks[n]);
		}
		for(Future<Integer> future : futures){
			try {
				int result = future.get();
				if (result != -1)
					i = result;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			}

		threadPool.shutdown();

		return i; // return -1 if the target is not found
	}

	public Integer call() throws Exception {
        // your algorithm needs to use this method to get results
		//Arrays.sort(A);
		
		int index = 0;
		while (index < A.length){
			if (A[index] == x){
				break;
			}
			index++;
		}
		return (index >= A.length) ? -1 : index+(offset);
	}
	
	//chunkArray method credit to Lesleh from GitHub, with slight modifications to fit purpose
	//https://gist.github.com/lesleh/7724554
    private static int[][] chunkArray(int[] array, int numOfChunks) {
        int[][] output = new int[numOfChunks][];
        int chunkSize = (int)Math.ceil((double)array.length/numOfChunks);
        int offset = 0;
        for (int i = 0; i < numOfChunks; i++){
        	if (array.length - offset < chunkSize){ //if what's going to be left over is less than chunkSize
        		chunkSize = array.length - offset;
        		//System.out.println(chunkSize);
        	}
        	int[] chunk = new int[chunkSize];
        	System.arraycopy(array, offset, chunk, 0, chunkSize);
        	System.out.println(Arrays.toString(chunk));
        	offset += chunk.length;

        	output[i] = chunk;
        }
 /*		
        for(int i = 0; i < numOfChunks; ++i) {
            int start = i * (int)Math.ceil((double)array.length/numOfChunks);
            int length = Math.min(array.length - start, (int)Math.ceil((double)array.length/numOfChunks));
 
            int[] temp = new int[length];
            System.arraycopy(array, start, temp, 0, length);
            output[i] = temp;
        }
 */
        return output;
    }
}
