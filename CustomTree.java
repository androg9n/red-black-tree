package com.javarush.task.task20.task2028;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;

/* 
Построй дерево(1)
*/

public class CustomTree extends AbstractList<String> implements Cloneable,Serializable {
    Entry<String> root;
    int hight;

    public CustomTree() {
        root = new Entry<String>("root");
        hight = 0;
    }

    @Override
    public boolean remove(Object o) {
        if (!(o instanceof String)) throw new UnsupportedOperationException();
        String s = (String) o;
        Entry<String> elementS = find(root, s);
        if (elementS != null && elementS.parent != null) {
            if (elementS.parent.leftChild == elementS) elementS.parent.leftChild = null;
            if (elementS.parent.rightChild == elementS) elementS.parent.rightChild = null;
            return true;
        }
        return false;
    }

    public String getParent(String s) {
        Entry<String> elementS = find(root, s);
        if (elementS != null && elementS.parent != null) {
            return elementS.parent.elementName;
        }
        return null;
    }

    private Entry<String> find(Entry<String> target, String elementName) {
        if (target == null) return null;
        if (target.elementName.equals(elementName)) return target;
        Entry<String> result = find(target.leftChild, elementName);
        if (result != null) return result;
        result = find(target.rightChild, elementName);
        if (result != null) return result;
        return null;
    }


    @Override
    public String get(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return calcSize(root, -1);
    }

    private int calcSize(Entry<String> target, int count) {
        if (target == null) return count;
        count++;
        count = calcSize(target.leftChild, count);
        count = calcSize(target.rightChild, count);
        return  count;
    }

    @Override
    public String set(int index, String element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, String element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(String s) {
        if (add(root, s, hight)) return true;
        if (add(root, s, hight + 1)) {
            hight++;
//            System.out.println("Hight = " + hight);
            return true;
        }
        addAvailability(root, 0);
        if (add(root, s, hight)) return true;
        if (add(root, s, hight + 1)) {
            hight++;
//            System.out.println("Hight = " + hight);
            return true;
        }
        return false;
    }

    private void addAvailability(Entry<String> target, int depth) {
        if (target == null) return;
        if (target.leftChild != null) addAvailability(target.leftChild, ++depth);
            else {
                target.availableToAddLeftChildren = true;
                if (hight > depth) {
                    hight = depth;
//                    System.out.println("Hight = " + hight);
                }
        }
        if (target.rightChild != null) addAvailability(target.rightChild, ++depth);
            else {
                target.availableToAddRightChildren = true;
                if (hight > depth) {
                    hight = depth;
//                    System.out.println("Hight = " + hight);
                }
        }
    }

    private boolean add(Entry<String> target, String s, int depth) {
        if (target == null) return false;
        if (target.availableToAddLeftChildren) {
            target.leftChild = new Entry<String>(s);
            target.leftChild.parent = target;
            target.availableToAddLeftChildren = false;
//            System.out.printf("Add element *%s* leftChild of *%s*%n", s, target.elementName);
            return true;
        }
        if (target.availableToAddRightChildren) {
            target.rightChild = new Entry<String>(s);
            target.rightChild.parent = target;
            target.availableToAddRightChildren = false;
//            System.out.printf("Add element *%s* rightChild of *%s*%n", s, target.elementName);
            return true;
        }
        if (depth > 0) return add(target.leftChild, s, depth - 1) || add(target.rightChild, s, depth - 1);
            else return false;
    }

    @Override
    public String remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends String> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    static class Entry<T> implements Serializable {
        String elementName;
        boolean availableToAddLeftChildren, availableToAddRightChildren;
        Entry<T> parent, leftChild, rightChild;

        public Entry(String elementName) {
            this.elementName = elementName;
            availableToAddLeftChildren = true;
            availableToAddRightChildren = true;
        }

        public boolean isAvailableToAddChildren() {
            return availableToAddLeftChildren || availableToAddRightChildren;
        }

    }
}
