#include "List.h"

List::List()
{
    first = nullptr;
    last = nullptr;
    length = 0;
}

void List::push_back(const unsigned int id, const Uczestnik* uczestnik)
{
    if(last)
    {
        last->next = new Node(id,uczestnik);
        last->next->prev = last;
        last = last->next;
    }
    else
        last = first = new Node(id,uczestnik);
    length++;
}

void List::insert(const unsigned int pozycja, const unsigned int id, const Uczestnik* uczestnik)
{
    Node* node = first;

    for(unsigned int i=pozycja; i>0; --i)
        node = node->next;

    if(node->next)
    {
        Node* ptr = new Node(id,uczestnik);
        ptr->prev = node;
        ptr->next = node->next;
        node->next = node->next->prev = ptr;
        length++;
    }
    else
        push_back(id, uczestnik);
}

void List::pop_back()
{
    if( !empty() )
    {
        Node* ptr = last->prev;
        if(ptr) ptr->next = nullptr;
        else    first = nullptr;
        delete last;
        last = ptr;
        length--;
    }
}

void List::clear()
{
    while(last != first)
        pop_back();
    length = 1;
}

bool List::remove(const Uczestnik* uczestnik)
{
    Node* ptr = first->next;

    while(ptr)
    {
        if(ptr->uczestnik != uczestnik)
            ptr = ptr->next;
        else
        {
            ptr->next->prev = ptr->prev;
            ptr->prev->next = ptr->next;
            length--;
            delete ptr;

            return true;
        }
    }

    return false;
}

bool List::remove(const unsigned int id)
{
    Node* ptr = first->next;

    while(ptr)
    {
        if(ptr->id != id)
            ptr = ptr->next;
        else
        {
            ptr->next->prev = ptr->prev;
            ptr->prev->next = ptr->next;
            length--;
            delete ptr;

            return true;
        }
    }

    return false;
}

const Node* const List::find(const Uczestnik* uczestnik) const
{
    Node* ptr = first->next;

    while(ptr)
    {
        if(ptr->uczestnik != uczestnik)
            ptr = ptr->next;
        else
            return ptr;
    }

    return nullptr;
}

const Node* const List::find(const unsigned int id) const
{
    Node* ptr = first->next;

    while(ptr)
    {
        if(ptr->id != id)
            ptr = ptr->next;
        else
            return ptr;
    }

    return nullptr;
}

bool List::one_gender() const
{
    Node* ptr = first->next;

    if(ptr)
    {
        const Uczestnik::Plec plec = ptr->uczestnik->plec;
        ptr = ptr->next;

        while(ptr)
        {
            if(ptr->uczestnik->plec != plec)
                return false;
        }
    }

    return true;
}

void List::print() const
{
    const Node* ptr = first;
    while(ptr)
    {
        ptr->print();
        std::cout << '\n';
        ptr = ptr->next;
    }
}

void List::print(Uczestnik::Plec p) const
{
    const Node* ptr = last;
    while(ptr)
    {
        ptr->print(p);
        ptr = ptr->prev;
    }
}

