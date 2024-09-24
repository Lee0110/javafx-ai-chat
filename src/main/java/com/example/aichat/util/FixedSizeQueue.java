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

  public void add(E element) {
    synchronized (queue) {
      if (queue.size() == maxSize) {
        queue.remove(0);
      }
      queue.add(element);
    }
  }

  public void clear() {
    synchronized (queue) {
      queue.clear();
    }
  }

  public List<E> toList() {
    synchronized (queue) {
      return new ArrayList<>(queue);
    }
  }
}