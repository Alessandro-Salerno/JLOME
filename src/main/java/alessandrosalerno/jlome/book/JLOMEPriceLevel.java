package alessandrosalerno.jlome.book;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;

import alessandrosalerno.jlome.order.JLOMEBookableTradeOrder;

public final class JLOMEPriceLevel implements Collection<JLOMEBookableTradeOrder> {
    private static final class ListEntry {
        JLOMEBookableTradeOrder data;
        ListEntry prev;
        ListEntry next;
    };

    // TODO: use long open hash map from fastutils
    private Map<Long, ListEntry> fastMap;
    private ListEntry head;
    private ListEntry tail;
    private long modCount;
    private int numElems;

    // Invariant: head = null <=> tail = null
    // head != null => (head == tail <=> list has one element)

    public JLOMEPriceLevel() {
        this.fastMap = new HashMap<>();
        this.head = null;
        this.tail = null;
        this.modCount = 0;
        this.numElems = 0;
    }

    @Override
    public boolean add(JLOMEBookableTradeOrder order) {
        ListEntry e = new ListEntry();
        e.data = order;

        if (this.tail == null) {
            this.head = this.tail = e;
        } else {
            e.prev = this.tail;
            this.tail.next = e;
            this.tail = e;
        }

        this.fastMap.put(order.getOrderId(), e);
        this.modCount++;
        this.numElems++;
        return true;
    }

    @Override
    public void clear() {
        this.head = null;
        this.tail = null;
        this.fastMap.clear();
        this.modCount++;
        this.numElems = 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o instanceof JLOMEBookableTradeOrder order) {
            return this.fastMap.containsKey(order.getOrderId());
        }

        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (o instanceof JLOMEBookableTradeOrder order &&
                    this.fastMap.containsKey(order.getOrderId())) {
                continue;
            }
            return false;
        }

        return true;
    }

    @Override
    public boolean isEmpty() {
        return this.numElems == 0;
    }

    @Override
    public Iterator<JLOMEBookableTradeOrder> iterator() {
        return new Iterator<JLOMEBookableTradeOrder>() {
            private ListEntry cursor = JLOMEPriceLevel.this.head;
            private long modCountStart = JLOMEPriceLevel.this.modCount;

            @Override
            public boolean hasNext() {
                if (this.modCountStart != JLOMEPriceLevel.this.modCount) {
                    throw new ConcurrentModificationException();
                }

                return this.cursor != null;
            }

            @Override
            public JLOMEBookableTradeOrder next() {
                if (this.modCountStart != JLOMEPriceLevel.this.modCount) {
                    throw new ConcurrentModificationException();
                }

                if (this.cursor == null) {
                    throw new NoSuchElementException();
                }

                ListEntry r = this.cursor;
                this.cursor = this.cursor.next;
                return r.data;
            }
        };
    }

    @Override
    public boolean remove(Object o) {
        if (!(o instanceof JLOMEBookableTradeOrder)) {
            return false;
        }

        JLOMEBookableTradeOrder order = (JLOMEBookableTradeOrder) o;
        ListEntry e = this.fastMap.get(order.getOrderId());

        if (e == null) {
            return false;
        }

        if (e.next != null) {
            e.next.prev = e.prev;
        } else {
            this.tail = e.prev;
        }

        if (e.prev != null) {
            e.prev.next = e.next;
        } else {
            this.head = e.next;
        }

        this.fastMap.remove(order.getOrderId());
        this.modCount++;
        this.numElems--;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            modified |= this.remove(o);
        }
        return modified;
    }

    @Override
    public int size() {
        return this.numElems;
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[this.size()];

        int i = 0;
        for (JLOMEBookableTradeOrder o : this) {
            arr[i++] = o;
        }

        return arr;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof JLOMEPriceLevel)) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        JLOMEPriceLevel other = (JLOMEPriceLevel) obj;

        if (this.size() != other.size()) {
            return false;
        }

        Iterator<JLOMEBookableTradeOrder> itThis = this.iterator();
        Iterator<JLOMEBookableTradeOrder> itOther = other.iterator();

        while (itThis.hasNext() && itOther.hasNext()) {
            if (!itThis.next().equals(itOther.next())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int h = 1;
        for (JLOMEBookableTradeOrder o : this) {
            h = 31 * h + o.hashCode();
        }
        return h;
    }

    @Override
    public boolean addAll(Collection<? extends JLOMEBookableTradeOrder> c) {
        boolean modified = false;
        for (JLOMEBookableTradeOrder o : c) {
            modified |= this.add(o);
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'retainAll'");
    }

    @Override
    public <T> T[] toArray(T[] arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toArray'");
    }
}
