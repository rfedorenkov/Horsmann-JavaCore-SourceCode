package corejava.v1ch09.circularArrayQueue;

import java.util.*;

/**
 * Эта программа демонстрирует, как расширить структуру коллекций
 * @version 1.21 2012-01-26
 * @author Cay Horstmann
 */
public class CircularArrayQueueTest {
    public static void main(String[] args) {
        Queue<String> q = new CircularArrayQueue<>(5);
        q.add("Amy");
        q.add("Bob");
        q.add("Carl");
        q.add("Deedee");
        q.add("Emile");
        q.remove();
        q.add("Fifi");
        q.remove();
        for (String s : q) System.out.println(s);
    }
}

/**
 * Первый пришел, первый обслужен.
 * Ограниченная коллекция в порядке поступления.
 */
class CircularArrayQueue<E> extends AbstractQueue<E> {
    private Object[] elements;
    private int head;
    private int tail;
    private int count;
    private int modcount;

    /**
     * Создает пустую очередь.
     * @param capacity Максимальная вместимость очереди
     */
    public CircularArrayQueue(int capacity) {
        elements = new Object[capacity];
        count = 0;
        head = 0;
        tail = 0;
    }


    @Override
    public boolean offer(E newElement) {
        assert newElement != null;
        if (count < elements.length) {
            elements[tail] = newElement;
            tail = (tail + 1) % elements.length;
            count++;
            modcount++;
            return true;
        } else
            return false;
    }

    @Override
    public E poll() {
        if (count == 0) return null;
        E r = peek();
        head = (head + 1) % elements.length;
        count--;
        modcount++;
        return r;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E peek() {
        if (count == 0) return null;
        return (E) elements[head];
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public Iterator<E> iterator() {
        return new QueueIterator();
    }

    private class QueueIterator implements Iterator<E> {
        private int offset;
        private int modcountAtConstruction;

        public QueueIterator() {
            modcountAtConstruction = modcount;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            E r = (E) elements[(head + offset) % elements.length];
            offset++;
            return r;
        }

        @Override
        public boolean hasNext() {
            if (modcount != modcountAtConstruction)
                throw new ConcurrentModificationException();
            return offset < count;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}