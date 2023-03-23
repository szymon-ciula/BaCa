//Szymon Ciula
#include "Morfologia.h"
#include <iostream>
#include <vector>

struct Piksel;
class BitmapaExt;

typedef void (*fun_dzialanie)(unsigned, unsigned, unsigned, Bitmapa&);
typedef bool (*fun_warunek)(unsigned, unsigned, unsigned, const Bitmapa&);
typedef void (*fun_dzialanie_warunek)(unsigned, unsigned, unsigned, std::vector<Piksel>&, Bitmapa&, fun_warunek);

std::ostream& operator<< (std::ostream& out, const Bitmapa& bitmapa);
void dodaj_piksel(unsigned x, unsigned y, unsigned z, std::vector<Piksel>& v, Bitmapa& bm, fun_warunek warunek);
void for_each_piksel(Bitmapa& bitmapa, fun_dzialanie_warunek funkcja, fun_warunek warunek, fun_dzialanie dzialanie);
void usredniany_piksel(unsigned x, unsigned y, unsigned z, std::vector<Piksel>& v, Bitmapa& bm, fun_warunek warunek);

/*-----------------------------------------------------------------*/
/*----------------------------- Klasy -----------------------------*/
/*-----------------------------------------------------------------*/
struct Piksel
{
    unsigned x,y,z;
    Piksel(const unsigned xx, const unsigned yy, const unsigned zz) : x{xx}, y{yy}, z{zz}  {}
    ~Piksel() = default;
};
class BitmapaExt : public Bitmapa
{
    const unsigned dim_x, dim_y, dim_z;
    bool*** bitmapa;
public:
    inline unsigned sx() const { return dim_x; }
    inline unsigned sy() const { return dim_y; }
    inline unsigned sz() const { return dim_z; }

    inline bool& operator()(unsigned x, unsigned y, unsigned z)      { return bitmapa[z][y][x]; }
    inline bool operator()(unsigned x, unsigned y, unsigned z) const { return bitmapa[z][y][x]; }
//konstruktory, destruktor i operator przypisania
    BitmapaExt(const unsigned x, const unsigned y, const unsigned z);
    BitmapaExt(const BitmapaExt& other);
    ~BitmapaExt();
    BitmapaExt& operator= (const BitmapaExt& other);
};
//---------------- Pochodne klasy Przeksztalcenie ----------------*/
void neg(unsigned x, unsigned y, unsigned z, std::vector<Piksel>&, Bitmapa& bm, fun_warunek w) { bm(x,y,z) = !bm(x,y,z); }
bool f(unsigned x, unsigned y, unsigned z, const Bitmapa& bm) { return bm(x,y,z)==false; }
bool t(unsigned x, unsigned y, unsigned z, const Bitmapa& bm) { return bm(x,y,z)==true; }
void f(unsigned x, unsigned y, unsigned z, Bitmapa& bm) { bm(x,y,z) = false; }
void t(unsigned x, unsigned y, unsigned z, Bitmapa& bm) { bm(x,y,z) = true; }
void neg(unsigned x, unsigned y, unsigned z, Bitmapa& bm) { bm(x,y,z) = !bm(x,y,z); }
void f(unsigned x, unsigned y, unsigned z, std::vector<Piksel>&, Bitmapa& bm, fun_warunek w) { bm(x,y,z) = false; }
void usredniany_piksel(unsigned x, unsigned y, unsigned z, std::vector<Piksel>& v, Bitmapa& bm, fun_warunek warunek)
{
    unsigned short i = 0;
    const bool p = bm(x,y,z);

    if( x!=0  &&  bm(x-1,y,z)!=p )  ++i;
    if( y!=0  &&  bm(x,y-1,z)!=p )  ++i;
    if( z!=0  &&  bm(x,y,z-1)!=p )  ++i;
    if( x!=bm.sx()-1  &&  bm(x+1,y,z)!=p )  ++i;
    if( y!=bm.sy()-1  &&  bm(x,y+1,z)!=p )  ++i;
    if( z!=bm.sz()-1  &&  bm(x,y,z+1)!=p )  ++i;

    if(i>3)
        v.push_back(Piksel(x,y,z));
};
struct Inwersja : public Przeksztalcenie
{
    void przeksztalc(Bitmapa& bitmapa) override
    { for_each_piksel(bitmapa, neg, nullptr, nullptr); }
};
struct Erozja : public Przeksztalcenie
{
    void przeksztalc(Bitmapa& bitmapa) override
    { for_each_piksel(bitmapa, dodaj_piksel, f, f); }
};
struct Dylatacja : public Przeksztalcenie
{
    void przeksztalc(Bitmapa& bitmapa) override
    { for_each_piksel(bitmapa, dodaj_piksel, t, t); }
};
struct Zerowanie : public Przeksztalcenie
{
    void przeksztalc(Bitmapa& bitmapa) override
    { for_each_piksel(bitmapa, f, nullptr, nullptr); }
};
struct Usrednianie : public Przeksztalcenie
{
    void przeksztalc(Bitmapa& bitmapa) override
    { for_each_piksel(bitmapa, usredniany_piksel, nullptr, neg); }
};
class ZlozeniePrzeksztalcen : public Przeksztalcenie
{
    std::vector <Przeksztalcenie*> przeksztalcenia;
public:
    void dodajPrzeksztalcenie(Przeksztalcenie* p)
    { przeksztalcenia.push_back(p); }
    void przeksztalc(Bitmapa& bitmapa) override
    {
        for(auto p : przeksztalcenia)
            p->przeksztalc(bitmapa);
    }
};

