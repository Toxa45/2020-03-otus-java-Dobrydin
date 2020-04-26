import java.util.*;

public class DIYarrayList<T> implements List<T> {
    private Object[] elements;
    private static final int DEF_CAPACITY = 10;
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 2;
    private int size;
    private int countElement;

    public DIYarrayList() {
        this.elements = new Object[0];
        this.size = 0;
    }

    public void ObjectInitialization() {
        this.size = this.elements.length;
        if (this.size == 0) {
            this.elements = new Object[0];
        }
    }

    public DIYarrayList(T[] elements) {
        this.elements = elements.clone();
        ObjectInitialization();
    }

    public DIYarrayList(Collection<? extends T> collection) {
        this.elements = collection.toArray();
        ObjectInitialization();
    }

    public DIYarrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            elements = new Object[initialCapacity];
        } else {
            if (initialCapacity != 0) {
                throw new IllegalArgumentException("Размер массива не может быть отрицательным! Capacity: " + initialCapacity);
            }
            this.elements = new Object[0];
        }
        this.size = 0;
    }

    private Object[] increaseArray() {
        return this.increaseArray(this.size + 1);
    }

    private Object[] increaseArray(int minCapacity) {
        return this.elements = Arrays.copyOf(this.elements, this.newCapacity(minCapacity));
    }

    private int newCapacity(int minCapacity) {
        int oldCapacity = this.elements.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity <= 0) {
            if (oldCapacity == 0) {
                return Math.max(DEF_CAPACITY, minCapacity);
            } else if (minCapacity < 0) {
                throw new OutOfMemoryError();
            } else {
                return minCapacity;
            }
        } else {
            return newCapacity - MAX_ARRAY_SIZE <= 0 ? newCapacity : validateCapacity(minCapacity);
        }
    }

    T elements(int index) {
        return (T) this.elements[index];
    }

    private static int validateCapacity(int minCapacity) {
        if (minCapacity < 0) {
            throw new OutOfMemoryError();
        } else {
            return minCapacity > MAX_ARRAY_SIZE ?
                    Integer.MAX_VALUE :
                    MAX_ARRAY_SIZE;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
         throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<T> iterator() {
        return new DIYarrayList.ListItr(0);
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(this.elements, this.size);
    }

    @Override
    public <E> E[] toArray(E[] es) {
        if (es.length < this.size) {
            return (E[]) Arrays.copyOf(this.elements, this.size, es.getClass());
        } else {
            System.arraycopy(this.elements, 0, es, 0, this.size);
            if (es.length > this.size) {
                es[this.size] = null;
            }

            return es;
        }
    }


    @Override
    public boolean add(T t) {
        if (this.size == this.elements.length) {
            increaseArray();
        }
        this.elements[this.size] = t;
        this.size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int i, Collection<? extends T> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public T get(int index) {
        if (index >= this.size || index < 0) return null;
        return this.elements(index);
    }

    @Override
    public T set(int index, T element) {
        if(index<0 || this.size==0) return null;
        if(index>=this.size) index=this.size-1;
        Objects.checkIndex(index, this.size);
        T oldValue = this.elements(index);
        this.elements[index] = element;
        return oldValue;
    }

    @Override
    public void add(int index, T element) {
        if (index > this.size || index < 0) {
            throw new IndexOutOfBoundsException("Error! index="+index);
        }
        int s=this.size;
        if (this.size == this.elements.length) {
            increaseArray();
        }
        System.arraycopy(elements, index, elements, index + 1, s - index);
        elements[index] = element;
        this.size = s + 1;
    }

    @Override
    public T remove(int index) {
        if (index >= this.size || index < 0) return null;
        Object[] es = this.elements;
        T oldValue = (T) es[index];
        this.size--;
        if (this.size > index) {
            System.arraycopy(es, index + 1, es, index, this.size - index);
        }
        es[this.size] = null;
        return oldValue;
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator() {
        return new DIYarrayList.ListItr(0);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new DIYarrayList.ListItr(index);
    }

    @Override
    public List<T> subList(int i, int i1) {
        throw new UnsupportedOperationException();
    }

    private class ListItr implements ListIterator<T> {
        private int cursorIndex = 0;
        private int lastIndex = -1;

        ListItr(int index) {
            super();
            this.cursorIndex = index;
        }
        @Override
        public boolean hasNext() {
            return cursorIndex < size;
        }

        @Override
        public T next() {
            if (cursorIndex >= DIYarrayList.this.size) {
                throw new NoSuchElementException();
            } else {
                    this.lastIndex = this.cursorIndex;
                    this.cursorIndex++;
                    return elements(this.lastIndex );
            }
        }

        @Override
        public boolean hasPrevious() {
            return cursorIndex -1 < size && cursorIndex >0 ;
        }

        @Override
        public T previous() {
            int index = this.cursorIndex - 1;
            if (index < 0) {
                throw new NoSuchElementException();
            } else {
                if (index >= elements.length) {
                    throw new ConcurrentModificationException();
                } else {
                    this.cursorIndex = index;
                    this.lastIndex = index;
                    return elements(this.lastIndex);
                }
            }
        }

        @Override
        public int nextIndex() {
            return cursorIndex;
        }

        @Override
        public int previousIndex() {
            return cursorIndex -1;
        }

        @Override
        public void remove() {
            if (this.lastIndex < 0) {
                throw new IllegalStateException();
            } else {
                try {
                    DIYarrayList.this.remove(this.lastIndex);
                    this.cursorIndex = this.lastIndex;
                    this.lastIndex=-1;
                } catch (IndexOutOfBoundsException var2) {
                    throw new ConcurrentModificationException();
                }
            }
        }

        @Override
        public void set(T t) {
            if (this.lastIndex < 0) {
                throw new IllegalStateException();
            } else {
                try {
                    DIYarrayList.this.set(this.lastIndex,t);
                } catch (IndexOutOfBoundsException var2) {
                    throw new ConcurrentModificationException();
                }
            }
        }

        @Override
        public void add(T t) {
            if (this.cursorIndex < 0) {
                this.cursorIndex = 0;
            }
            else {
                try {
                    DIYarrayList.this.add(this.cursorIndex,t);
                    this.cursorIndex++;
                    this.lastIndex = -1;
                } catch (IndexOutOfBoundsException var2) {
                    throw new ConcurrentModificationException();
                }
            }
        }
    }
}
