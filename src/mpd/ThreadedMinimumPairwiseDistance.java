package mpd;

public class ThreadedMinimumPairwiseDistance implements MinimumPairwiseDistance {
    private long absMin = Integer.MAX_VALUE;
    public int[] array;
    public int N;

    @Override
    public long minimumPairwiseDistance(int[] values) {
      Thread[] threads = new Thread[4];
      this.array = values;
      this.N = this.array.length;

      threads[0] = new Thread(new LowerLeft());
      threads[1] = new Thread(new LowerRight());
      threads[2] = new Thread(new TopRight());
      threads[3] = new Thread(new Center());

      for(Thread thread : threads) {
        thread.start();
      }
      try{
    	  for(Thread thread : threads) {
    	  thread.join();
      	}
      } catch(InterruptedException e) {
    	  System.out.println(e);
      }

      return this.absMin;

    }

    private synchronized void putMin(long value) {
      this.absMin = Math.min(absMin, value);
    }

    private class LowerLeft implements Runnable{
      private long leftMin = Integer.MAX_VALUE;

      public LowerLeft() {}

      public void run(){

        for(int j = 0; j < N/2; j++) {
          for(int i = j+1; i < N/2; i++) {
            long diff = Math.abs((long) array[j]-array[i]);
            this.leftMin = Math.min(diff, this.leftMin);
          }
        }

        putMin(this.leftMin);

      }
    }

    private class LowerRight implements Runnable{
      private long rightMin = Integer.MAX_VALUE;

      public LowerRight() {}

      public void run(){

        for(int i = (int) Math.floor(N/2); i < N/2; i++) {
          for(int j = 0; j+N/2 < i; j++) {
            long diff = Math.abs((long) array[j]-array[i]);
            this.rightMin = Math.min(diff, this.rightMin);
          }
        }

        putMin(this.rightMin);

      }
    }

    private class TopRight implements Runnable{
      private long topRightMin = Integer.MAX_VALUE;

      public TopRight() {}

      public void run(){

        for(int j = (int) Math.floor(N/2); j < N-1; j++) {
          for(int i = j+1; i < N; i++) {
            long diff = Math.abs((long) array[j]-array[i]);
            this.topRightMin = Math.min(diff, this.topRightMin);
          }
        }

        putMin(this.topRightMin);

      }
    }

    private class Center implements Runnable{
      private long centerMin = Integer.MAX_VALUE;

      public Center() {}

      public void run(){

        for(int j = 0; j < N/2; j++) {
          for(int i = (int) Math.floor(N/2); i < j+N/2; i++) {
            long diff = Math.abs((long) array[j]-array[i]);
            this.centerMin = Math.min(diff, centerMin);
          }
        }

        putMin(this.centerMin);

      }
    }

}
