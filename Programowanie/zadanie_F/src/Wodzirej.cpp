#include "Wodzirej.h"

Wodzirej::Wodzirej() : wodzirej_ptr{new Uczestnik(Uczestnik::W)}, posiadacz_chusteczki{wodzirej_ptr}
{
    czy_rozpoczeta = false;
    id_posiadacza = 0;
    plec_posiadacza = Uczestnik::W;
    kolejne_id = 1;
    koleczko.push_back(0, wodzirej_ptr);
}

unsigned int Wodzirej::dolacz(Uczestnik* osoba)
{
    if( osoba && (osoba->plec != Uczestnik::W) && !(static_cast<bool>(koleczko.find(osoba))) )
    {
        koleczko.push_back(kolejne_id,osoba);
        return kolejne_id++;
    }

    return 0;
}

unsigned int Wodzirej::dolacz(Uczestnik* osoba, unsigned int pozycja)
{
    if( osoba && (osoba->plec != Uczestnik::W) && (pozycja>=0 && pozycja<koleczko.size()) && !(static_cast<bool>(koleczko.find(osoba))) )
    {
        koleczko.insert(pozycja,kolejne_id,osoba);
        return kolejne_id++;
    }

    return 0;
}

bool Wodzirej::zakoncz()
{
    if(czy_rozpoczeta)
    {
        czy_rozpoczeta = false;
        posiadacz_chusteczki = wodzirej_ptr;
        id_posiadacza = 0;
        plec_posiadacza = Uczestnik::W;
        koleczko.clear();
        return true;
    }

    return false;
}

bool Wodzirej::przekaz(Uczestnik* osoba)
{
    if(czy_rozpoczeta && osoba!=wodzirej_ptr && osoba)
    {
        const Node* const ptr = koleczko.find(osoba);
        if(ptr && (ptr->uczestnik->plec != plec_posiadacza))
        {
            posiadacz_chusteczki = ptr->uczestnik;
            plec_posiadacza = posiadacz_chusteczki->plec;
            id_posiadacza = ptr->id;
            return true;
        }
    }

    return false;
}

bool Wodzirej::przekaz(unsigned int id)
{
    if(czy_rozpoczeta && id<kolejne_id && id)
    {
        const Node* const ptr = koleczko.find(id);
        if(ptr && (ptr->uczestnik->plec != plec_posiadacza))
        {
            posiadacz_chusteczki = ptr->uczestnik;
            plec_posiadacza = posiadacz_chusteczki->plec;
            id_posiadacza = ptr->id;
            return true;
        }
    }

    return false;
}

