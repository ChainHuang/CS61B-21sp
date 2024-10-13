package deque;

import java.util.Arrays;

//circular double array
//invariants:
//1.size == 0 means empty array;
//2.nextFirst ,nextLast means the item to be insert next in the front or the end;
//so, the first item is items[(nextFirst+1)%items.length]
// and the last item is items[(nextLast-1+8)%items.length]
//3.size == items.length() means full ;
//if nextFirst/nextLast need to -1 then should ( x -1 + 8) % 8 for 0-1 == 7
public class ArrayDeque<type> {
    private type[] items;
    private int size;
    private int nextFirst;
    private int nextLast;
    public ArrayDeque(){
        items = (type[]) new Object[8];
        size = 0;
        nextFirst = 0;//
        nextLast = 1;//0-7 is all OK
    }//create empty arrry deque;
    public ArrayDeque(ArrayDeque<type> other){
        size = other.size();
        nextFirst = other.nextFirst;
        nextLast = other.nextLast;
        this.items = (type[]) new Object[other.items.length];
        /*int start = (other.nextFirst+1)%other.items.length;
        /System.arraycopy(other.items, start, this.items,  start, size);虽然无法循环，
        🎉但是ArrayDeque元素只需nextFirst, size即可打印正确的conceptual array(用户要的/呈现给用户的）
        实际内存里元素的实际存放顺序可和它不同，因此可以直接原原本本复制整个items(即元素对应的索引也一样） */
        System.arraycopy(other.items, 0, this.items, 0, other.items.length);
        /*int cnt = 0;
        int i = (nextFirst+1) % other.items.length;
        while(cnt < size){
            items[i] = other.get(i);
            cnt ++;
            i = (i+1) % 8;
        }*/
    }
    /* 新new一长度为capacity的数组内存 并将this.items指向的数组复制过去,然后items指向它,并更新this.nextFirst /nextLast*/
   public void resize(int capacity){
        type[] newItems = (type[]) new Object[capacity];
        /* ⚠️这里就不能用arraycopy直接原原本本的复制过去了
        有3种复制方法，详见ppt,这里选择第二种往后移一位，从1开始放conceptual 数组元素，方便nextFirst更新为0*/
       int i = 1;
       int start = (nextFirst +1) % items.length;
       while(i <= size){//i==1说明已经放了0个，i==size说明已经放了size-1个，得再进入循环放元素，因此i <= size
            newItems[i] = items[start];
            start = (start + 1) % items.length;
            i++;
       }
       items = newItems;
       nextFirst = 0;
       nextLast = i;
   }
    public void addLast(type x){
        if(size == items.length)
            resize(size * 2);
        items[nextLast] = x;
        size++;
        nextLast = (nextLast + 1) % items.length;
    }
    public void addFirst(type x){
         if(size == items.length)
            resize(size * 2);
        items[nextFirst] = x;
        size ++;
        nextFirst = (nextFirst + -1 + items.length) % items.length;//0-1 -> 7
    }
    public boolean isEmpty(){
        if(size == 0)
            return true;
        else
            return false;
    }
    public int size(){ return size;}
    /* 因为nextFirst nextLast谁大水小不定，所以不能用 (nextFirst...)<i < (nextLast...)作为print的限制条件（比如当full时会出错）
    我们利用size和nextFirst */
    public void printDeque(){
       int cnt = 0;
       int i = (nextFirst+1) % items.length;
       while(cnt < size) {//cnt=0说明一个没打印，=1说明已经打印了一个。。。=size说明已经打印了size个，不应该进入循环打印了，
           if (cnt < size-1)
               System.out.print(items[i] + " ");
           if(cnt == size-1)
               System.out.println(items[i]);
           i = (i+1) % items.length;
           cnt ++;//等价为for(int cnt = 0; cnt < size ; cnt ++){} 先执行循环体内的打印，再cnt++
       }
    }
    //
    public type removeFirst(){
        if(size == 0)
            return null;
        else{
            size --;
            type x = items[(nextFirst+1) % items.length];
            items[(nextFirst+1) % items.length] = null;//将移除的设置为null,对于非9大原始类型可以节省内存，对于原始类型可以简化get()方法
            nextFirst = (nextFirst +1) % items.length;//非原始类型，如果丢失reference，java的garbage collector会自动释放内存。
            return x;
        }
    }
    public type removeLast(){
        if(size == 0)
            return null;
        else{
            size --;
            type x  = items[(nextLast-1+items.length) % items.length];
            items[(nextLast-1+items.length) % items.length] = null;//
            nextLast  = (nextLast -1 + items.length) % items.length;
            return x;
        }
    }
    public type get(int index){//???有什么实际用处？(实际存放和conceptual的顺序不一致
        if(size == 0 )
            return null;
        return items[index];
    }

}
