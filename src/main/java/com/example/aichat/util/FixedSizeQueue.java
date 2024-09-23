package com.example.aichat.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class FixedSizeQueue<E> {
  private final int maxSize;
  private final List<E> queue;

  public FixedSizeQueue(int size) {
    this.maxSize = size;
    this.queue = Collections.synchronizedList(new LinkedList<>());
  }

  public synchronized void add(E element) {
    if (queue.size() == maxSize) {
      queue.remove(0);
    }
    queue.add(element);
  }

  public synchronized void clear() {
    queue.clear();
  }

  public synchronized List<E> toList() {
    List<E> list = new ArrayList<>();
    Collections.copy(list, queue);
    return list;
  }
}
