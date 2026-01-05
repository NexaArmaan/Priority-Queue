import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

class Store{
    String owner;
    int sales;

    public Store(){
        this.owner = "";
        this.sales = 0;
    }

    public Store(String owner, int sales) {
        this.owner = owner;
        this.sales = sales;
    }

    public Store(Store s){
        this.owner = s.owner;
        this.sales = s.sales;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public Store copyInstance(){
        return new Store(this);
    }

    @Override
    public boolean equals(Object o){
        if (this == o)
            return true;
        if (!(o instanceof Store))
            return false;
        Store s = (Store) o;
        return Objects.equals(this.owner, s.owner);
    }
}

interface StorePriorityQueue{
    void add(Store a);
    Store getHighestSalesStore();
    void clear();
    int getSize();
    boolean isEmpty();
}

class PriorityQueueDLLDummy implements StorePriorityQueue{

    private static final class Node {
        Store data;
        Node prev, next;
        Node(Store data){
            this.data = data;
        }
    }

    private final Node dummy;
    private int size;

    public PriorityQueueDLLDummy(){
        dummy = new Node(null);
        dummy.next = dummy;
        dummy.prev = dummy;
        size = 0;
    }

    public PriorityQueueDLLDummy(PriorityQueueDLLDummy other) {
        this();
        if (other != null) {
            for (Node current = other.dummy.next; current != other.dummy; current = current.next) {
                insertBefore(dummy, current.data.copyInstance());
            }
            this.size = other.size;
        }
    }

    public PriorityQueueDLLDummy copyInstance() {
        return new PriorityQueueDLLDummy(this);
    }

    private void insertBefore(Node ref, Store data) {
        Node n = new Node(data);
        n.prev = ref.prev;
        n.next = ref;
        ref.prev.next = n;
        ref.prev = n;
        size++;
    }

    private void insertAfter(Node ref, Store data) {
        Node n = new Node(data);
        n.next = ref.next;
        n.prev = ref;
        ref.next.prev = n;
        ref.next = n;
        size++;
    }

    @Override
    public void add(Store a) {
        insertAfter(dummy, a);
    }

    @Override
    public Store getHighestSalesStore() {
        if (isEmpty())
            return null;
        Node maxNode = dummy.next;
        for (Node current = maxNode.next; current != dummy; current = current.next) {
            if (current.data.getSales() > maxNode.data.getSales()) {
                maxNode = current;
            }
        }

        Store result = maxNode.data;
        maxNode.prev.next = maxNode.next;
        maxNode.next.prev = maxNode.prev;
        size--;

        return result;
    }

    @Override
    public void clear() {
        Node current = dummy.next;
        while (current != dummy) {
            Node nxt = current.next;
            current.prev = current.next = null;
            current.data = null;
            current = nxt;
        }
        dummy.next = dummy;
        dummy.prev = dummy;
        size = 0;
    }

    @Override
    public int getSize(){
        return size;
    }

    @Override
    public boolean isEmpty(){
        return size == 0;
    }
}

public class Main {
    public static void main(String[] args) {
        String path = "stores.txt";

        PriorityQueueDLLDummy pq = new PriorityQueueDLLDummy();

        int loaded = loadSimple(path, pq);
        System.out.println("Loaded: " + loaded + " stores");
        System.out.println("Size now: " + pq.getSize());
        System.out.println();

        PriorityQueueDLLDummy copyCtr = new PriorityQueueDLLDummy(pq);
        PriorityQueueDLLDummy copyMeth = pq.copyInstance();
        System.out.println("Is copyCtr size = original? " + (copyCtr.getSize() == pq.getSize()));
        System.out.println("Is copyInstance size = original? " + (copyMeth.getSize() == pq.getSize()));
        System.out.println();

        System.out.println("Highest → Lowest:");
        int rank = 1;
        while (!pq.isEmpty()) {
            Store s = pq.getHighestSalesStore();
            System.out.printf("%4d) %-25s sales=%d%n", rank++, s.getOwner(), s.getSales());
        }
        System.out.println("\nEmpty? " + pq.isEmpty());
    }

    private static int loadSimple(String path, PriorityQueueDLLDummy pq) {
        int count = 0;
        try (Scanner sc = new Scanner(new File(path))) {
            while (sc.hasNextLine()) {
                String owner = sc.nextLine().trim();
                if (owner.isEmpty())
                    continue;
                if (!sc.hasNextLine())
                    break;
                String salesLine = sc.nextLine().trim();
                int sales = Integer.parseInt(salesLine);
                pq.add(new Store(owner, sales));
                count++;
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + path);
        } catch (Exception e) {
            System.err.println("Load error: " + e.getMessage());
        }
        return count;
    }
}