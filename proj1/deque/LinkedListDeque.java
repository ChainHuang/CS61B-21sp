package deque;

public class LinkedListDeque<type> {
    public class IntNode{
        type item;
        IntNode next;
        IntNode prev;
        public IntNode(type x, IntNode n, IntNode p){
            item = x;
            next = n;
            prev = p;
        }
        public type getRecursiveIntNode(int index){
            if(index == 0)
                return this.item;
            else
                return this.next.getRecursiveIntNode(index -1);
        }
    }

    private IntNode sentinel;
    private int size;
    /* create a list  --constructors */
    public LinkedListDeque(){
        sentinel = new IntNode(null, sentinel, sentinel);//sentinel node 's item value doesn't matter.
       sentinel.next = sentinel;//⚠️sentinel，item, next,prev默认为null, 若没有这两句则sentinel的next,prev都是null,
       sentinel.prev = sentinel;//so, the first code can be new IntNode(null, null, null),is the same;
        size = 0;
    }//create an empty list;
    public LinkedListDeque(type x){
        sentinel = new IntNode(null, sentinel, sentinel);
        sentinel.next = new IntNode(x, sentinel, sentinel);
        sentinel.prev = sentinel.next;
        size = 1;
    }//
    public  LinkedListDeque(LinkedListDeque<type> other){
       /* LinkedListDeque<type> newLink = new LinkedListDeque();*/
        this.sentinel = new IntNode(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
        for(int i = 0; i < other.size(); i++){
            addLast(other.get(i));
        }
    }//
    public void addFirst(type x){
        sentinel.next  = new IntNode(x, sentinel.next, sentinel);//instansiation and assignment,seeing notebook
        sentinel.next.next.prev = sentinel.next;
        size++;
    }
    public void addLast(type x){
        sentinel.prev = new IntNode(x, sentinel, sentinel.prev);//instansiation and assignment,seeing notebook
        sentinel.prev.prev.next = sentinel.prev;
        size++;
    }
    public int size(){ return size;}
    public  boolean isEmpty(){
        if(sentinel.next == sentinel || sentinel.prev == sentinel)
            return true;
        else
            return false;
    }
    public void printDeque(){
        IntNode p = sentinel;
        for(int i = 1; i <= size; i++)
        {
            p = p.next;
            if(i < size )
                System.out.print(p.item + " ");
            else
                System.out.println(p.item);
        }
    }
    /*remove node and return the removed node's item value */
    public type removeFirst(){
        if(this.isEmpty())//⚠️考虑万一是空链表
            return null;
        size --;
        IntNode p = sentinel.next;
        sentinel.next  = sentinel.next.next;
        sentinel.next.prev = sentinel;//要不要释放内存：没有reference指向的内存会被java清理掉
        return p.item;
    }
    public type removeLast(){
        if(this.isEmpty())//⚠️万一是空链表
            return null;
        size --;
        IntNode p = sentinel.prev;
        sentinel.prev = sentinel.prev.prev;//顺序不能换
        sentinel.prev.next= sentinel;//
        return p.item;
    }
    public type get(int index){
        if(this.isEmpty())
            return null;
        IntNode p = sentinel;
        for(int i = 0; i < size; i++){
            p = p.next;
            if(i == index)
                return p.item;
        }
        return null;
    }

    public type getRecursive(int index){
        if(this.isEmpty())
            return null;
        return sentinel.next.getRecursiveIntNode(index);//借助了helper method in IntNode class;
    }
}
