#ifndef WODZIREJ_H
#define WODZIREJ_H

#include "Uczestnik.h"
#include "List.h"

class Wodzirej
{
public:
    Wodzirej();
    ~Wodzirej() { delete wodzirej_ptr; }

    inline bool rozpocznij()
    {  return ( (!czy_rozpoczeta && koleczko.size()!=1) ? koleczko.one_gender() : false );  }
    inline unsigned int liczba() const
    {  return koleczko.size();  }
    inline Uczestnik* wodzirej() const
    {  return const_cast<Uczestnik*>(wodzirej_ptr);  }
    inline bool zrezygnuj(Uczestnik* osoba)
    {  return ( (osoba && osoba!=posiadacz_chusteczki && osoba!=wodzirej_ptr) ? koleczko.remove(osoba) : false );  }
    inline bool zrezygnuj(unsigned int id)
    {  return ( (id!=id_posiadacza && id<kolejne_id && id) ? koleczko.remove(id) : false );  }
    inline void uczestnicy() const
    {  koleczko.print();  }
    inline void uczestnicy(Uczestnik::Plec p) const
    {  koleczko.print(p);  }

    unsigned int dolacz(Uczestnik* osoba);
    unsigned int dolacz(Uczestnik* osoba, unsigned int pozycja);
    bool zakoncz();
    bool przekaz(Uczestnik* osoba);
    bool przekaz(unsigned int id);

private:
    bool czy_rozpoczeta;
    Uczestnik* wodzirej_ptr;
    unsigned int kolejne_id;
    List koleczko;
    Uczestnik* posiadacz_chusteczki;
    unsigned int id_posiadacza;
    Uczestnik::Plec plec_posiadacza;
};

#endif // WODZIREJ_H