/*-----------------------------------------------------------------*/
/*----------------------- Definicje funkcji -----------------------*/
/*-----------------------------------------------------------------*/

/*-------------------- Funkcje klasy BitmapaExt --------------------*/
BitmapaExt::BitmapaExt(const unsigned x, const unsigned y, const unsigned z) : dim_x{x}, dim_y{y}, dim_z{z}
{
    bitmapa = new bool** [dim_z];
    for(unsigned i=0; i<dim_z; ++i)
    {
        bitmapa[i] = new bool* [dim_y];
        for(unsigned j=0; j<dim_y; ++j)
        {
            bitmapa[i][j] = new bool [dim_x];
            for(unsigned k=0; k<dim_x; ++k)
                bitmapa[i][j][k] = false;
        }
    }
}
BitmapaExt::BitmapaExt(const BitmapaExt& other) : Bitmapa(), dim_x{other.dim_x}, dim_y{other.dim_y}, dim_z{other.dim_z}
{
    bitmapa = new bool** [dim_z];
    for(unsigned i=0; i<dim_z; ++i)
    {
        bitmapa[i] = new bool* [dim_y];
        for(unsigned j=0; j<dim_y; ++j)
        {
            bitmapa[i][j] = new bool [dim_x];
            for(unsigned k=0; k<dim_x; ++k)
                bitmapa[i][j][k] = other.bitmapa[i][j][k];
        }
    }
}
BitmapaExt::~BitmapaExt()
{
    for(unsigned i=0; i<dim_z; ++i)
    {
        for(unsigned j=0; j<dim_y; ++j)
            delete [] bitmapa[i][j];
        delete [] bitmapa[i];
    }
    delete [] bitmapa;
}
BitmapaExt& BitmapaExt::operator= (const BitmapaExt& other)
{
    if(this != &other)
    {
    //czesc destruktorowa
        for(unsigned i=0; i<dim_z; ++i)
        {
            for(unsigned j=0; j<dim_y; ++j)
                delete [] bitmapa[i][j];
            delete [] bitmapa[i];
        }
        delete [] bitmapa;
    //czesc konstruktorowa
        bitmapa = new bool** [dim_z];
        for(unsigned i=0; i<dim_z; ++i)
        {
            bitmapa[i] = new bool* [dim_y];
            for(unsigned j=0; j<dim_y; ++j)
            {
                bitmapa[i][j] = new bool [dim_x];
                for(unsigned k=0; k<dim_x; ++k)
                    bitmapa[i][j][k] = other.bitmapa[i][j][k];
            }
        }
    }
    return *this;
}

/*---------------------- Funkcje pomocnicze ----------------------*/
std::ostream& operator<< (std::ostream& out, const Bitmapa& bitmapa)
{
    std::string s = "{";

    for(unsigned x=0; x<bitmapa.sx(); ++x)
    {
        s += "\n {";
        for(unsigned y=0; y<bitmapa.sy(); ++y)
        {
            s += "\n  {";
            for(unsigned z=0; z<bitmapa.sz(); ++z)
                s += bitmapa(x,y,z) ? "1," : "0,";
            s.replace(s.length()-1, 2, "},");
        }
        s.replace(s.length()-1, 4, "\n },");
    }

    return ( out << (s.replace(s.length()-1, 2, "\n}")) );
}
void dodaj_piksel(unsigned x, unsigned y, unsigned z, std::vector<Piksel>& v, Bitmapa& bm, fun_warunek warunek)
{
    if( (x!=0 && warunek(x-1,y,z,bm))  ||  ((x!=bm.sx()-1) && warunek(x+1,y,z,bm))  ||
        (y!=0 && warunek(x,y-1,z,bm))  ||  ((y!=bm.sy()-1) && warunek(x,y+1,z,bm))  ||
        (z!=0 && warunek(x,y,z-1,bm))  ||  ((z!=bm.sz()-1) && warunek(x,y,z+1,bm))  )
    {
        v.push_back(Piksel(x,y,z));
    }
}
void for_each_piksel(Bitmapa& bitmapa, fun_dzialanie_warunek funkcja, fun_warunek warunek, fun_dzialanie dzialanie)
{
    std::vector <Piksel> piksele;

    for(unsigned z=0; z<bitmapa.sz(); ++z)
    {
        for(unsigned y=0; y<bitmapa.sy(); ++y)
        {
            for(unsigned x=0; x<bitmapa.sx(); ++x)
                funkcja(x,y,z, piksele, bitmapa, warunek);
        }
    }

    for(auto p : piksele)
        dzialanie(p.x, p.y, p.z, bitmapa);
}

