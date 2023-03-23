#ifndef LIST_H
#define LIST_H

#include <iostream>
#include "Node.h"

class List
{
    Node* first;
    Node* last;
    size_t length;
public:
    List();
    ~List() { clear(); }

    inline const Uczestnik* front() const { return first->uczestnik; }
    inline const Uczestnik* back() const { return last->uczestnik; }
    inline size_t size() const { return length; }
    inline bool empty() const { return !(static_cast<bool>(length)); }

    void push_back(const unsigned int id, const Uczestnik*);
    void insert(const unsigned int pozycja, const unsigned int id, const Uczestnik*);
    void pop_back();
    void clear();

    bool remove(const Uczestnik*);
    bool remove(const unsigned int id);
    const Node* const find(const Uczestnik*) const;
    const Node* const find(const unsigned int id) const;
    bool one_gender() const;
    void print() const;
    void print(Uczestnik::Plec) const;
};

#endif // LIST_H
