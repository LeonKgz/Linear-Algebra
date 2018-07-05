public class MyMath {



    public static int getSignificants(Integer val) {// i.e getSignificants(-139) returns 3
        /*int ret = 1;
        Integer absVal = Math.abs(val);
        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            if (absVal >= Math.pow(10, i)){
                ret ++;
            } else {
                return ret;
            }
        }
        return ret;
        */
        return (int) Math.log10(val) + 1;
    }



}
