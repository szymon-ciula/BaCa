#ifndef NODE_H
#define NODE_H

#include <iostream>
#include "Uczestnik.h"

struct Node
{
    Node* prev;
    Node* next;
    unsigned int id;
    Uczestnik* uczestnik;

    Node(unsigned int idd, const Uczestnik* u) : prev{nullptr}, next{nullptr}, id{idd}, uczestnik{u}   {}

    inline void print() const   { std::cout << "plec: " << uczestnik->plec << ", nr: " << id; }

    void print(Uczestnik::Plec p) const
    {
        if(uczestnik->plec == p)
            std::cout << "nr: " << id << '\n';
    }
};

#endif // NODE_H
