package deque;

import org.junit.Test;
import static org.junit.Assert.*;
public class ArrayDequeTest {
    @Test
    public void  arrayDequeTest() {
        ArrayDeque<String> a1 = new ArrayDeque<>();
        assertTrue("A newly initialized ADeque should be empty", a1.isEmpty());
        a1.addLast("last1");
        a1.addFirst("first1");
        a1.addFirst("first2");
        a1.addLast("last2");
        a1.addLast("last3");
        a1.addLast("last4");
        a1.addFirst("first3");
        a1.addFirst("first4");//should be  [first4 first3 first2 first1 last1 last2 last3 last4]
        /*System.out.println(" addTest Printing out deque: ");
        a1.printDeque();*/
        ArrayDeque<String> a2 = new ArrayDeque<>(a1);
        System.out.println("arrayCopy Constructor Test, the a2 :");
        a2.printDeque();
        a2.addLast("last 5");
        a2.addFirst("first5");////should be [first5 xxxxx last5]
        System.out.println(" resize Test, the a2 :");
        a2.printDeque();
        System.out.println("the size of the a2:" + a2.size());//should be 10

        /*
        a1.removeFirst();
        a1.removeLast();
        a1.removeFirst();//should be  [ first2 first1 last1 last2 last3 ]
        a1.removeFirst();
        a1.removeFirst();
        a1.removeFirst();
        a1.removeFirst();
        a1.removeFirst();//empty;
        a1.removeFirst();
        System.out.println(" removeTest Printing out deque: ");
        a1.printDeque(); */
    }
    public void arrayCopyTest(){
    }
}
